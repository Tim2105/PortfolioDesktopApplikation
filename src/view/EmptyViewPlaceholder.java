package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EmptyViewPlaceholder extends Parent {
	
	private VBox container;
	
	private Label label;
	
	private Button button;
	
	public EmptyViewPlaceholder(String labelText, String buttonText) {
		this.container = new VBox();
		this.container.setAlignment(Pos.CENTER);
		this.container.setSpacing(5.0);
		
		this.label = new Label(labelText);
		this.button = new Button(buttonText);
		
		this.container.getChildren().addAll(this.label, this.button);
		this.getChildren().add(this.container);
	}
	
	public EmptyViewPlaceholder(String labelText, String buttonText, EventHandler<ActionEvent> evHandler) {
		this(labelText, buttonText);
		this.setOnAction(evHandler);
	}
	
	public void setOnAction(EventHandler<ActionEvent> evHandler) {
		this.button.setOnAction(evHandler);
	}
	
	public String getLabelText() {
		return this.label.getText();
	}
	
	public String getButtonText() {
		return this.button.getText();
	}
	
}
