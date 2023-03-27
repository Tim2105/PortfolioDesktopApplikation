package view;

import entity.Project;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;

public class DetailedProjectListCell extends ListCell<Project> {
	
	@Override
	protected void updateItem(Project item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER_LEFT);
			vbox.setSpacing(7.0);
			
			Label title = new Label(item.getTitle());
			title.setWrapText(true);
			title.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			title.setStyle("-fx-font-weight: bold;");
			
			Label description = new Label(item.getDescription());
			description.setStyle("-fx-font-style: italic;");
			description.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			description.setWrapText(true);
			description.setTextOverrun(OverrunStyle.ELLIPSIS);
			description.setMaxHeight(40.0);
			
			vbox.getChildren().addAll(title, description);
			this.setGraphic(vbox);
		}
	}
	
}
