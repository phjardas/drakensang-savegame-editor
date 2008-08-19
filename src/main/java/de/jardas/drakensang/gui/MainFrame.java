package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.validation.ValidationResult;
import de.jardas.drakensang.model.validation.Validator;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;


public class MainFrame extends JFrame {
    private final JToolBar toolbar = new JToolBar();
    private final JFileChooser fileChooser = new JFileChooser();
    private final Validator validator = new Validator();
    private CharacterDao characterDao;
    private JList characterList = new JList();
    private CharacterPanel characterPanel = new CharacterPanel(this);
    private JButton saveButton;
    private JComponent glassPane = new JPanel();
    private JComponent defaultGlassPane;
    private boolean busy;
    private List<Character> characters;

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
        setBusy(true);

        characterDao = new CharacterDao(file.getAbsolutePath());

        characters = new ArrayList<Character>(characterDao.getCharacters());

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

        saveButton.setEnabled(true);

        characterList.setSelectedIndex(0);

        setBusy(false);
    }

    private String getCharacterName(Character character) {
        return character.isLocalizeLookAtText()
        ? Messages.get(character.getLookAtText()) : character.getLookAtText();
    }

    public void save() {
        characterDao.saveAll();
        JOptionPane.showMessageDialog(this, Messages.get("GameSaved"),
            Messages.get("SaveGame"), JOptionPane.INFORMATION_MESSAGE);
    }

    private ValidationResult validateModel() {
        ValidationResult result = new ValidationResult();

        for (Character character : characters) {
            result.merge(validator.validate(character));
        }

        return result;
    }

    private void updateSelection(Character character) {
        characterPanel.setCharacter(character);
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

        if ((saves == null) || (saves.length == 0)) {
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
