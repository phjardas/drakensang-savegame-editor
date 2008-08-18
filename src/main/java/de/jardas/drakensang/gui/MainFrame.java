package de.jardas.drakensang.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Character;

public class MainFrame extends JFrame {
	private final JToolBar toolbar = new JToolBar();
	private final JFileChooser fileChooser = new JFileChooser();
	private CharacterDao characterDao;
	private JList characterList = new JList();
	private CharacterPanel characterPanel = new CharacterPanel();
	private JButton saveButton;

	public MainFrame() {
		super();
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("images/drakensang.png")));
		init();
	}

	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(Messages.get("title"));
		getContentPane().setLayout(new BorderLayout());

		fileChooser.setDialogTitle(Messages.get("LoadGame"));
		fileChooser.setApproveButtonText(Messages.get("LoadGame"));
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		File latest = getLatestSavegame();
		if (latest != null) {
			fileChooser.setCurrentDirectory(latest.getParentFile());
		}

		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return Messages.get("filechooser.type");
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().matches("\\.dsa$");
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
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		saveButton.setEnabled(false);
		toolbar.add(saveButton);

		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(characterList, BorderLayout.WEST);
		getContentPane().add(characterPanel, BorderLayout.CENTER);

		setSize(800, 730);
		centerOnScreen();
	}

	private void showLoadDialog() {
		int result = fileChooser.showDialog(MainFrame.this, null);

		if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			loadSavegame(file);
		} else if (result == javax.swing.JFileChooser.ERROR_OPTION) {
			// FIXME error handling
		} else if (result == javax.swing.JFileChooser.CANCEL_OPTION) {
			// do nothing
		}
	}

	public void loadSavegame(File file) {
		characterDao = new CharacterDao(file.getAbsolutePath());

		final List<Character> characters = new ArrayList<Character>(
				characterDao.getCharacters());

		Collections.sort(characters, new Comparator<Character>() {
			private final Collator collator = Collator.getInstance();

			public int compare(Character o1, Character o2) {
				if (o1.isPlayerCharacter())
					return -1;
				if (o2.isPlayerCharacter())
					return 1;

				return collator.compare(getCharacterName(o1),
						getCharacterName(o2));
			}
		});

		characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		characterList.setModel(new AbstractListModel() {
			public Object getElementAt(int index) {
				return getCharacterName(characters.get(index));
			}

			public int getSize() {
				return characters.size();
			}
		});

		characterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (characterList.getSelectedIndex() >= 0
						&& characterList.getSelectedIndex() < characters.size()) {
					updateSelection(characters.get(characterList
							.getSelectedIndex()));
				}
			}
		});

		saveButton.setEnabled(true);

		characterList.setSelectedIndex(0);
	}

	private String getCharacterName(Character character) {
		return character.isLocalizeLookAtText() ? Messages.get(character
				.getLookAtText()) : character.getLookAtText();
	}

	public void save() {
		characterDao.saveAll();
		JOptionPane.showMessageDialog(this,
				"Der Spielstand wurde gespeichert.", "Speichern",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void updateSelection(Character character) {
		characterPanel.setCharacter(character);
	}

	private void centerOnScreen() {
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
	}

	private File getLatestSavegame() {
		FileSystemView fw = fileChooser.getFileSystemView();
		File documentsDir = fw.getDefaultDirectory();
		File savedir = new File(documentsDir,
				"Drakensang/profiles/default/save/");
		File[] saves = savedir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory()
						&& pathname.getName().startsWith("save");
			}
		});

		if (saves == null || saves.length == 0) {
			return null;
		}

		return new File(saves[saves.length - 1], "savegame.dsa");
	}

	public void loadDefaultSavegame() {
		File latest = getLatestSavegame();

		if (latest != null) {
			loadSavegame(latest);
		} else {
			showLoadDialog();
		}
	}
}
