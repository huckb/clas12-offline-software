package cnuphys.cnf.alldata.graphics;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.jlab.io.base.DataDescriptor;

import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.cnf.alldata.DataManager;

public class ColumnList extends DragDropList {

	private static Dimension _size = new Dimension(220, 250);

	// the scroll pane
	private JScrollPane _scrollPane;

	public ColumnList() {
		this(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public ColumnList(int selectionMode) {
		super(new DefaultListModel());
		setSelectionMode(selectionMode);
		_scrollPane = new JScrollPane(this);
		_scrollPane.setPreferredSize(_size);
		_scrollPane.setBorder(new CommonBorder("Column Name"));
	}


	private void clear() {
		DefaultListModel listModel = (DefaultListModel) getModel();
		listModel.removeAllElements();
	}

	/**
	 * Set the list to the columns of the given bank
	 * 
	 * @param bankName the name of the bank
	 */
	public void setList(String bankName) {
		if (bankName != null) {
			DataDescriptor dd = DataManager.getInstance().getDictionary().getDescriptor(bankName);
			if (dd != null) {
				String columns[] = dd.getEntryList();
				Arrays.sort(columns);
				
				DefaultListModel model = (DefaultListModel)(this.getModel());
				model.clear();
				for (String cn : columns) {
					model.addElement(cn);
				}
				
				
	//			setListData(columns);
			} else {
				clear();
			}
		} else {
			clear();
		}
	}

	/**
	 * Get the scroll pane
	 * 
	 * @return the scroll pane
	 */
	public JScrollPane getScrollPane() {
		return _scrollPane;
	}

}
