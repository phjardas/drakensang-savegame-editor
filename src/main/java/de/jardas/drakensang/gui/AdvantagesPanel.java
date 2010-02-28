package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.db.AdvantageDao;
import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.model.Advantage;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Effect;

public class AdvantagesPanel extends JPanel {
	private Character character;
	private final Set<JCheckBox> boxes = new HashSet<JCheckBox>();

	public AdvantagesPanel() {
		setLayout(new GridBagLayout());
	}

	private void update() {
		boxes.clear();
		removeAll();
		addAdvantageFields();
	}

	private void addAdvantageFields() {
		final List<Advantage> advantages = Arrays.asList(AdvantageDao.values());
		Collections.sort(advantages, new Comparator<Advantage>() {
			private final Collator collator = Collator.getInstance();

			public int compare(Advantage o1, Advantage o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		int row = 0;

		for (Advantage advantage : advantages) {
			addAdvantageField(advantage, row++);
		}
	}

	private void addAdvantageField(final Advantage advantage, int row) {
		final JCheckBox box = new JCheckBox();
		boolean selected = getCharacter().getAdvantages().contains(advantage);

		box.setSelected(selected);
		box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (box.isSelected()) {
					if (getCharacter().getAdvantages().size() >= 4) {
						throw new DrakensangException(
								"A character must not have more than four treats!");
					} else {
						getCharacter().getAdvantages().add(advantage);
					}
				} else {
					getCharacter().getAdvantages().remove(advantage);
				}

				enableUncheckedBoxes(getCharacter().getAdvantages().size() < 4);
			}
		});
		boxes.add(box);

		add(box, new GridBagConstraints(0, row, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3,
						6, 3, 6), 0, 0));

		add(new InfoLabel(advantage.getName(), advantage.getInfo(), false),
				new GridBagConstraints(1, row, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(3, 6, 3, 6), 0, 0));

		final StringBuffer effects = new StringBuffer();

		for (Effect effect : advantage.getEffects()) {
			if (effects.length() > 0) {
				effects.append(", ");
			}

			effects.append(Messages.get(effect.getTargetNameKey()));
			effects.append(" ");
			effects.append((effect.getModifier() > 0) ? "+" : "");
			effects.append(effect.getModifier());
		}

		add(new JLabel(effects.toString()), new GridBagConstraints(2, row, 1,
				1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(3, 6, 3, 6), 0, 0));
	}

	private void enableUncheckedBoxes(boolean enabled) {
		for (JCheckBox box : boxes) {
			if (!box.isSelected()) {
				box.setEnabled(enabled);
			}
		}
	}

	public Character getCharacter() {
		return this.character;
	}

	public void setCharacter(Character character) {
		if (this.character == character) {
			return;
		}

		this.character = character;
		update();
	}
}
