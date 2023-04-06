package view;

import entity.DemonstrationFile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class FilenameTableCell extends TableCell<DemonstrationFile, String> {

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else {
			Label content = new Label(item);
			content.setWrapText(false);
			content.setAlignment(Pos.CENTER_LEFT);
			
			this.setGraphic(content);
		}
	}
	
}
