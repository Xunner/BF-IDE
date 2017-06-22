package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import rmi.RemoteHelper;
import javafx.scene.control.PasswordField;

import javafx.scene.control.CheckBox;

public class LogInInterfaceController {
	@FXML
	private AnchorPane root;
	@FXML
	private TextField userIdTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private CheckBox isRememberPassword;
	@FXML
	private Button logInButton;
	@FXML
	private Button registerButton;
	@FXML
	private Label nameLabel;

	public void init(){
		nameLabel.setText(ui.Main.Name);
	}
	
	// Event Listener on Button[#logInButton].onAction
	@FXML
	public void clickLogInButton(ActionEvent event) {
		if(checkIfHasIdAndPassword()){
			try {
				if(RemoteHelper.getInstance().getUserService().login(userIdTextField.getText(), passwordField.getText())){
					ui.Main.mainFrameController.setUserData(userIdTextField.getText());
					ui.Main.primaryStage.setScene(ui.Main.mainFrameScene);
				}
				else{
					showAlert(AlertType.ERROR, "错误", "登录失败", "用户名或密码错误");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	// Event Listener on Button[#registerButton].onAction
	@FXML
	public void clickRegisterButton(ActionEvent event) {
		if(checkIfHasIdAndPassword()){
			try {
				if(RemoteHelper.getInstance().getUserService().register(userIdTextField.getText(), passwordField.getText())){
					showAlert(AlertType.INFORMATION, "提示", "注册成功", "你现在可以登录了");
				}
				else{
					showAlert(AlertType.ERROR, "错误", "注册失败", "用户名已被占用");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void typeKey(KeyEvent event){
		switch(event.getCode()){
		case ENTER:
			clickLogInButton(null);
			break;
		case ESCAPE:
			ui.Main.exit();
			break;
		default:
		}
	}
	
	private boolean checkIfHasIdAndPassword(){
		if(userIdTextField.getText().equals("")){
			if(passwordField.getText().equals("")){
				showAlert(AlertType.WARNING, "提示", "用户名和密码不能为空", "请输入用户名和密码");
			}
			else{
				showAlert(AlertType.WARNING, "提示", "用户名不能为空", "请输入用户名");
			}
		}
		else{
			if(passwordField.getText().equals("")){
				showAlert(AlertType.WARNING, "提示", "密码不能为空", "请输入密码");
			}
			else{
				return true;
			}
		}
		return false;
	}
	
	private void showAlert(AlertType alertType, String title, String header, String content){
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();	//	Optional<ButtonType> result = 
//		if(result.get() == ButtonType.OK){
//		}
	}
}
