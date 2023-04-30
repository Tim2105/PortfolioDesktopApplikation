module Projekt {
	exports controller;
	exports main;
	exports entity;

	requires jakarta.persistence;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	
	opens controller to javafx.fxml;
}