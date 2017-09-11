package Screen;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import Helper.P_Log;
import Objects.Restaurant;

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
	
	public String[] getComponents() {
		String[] list = new String;
		
		for(Component c : this.components)
		
		list.
		
		return this.components;
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

	public void setToggles(Vector<Restaurant<String, Integer, Double>> percentages){
		for (int i = 0; i < components.length; i++){
			P_Log log = P_Log.getLog();
			log.writeLog("Component " + i, Level.INFO);
			Component c = components[i];
			for(int j = 0; j <= percentages.size(); j++) {
				if(((JToggleButton) c).getText().equals(percentages.get(j).getLeft())) {
					
					Restaurant<String, Integer, Double> res = percentages.get(j);

					if(res.getRight() >= 80.00){
						if (c instanceof JToggleButton)
							((JToggleButton) c).setSelected(true);;
					}
					else {
						if (c instanceof JToggleButton)
							((JToggleButton) c).setSelected(false);;
					}
						
				}
			}
		}
	}
}