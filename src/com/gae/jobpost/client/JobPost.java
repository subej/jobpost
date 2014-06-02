package com.gae.jobpost.client;

import com.gae.jobpost.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JobPost implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// Convienent access to rootpanel
	final RootLayoutPanel rootpanel = RootLayoutPanel.get();

	// Boolean to determine if the user is employer or student
	private boolean isEmployer;
	// Boolean to ensure the user has loggedin 
	private boolean hasLoggedIn = false;

	// Create login panel that all users see before login
	private VerticalPanel loginPanel = new VerticalPanel();
	private VerticalPanel userInputPanel = new VerticalPanel(); 
	// Main panel
	DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.EM);

	// Alias of user
	private String userName = "";

	// Widgets to enter login details
	private TextBox enterName = new TextBox();
	private TextBox enterPassword = new TextBox();

	// Creates header above widgets
	private HTML header = new HTML("<h1>JobPost</h1>");
	private HTML greeting = new HTML("<p> Hello " + userName + "!</p>");
	private HTML loginMessage = new HTML("<p> Please select one of the following: </p>");
	private Label requestUserName = new Label("Username:");
	private Label requestPassword = new Label("Password:");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		dockPanel.addNorth(header, 6);
		rootpanel.add(dockPanel);
		if(!hasLoggedIn){loadLogin();}
		else{loadProperInterface();}
	}	

	// Loads the student interface
	private void loadStudentInterface(){
		dockPanel.addNorth(header, 6);
		dockPanel.addNorth(greeting, 2);
		Button logout = new Button("Logoff");
		dockPanel.addSouth(logout, 6);

		// Create tabs to go to relevant sections
		TabLayoutPanel studentTabPanel = new TabLayoutPanel(3.0, Unit.EM);
		studentTabPanel.setVisible(hasLoggedIn && !isEmployer);

		VerticalPanel jobs = new VerticalPanel();
		VerticalPanel portfolio = new VerticalPanel();
		VerticalPanel Profile = new VerticalPanel();

		studentTabPanel.add(jobs, "Job Postings");
		studentTabPanel.add(portfolio, "Portfolio");
		studentTabPanel.add(Profile, "Profile");

		logout.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				hasLoggedIn = false;
				dockPanel.clear();
				dockPanel.addNorth(header, 6);
				loadLogin();

			}

		});

		dockPanel.add(studentTabPanel);
	}

	// Loads the employer interface
	private void loadEmployerInterface(){
		dockPanel.addNorth(header, 6);
		dockPanel.addNorth(greeting, 2);
		Button logout = new Button("Logoff");
		dockPanel.addSouth(logout, 6);

		TabLayoutPanel employerTabPanel = new TabLayoutPanel(3.0, Unit.EM);
		employerTabPanel.setVisible(hasLoggedIn && isEmployer);

		VerticalPanel jobsPosted = new VerticalPanel();
		VerticalPanel studentResponses = new VerticalPanel();
		VerticalPanel currentOffers = new VerticalPanel();

		employerTabPanel.add(jobsPosted, "Your Postings");
		employerTabPanel.add(studentResponses, "Posting Candidates");
		employerTabPanel.add(currentOffers, "Offers Pending");

		logout.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				dockPanel.clear();
				dockPanel.addNorth(header, 6);
				hasLoggedIn = false;
				loadLogin();

			}

		});

		dockPanel.add(employerTabPanel);
	}

	// Loads login screen
	private void loadLogin(){
		loginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// Buttons for users to select at login
		final Button employer = new Button("Employer Login");
		final Button student = new Button("Student Login");
		final Button register = new Button("Register");

		loginPanel.add(loginMessage);
		loginPanel.add(employer);
		loginPanel.add(student);
		employer.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				hasLoggedIn = true;
				isEmployer = true;
				loginPanel.clear();
				dockPanel.clear();
				callLoginWindow(isEmployer);
			}

		});

		student.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				hasLoggedIn = true;
				isEmployer = false;
				loginPanel.clear();
				dockPanel.clear();
				callLoginWindow(false);
			}

		});
		
		register.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		dockPanel.add(loginPanel);
	}

	// Call to load the proper interface
	private void loadProperInterface(){
		if(isEmployer){
			loadEmployerInterface();
		} else{
			loadStudentInterface();
		}
	}

	// Call for the widget that will make login window
	private void callLoginWindow(final boolean isEmp){
		dockPanel.addNorth(header, 6);


		//userInputPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Button submit = new Button("Submit");
		
		// Add text boxes for users to put in information
		userInputPanel.add(requestUserName);
		userInputPanel.add(enterName);
		userInputPanel.add(requestPassword);
		userInputPanel.add(enterPassword);
		userInputPanel.add(submit);
		
		dockPanel.add(userInputPanel);
		
		submit.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				userName = enterName.getText().trim();
				if(userName.length() < 5){
					Window.alert("User name must be at least five characters");
					return;}
				hasLoggedIn = true;
				userInputPanel.clear();
				dockPanel.clear();
				resetGreeting(userName);
				if(isEmp){loadEmployerInterface();}
				else{loadStudentInterface();}
			}

		});
	}
	// Do later
	private void registerUser(){
		
	}
	
	private void resetGreeting(String s){
		greeting = new HTML("<p> Hello " + s + "!</p>");
	}

}
