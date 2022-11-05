package application;
	
import java.awt.event.MouseEvent;
import java.security.acl.Group;
import java.util.ArrayList;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class Main extends Application {
	
	int counter = 0;

	@Override
	public void start(Stage primaryStage) {
		try {
			switchToMain(primaryStage);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//switches the scene to the main login page
	private void switchToMain(Stage primary) {
		primary.setTitle("Sign In");
		
		//name of the application
		Label nameLbl = new Label("CardX");
		nameLbl.setFont(new Font("Georgia", 75));
		nameLbl.setPadding(new Insets(-20, 0, 50, 55));
		/*
		NOTE: new Insets(a, b, c, d)
			a = top padding
			b = left padding
			c = down padding
			d = right padding
		 */
		
		//"Welcome Back" text
		Label welcomeLbl = new Label("Welcome Back");
		welcomeLbl.setFont(new Font("Georgia", 24));
		
		//login button
		Button loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: #e6d1f2");
		//  
		loginBtn.setOnAction(event -> switchToCourses(primary));
		
		//links for signing up for a new account and resetting password
		Hyperlink signupBtn = new Hyperlink("Don't have an account? Click here");
		signupBtn.setOnAction(event -> switchToSignup(primary));
		Hyperlink resetBtn = new Hyperlink("Forgot your password?");
		resetBtn.setOnAction(event -> switchToReset(primary));
				
		//user-name and password text fields
		Label userText = new Label("Username/email: ");
		Label passText = new Label("Password: ");
		TextField userField = new TextField();
		userField.setMaxWidth(350);
		PasswordField passField = new PasswordField();
		passField.setMaxWidth(350);
		
		//the VBox pane where everything goes
		VBox root = new VBox();
		root.setStyle("-fx-background-color: #dfe9f5");
		root.setPadding(new Insets(100, 1, 100, 105));
		root.setSpacing(10);
		root.getChildren().add(nameLbl);
		root.getChildren().add(welcomeLbl);
		root.getChildren().addAll(userText, userField);
		root.getChildren().addAll(passText, passField);
		root.getChildren().add(loginBtn);
		root.getChildren().add(resetBtn);
		root.getChildren().add(signupBtn);
		
		
		Scene scene = new Scene(root,550,550);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene);

	}
		
	//switches the scene to the reset page
	private void switchToReset(Stage primary) {
		primary.setTitle("Reset Password");
		Hyperlink returnToMain = new Hyperlink("Return to login");
		returnToMain.setOnAction(event -> switchToMain(primary));
		VBox reset = new VBox();
		reset.getChildren().add(returnToMain);
		Scene scene1 = new Scene(reset, 500, 500);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
	}
	
	//switches the scene to the signup page
	private void switchToSignup(Stage primary) {
		primary.setTitle("Sign up for an account");
		Hyperlink returnToMain = new Hyperlink("Return to login");
		returnToMain.setOnAction(event -> switchToMain(primary));
		VBox signup = new VBox();
		signup.getChildren().add(returnToMain);
		Scene scene1 = new Scene(signup, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
	}
	
	//switches the scene to the main course page
		private void switchToCourses(Stage primary) {
			ArrayList<Course> listCourses = new ArrayList<Course>();
			ArrayList<Button> listButtons = new ArrayList<Button>();
			primary.setTitle("My Courses");
			// logout button and enter
			Button returnToMain = new Button("Logout");
			returnToMain.setOnAction(event -> switchToMain(primary));
			Button enter = new Button("enter");
			Button delete = new Button("Delete");
			returnToMain.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #AFBAF7;" 
										+"-fx-font-size: 13;" + "-fx-cursor: hand;");
			enter.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #0038A8;" 
					+"-fx-font-size: 13;" + "-fx-cursor: hand;");	
			delete.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF6A6A;" 
					+"-fx-font-size: 13;" + "-fx-cursor: hand;");
			Label name = new Label("Enter Name");
			//set window look
			name.setFont(new Font("Georgia", 15));
			FlowPane signup = new FlowPane();
			signup.setVgap(10);
			signup.setHgap(10);
			signup.setOrientation(Orientation.HORIZONTAL);
			signup.getChildren().add(name);
			signup.setMargin(name, new Insets(20, 20, 20, 20));
			
			TextField text = new TextField();
			
			text.setMaxWidth(200);
			
			// adds a new course 
			enter.setOnAction(event ->{
				String enterName = text.getText();
				Button newCourse = new Button(enterName);
				newCourse.setStyle("-fx-text-fill: #000000;" + "-fx-background-color: #F5DE9C;" 
						+ "-fx-border-color: #EBC861; -fx-border-width: 2px;" 
						+ "-fx-cursor: hand;");
				newCourse.setMinSize(100, 60);
				signup.getChildren().add(newCourse);
				listCourses.add(new Course(enterName));
				listButtons.add(newCourse);
			});
			
			/*delete.setOnAction(event ->{
				if 
			});*/
			
			
			//signup.setPadding(new Insets(10, 10, 10, 10));
			
			Scene scene1 = new Scene(signup, 500, 550);
			// sets background
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			signup.getChildren().addAll(text, enter, delete, returnToMain);
			signup.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
			

			
	
			
			primary.setScene(scene1);
			
			
		}
		
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
