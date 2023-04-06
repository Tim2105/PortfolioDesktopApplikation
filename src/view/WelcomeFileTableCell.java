package view;

import java.util.ArrayList;
import java.util.List;

import entity.DemonstrationFile;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class WelcomeFileTableCell extends TableCell<DemonstrationFile, Boolean> {
	
	List<ChangeListener<Boolean>> listenerList;
	
	public WelcomeFileTableCell() {
		super();
		
		this.listenerList = new ArrayList<ChangeListener<Boolean>>();
	}
	
	@Override
	protected void updateItem(Boolean item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			CheckBox content = new CheckBox();
			content.allowIndeterminateProperty().set(false);
			content.setAlignment(Pos.CENTER);
			content.setSelected(item);
			
			content.setOnAction(ev -> {
				boolean newValue = content.selectedProperty().get();
				
				for(ChangeListener<Boolean> listener : listenerList) {
					listener.changed(content.selectedProperty(), !newValue, newValue);
				}
			});
			
			this.setAlignment(Pos.CENTER);
			this.setGraphic(content);
		}
	}
	
	public void addChangeListener(ChangeListener<Boolean> changeListener) {
		this.listenerList.add(changeListener);
	}
	
	public void removeChangeListener(ChangeListener<Boolean> changeListener) {
		this.listenerList.remove(changeListener);
	}
	
	public DemonstrationFile getDemonstrationFile() {
		return this.getTableRow().getItem();
	}
	
}
