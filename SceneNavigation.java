package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SceneNavigation {

	private final int TEXTBOX_SIZE = 350;
		
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
		Label welcomeLbl = new Label("Welcome Back!");
		welcomeLbl.setFont(new Font("Georgia", 24));
		
		//login button
		Button loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: #e6d1f2;" + "-fx-cursor: hand;");
		loginBtn.setOnAction(event -> switchToCourses(primary));
		
		//links for signing up for a new account and resetting password
		Hyperlink signupBtn = new Hyperlink("Don't have an account? Click here");
		signupBtn.setOnAction(event -> switchToSignup(primary));
		Hyperlink resetBtn = new Hyperlink("Forgot your password?");
		resetBtn.setOnAction(event -> switchToReset(primary));
				
		//user-name and password text fields
		//extra lambda expression written to allow login by pressing return key
		Label userText = new Label("Username: ");
		Label passText = new Label("Password: ");
		TextField userField = new TextField();
		userField.setMaxWidth(TEXTBOX_SIZE);
		PasswordField passField = new PasswordField();
		passField.setMaxWidth(TEXTBOX_SIZE);
		//the lambda to login by pressing the return key
		passField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (!userField.getText().isEmpty() && !passField.getText().isEmpty()) {
					switchToCourses(primary);
				}
			}
		});
		
		//styling the VBox pane where everything goes
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
		
		//security question and space to answer
		Label questionLabel = new Label("Security Question:");
		Text question = new Text("This is where the question should go.");
		Label answerLabel = new Label("Answer");
		TextField answer = new TextField();
		answer.setMaxWidth(TEXTBOX_SIZE);
		
		//line break
		Label lineBreak = new Label("");
		
		//text fields for new password and confirming password
		Label newLabel = new Label("New Password");
		PasswordField newPass = new PasswordField();
		Label confirmLabel = new Label("Confirm Password");
		PasswordField confirmPass = new PasswordField();
		newPass.setMaxWidth(TEXTBOX_SIZE);
		confirmPass.setMaxWidth(TEXTBOX_SIZE);
		
		//submit button to save changes
		Button submitReset = new Button("Submit");
		submitReset.setOnAction(event -> switchToMain(primary));
		
		//styling the layout pane and adding components
		GridPane reset = new GridPane();
		reset.setStyle("-fx-background-color: #dfe9f5");
		reset.setPadding(new Insets(100, 100, 100, 120));
		reset.setVgap(20);
		reset.setHgap(10);
		reset.add(questionLabel, 0, 2);
		reset.add(question, 1, 2);
		reset.add(answerLabel, 0, 3);
		reset.add(answer, 1, 3);
		reset.add(lineBreak, 1, 4);
		reset.add(newLabel, 0, 5);
		reset.add(newPass, 1, 5);
		reset.add(confirmLabel, 0, 6);
		reset.add(confirmPass, 1, 6);
		reset.add(submitReset, 0, 8);
		Scene scene1 = new Scene(reset, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
	}
	
	//switches the scene to the signup page
	private void switchToSignup(Stage primary) {
		
		//creates the heading at the top of the screen 
		primary.setTitle("Sign Up for an Account");
		Label accountLabel = new Label("Sign Up for an Account");
		accountLabel.setFont(new Font("Georgia", 24));
		
		//text field for creating a new username
		Label userLabel = new Label("Username");
		TextField user = new TextField();
		user.setMaxWidth(TEXTBOX_SIZE);
		
		//text field for creating a new password
		Label passLabel = new Label("Password");
		PasswordField pass = new PasswordField();
		pass.setMaxWidth(TEXTBOX_SIZE);
		
		//text field for confirming the new password
		Label confirmLabel = new Label("Confirm Password");
		PasswordField confirm = new PasswordField();
		confirm.setMaxWidth(TEXTBOX_SIZE);
		
		//field for picking a security question
		Label questionLabel = new Label("Security Question");
		ObservableList<String> questions = 
			    FXCollections.observableArrayList(
			        "What is your mother's maiden name?",
			        "What was your pet's name?",
			        "What is the name of your high school?"
			    );
		ComboBox questionList = new ComboBox(questions);
		questionList.setMaxWidth(TEXTBOX_SIZE);
		
		//text field for coming up with an answer to the security question
		Label answer = new Label("Answer ");
		TextField qAnswer = new TextField();
		qAnswer.setMaxWidth(TEXTBOX_SIZE);
		
		//submit button to save changes
		Button submit = new Button("Submit");
		submit.setOnAction(event -> switchToMain(primary));
		
		//styling the layout pane and adding components
		GridPane signup = new GridPane();
		signup.setStyle("-fx-background-color: #dfe9f5");
		signup.setPadding(new Insets(100, 100, 100, 85));
		signup.setVgap(20);
		signup.setHgap(10);
		signup.add(accountLabel, 1, 0);
		signup.add(userLabel, 0, 2);
		signup.add(user, 1, 2);
		signup.add(passLabel, 0, 3);
		signup.add(pass, 1, 3);
		signup.add(confirmLabel, 0, 4);
		signup.add(confirm, 1, 4);
		signup.add(questionLabel, 0, 5);
		signup.add(questionList, 1, 5);
		signup.add(answer, 0, 6);
		signup.add(qAnswer, 1, 6);
		signup.add(submit, 0, 8);
		Scene scene1 = new Scene(signup, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
	}
	
	//switches the scene to the main course page
	private void switchToCourses(Stage primary) {
		primary.setTitle("My Courses");
		Hyperlink returnToMain = new Hyperlink("Return to login");
		returnToMain.setOnAction(event -> switchToMain(primary));
		VBox signup = new VBox();
		signup.getChildren().add(returnToMain);
		Scene scene1 = new Scene(signup, 500, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
	}
	
	//runs the app. encapsulation is used to ensure the user cannot
	//see the other methods and only knows to run the program.
	public void runApp(Stage primary) {
		switchToMain(primary);
	}
	
	
}
