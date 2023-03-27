package view;

import entity.Project;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class ProjectListCell extends ListCell<Project> {
	
	@Override
	protected void updateItem(Project item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			Label title = new Label(item.getTitle());
			title.setWrapText(true);
			title.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			
			this.setGraphic(title);
		}
	}
	
}
