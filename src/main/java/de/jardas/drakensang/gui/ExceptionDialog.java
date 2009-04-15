package de.jardas.drakensang.gui;

import de.jardas.drakensang.Main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.URL;
import java.net.URLEncoder;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class ExceptionDialog extends JDialog {
    public ExceptionDialog(final Frame parent, Throwable throwable) {
        super(parent, "Application Failure", true);

        setLayout(new BorderLayout(5, 5));

        final StringWriter trace = new StringWriter();
        PrintWriter writer = new PrintWriter(trace);
        throwable.printStackTrace(writer);
        writer.flush();
        writer.close();

        add(new JLabel("Unfortunately an application failure has occurred."),
            BorderLayout.NORTH);
        add(new JTextArea(trace.toString()), BorderLayout.CENTER);

        JPanel toolbar = new JPanel();
        toolbar.add(new JButton(new AbstractAction(
                    "Send anonymous error report") {
                public void actionPerformed(ActionEvent e) {
                    try {
                        URL url = new URL(
                                    "http://www.jardas.de/drakensang/report.php?version="
                                    + URLEncoder.encode(
                                        Main.getCurrentVersion(), "utf-8")
                                    + "&stacktrace="
                                    + URLEncoder.encode(trace.toString(),
                                        "utf-8"));
                        url.getContent();
                        JOptionPane.showMessageDialog(parent,
                                "Your error report has been submitted.",
                                "Thank you!", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(parent,
                                "Sorry, your bug report could not be submitted.\n\n"
                                + "Please visit http://www.jardas.de/drakensang/\n"
                                + "or file your bug report by mail via philipp@jardas.de.\n\n"
                                + "Thank you.", "Sorry...",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }));
        toolbar.add(new JButton(new AbstractAction("Quit") {
                public void actionPerformed(ActionEvent e) {
                    System.exit(1);
                }
            }));
        add(toolbar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
}
