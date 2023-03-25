package view;

import entity.Developer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;

public class DetailedDeveloperListCell extends ListCell<Developer> {
	
	@Override
	protected void updateItem(Developer item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER_LEFT);
			vbox.setSpacing(7.0);
			
			Label title = new Label(item.getFirstname() + " " + item.getLastname());
			title.setWrapText(true);
			title.prefWidthProperty().bind(this.prefWidthProperty());
			title.setStyle("-fx-font-weight: bold;");
			
			Label description = new Label(item.getDescription());
			description.setStyle("-fx-font-style: italic;");
			description.prefWidthProperty().bind(this.prefWidthProperty());
			description.setWrapText(true);
			description.setTextOverrun(OverrunStyle.ELLIPSIS);
			description.setMaxHeight(40.0);
			
			vbox.getChildren().addAll(title, description);
			this.setGraphic(vbox);
		}
	}
	
}
