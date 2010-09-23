package de.jardas.drakensang.gui.load;

import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.dao.SavegameDao.Progress;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.Launcher;
import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.InfoLabel;

import org.jdesktop.swingworker.SwingWorker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.io.File;

import java.text.MessageFormat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;


public class LoadDialog extends JDialog implements SavegameListener {
    private final SavegameListener savegameListener;
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final LoadProgress loadProgress = new LoadProgress();
    private final Frame owner;
    private SavegameListPanel list;
    private JToolBar toolbar;
    private JButton abortButton;

    public LoadDialog(final Frame owner, final SavegameListener savegameListener) {
        super(owner, Messages.get("LoadGame"), true);
        this.savegameListener = savegameListener;
        this.owner = owner;
    }

    public void showDialog() {
        loadProgress.showDialog();
    }

    private void init(final List<Savegame> savegames, final SavegameProgress progress) {
        setLayout(new BorderLayout());

        list = new SavegameListPanel(savegames, progress);

        final SwingWorker<Object, Object> worker =
            new SwingWorker<Object, Object>() {
                @Override
                protected Object doInBackground() throws Exception {
                    list.loadGames();

                    return null;
                }

                private void showFailed(final List<File> failed) {
                    final JPanel failedPanel = new JPanel();
                    failedPanel.setLayout(new GridBagLayout());

                    int row = 0;
                    failedPanel.add(new InfoLabel("load.failed.message"),
                        new GridBagConstraints(0,
                            row++,
                            1,
                            1,
                            1,
                            0,
                            GridBagConstraints.NORTHWEST,
                            GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 10, 0),
                            0,
                            0));

                    for (final File file : failed) {
                        failedPanel.add(new JLabel(file.getName()),
                            new GridBagConstraints(0,
                                row++,
                                1,
                                1,
                                1,
                                0,
                                GridBagConstraints.NORTHWEST,
                                GridBagConstraints.HORIZONTAL,
                                new Insets(0, 20, 0, 0),
                                0,
                                0));
                    }

                    JOptionPane.showMessageDialog(owner,
                        failedPanel,
                        Messages.get("load.failed.title"),
                        JOptionPane.ERROR_MESSAGE);
                }

                @Override
                protected void done() {
                    abortButton =
                        new JButton(new AbstractAction(Messages.get("Cancel")) {
                                public void actionPerformed(final ActionEvent e) {
                                    setVisible(false);
                                }
                            });

                    toolbar = new JToolBar();
                    toolbar.setFloatable(false);
                    toolbar.add(abortButton);

                    add(new JScrollPane(list), BorderLayout.CENTER);
                    add(toolbar, BorderLayout.SOUTH);

                    if (!progress.getFailed().isEmpty()) {
                        final JButton failedButton =
                            new JButton(
                                new AbstractAction(
                                    MessageFormat.format(Messages.get("load.failed.button"),
                                        new Object[] { progress.getFailed().size() })) {
                                    public void actionPerformed(final ActionEvent e) {
                                        showFailed(progress.getFailed());
                                    }
                                });
                        failedButton.setForeground(Color.RED);
                        toolbar.add(failedButton);
                    }

                    loadProgress.setVisible(false);

                    setSize(500, 500);
                    setLocationRelativeTo(owner);
                    setVisible(true);
                }
            };

        worker.execute();
    }

    public void loadSavegame(final Savegame savegame) {
        setVisible(false);
        savegameListener.loadSavegame(savegame);
    }

    private class SavegameProgress implements Progress {
        private final List<File> failed = new LinkedList<File>();
        private int done = 0;

        public void setTotalNumberOfSavegames(final int total) {
            progressBar.setMinimum(0);
            progressBar.setMaximum(total * 2);
            progressBar.setValue(done);
        }

        public void onSavegameLoaded(final Savegame savegame) {
            step();
        }

        public void onSavegameFailed(final File file, final Throwable cause) {
            failed.add(file);
            step();

            // Wird nicht gerendert
            progressBar.setMaximum(progressBar.getMaximum() - 1);
        }

        public void onSavegameRendered() {
            step();
        }

        private void step() {
            done++;
            progressBar.setValue(done);
        }

        public List<File> getFailed() {
            return failed;
        }
    }

    private class LoadProgress extends JDialog {
        public LoadProgress() {
            super(owner, Messages.get("LoadingGames"), true);

            setLayout(new BorderLayout());
            progressBar.setPreferredSize(new Dimension(300, 20));

            add(progressBar, BorderLayout.CENTER);
        }

        public void showDialog() {
            final SavegameProgress progress = new SavegameProgress();

            // Let's start loading the savegames.
            final SwingWorker<List<Savegame>, Object> worker =
                new SwingWorker<List<Savegame>, Object>() {
                    @Override
                    protected List<Savegame> doInBackground() throws Exception {
                        try {
                            return SavegameDao.getSavegames(progress);
                        } catch (Exception e) {
                            Launcher.handleException(
                                new DrakensangException("Error loading save games: " + e, e));

                            throw e;
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            init(get(), progress);
                        } catch (InterruptedException e) {
                            // ignore
                        } catch (ExecutionException e) {
                            Launcher.handleException(
                                new DrakensangException("Error loading save games: " + e, e));
                        }
                    }
                };

            worker.execute();

            pack();
            setLocationRelativeTo(owner);
            setVisible(true);
        }
    }

    private class SavegameListPanel extends JPanel {
        private final List<Savegame> savegames;
        private final SavegameProgress progress;
        private final GridBagConstraints gbc =
            new GridBagConstraints(0,
                0,
                1,
                1,
                1,
                0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6),
                0,
                0);

        public SavegameListPanel(final List<Savegame> savegames, final SavegameProgress progress) {
            super();
            setLayout(new GridBagLayout());
            this.savegames = savegames;
            this.progress = progress;
        }

        public void loadGames() {
            for (final Savegame savegame : savegames) {
                SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            final SavegameListItem item =
                                new SavegameListItem(savegame, LoadDialog.this);
                            gbc.gridy += 1;
                            add(item, gbc);
                            progress.onSavegameRendered();
                        }
                    });
            }
        }
    }
}
