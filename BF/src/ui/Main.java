package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	static Stage primaryStage;
	static Scene logInScene;
	static Scene mainFrameScene;
	static LogInInterfaceController logInInterfaceController;
	static MainFrameController mainFrameController;
	final static String Name = "还不会唱歌的IDE";

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
		FXMLLoader logInLoader = new FXMLLoader(getClass().getResource("logInInterface.fxml"));
		FXMLLoader mainFrameLoader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
		try {
			logInScene = new Scene(new Pane((Pane)logInLoader.load()));
			mainFrameScene = new Scene(new Pane((Pane)mainFrameLoader.load()));
			logInInterfaceController = (LogInInterfaceController) logInLoader.getController();
			mainFrameController = (MainFrameController) mainFrameLoader.getController();
			logInInterfaceController.init();
			mainFrameController.init();
			primaryStage.setScene(logInScene);
			primaryStage.setTitle(Name);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void exit(){
		primaryStage.close();
		System.exit(0);
	}
}
