package view;

import entity.Demonstration;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;
import main.Main;
import model.DBInterface;

public class DemonstrationListCell extends ListCell<Demonstration> {

	@Override
	protected void updateItem(Demonstration item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER_LEFT);
			vbox.setSpacing(7.0);
			
			Label name = new Label(item.getName());
			name.setWrapText(true);
			name.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			name.setStyle("-fx-font-weight: bold;");
			
			String url = DBInterface.getInstance().getConnectionData().getURL();
			
			int index = url.indexOf("/");
			if(index != -1 && url.length() > index + 1)
				url = url.substring(index + 2);
			
			index = url.lastIndexOf(":");
			if(index != -1)
				url = url.substring(0, index);
			
			index = url.lastIndexOf(":");
			if(index != -1)
				url = url.substring(0, index);
			
			Hyperlink link = new Hyperlink("http://" + url + ":5173/demo/" + String.valueOf(item.id));
			link.setStyle("-fx-font-style: italic;");
			link.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			link.setTextOverrun(OverrunStyle.ELLIPSIS);
			link.setOnAction(ev -> {
				Main.getApplication().getHostServices().showDocument(link.getText());
			});
			
			vbox.getChildren().addAll(name, link);
			this.setGraphic(vbox);
		}
	}
	
}
