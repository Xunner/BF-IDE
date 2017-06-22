package ui;

import javafx.fxml.FXML;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

import javafx.scene.image.ImageView;
import rmi.RemoteHelper;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MainFrameController {
	@FXML
	private Menu openMenu;
	@FXML
	private Menu languageMenu;
	@FXML
	private CheckMenuItem BFMenuItem;
	@FXML
	private CheckMenuItem OOKMenuItem;
	@FXML
	private TextArea codeArea;
	@FXML
	private TextArea inputArea;
	@FXML
	private TextArea outputArea;
	@FXML
	private TitledPane loggedInPane;
	@FXML
	private ImageView userDisplayPicture;
	@FXML
	private Label userName;
	
	/**
	 * 当前代码是否已保存
	 */
	private boolean isSaved = true;
	
	public void init(){
		codeArea.textProperty().addListener(cl -> {
			isSaved = false;
		});
	}

	// Event Listener on MenuItem.onAction
	@FXML
	public void clickSaveMenuItem(ActionEvent event) {
		if(!isSaved){
			LocalDateTime localDateTime = LocalDateTime.now();
			String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "-");
			if(time.contains(".")){
				time = time.substring(0, time.charAt('.'));
			}
			try {
				RemoteHelper.getInstance().getIOService().writeFile(codeArea.getText(), userName.getText(), localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + '-' + time);
				refreshOpenMenu();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			isSaved = true;
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickExitMenuItem(ActionEvent event) {
		if(isSaved){
			exit();
		}
		else{
			Alert alert = new Alert(AlertType.CONFIRMATION, "要保存当前代码吗？", 
					new ButtonType("是", ButtonData.YES),
					new ButtonType("否", ButtonData.NO),
					new ButtonType("取消", ButtonData.CANCEL_CLOSE));
			alert.setTitle("确认");
			alert.setHeaderText("当前代码尚未保存");
			alert.showAndWait().ifPresent(response -> {
				if(response != ButtonType.CANCEL){
					if(response == ButtonType.YES){
						clickSaveMenuItem(null);
					}
					exit();
				}
			});
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickExecuteMenuItem(ActionEvent event) {
		String language = null;
		for(MenuItem mi : languageMenu.getItems()){
			CheckMenuItem cmi = (CheckMenuItem) mi;
			if(cmi.isSelected()){
				language = cmi.getText();
				break;
			}
		}
		try {
			switch(language){
			case "BF":
				outputArea.setText(RemoteHelper.getInstance().getExecuteService().BFExecute(codeArea.getText(), inputArea.getText()));
				break;
			case "OOK":
				outputArea.setText(RemoteHelper.getInstance().getExecuteService().OOKExecute(codeArea.getText(), inputArea.getText()));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickAboutMenuItem(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("关于");
		alert.setHeaderText(ui.Main.Name);
		alert.setContentText("开发者：胡本霖");
		alert.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void clickLogOutButton(ActionEvent event) {
		try {
			RemoteHelper.getInstance().getUserService().logout(userName.getText());
			ui.Main.primaryStage.setScene(ui.Main.logInScene);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserData(String userId){
		userName.setText(userId);
		refreshOpenMenu();
		// TODO 加载头像功能
	}
	
	private void exit(){
		try {
			RemoteHelper.getInstance().getUserService().logout(userName.getText());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		ui.Main.exit();
	}
	
	@FXML
	public void selectBFLanguage(ActionEvent event){
		for(MenuItem mi : languageMenu.getItems()){
			CheckMenuItem cmi = (CheckMenuItem) mi;
			cmi.setSelected(false);
		}
		BFMenuItem.setSelected(true);
	}
	
	@FXML
	public void selectOOKLanguage(ActionEvent event){
		for(MenuItem mi : languageMenu.getItems()){
			CheckMenuItem cmi = (CheckMenuItem) mi;
			cmi.setSelected(false);
		}
		OOKMenuItem.setSelected(true);
	}
	
	public void refreshOpenMenu(){
		try {
			String[] fileList = RemoteHelper.getInstance().getIOService().readFileList(userName.getText()).split(System.lineSeparator());
			openMenu.getItems().clear();
			for(String fileName : fileList){
				CheckMenuItem menuItem = new CheckMenuItem(fileName);
				menuItem.setOnAction(e -> {
					if(!isSaved){
						Alert alert = new Alert(AlertType.CONFIRMATION, "要保存当前代码吗？", 
								new ButtonType("是", ButtonData.YES),
								new ButtonType("否", ButtonData.NO),
								new ButtonType("取消", ButtonData.CANCEL_CLOSE));
						alert.setTitle("确认");
						alert.setHeaderText("当前代码尚未保存");
						alert.showAndWait().ifPresent(response -> {
							if(response != ButtonType.CANCEL){
								if(response == ButtonType.YES){	//	若选“是”：保存
									clickSaveMenuItem(null);
								}
								openAFile(fileName, menuItem);
							}
						});
					}
					else{
						openAFile(fileName, menuItem);
					}
				});
				openMenu.getItems().add(menuItem);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void openAFile(String fileName, CheckMenuItem menuItem){
		//	从服务器获取并显示该版本的代码
		try {
			codeArea.setText(RemoteHelper.getInstance().getIOService().readFile(userName.getText(), fileName));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		//	刷新子菜单选中状况
		for(MenuItem mi : openMenu.getItems()){
			((CheckMenuItem)mi).setSelected(false);
		}
		menuItem.setSelected(true);
	}
}
