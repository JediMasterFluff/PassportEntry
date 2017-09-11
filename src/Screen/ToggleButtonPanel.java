package Screen;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.List;
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

	public List<String> selectedToggles() {
		List<String> names = new Vector<String>();

		for (int i = 0; i <= components.length; i++) {
			Component c = components[i];
			if (c instanceof JToggleButton)
				if (((JToggleButton) c).isSelected())
					names.add(c.getName());

		}
		return names;
	}

	public void setToggles(LinkedHashMap<String, Double> percentages){
		for (int i = 0; i <= components.length; i++){
			Component c = components[i];
			if(percentages.containsKey(c.getName())){
				Double d = percentages.get(c.getName());
				if(d >= 80.00){
					if (c instanceof JToggleButton)
						((JToggleButton) c).setSelected(true);;
				}
			}
		}
	}
}