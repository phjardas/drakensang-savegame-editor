package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.model.Character;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import java.io.File;

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

public class MainFrame extends JFrame {
	private final JToolBar toolbar = new JToolBar();
	private final JFileChooser fileChooser = new JFileChooser();
	private CharacterDao characterDao;
	private JList characterList = new JList();
	private CharacterPanel characterPanel = new CharacterPanel();
	private JButton saveButton;

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
		toolbar.setFloatable(false);

		toolbar.add(new JButton(new AbstractAction("Spielstand laden",
				new ImageIcon(getClass().getResource("images/open.gif"))) {
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

		saveButton = new JButton(new AbstractAction("Spielstand speichern",
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

		setSize(800, 720);
		centerOnScreen();
	}

	public void loadSavegame(File file) {
		characterDao = new CharacterDao(file.getAbsolutePath());

		final List<Character> characters = new ArrayList<Character>(
				characterDao.getCharacters());

		Collections.sort(characters, new Comparator<Character>() {
			public int compare(Character o1, Character o2) {
				return o1.getId().compareToIgnoreCase(o2.getId());
			}
		});

		characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		characterList.setModel(new AbstractListModel() {
			public Object getElementAt(int index) {
				return characters.get(index).getId();
			}

			public int getSize() {
				return characters.size();
			}
		});

		characterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateSelection(characters
						.get(characterList.getSelectedIndex()));
			}
		});

		saveButton.setEnabled(true);

		characterList.setSelectedIndex(0);
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
}
