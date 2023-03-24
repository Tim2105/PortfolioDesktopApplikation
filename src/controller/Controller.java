package controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Controller {
	
	@FXML
	protected Parent root;
	
	public void close() {
		((Stage)root.getScene().getWindow()).close();
	}
	
}
