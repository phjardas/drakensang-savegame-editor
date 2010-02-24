package de.jardas.drakensang.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingworker.SwingWorker;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.dao.CharacterDao.Progress;
import de.jardas.drakensang.gui.load.LoadDialog;
import de.jardas.drakensang.gui.load.SavegameListener;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.model.savegame.SavegameIcon;
import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.Launcher;
import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.LocaleChooserDialog;
import de.jardas.drakensang.shared.gui.WordWrap;
import de.jardas.drakensang.shared.model.Character;

public class MainFrame extends JFrame {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
			.getLogger(MainFrame.class);
	private final JToolBar toolbar = new JToolBar();
	private CharacterPanel characterPanel = new CharacterPanel();
	private JButton saveButton;
	private JButton settingsButton;
	private JComponent glassPane = new JPanel();
	private JComponent defaultGlassPane;
	private boolean busy;
	private Savegame savegame;
	private JPanel left = new JPanel();
	private JLabel savegameIcon = new JLabel("");
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private CharactersLoader charactersLoader = new CharactersLoader();
	private final Progress progress = new Progress() {
		private int count = 0;

		public void setTotalNumberOfCharacters(int total) {
			count = 0;
			progressBar.setValue(count);
			progressBar.setMaximum(total);
		}

		public void onCharacterLoaded(Character character) {
			count++;
			LOG.debug("Character " + count + " of " + progressBar.getMaximum()
					+ " loaded: " + character.getId());
			progressBar.setValue(count);
		}
	};

	public MainFrame() {
		super();
		init();
	}

	private void init() {
		LOG.debug("Initializing main frame.");

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("images/drakensang.png")));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(Messages.get("title"));
		getContentPane().setLayout(new BorderLayout());

		glassPane.setOpaque(false);
		glassPane.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				e.consume();
			}

			public void keyReleased(KeyEvent e) {
				e.consume();
			}

			public void keyTyped(KeyEvent e) {
				e.consume();
			}
		});
		glassPane.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				e.consume();
			}

			public void mouseEntered(MouseEvent e) {
				e.consume();
			}

			public void mouseExited(MouseEvent e) {
				e.consume();
			}

			public void mousePressed(MouseEvent e) {
				e.consume();
			}

			public void mouseReleased(MouseEvent e) {
				e.consume();
			}
		});

		toolbar.setFloatable(false);

		toolbar.add(new JButton(new AbstractAction(Messages.get("LoadGame"),
				new ImageIcon(getClass().getResource("images/open.gif"))) {
			public void actionPerformed(ActionEvent e) {
				showLoadDialog();
			}
		}));

		saveButton = new JButton(new AbstractAction(Messages.get("SaveGame"),
				new ImageIcon(getClass().getResource("images/save.gif"))) {
			public void actionPerformed(ActionEvent evt) {
				try {
					save();
				} catch (Exception e) {
					Launcher.handleException(e);
				}
			}
		});
		saveButton.setEnabled(false);
		toolbar.add(saveButton);

		settingsButton = new JButton(new AbstractAction(Messages
				.get("Settings"), new ImageIcon(getClass().getResource(
				"images/wrench.png"))) {
			public void actionPerformed(ActionEvent e) {
				new LocaleChooserDialog(MainFrame.this) {
					@Override
					public void onLocaleChosen(Locale locale) {
						setVisible(false);

						if (Locale.getDefault().equals(locale)) {
							return;
						}

						Launcher.setUserLocale(locale);
						JOptionPane.showMessageDialog(MainFrame.this, WordWrap
								.addNewlines(Messages
										.get("languagechooser.afterchosen")),
								Messages.get("languagechooser.title"),
								JOptionPane.INFORMATION_MESSAGE);
					}

					@Override
					public void onAbort() {
						setVisible(false);
					}
				};
			}
		});
		toolbar.add(settingsButton);

		toolbar.add(new JButton(new AbstractAction(
				Messages.get("about.button"), new ImageIcon(getClass()
						.getResource("images/about.gif"))) {
			public void actionPerformed(ActionEvent e) {
				new AboutDialog(MainFrame.this).setVisible(true);
			}
		}));

		left.setLayout(new BorderLayout());
		left.add(savegameIcon, BorderLayout.SOUTH);

		getContentPane().add(characterPanel, BorderLayout.CENTER);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(left, BorderLayout.WEST);
		getContentPane().add(new Footer(), BorderLayout.SOUTH);

		setSize(900, 730);
		setLocationRelativeTo(null);

		LOG.debug("Launcher frame initialized.");
	}

	public void setBusy(boolean busy) {
		// only set if changing
		if (this.busy != busy) {
			this.busy = busy;

			// If busy, keep current glass pane to put back when not
			// busy. This is done in case the application is using
			// it's own glass pane for something special.
			if (busy) {
				defaultGlassPane = glassPane;
				setGlassPane(glassPane);
			} else {
				setGlassPane(defaultGlassPane);
				defaultGlassPane = null;
			}

			glassPane.setVisible(busy);
			glassPane.setCursor(busy ? Cursor
					.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor
					.getDefaultCursor());
			setCursor(glassPane.getCursor());
		}
	}

	public void showLoadDialog() {
		LOG.debug("Showing load dialog.");
		new LoadDialog(this, new SavegameListener() {
			public void loadSavegame(Savegame savegame) {
				MainFrame.this.loadSavegame(savegame);
			}
		}).showDialog();
	}

	public void loadSavegame(Savegame savegame) {
		LOG.debug("Loading " + savegame);
		setBusy(true);

		this.savegame = savegame;
		savegameIcon.setIcon(new SavegameIcon(this.savegame, 150));

		setTitle(savegame.getName() + " - " + Messages.get("title"));
		SavegameDao.open(savegame);
		charactersLoader.loadCharacters();
	}

	private void onCharactersLoaded(final List<Character> characters) {
		LOG.debug("Sorting characters.");
		Collections.sort(characters, new Comparator<Character>() {
			private final Collator collator = Collator.getInstance();

			public int compare(Character o1, Character o2) {
				if (o1.isPlayerCharacter()) {
					return -1;
				}

				if (o2.isPlayerCharacter()) {
					return 1;
				}

				return collator.compare(getCharacterName(o1),
						getCharacterName(o2));
			}
		});

		final JList characterList = new JList(new AbstractListModel() {
			public Object getElementAt(int index) {
				return getCharacterName(characters.get(index));
			}

			public int getSize() {
				return characters.size();
			}
		});
		characterList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean selected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						selected, cellHasFocus);

				final Character ch = characters.get(index);

				if (ch.isPlayerCharacter()) {
					setForeground(Color.RED);
				} else if (ch.isCurrentPartyMember()) {
					setForeground(Color.BLUE);
				}

				if (!ch.isPartyMember()) {
					setFont(getFont().deriveFont(Font.PLAIN));
				}

				return this;
			}
		});

		characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		characterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if ((characterList.getSelectedIndex() >= 0)
						&& (characterList.getSelectedIndex() < characters
								.size())) {
					Character character = characters.get(characterList
							.getSelectedIndex());
					LOG.debug("Character #" + characterList.getSelectedIndex()
							+ " selected.");
					updateSelection(character);
				}
			}
		});

		for (Component comp : left.getComponents()) {
			if (comp instanceof JList) {
				LOG.debug("Removing old character list.");
				left.remove(comp);
			}
		}

		left.add(characterList, BorderLayout.CENTER);

		LOG.debug("Selecting first character.");
		characterList.setSelectedIndex(0);
		saveButton.setEnabled(true);

		LOG.debug("Repainting.");
		repaint();

		LOG.debug("Loading complete: " + savegame);
		charactersLoader.setVisible(false);
		setBusy(false);
	}

	private String getCharacterName(Character character) {
		return character.isLocalizeLookAtText() ? Messages.get(character
				.getLookAtText()) : character.getLookAtText();
	}

	public void save() {
		final File backup = SavegameDao.createBackup();
		CharacterDao.saveAll();

		final String message = (backup != null) ? MessageFormat.format(Messages
				.get("GameSavedWithBackup"), savegame.getFile()
				.getAbsolutePath(), backup.getAbsolutePath()) : MessageFormat
				.format(Messages.get("GameSaved"), savegame.getFile()
						.getAbsolutePath());

		JOptionPane.showMessageDialog(this, message, Messages.get("SaveGame"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void updateSelection(Character character) {
		try {
			characterPanel.setCharacter(character);
		} catch (RuntimeException e) {
			Launcher.handleException(e);
		}
	}

	private class CharactersLoader extends JDialog {
		public CharactersLoader() {
			super(MainFrame.this, Messages.get("LoadGame"), true);

			setLayout(new BorderLayout());
			progressBar.setPreferredSize(new Dimension(300, 20));

			add(progressBar, BorderLayout.CENTER);
		}

		public void loadCharacters() {
			// Let's start loading the savegames.
			final SwingWorker<List<Character>, Object> worker = new SwingWorker<List<Character>, Object>() {
				@Override
				protected List<Character> doInBackground() throws Exception {
					return new ArrayList<Character>(CharacterDao
							.loadCharacters(progress));
				}

				@Override
				protected void done() {
					try {
						onCharactersLoaded(get());
					} catch (InterruptedException e) {
						// ignore
					} catch (ExecutionException e) {
						Launcher.handleException(new DrakensangException(
								"Error loading savegame " + savegame.getFile(),
								e));
					}
				}
			};

			worker.execute();

			pack();
			setLocationRelativeTo(MainFrame.this);
			setVisible(true);
		}
	}
}
