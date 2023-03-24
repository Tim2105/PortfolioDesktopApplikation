package controller;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectController<T> extends Controller {

	private List<T> itemsShown = new ArrayList<T>();
	private List<T> selectedItems = new ArrayList<T>();
	
	public List<T> getSelectedItems() {
		return this.selectedItems;
	}
	
	public void showItems(List<T> items) {
		this.itemsShown = new ArrayList<T>(items);
		this.update();
	}
	
	public List<T> getShownItems() {
		return this.itemsShown;
	}
	
	protected abstract void update();
	
}
