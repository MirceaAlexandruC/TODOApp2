package com.mac.controller;

import com.mac.model.Task;
import com.mac.model.User;
import com.mac.repository.TaskRepository;
import com.mac.repository.UserRepository;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Controller {


    public Label lblInformation;
    public PasswordField pwdFieldConfirmRegister;
    public PasswordField pwdFieldRegister;
    public TextField txtFieldUsernameRegister;
    public Button btnRegister;
    public TextField txtFieldUsernameLogin;
    public PasswordField pwdFieldLogin;
    public Button btnLogin;
    public Button btnShowPwdRegister;
    public TextField txtFieldPwdRegister;
    public TextField txtFieldPwdConfirmRegister;
    public Button btnShowPwdConfirmRegister;
    public TextField txtFieldPwdLogin;
    public Label lblUsernameRegister;
    public Label lblPasswordRegister;
    public Label lblConfirmPasswordRegister;
    public Button btnShowLogin;
    public Label lblPasswordLogin;
    public Label lblUsernameLogin;
    public MenuItem mnuItemRegister;
    public MenuItem mnuItemLogin;
    public TabPane tabPane;
    public Tab tabRegister;
    public Tab tabLogin;
    public TextField txtFieldTODO;
    public Button btnInsert;
    public VBox vBoxTasks;
    public MenuItem mnuTask;
    public Tab tabTask;
    public TableView tblView;
    public TableColumn colTaskId;
    public TableColumn colTaskDescription;
    public TableColumn colUsername;
    public Tab tabAllTask;
    public Tab tabTasks;
    public VBox vBoxTaskList;
    public VBox vBoxTaskListAllocated;
    public Tab tabProgress;
    public VBox vBoxTaskInProgress;
    public VBox vBoxTaskDone;
    public TextField txtTaskInProgress;
    public TextField txtTaskDone;



    private UserRepository userRepository;
    private TaskRepository taskRepository;

    private User loggedInUser;

    private boolean isConnectionSuccessful = false;

    public void initialize(){
        try {
            colTaskId.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
            colTaskDescription.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
            colUsername.setCellValueFactory(new PropertyValueFactory<Task, String>("user"));
            persistenceConnection();

        }
        catch (Exception e){
            System.out.println("Connection is not allowed");
            e.printStackTrace();
            isConnectionSuccessful = false;
        }
//        tabPane.getTabs().clear();

    }

    private void persistenceConnection() {
        EntityManagerFactory entityManagerFactory=
                Persistence.createEntityManagerFactory("TODOFx");
        EntityManager entityManager= entityManagerFactory.createEntityManager();

        userRepository = new UserRepository(entityManager);
        taskRepository = new TaskRepository(entityManager);
    }

    public void registerUser(ActionEvent actionEvent) {
        try {
            registerWarnings();
            if(isAlreadyIn()) {
            }
            else{
            tabPane.getTabs().remove(tabRegister);
            tabPane.getTabs().add(tabLogin);
            }
        } catch (Exception e) {
            System.out.println("You can not connect");
            e.printStackTrace();
        }
    }

    private void registerWarnings() {
        lblUsernameFieldRegisterWarning();
        lblPasswordWarning();
        lblConfirmPasswordWarning();
        passwordRegisterNotMatch();
        pwdSpecialCharactersRegister();
    }

    private boolean isAlreadyIn() {
        User user = userRepository.findByUsername(txtFieldUsernameRegister.getText());
        if (user == null) {
            user = new User();
            user.setUsername(txtFieldUsernameRegister.getText());
            user.setPassword(pwdFieldRegister.getText());

            userRepository.save(user);
            return false;
        } else {
            lblInformation.setTextFill(Color.RED);
            lblInformation.setVisible(true);
            lblInformation.setText("Is already in");
            return true;
        }
    }

    private void lblUsernameFieldRegisterWarning() {
        if (txtFieldUsernameRegister.getText().length() < 1) {
            lblConfirmPasswordRegister.setTextFill(Color.BLACK);
            lblPasswordRegister.setTextFill(Color.BLACK);
            lblUsernameRegister.setTextFill(Color.RED);
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Please fill Username");
            userRepository.save(null);
        } else{
            lblUsernameRegister.setTextFill(Color.BLACK);
        }
    }

    private void lblPasswordWarning(){
        if(pwdFieldRegister.getText().length() < 1) {
                lblUsernameRegister.setTextFill(Color.BLACK);
                lblConfirmPasswordRegister.setTextFill(Color.BLACK);
                lblPasswordRegister.setTextFill(Color.RED);
                lblInformation.setVisible(true);
                lblInformation.setTextFill(Color.RED);
                lblInformation.setText("Please fill Password field");
                userRepository.save(null);
        }else {
            lblPasswordRegister.setTextFill(Color.BLACK);

        }
    }

    private void lblConfirmPasswordWarning() {
        if(pwdFieldConfirmRegister.getText().length() < 1) {
            lblPasswordRegister.setTextFill(Color.BLACK);
            lblUsernameRegister.setTextFill(Color.BLACK);
            lblConfirmPasswordRegister.setTextFill(Color.RED);
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Please fill Confirm password field");
            userRepository.save(null);
        } else {
            lblConfirmPasswordRegister.setTextFill(Color.BLACK);
        }
    }

    private void passwordRegisterNotMatch(){
        if(!pwdFieldRegister.getText().equals(pwdFieldConfirmRegister.getText())){
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Passwords not match");
            userRepository.save(null);
        }
        else{
            lblConfirmPasswordRegister.setTextFill(Color.BLACK);
        }
    }

    private void pwdSpecialCharactersRegister(){
        String regex = "([a-zA-Z]+[0-9]...)";
        //(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$"
        String string = pwdFieldRegister.getText();
        if(!string.matches(regex)){
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Please write with special characters");
            userRepository.save(null);
        }
        else {
            lblInformation.setVisible(false);
        }
    }

    public void loginUser(ActionEvent actionEvent) {
        lblUsernameLoginWarning();
        lblPasswordLoginWarning();

        loggedInUser = userRepository.findByUsername(txtFieldUsernameLogin.getText());
        if(loggedInUser != null) {
            tabPane.getTabs().clear();
            tabPane.getTabs().add(tabTask);
            tabPane.getTabs().add(tabAllTask);
            tabPane.getTabs().add(tabTasks);
            tabPane.getTabs().add(tabProgress);

        }else {
            lblInformation.setVisible(true);
            lblInformation.setText("User not exist");
        }

    }

    public void showPassword(ActionEvent actionEvent) {
       if( !txtFieldPwdRegister.isVisible()){
           btnShowPwdRegister.setText("Hide");
           txtFieldPwdRegister.setText(pwdFieldRegister.getText());
           txtFieldPwdRegister.setEditable(false);
           txtFieldPwdRegister.setVisible(true);
           pwdFieldRegister.setVisible(false);
        }
       else{
           btnShowPwdRegister.setText("Show");
           txtFieldPwdRegister.setVisible(false);
           pwdFieldRegister.setVisible(true);
       }
    }

    public void showConfirmPassword(ActionEvent actionEvent) {
        if( !txtFieldPwdConfirmRegister.isVisible()){
            btnShowPwdConfirmRegister.setText("Hide");
            txtFieldPwdConfirmRegister.setText(pwdFieldConfirmRegister.getText());
            txtFieldPwdConfirmRegister.setEditable(false);
            txtFieldPwdConfirmRegister.setVisible(true);
            pwdFieldConfirmRegister.setVisible(false);
        }
        else{
            btnShowPwdConfirmRegister.setText("Show");
            txtFieldPwdConfirmRegister.setVisible(false);
            pwdFieldConfirmRegister.setVisible(true);
        }
    }

    public void showPasswordLogin(ActionEvent actionEvent) {
        if( !txtFieldPwdLogin.isVisible()){
            btnShowLogin.setText("Hide");
            txtFieldPwdLogin.setText(pwdFieldLogin.getText());
            txtFieldPwdLogin.setEditable(false);
            txtFieldPwdLogin.setVisible(true);
            pwdFieldLogin.setVisible(false);
        }
        else{
            btnShowLogin.setText("Show");
            txtFieldPwdLogin.setVisible(false);
            pwdFieldLogin.setVisible(true);
        }
    }

    public void lblUsernameLoginWarning (){
        if (txtFieldUsernameLogin.getText().length() < 1) {
            lblUsernameLogin.setTextFill(Color.RED);
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Please fill Username field");
        } else{
            lblUsernameLogin.setTextFill(Color.BLACK);
        }
    }

    public void lblPasswordLoginWarning (){
        if (pwdFieldLogin.getText().length() < 1) {
            lblPasswordLogin.setTextFill(Color.RED);
            lblInformation.setVisible(true);
            lblInformation.setTextFill(Color.RED);
            lblInformation.setText("Please fill Password field");
        } else{
            lblPasswordLogin.setTextFill(Color.BLACK);
        }

//        if(pwdFieldLogin != pwdFieldRegister){
//
//        }
    }

    public void showRegisterPane(ActionEvent actionEvent) {
        tabPane.getTabs().add(tabRegister);
    }

    public void showLoginPane(ActionEvent actionEvent) {
        tabPane.getTabs().add(tabLogin);
    }

    public void insertTaskEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            insertTask();
        }
    }

    private void insertTask() {
        Task task = new Task();
        task.setCreatedAt(new Date());
        task.setDescription(txtFieldTODO.getText());

        taskRepository.save(task);

        CheckBox checkBox = new CheckBox();
        checkBox.setText(task.getDescription());

        vBoxTasks.getChildren().add(checkBox);
    }


    public void insertTask(ActionEvent actionEvent) {
        insertTask();
    }

    public void loadTasks(Event event) {
        List<Task> tasks = taskRepository.findAll();
        final ObservableList<Task> dbTasks = FXCollections.observableList(tasks);
        tblView.setItems(dbTasks);
        System.out.println("Tasks loaded");
    }

    public void loadTaskList(Event event) {

        vBoxTaskList.getChildren().clear();
        vBoxTaskListAllocated.getChildren().clear();
        List<Task> tasks =  taskRepository.findAll();
        for ( Task task : tasks) {
            CheckBox checkBox = new CheckBox();

             if(task.getUser() !=null) {
                task.setInProgress(true);
                checkBox.setText(task.getId() + ". " + task.getDescription()
                + "allocated to " + task.getUser().getUsername());
                checkBox.setDisable(true);
                vBoxTaskListAllocated.getChildren().add(checkBox);
                task.setInProgress(true);

            }else{
                vBoxTaskList.getChildren().add(checkBox);
                checkBox.setText(task.getId() + ". " + task.getDescription());
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    task.setUser(loggedInUser);
                    task.setInProgress(true);
                    taskRepository.save(task);
                    loadTaskList(null);
                });
            }
        }
    }

    public void loadProgress(Event event) {
        vBoxTaskInProgress.getChildren().clear();
        vBoxTaskDone.getChildren().clear();
        List<Task> tasks= taskRepository.findAll();

    for(Task task : tasks){

        CheckBox checkBox = new CheckBox(task.getId() + ". " +task.getDescription() + " in progress" );
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            task.setInProgress(false);
            vBoxTaskInProgress.getChildren().remove(checkBox);
            CheckBox checkBox1 = new CheckBox(task.getId() + ". " + task.getDescription() + " is done by " + task.getUser().getUsername());
            vBoxTaskDone.getChildren().add(checkBox1);
            checkBox1.setDisable(true);
            task.setTaskDone(true);
            taskRepository.save(task);
            loadTaskList(null);
                });
        vBoxTaskInProgress.getChildren().add(checkBox);

//            CheckBox checkBox = new CheckBox();
//            if (task.getUser() !=null){
//                vBoxTaskInProgress.getChildren().add(checkBox);
//                checkBox.setText(task.getId() + ". " + task.getDescription()+ " in progress ");
//                task.getInProgress();
//                task.setTaskDone(true);
//            }
//            else{
//                vBoxTaskDone.getChildren().add(checkBox);
//                checkBox.setText(task.getId() + ". " + task.getDescription() + " done!");
//                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                        loadProgress(event);
//                        task.getTaskDone();
//                    });
//                }
            }
            }
        }
