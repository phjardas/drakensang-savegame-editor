package de.jardas.drakensang.gui.inventory.wizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JToolBar;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.model.Character;

public class NewItemWizard extends JDialog {
	private final JToolBar toolbar = new JToolBar();
	private final NewItemPanel panel;
	
	public NewItemWizard(List<Character> characters) {
		super(Main.getFrame(), "Neuer Gegenstand", true);
		
		setLayout(new BorderLayout());
		
		toolbar.add(new AbstractAction("Speichern") {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("SAVE");
			}
		});
		
		toolbar.add(new AbstractAction("Abbrechen") {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		
		toolbar.setFloatable(false);
		
		add(toolbar, BorderLayout.SOUTH);
		
		panel = new NewItemPanel(characters);
		add(panel, BorderLayout.CENTER);
		
		pack();
		setLocationRelativeTo(Main.getFrame());
	}
}
