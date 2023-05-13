package view;

import entity.Demonstration;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import main.App;
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
			else {
				index = url.indexOf("/");
				if(index != -1)
					url = url.substring(0, index);
			}
			
			String finalUrl = url;
			
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.prefWidthProperty().bind(this.widthProperty().subtract(16.0));
			
			Hyperlink link = new Hyperlink("/demo/" + String.valueOf(item.id));
			link.setStyle("-fx-font-style: italic;");
			link.setPrefWidth(USE_COMPUTED_SIZE);
			link.setMaxWidth(USE_COMPUTED_SIZE);
			link.setTextOverrun(OverrunStyle.ELLIPSIS);
			link.setOnAction(ev -> {
				App.getApplication().getHostServices().showDocument("http://" + finalUrl + "/demo/" + String.valueOf(item.id));
			});
			
			Region region = new Region();
			region.setPrefWidth(0.0);
			
			Button copyButton = new Button("Kopieren");
			copyButton.setOnAction(ev -> {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				
				ClipboardContent content = new ClipboardContent();
				content.putString("/demo/" + String.valueOf(item.id));
				
				clipboard.setContent(content);
			});
			
			hbox.getChildren().addAll(link, region, copyButton);
			
			HBox.setHgrow(region, Priority.ALWAYS);
			
			vbox.getChildren().addAll(name, hbox);
			this.setGraphic(vbox);
		}
	}
	
}
