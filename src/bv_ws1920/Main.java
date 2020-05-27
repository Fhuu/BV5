// BV Ue4 WS2019/20 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16

package bv_ws1920;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ImageAnalysisAppView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Image Analysis - WS2019/20 - <SON NURI _ DIRO BALOSKA>"); // TODO: add your name(s)
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
