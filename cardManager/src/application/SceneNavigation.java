package application;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import edu.sjsu.yazdankhah.crypto.util.PassUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SceneNavigation {
	
	ArrayList<User> Users = new ArrayList<User>();
	PassUtil passUtil  = new PassUtil();
	User CurrentUser = null;
	private final int TEXTBOX_SIZE = 350;
	
	/**
	 * Pulls users from backend file to Users arraylist
	 */
	private void pullUsers() {
		try {
			Users = new ArrayList<User>();
			File input = new File("data.txt");
			
			Scanner fr = new Scanner(input);
			
			//ArrayList<String> usersString = new ArrayList<String>();
			while (fr.hasNextLine()) {
				String username = fr.nextLine();
				String password = fr.nextLine();
				String question = fr.nextLine();
				String answer = fr.nextLine();
				Users.add(new User(username, password, question, answer));
			}
			
			fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Pushes users from Users arraylist to backend file
	 */
	public void pushUsers() {
		try {
			FileWriter fw2 = new FileWriter("data.txt", false);
			BufferedWriter fw = new BufferedWriter(fw2);
			for(User user:Users) {
//				String s = user.getUser() + ":" + user.getPass() + ":" + user.getQuestion() + ":" + user.getAnswer() + ":";
//				System.out.println(s);
				fw.write(user.getUser());
				fw.newLine();
				fw.write(user.getPass());
				fw.newLine();
				fw.write(user.getQuestion());
				fw.newLine();
				fw.write(user.getAnswer());
				fw.newLine();
			}
			fw.close();
			System.out.println("File Sucessfully Saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
		
	/**
	 * Switches the scene to the main login scene, which includes
	 * features for logging in, resetting the password, and signing up 
	 * for a new account. 
	 * 
	 * @param primary the main stage where the scene will take place
	 */
	private void switchToMain(Stage primary) {
		pullUsers();
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
		
			
		//links for signing up for a new account and resetting password
		Hyperlink signupBtn = new Hyperlink("Don't have an account? Click here");
		signupBtn.setOnAction(event -> switchToSignup(primary));
		signupBtn.setStyle("-fx-text-fill: #ffffff");
		Hyperlink resetBtn = new Hyperlink("Forgot your password?");
		resetBtn.setOnAction(event -> switchToReset1(primary));
		resetBtn.setStyle("-fx-text-fill: #ffffff");
				
		//user-name and password text fields
		//extra lambda expressions written to allow login by pressing return key
		Label userText = new Label("Username: ");
		Label passText = new Label("Password: ");
		TextField userField = new TextField();
		userField.setMaxWidth(TEXTBOX_SIZE);
		PasswordField passField = new PasswordField();
		passField.setMaxWidth(TEXTBOX_SIZE);
		
		
		//login button
		Button loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: #FFFAEA;" + "-fx-cursor: hand;");
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				signIn(primary, userField.getText(), passField.getText());
			}
		});
				
				
		//the lambda expressions to login by pressing the return key
		userField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (!userField.getText().isEmpty() && !passField.getText().isEmpty()) {
					signIn(primary, userField.getText(), passField.getText());
				}
			}
		});
		passField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (!userField.getText().isEmpty() && !passField.getText().isEmpty()) {
					signIn(primary, userField.getText(), passField.getText());
				}
			}
		});
		
		//styling the VBox pane where everything goes
		VBox root = new VBox();
		root.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
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
		primary.show();
	}
	
	/**
	 * Checks if all sign in credentials are valid, and if so, switched the user to the courses page.
	 * 
	 * @param primary the main stage where the scene will take place
	 * @param username the username the user provides
	 * @param password the password the user provides
	 */
	private void signIn(Stage primary, String username, String password) {
		
		//check if user exists, and if so, check if password matches
		User user = null;
		Boolean userExists = false;
		Boolean passwordMatch = false;
		for (User u : Users) {
			if (u.getUser().equals(username)){
				userExists = true;
				if(passUtil.decrypt(u.getPass()).equals(password)) {
					passwordMatch = true;
					user = u;
					break;
				}
			}
		}
		
		//call the appropriate alert or sign in the user if no issues are present
		if (!userExists) {
			Alert alert = new Alert(AlertType.INFORMATION, "Username does not exist.", ButtonType.OK);
			alert.showAndWait();
		} else if (!passwordMatch) {
			Alert alert = new Alert(AlertType.INFORMATION, "Incorrect Password.", ButtonType.OK);
			alert.showAndWait();
		} else {
			CurrentUser = user;
			switchToCourses(primary);
		}
	}
	
	/**
	 * Creates a new User account if all credentials are valid, then switches the user to the main page.
	 * 
	 * @param primary the main stage where the scene will take place
	 * @param username the username the user provides
	 * @param password the password the user provides
	 * @param confirmPassword the confirm password that the user provides
	 * @param question the security question that the user chooses to answer
	 * @param answer the answer to the security question that the user provides
	 */
	private void createOrModifyAccount(Stage primary, String username, String password, String confirmPassword, String question, String answer) {
		String alertName = "";
		boolean newAccount = CurrentUser == null;
		boolean valid = false;
		boolean usernameTaken = false;
		for (User u : Users) {
			if ((newAccount || !u.getUser().equals(CurrentUser.getUser())) && u.getUser().equals(username)){
				usernameTaken = true;
				break;
			}
				
		}
		
		//Make sure all credentials are valid
		if (username.length() < 1) {
			alertName = "Username must be at least 1 characters.";
		} else if (password.length() < 1) {
			alertName = "Password must be at least 1 characters.";
		} else if (!password.equals(confirmPassword)) {
			alertName = "Passwords do not match";
		} else if (usernameTaken) {
			alertName = "This username is already taken.";
		} else if (question == null) {
				alertName = "Please choose a security question.";
		} else if (answer.length()<=2){
			alertName = "Please type in a valid answer.";
		} else {
			valid = true;
		}
		
		//call the appropriate alert or create a new account and user if no issues are present
		if (valid) {
			if(newAccount) {
				User u = new User(username, passUtil.encrypt(password), question, answer);
				Users.add(u);
			} else {
				for(User u : Users) {
					if(u.getUser().equals(CurrentUser.getUser())){
						u.resetAll(username, passUtil.encrypt(password), question, answer);
					}
				}
			}
			pushUsers();
			
			
			Alert alert = newAccount ? new Alert(AlertType.CONFIRMATION, "Account successfully created!", ButtonType.OK) : new Alert(AlertType.CONFIRMATION, "Account successfully modified!", ButtonType.OK);
			alert.showAndWait();
			
			if(newAccount) {
				switchToMain(primary);
			} else {
				switchToAccount(primary);
			}
			
		} else {
			Alert alert = new Alert(AlertType.INFORMATION, alertName, ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	/**
	 * Switches the scene to the password reset scene 1, where users can 
	 * type in their user name to confirm that their account exists
	 * 
	 * @param primary the main stage where the scene will take place 
	 */
	private void switchToReset1(Stage primary) {
		primary.setTitle("Reset Password");
		
		//security question and space to answer
		Label resetPassLabel = new Label("Reset Password");
		resetPassLabel.setFont(new Font("Georgia", 24));
		
		Label userLabel = new Label("Username");
		TextField username = new TextField();
		username.setMaxWidth(TEXTBOX_SIZE);
		
		//back button
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");

		backBtn.setOnAction(event -> switchToMain(primary));
		
		//line break
		Label lineBreak = new Label("");
		
		//text fields for new password and confirming password
		
		
		//continue button to switch to reset2 if username exists
		Button continueBtn = new Button("Continue");
		continueBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				//find the user
				User user = null;
				for(User u : Users) {
					if(u.getUser().equals(username.getText())) {
						user = u;
						break;
					}
				}
				
				//throw and error if the user does not exist, or take the user to the next page to reset their password
				if(user == null) {
					Alert alert = new Alert(AlertType.INFORMATION, "Username does not exist.", ButtonType.OK);
					alert.showAndWait();
				} else {
					switchToReset2(primary, user);
				}
			}
		});
		
		//styling the layout pane and adding components
		GridPane reset = new GridPane();
		reset.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
		reset.setPadding(new Insets(100, 100, 100, 120));
		reset.setVgap(20);
		reset.setHgap(10);
		reset.add(resetPassLabel, 1, 0);
		reset.add(userLabel, 0, 3);
		reset.add(username, 1, 3);
		reset.add(lineBreak, 1, 4);
		reset.add(continueBtn, 0, 8);
		reset.add(backBtn, 0, 9);
		Scene scene1 = new Scene(reset, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
		
	/**
	 * Switches the scene to the password reset scene, where users can 
	 * set a new password if they answer their security question correctly.
	 * 
	 * @param primary the main stage where the scene will take place 
	 */
	private void switchToReset2(Stage primary, User user) {
		primary.setTitle("Reset Password");
		
		//security question and space to answer
		Label questionLabel = new Label("Security Question:");
		String s = user.getQuestion();
		Text question = new Text(user.getQuestion());
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
		
		//back button
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");

		backBtn.setOnAction(event -> switchToReset1(primary));
		
		//submit button to save changes
		Button submitReset = new Button("Submit");
		submitReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				//make sure the security question is answered correctly and new credentials are valid
				String alertName = "";
				if(!user.getAnswer().equals(answer.getText())) {
					alertName = "Incorrect Answer";
				} else if(newPass.getText().length() < 1){
					alertName = "New Password must be at least 1 characters.";
				} else if (!newPass.getText().equals(confirmPass.getText())) {
					alertName = "Passwords do not match";
				}
				
				
				//throw and appropriate alert if answer is incorrect or credentials are invalid, or take the user to the main page if no issues are present
				if(alertName != "") {
					Alert alert = new Alert(AlertType.INFORMATION, alertName, ButtonType.OK);
					alert.showAndWait();
				} else {
					user.setPass(passUtil.encrypt(newPass.getText()));
					pushUsers();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Password has been reset!", ButtonType.OK);
					alert.showAndWait();
					switchToMain(primary);
				}
				
			}
		});
		
		//styling the layout pane and adding components
		GridPane reset = new GridPane();
		reset.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
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
		reset.add(backBtn, 0, 9);
		Scene scene1 = new Scene(reset, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
	
	/**
	 * Switches the scene to the account sign up scene, where users can 
	 * set a new username, password, and security question and answer. 
	 * 
	 * @param primary the main stage where the scene will take place
	 */
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
		
		//back button
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");

		backBtn.setOnAction(event -> switchToMain(primary));
		
		//text field for coming up with an answer to the security question
		Label answer = new Label("Answer ");
		TextField qAnswer = new TextField();
		qAnswer.setMaxWidth(TEXTBOX_SIZE);
		
		//submit button to save changes
		Button submit = new Button("Submit");
		
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				createOrModifyAccount(primary, user.getText(), pass.getText(), confirm.getText(), (String)questionList.getValue(), qAnswer.getText());
			}
		});
		
		//styling the layout pane and adding components
		GridPane signup = new GridPane();
		signup.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
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
		signup.add(backBtn, 0, 9);
		Scene scene1 = new Scene(signup, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
	
	/**
	 * Switches the scene to the main course scene, where users can create, delete, 
	 * and rename courses. The features to delete and rename a course can be accessed
	 * by right clicking on the course. 
	 * 
	 * @param primary
	 */
	private void switchToCourses(Stage primary) {
		ArrayList<Course> listCourses = new ArrayList<Course>();
		ArrayList<Button> listButtons = new ArrayList<Button>();
		primary.setTitle("My Courses");
		//styling the buttons for entering, deleting a course, renaming, and logging out
		//also adds a tooltip to see instructions when cursor hovers over directions button
		Button goToAccount = new Button("Account");
		goToAccount.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #AFBAF7;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		Button enter = new Button("Enter");
		enter.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #0038A8;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");	
		Button directions = new Button("Instructions");
		directions.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF6A6A;" 
				+"-fx-font-size: 13;");
		Tooltip t = new Tooltip();
		t.setText("- To add a course: type the name of your course and press \"Enter\"\n"
				+ "- To rename a course: right click on the course and press \"Rename\", \n	then type in the new name and press the return key\n"
				+ "- To delete a course: right click on the course and press \"Delete\"");
		//t.setShowDelay(Duration.seconds(0));
		//t.setShowDuration(Duration.seconds(60));
		directions.setTooltip(t);
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
		
		//logs out of the app
		goToAccount.setOnAction(event -> switchToAccount(primary));
		
		// adds a new course 
		enter.setOnAction(event -> {
			String enterName = text.getText();
			if (!enterName.isEmpty()) {
				Button newCourse = new Button(enterName);
				newCourse.setStyle("-fx-text-fill: #000000;" + "-fx-background-color: #F5DE9C;" 
						+ "-fx-border-color: #EBC861; -fx-border-width: 2px;" 
						+ "-fx-cursor: hand;");
				newCourse.setMinSize(100, 60);
				signup.getChildren().add(newCourse);
				listCourses.add(new Course(enterName));
				listButtons.add(newCourse);
				text.clear();
				
				//creating the new drop down menu for each course
				//the menu can be accessed by right clicking the mouse
				ContextMenu menu = new ContextMenu();
				MenuItem deleteBtn = new MenuItem("Delete");
				MenuItem renameBtn = new MenuItem("Rename");
				menu.getItems().addAll(deleteBtn, renameBtn);
				newCourse.setContextMenu(menu);
				
				//option to delete the course
				deleteBtn.setOnAction(e -> signup.getChildren().remove(newCourse));
				
				//renaming the course
				TextField renaming = new TextField();
				renameBtn.setOnAction(f -> {
					renaming.setText(renaming.getText());
					newCourse.setText("");
					newCourse.setGraphic(renaming);
					renaming.setOnKeyPressed(g -> {
						if (g.getCode() == KeyCode.ENTER) {
							newCourse.setText(renaming.getText());
							newCourse.setGraphic(null);
						}
					});
				});
				
				//opening the actual course up
				newCourse.setOnAction(g -> {
					primary.setTitle(newCourse.getText());
					switchToCourseCards(primary);
				});
			}
		});
		
		signup.getChildren().addAll(text, enter, directions, goToAccount);
		signup.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");

		//set up the scene
		Scene scene1 = new Scene(signup, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
		
	}
	
	//Switches to Account Page
	private void switchToAccount(Stage primary) {
		pullUsers();
		primary.setTitle("Account Settings");
		
		//name of the application
		Label nameLbl = new Label("CardX");
		nameLbl.setFont(new Font("Georgia", 75));
		nameLbl.setPadding(new Insets(-20, 0, 50, 55));
		
		Button logOut = new Button("Log Out");
		logOut.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #0038A8;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");	
		
		Button modifyAccount = new Button("Modify Account");
		modifyAccount.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #0038A8;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");		
		
		Button deleteAccount = new Button("Delete Account");
		deleteAccount.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF6A6A;" 
				+"-fx-font-size: 13;");

		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		
		backBtn.setOnAction(event -> switchToCourses(primary));
		
		logOut.setOnAction(event -> {
			switchToMain(primary);
			CurrentUser = null;
		});
		
		modifyAccount.setOnAction(event -> switchToModifyAccount1(primary));
		deleteAccount.setOnAction(event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete your account? This action cannot be undone.", ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();
			if(alert.getResult() == ButtonType.YES) {
				for(User u: Users) {
					if(u.getUser().equals(CurrentUser.getUser())) {
						Users.remove(u);
						break;
					}
				}
				pushUsers();
				CurrentUser = null;
				switchToMain(primary);
			}
		});
		
		//"Welcome Back" text
		Label accountLbl = new Label("Account Settings");
		accountLbl.setFont(new Font("Georgia", 24));
		
			
		
		//styling the VBox pane where everything goes
		VBox root = new VBox();
		root.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
		root.setPadding(new Insets(100, 1, 100, 105));
		root.setSpacing(15);
		root.getChildren().add(nameLbl);
		root.getChildren().add(accountLbl);
		root.getChildren().addAll(modifyAccount, logOut, deleteAccount, backBtn);
		Scene scene = new Scene(root,550,550);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene);
		primary.show();
	}
	
	private void switchToModifyAccount1(Stage primary) {
		primary.setTitle("Confirm Password");
		
		//security question and space to answer
		Label resetPassLabel = new Label("Confirm Password");
		resetPassLabel.setFont(new Font("Georgia", 24));
		
		Label passLabel = new Label("Password");
		PasswordField pass = new PasswordField();
		pass.setMaxWidth(TEXTBOX_SIZE);
		
		//line break
		Label lineBreak = new Label("");
		
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");

		backBtn.setOnAction(event -> switchToAccount(primary));
		
		//continue button to switch to reset2 if username exists
		Button continueBtn = new Button("Continue");
		continueBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				//find the user
				
				if(passUtil.decrypt(CurrentUser.getPass()).equals(pass.getText())) {
					switchToModifyAccount2(primary);
				} else {
					Alert alert = new Alert(AlertType.INFORMATION, "Incorrect Password.", ButtonType.OK);
					alert.showAndWait();
				}
				
			}
		});
		
		//styling the layout pane and adding components
		GridPane reset = new GridPane();
		reset.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
		reset.setPadding(new Insets(100, 100, 100, 120));
		reset.setVgap(20);
		reset.setHgap(10);
		reset.add(resetPassLabel, 1, 0);
		reset.add(passLabel, 0, 3);
		reset.add(pass, 1, 3);
		reset.add(lineBreak, 1, 4);
		reset.add(continueBtn, 0, 8);
		reset.add(backBtn, 0, 9);
		Scene scene1 = new Scene(reset, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
	
	private void switchToModifyAccount2(Stage primary) {
		
		//creates the heading at the top of the screen 
		primary.setTitle("Modify Account");
		Label accountLabel = new Label("Modify Account");
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
		
		//back button
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");

		backBtn.setOnAction(event -> switchToModifyAccount1(primary));
		
		//text field for coming up with an answer to the security question
		Label answer = new Label("Answer ");
		TextField qAnswer = new TextField();
		qAnswer.setMaxWidth(TEXTBOX_SIZE);
		
		//submit button to save changes
		Button submit = new Button("Submit");
		
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				createOrModifyAccount(primary, user.getText(), pass.getText(), confirm.getText(), (String)questionList.getValue(), qAnswer.getText());
			}
		});
		
		//styling the layout pane and adding components
		GridPane signup = new GridPane();
		signup.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");
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
		signup.add(backBtn, 0, 9);
		Scene scene1 = new Scene(signup, 550, 550);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
	
	
	//switches to an individual course
	private void switchToCourseCards(Stage primary) {
		//list of actual cards
		ArrayList<Card> cardList = new ArrayList<Card>();
		//list of gridpanes representing cards
		ArrayList<GridPane> cardBoxes = new ArrayList<GridPane>();
		//lists of learned and not learned cards
		ArrayList<GridPane> learnedCards = new ArrayList<>();
		ArrayList<GridPane> notLearnedCards = new ArrayList<>();
		//styling the buttons for entering, deleting a course, renaming, and logging out
		//also adds a tooltip to see instructions when cursor hovers over directions button
		Button backBtn = new Button("Back");
		backBtn.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #FF0000;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		Button enter = new Button("Enter");
		enter.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #0038A8;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");	
		Label inputs = new Label("Type question & answer here:");
		
		// all cards, learned, unlearned buttons
		Button allButton = new Button("All Cards");
		Button learnedButton = new Button("Learned");
		Button notButton = new Button("Not Learned");
		Button shuffleButton = new Button("Shuffle");
		allButton.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #000080;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		learnedButton.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #000080;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		notButton.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #000080;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
		shuffleButton.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #000080;" 
				+"-fx-font-size: 13;" + "-fx-cursor: hand;");
				
		//set window look
		inputs.setFont(new Font("Georgia", 15));
		
		
		
		FlowPane cardPane = new FlowPane();
		cardPane.setVgap(10);
		cardPane.setHgap(10);
		cardPane.setOrientation(Orientation.HORIZONTAL);
		cardPane.getChildren().add(inputs);
		cardPane.setPrefWidth(730);
		cardPane.setPrefHeight(750);
		cardPane.setMargin(inputs, new Insets(20, 20, 20, 20));
		
		ScrollPane scrollCardPane = new ScrollPane();
		scrollCardPane.setContent(cardPane);
		scrollCardPane.setFitToHeight(true);
		scrollCardPane.setFitToWidth(true);
		
		TextField qText = new TextField();
		qText.setMaxWidth(250);
		TextField aText = new TextField();
		aText.setMaxWidth(250);
		
		
		//switches back to classes
		backBtn.setOnAction(event -> switchToCourses(primary));
		
		
		//creates and adds a new card 
		enter.setOnAction(event -> {
			String enterQuestion = qText.getText();
			String enterAnswer = aText.getText();
			if (!enterQuestion.isEmpty() && !enterAnswer.isEmpty()) {
				GridPane newCard = new GridPane();
				
				//the question and answer "fields"
				Button qButton = new Button(enterQuestion);
				Button aButton = new Button(enterAnswer);
				
				//learned and not learned checkbox			
				CheckBox c = new CheckBox("Learned?");
				//it's because whenever you make a new card, 
				//it's automatically not learned at first
				notLearnedCards.add(newCard);
				//if we press box and it becomes unlearned, add card to unlearned list
				//otherwise add card to learned list
				c.setOnAction(e -> {
					if (c.isSelected()) {
						learnedCards.add(newCard);
						notLearnedCards.remove(newCard);
					} else {
						notLearnedCards.add(newCard);
						learnedCards.remove(newCard);
					}
				});
				
				//formatting the card
				newCard.setStyle("-fx-text-fill: #000000;" + "-fx-background-color: #F5F5F5;" 
						+ "-fx-border-color: #808080; -fx-border-width: 2px;");
				newCard.setMinSize(400, 200);
				newCard.setPadding(new Insets(10, 10, 10, 10));
				
				//adding the card to the gridpane list and card lists
				cardPane.getChildren().add(newCard);
				cardList.add(new Card(enterQuestion, enterAnswer));
				cardBoxes.add(newCard);
				
				//clearing the textfields for further input
				qText.clear();
				aText.clear();
				
				//creating the button to delete
				Button deleteBtn = new Button("Delete");
				deleteBtn.setOnAction(e -> {
					cardBoxes.remove(newCard);
					cardPane.getChildren().remove(newCard);
					for(int i = 0; i < cardList.size(); i++) {
						if(cardList.get(i).getQuestion().equals(enterQuestion)) {
							cardList.remove(i);
						}
					}
				});
				
				
				//renaming the question - press question to rename
				TextField renamingQuestion = new TextField();
				qButton.setOnAction(f -> {
					renamingQuestion.setText(renamingQuestion.getText());
					qButton.setText("");
					qButton.setGraphic(renamingQuestion);
					renamingQuestion.setOnKeyPressed(g -> {
						if (g.getCode() == KeyCode.ENTER) {
							qButton.setText(renamingQuestion.getText());
							qButton.setGraphic(null);
						}
					});
					for(int i = 0; i < cardList.size(); i++) {
						if(cardList.get(i).getQuestion().equals(enterQuestion)) {
							cardList.get(i).setQuestion(renamingQuestion.getText());
						}
					}
					
				});
				
				//renaming the answer - press answer to rename
				TextField renamingAnswer = new TextField();
				aButton.setOnAction(f -> {
					renamingAnswer.setText(renamingAnswer.getText());
					aButton.setText("");
					aButton.setGraphic(renamingAnswer);
					renamingAnswer.setOnKeyPressed(g -> {
						if (g.getCode() == KeyCode.ENTER) {
							aButton.setText(renamingAnswer.getText());
							aButton.setGraphic(null);
						}
					});
					for(int i = 0; i < cardList.size(); i++) {
						if(cardList.get(i).getAnswer().equals(enterAnswer)) {
							cardList.get(i).setAnswer(renamingAnswer.getText());
						}
					}
				});
				
				//formatting the nodes of the card	
				newCard.setVgap(7);
				newCard.setHgap(140);
				GridPane.setHalignment(qButton, HPos.CENTER);
				newCard.add(qButton, 1, 0);
				GridPane.setHalignment(aButton, HPos.CENTER);
				newCard.add(aButton, 1, 1);
				GridPane.setHalignment(deleteBtn, HPos.CENTER);
				newCard.add(deleteBtn, 1, 4);
				newCard.getChildren().add(c);
			}
		});

		//presents learned cards
		//basically, the cards are reset to all visible at first
		//then, i iterate through all the cards
		//if the card is not in the entire list OR not in the list of learned cards
		//	then i make it not visible
		//this leaves only the learned cards visible
		//
		//this same concept applies to notButton
		learnedButton.setOnAction(e -> {
			cardPane.getChildren().removeAll(cardBoxes);
			cardPane.getChildren().addAll(cardBoxes);
			for (GridPane g : cardBoxes) {
				if (!cardBoxes.contains(g) || !learnedCards.contains(g)) {
					cardPane.getChildren().remove(g);
				}
			}
		});	
				
		//presents unlearned cards
		notButton.setOnAction(e -> {
			cardPane.getChildren().removeAll(cardBoxes);
			cardPane.getChildren().addAll(cardBoxes);
			for (GridPane g : cardBoxes) {
				if (!cardBoxes.contains(g) || !notLearnedCards.contains(g)) {
					cardPane.getChildren().remove(g);
				}
			}
		});
		
		//presents all cards
		allButton.setOnAction(a -> {
			cardPane.getChildren().removeAll(cardBoxes);
			cardPane.getChildren().addAll(cardBoxes);
		});
		
		shuffleButton.setOnAction(a -> {
			//clear existing cards
			cardPane.getChildren().removeAll(cardBoxes);
			
			//shuffle cardBoxes
			ArrayList<GridPane> cardBoxes2 = new ArrayList<GridPane>();
			while(cardBoxes.size() > 0) {
				int random = (int) (Math.random() * cardBoxes.size());
				GridPane j = cardBoxes.get(random);
				cardBoxes.remove(j);
				cardBoxes2.add(j);
			}
			for(int i = 0; i < cardBoxes2.size(); i++) {
				cardBoxes.add(cardBoxes2.get(i));
			}
			
			//add back all the cards to the cardPane in the new shuffled order
			cardPane.getChildren().addAll(cardBoxes);
			
		});

		cardPane.getChildren().addAll(qText, aText, enter, backBtn, allButton, learnedButton,
								notButton, shuffleButton);
		cardPane.setStyle("-fx-background-color: linear-gradient(to top,#6299F9, #FFFAEA)");

		//set up the scene
		Scene scene1 = new Scene(scrollCardPane, 1050, 750);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primary.setScene(scene1);
		primary.show();
	}
	
	/**
	 * Runs the application by having the main stage start with the main
	 * login scene. 
	 * 
	 * @param primary the main stage where application execution will take place
	 */
	public void runApp(Stage primary) {
		switchToMain(primary);
	}
	
	
}
