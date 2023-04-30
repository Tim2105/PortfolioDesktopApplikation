package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import main.App;

public class AboutViewController extends Controller {
	
	@FXML
	private Hyperlink urlHyperlink;
	
	@FXML
	private Button closeButton;
	
	@FXML
	private void handleUrlHyperlinkAction() {
		App.getApplication().getHostServices().showDocument(this.urlHyperlink.getText());
	}
	
	@FXML
	public void handleCloseButtonAction() {
		this.close();
	}
}