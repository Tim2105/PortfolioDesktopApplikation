module Projekt {
	exports main;

	requires jakarta.persistence;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	
	opens controller to javafx.fxml;
}