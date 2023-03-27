package view;

import entity.Developer;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class DeveloperListCell extends ListCell<Developer> {
	
	@Override
	protected void updateItem(Developer item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			Label title = new Label(item.getFirstname() + " " + item.getLastname());
			title.setWrapText(true);
			title.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			
			this.setGraphic(title);
		}
	}
	
}
