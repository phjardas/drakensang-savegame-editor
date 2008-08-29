package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.gui.load.LoadDialog;
import de.jardas.drakensang.gui.load.SavegameListener;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.model.savegame.SavegameIcon;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.text.Collator;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class MainFrame extends JFrame {
    private final JToolBar toolbar = new JToolBar();
    private JList characterList = new JList();
    private CharacterPanel characterPanel = new CharacterPanel(this);
    private JButton saveButton;
    private JComponent glassPane = new JPanel();
    private JComponent defaultGlassPane;
    private boolean busy;
    private List<Character> characters;
    private Savegame savegame;
    private JPanel left = new JPanel();
    private JLabel savegameIcon = new JLabel("");

    public MainFrame() {
        super();
        setIconImage(Toolkit.getDefaultToolkit()
                            .getImage(getClass()
                                          .getResource("images/drakensang.png")));
        init();
    }

    private void init() {
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
                    public void actionPerformed(ActionEvent e) {
                        save();
                    }
                });
        saveButton.setEnabled(false);
        toolbar.add(saveButton);

        //		toolbar.add(new AbstractAction("Neuer Gegenstand") {
        //			public void actionPerformed(ActionEvent e) {
        //				new NewItemWizard(characterDao.getInventoryDao(), getCharacters()).setVisible(true);
        //			}
        //		});
        left.setLayout(new BorderLayout());
        left.add(savegameIcon, BorderLayout.SOUTH);
        left.add(characterList, BorderLayout.CENTER);

        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(characterPanel, BorderLayout.CENTER);
        getContentPane().add(new Footer(), BorderLayout.SOUTH);

        setSize(800, 730);
        setLocationRelativeTo(null);
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
            glassPane.setCursor(busy
                ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                : Cursor.getDefaultCursor());
            setCursor(glassPane.getCursor());
        }
    }

    private void showLoadDialog() {
        new LoadDialog(this,
            new SavegameListener() {
                public void loadSavegame(Savegame savegame) {
                    MainFrame.this.loadSavegame(savegame);
                }
            });
    }

    public void loadSavegame(Savegame savegame) {
        setBusy(true);

        this.savegame = savegame;
        savegameIcon.setIcon(new SavegameIcon(this.savegame, 150));

        setTitle(savegame.getName() + " - " + Messages.get("title"));
        SavegameDao.open(savegame);

        characters = new ArrayList<Character>(CharacterDao.getCharacters());

        Collections.sort(characters,
            new Comparator<Character>() {
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
                    if ((characterList.getSelectedIndex() >= 0)
                            && (characterList.getSelectedIndex() < characters
                            .size())) {
                        updateSelection(characters.get(
                                characterList.getSelectedIndex()));
                    }
                }
            });

        characterList.setSelectedIndex(0);
        saveButton.setEnabled(true);

        setBusy(false);
    }

    private String getCharacterName(Character character) {
        return character.isLocalizeLookAtText()
        ? Messages.get(character.getLookAtText()) : character.getLookAtText();
    }

    public void save() {
        CharacterDao.saveAll();
        JOptionPane.showMessageDialog(this,
            MessageFormat.format(Messages.get("GameSaved"),
                savegame.getFile().getAbsolutePath()),
            Messages.get("SaveGame"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSelection(Character character) {
        characterPanel.setCharacter(character);
    }

    public void loadDefaultSavegame() {
        Savegame latest = SavegameDao.getLatestSavegame();

        if (latest != null) {
            loadSavegame(latest);
        } else {
            showLoadDialog();
        }
    }

    public List<Character> getCharacters() {
        return characters;
    }
}
