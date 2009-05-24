package de.jardas.drakensang.gui.load;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.model.savegame.Savegame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class LoadDialog extends JDialog implements SavegameListener {
    private final SavegameListener savegameListener;
    private SavegameListPanel list;
    private JToolBar toolbar;
    private JButton abortButton;

    public LoadDialog(Frame owner, SavegameListener savegameListener) {
        super(owner, Messages.get("LoadGame"), true);
        this.savegameListener = savegameListener;

        setLayout(new BorderLayout());

		// FIXME show a progress bar while loading
        list = new SavegameListPanel(SavegameDao.getSavegames(), this);

        abortButton = new JButton(new AbstractAction(Messages.get("Cancel")) {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });

        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(abortButton);

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(toolbar, BorderLayout.SOUTH);

        setSize(500, 500);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public void loadSavegame(Savegame savegame) {
        setVisible(false);
        savegameListener.loadSavegame(savegame);
    }
}
