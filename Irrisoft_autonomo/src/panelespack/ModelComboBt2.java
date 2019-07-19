package panelespack;

import irrisoftpack.Irrisoft;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ModelComboBt2 extends AbstractListModel<String> implements
	ComboBoxModel<String> {

    private static final long serialVersionUID = 1L;

    String selection = null;

    public String getElementAt(int index) {
	return Irrisoft.window.bt2combo.get(index);
    }

    public int getSize() {
	return Irrisoft.window.bt2combo.size();
    }

    public void setSelectedItem(Object anItem) {
	selection = (String) anItem; // to select and register an
    } // item from the pull-down list

    // Methods implemented from the interface ComboBoxModel
    public Object getSelectedItem() {
	return selection; // to add the selection to the combo box
    }
}