package Screen;

import java.awt.Component;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ToggleButtonPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Component[] components;

	public ToggleButtonPanel() {
		super();
	}

	public void generateComponents() {
		components = getComponents();
	}

	public SortedSet<String> getComponentNames() {
		SortedSet<String> list = new TreeSet<String>();

		for (int i = 0; i < this.components.length; i++) {
			Component c = this.components[i];
			list.add(((JToggleButton) c).getText());
		}

		return list;
	}

	public List<String> selectedToggles() {
		List<String> names = new Vector<String>();

		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (c instanceof JToggleButton)
				if (((JToggleButton) c).isSelected())
					names.add(((JToggleButton) c).getText());

		}
		return names;
	}

	public void setToggles(LinkedHashMap<String, Double> percentages) {
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			for (Entry<String, Double> e : percentages.entrySet()) {
				if (((JToggleButton) c).getText() == e.getKey()) {

					if (e.getValue() >= 75.00) {
						if (c instanceof JToggleButton)
							((JToggleButton) c).setSelected(true);

					} else {
						if (c instanceof JToggleButton)
							((JToggleButton) c).setSelected(false);

					}

				}
			}
		}
	}

	@Override
	public String toString() {
		return "ToggleButtonPanel [components=" + Arrays.toString(components) + "]";
	}

}