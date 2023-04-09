package view;

import entity.DemonstrationFile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class FilenameTableCell extends TableCell<DemonstrationFile, String> {
	
	public FilenameTableCell() {
		super();
		
		this.editingProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue && oldValue) {
				String newFilename = ((TextField)(this.getGraphic())).getText();
				
				this.getTableRow().getItem().setFilename(newFilename);
			}
			
			this.update(this.getItem());
		});
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty)
			this.setGraphic(null);
		else
			this.update(item);
	}
	
	private void update(String item) {
		if(this.isEditing()) {
			TextField content = new TextField(item);
			content.setAlignment(Pos.CENTER_LEFT);
			
			this.setGraphic(content);
		} else {
			Label content = new Label(item);
			content.setWrapText(false);
			content.setAlignment(Pos.CENTER_LEFT);
			
			this.setGraphic(content);
		}
	}
	
}
