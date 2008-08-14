package de.jardas.drakensang.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.model.Character;

public class MainFrame extends JFrame {
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu menuFile = new JMenu("Datei");
	private final JFileChooser fileChooser = new JFileChooser();
	private CharacterDao characterDao;
	private JList characterList = new JList();
	private CharacterPanel characterPanel = new CharacterPanel();

	public MainFrame() {
		init();
	}

	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Drakensang Savegame Editor");
		getContentPane().setLayout(new BorderLayout());

		String home = System.getProperty("user.home").replace('\\', '/');

		fileChooser.setDialogTitle("Spielstand laden...");
		fileChooser.setApproveButtonText("Spielstand laden");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setCurrentDirectory(new File(home,
				"Documents/Drakensang/profiles/default/save/"));
		// fileChooser.setFileFilter(new FileFilter() {
		// @Override
		// public String getDescription() {
		// return "Drakensang save games (*.dsa)";
		// }
		//
		// @Override
		// public boolean accept(File f) {
		// return f.isDirectory() || f.getName().matches("\\.dsa$");
		// }
		// });

		menuBar.add(menuFile);
		menuFile.add(new JMenuItem(new AbstractAction("Spielstand laden...") {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		}));
		menuFile.add(new JMenuItem(new AbstractAction("Spielstand speichern") {
			@Override
			public void actionPerformed(ActionEvent e) {
				characterDao.saveAll();
			}
		}));
		
		menuFile.addSeparator();
		
		menuFile.add(new JMenuItem(new AbstractAction("Beenden") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));

		getContentPane().add(menuBar, BorderLayout.NORTH);
		getContentPane().add(characterList, BorderLayout.WEST);
		getContentPane().add(characterPanel, BorderLayout.CENTER);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		int width = Math.max(screenSize.width / 2, 610);
		int height = (int) (((Math.sqrt(5.0) - 1.0) / 2.0) * width);
		setSize(width, height);
		centerOnScreen();
	}

	public void loadSavegame(File file) {
		characterDao = new CharacterDao(file.getAbsolutePath());
		final List<Character> characters = new ArrayList<Character>(
				characterDao.getCharacters());

		Collections.sort(characters, new Comparator<Character>() {
			@Override
			public int compare(Character o1, Character o2) {
				return o1.getId().compareToIgnoreCase(o2.getId());
			}
		});

		characterList.setModel(new AbstractListModel() {
			@Override
			public Object getElementAt(int index) {
				return characters.get(index).getId();
			}

			public int getSize() {
				return characters.size();
			};
		});

		characterList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateSelection(characters.get(e.getLastIndex()));
			}
		});
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
}
