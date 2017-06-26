package ui;

import javafx.fxml.FXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import logic.CodeInquisitor;
import logic.Fallback;
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
	 * Indicator light<br>
	 * 用于指示当前代码是否存在非法字符（串）
	 */
	@FXML
	private Circle light;
	
	private FillTransition fillTransition;
	
	/**
	 * 当前代码是否已保存
	 */
	private BooleanProperty isSaved;
	
	private BooleanProperty isCodeLegal;
	
	private Fallback fallback;
	
	private CodeInquisitor codeInquisitor;
	
	private static final Color GREEN1 = Color.SPRINGGREEN;
	private static final Color GREEN2 = Color.LAWNGREEN;
	private static final Color RED1 = Color.RED;
	private static final Color RED2 = Color.BROWN;
	
	public static final String BF = "BF";
	public static final String OOK = "Ook!";
	
	/**
	 * 用于撤销操作的计时器
	 */
	private Timer timer = null;
	
	public void init(){
		//	界面
		codeArea.textProperty().addListener(cl -> {
			if(fallback.isPushable(codeArea.getText())){
				isSaved.setValue(false);
				
				//	更新计时器
				if(timer!=null){	//	若已有计时，取消之
					timer.cancel();
				}
				timer = new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						fallback.push(codeArea.getText());
					}
				}, 2000L);
			}
			
			//	检查代码以刷新indicator light
			if(checkIfCodeLegal()){
				isCodeLegal.setValue(true);
			}
			else{
				isCodeLegal.setValue(false);
			}
		});
		
		isSaved = new SimpleBooleanProperty(true);
		isSaved.addListener(cl -> {
			if(isSaved.getValue()){
				ui.Main.primaryStage.setTitle(ui.Main.Name);
			}
			else{
				ui.Main.primaryStage.setTitle(ui.Main.Name+"*");
			}
		});
		
		isCodeLegal = new SimpleBooleanProperty(true);
		isCodeLegal.addListener(cl -> {
			fillTransition.stop();
			fillTransition = new FillTransition(Duration.seconds(0.9), light);
			if(isCodeLegal.getValue()){
				fillTransition.setToValue(GREEN1);
				fillTransition.setOnFinished(e -> {
					fillTransition = new FillTransition(Duration.seconds(1), light, GREEN1, GREEN2);
					fillTransition.setAutoReverse(true);
					fillTransition.setCycleCount(Animation.INDEFINITE);
					fillTransition.play();	//	启动呼吸灯
				});
			}
			else{
				fillTransition.setToValue(RED1);
				fillTransition.setOnFinished(e -> {
					fillTransition = new FillTransition(Duration.seconds(1), light, RED1, RED2);
					fillTransition.setAutoReverse(true);
					fillTransition.setCycleCount(Animation.INDEFINITE);
					fillTransition.play();	//	启动呼吸灯
				});
			}
			fillTransition.play();
		});
		
		//	逻辑
		codeInquisitor = new CodeInquisitor();
	}

	private boolean checkIfCodeLegal() {
		String language = null;
		for(MenuItem mi : languageMenu.getItems()){
			CheckMenuItem cmi = (CheckMenuItem) mi;
			if(cmi.isSelected()){
				language = cmi.getText();
				break;
			}
		}
		if(language!=null){
			return codeInquisitor.isCodeLegal(codeArea.getText(), language);
		}
		else{
			return true;
		}
	}

	// Event Listener on MenuItem.onAction
	@FXML
	public void clickSaveMenuItem(ActionEvent event) {
		if(!isSaved.getValue()){
			LocalDateTime localDateTime = LocalDateTime.now();
			String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "-");
			if(time.contains(".")){
				time = time.substring(0, time.lastIndexOf('.'));
			}
			try {
				String fileName = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + '-' + time;
				RemoteHelper.getInstance().getIOService().writeFile(codeArea.getText(), userName.getText(), fileName);
				refreshOpenMenu();
				for(MenuItem mi : openMenu.getItems()){
					if(mi.getText().equals(fileName)){
						((CheckMenuItem)mi).setSelected(true);
						break;
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			isSaved.setValue(true);
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickExitMenuItem(ActionEvent event) {
		if(isSaved.getValue()){
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
				if(response.getButtonData() != ButtonData.CANCEL_CLOSE){
					if(response.getButtonData() == ButtonData.YES){
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
			case BF:
				outputArea.setText(RemoteHelper.getInstance().getExecuteService().BFExecute(codeArea.getText(), inputArea.getText()));
				break;
			case OOK:
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
		if(isSaved.getValue()){
			logOut();
		}
		else{
			Alert alert = new Alert(AlertType.CONFIRMATION, "要保存当前代码吗？", 
					new ButtonType("是", ButtonData.YES),
					new ButtonType("否", ButtonData.NO),
					new ButtonType("取消", ButtonData.CANCEL_CLOSE));
			alert.setTitle("确认");
			alert.setHeaderText("当前代码尚未保存");
			alert.showAndWait().ifPresent(response -> {
				if(response.getButtonData() != ButtonData.CANCEL_CLOSE){
					if(response.getButtonData() == ButtonData.YES){
						clickSaveMenuItem(null);
					}
					logOut();
				}
			});
		}
	}
	
	private void logOut(){
		try {
			RemoteHelper.getInstance().getUserService().logout(userName.getText());
			ui.Main.primaryStage.setScene(ui.Main.logInScene);
			
			//	界面重置
			codeArea.setText("");
			inputArea.setText("");
			outputArea.setText("");
			fillTransition.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserData(String userId){
		//	界面
		loggedInPane.setText(userId);
		userName.setText(userId);
		refreshOpenMenu();
		loggedInPane.setExpanded(false);
		File file = new File(userId+".jpg");
		if(file.exists()){
			userDisplayPicture.setImage(new Image(file.toURI().toString()));
		}
		else{
			userDisplayPicture.setImage(new Image("defaultAvatar.jpg"));
		}
		
		fillTransition = new FillTransition(Duration.seconds(1), light, GREEN1, GREEN2);
		fillTransition.setAutoReverse(true);
		fillTransition.setCycleCount(Animation.INDEFINITE);
		fillTransition.play();	//	启动呼吸灯
		
		//	逻辑
		fallback = new Fallback("");
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
		languageMenu.setText(BF);
		
		//	刷新指示灯
		if(checkIfCodeLegal()){
			isCodeLegal.setValue(true);
		}
		else{
			isCodeLegal.setValue(false);
		}
	}
	
	@FXML
	public void selectOOKLanguage(ActionEvent event){
		for(MenuItem mi : languageMenu.getItems()){
			CheckMenuItem cmi = (CheckMenuItem) mi;
			cmi.setSelected(false);
		}
		OOKMenuItem.setSelected(true);
		languageMenu.setText(OOK);

		//	刷新指示灯
		if(checkIfCodeLegal()){
			isCodeLegal.setValue(true);
		}
		else{
			isCodeLegal.setValue(false);
		}
	}
	
	public void refreshOpenMenu(){
		try {
			String[] fileList = RemoteHelper.getInstance().getIOService().readFileList(userName.getText()).split(System.lineSeparator());
			openMenu.getItems().clear();
			for(String fileName : fileList){
				CheckMenuItem menuItem = new CheckMenuItem(fileName);
				menuItem.setOnAction(e -> {
					if(!isSaved.getValue()){
						Alert alert = new Alert(AlertType.CONFIRMATION, "要保存当前代码吗？", 
								new ButtonType("是", ButtonData.YES),
								new ButtonType("否", ButtonData.NO),
								new ButtonType("取消", ButtonData.CANCEL_CLOSE));
						alert.setTitle("确认");
						alert.setHeaderText("当前代码尚未保存");
						alert.showAndWait().ifPresent(response -> {
							if(response.getButtonData() != ButtonData.CANCEL_CLOSE){
								if(response.getButtonData() == ButtonData.YES){	//	若选“是”：保存
									clickSaveMenuItem(null);
								}
								openAFile(fileName, menuItem);
							}
						});
					}
					else{
						openAFile(fileName, menuItem);
					}
					fallback = new Fallback(codeArea.getText());
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
		
		isSaved.setValue(true);
	}
	
	@FXML
	public void pressKey(KeyEvent event){
		switch(event.getCode()){
		case S:
			if(event.isControlDown()){
				clickSaveMenuItem(null);
			}
			break;
		default:
		}
	}
	
	@FXML
	public void typeKeyAtCodeArea(KeyEvent event){
		switch(event.getCode()){
		case Z:
			if(event.isControlDown()){
				codeArea.setText(fallback.undo());
			}
			break;
		case Y:
			if(event.isControlDown()){
				codeArea.setText(fallback.redo());
			}
			break;
		default:
		}
	}
	
	@FXML
	public void releaseKey(KeyEvent event){
		switch(event.getCode()){
		case ESCAPE:
			clickExitMenuItem(null);
			break;
		case F11:
			clickExecuteMenuItem(null);
		default:
		}
	}
	
	@FXML
	public void changeAvatar(MouseEvent event){
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
		fileChooser.setTitle("设置头像");
		File file = fileChooser.showOpenDialog(ui.Main.primaryStage);
		if(file!=null){
			userDisplayPicture.setImage(new Image(file.toURI().toString()));
			//	存储图片在客户端目录下
			File file2 = new File(userName.getText()+".jpg");
			try(FileInputStream fi = new FileInputStream(file);
					FileOutputStream fo = new FileOutputStream(file2);
					FileChannel in = fi.getChannel();
					FileChannel out = fo.getChannel()){
				in.transferTo(0, in.size(), out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
