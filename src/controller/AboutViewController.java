package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import main.Main;

public class AboutViewController extends Controller {
	
	@FXML
	private Hyperlink urlHyperlink;
	
	@FXML
	private Button closeButton;
	
	@FXML
	private void handleUrlHyperlinkAction() {
		Main.getApplication().getHostServices().showDocument(this.urlHyperlink.getText());
	}
	
	@FXML
	public void handleCloseButtonAction() {
		this.close();
	}
}