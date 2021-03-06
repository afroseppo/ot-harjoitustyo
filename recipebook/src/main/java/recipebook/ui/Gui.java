/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.ui;

import java.io.FileInputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import recipebook.dao.DatabaseRecipeDao;
import recipebook.dao.DatabaseUserDao;
import recipebook.dao.RecipeDao;
import recipebook.dao.UserDao;
import recipebook.domain.Recipe;
import recipebook.domain.RecipebookService;

/**
 * A class that implements the GUI for the recipebook.
 *
 * @author tiitinha
 */
public class Gui extends Application {

    private RecipebookService recipebook;
    private Scene loginScene, newUserScene, mainScene, addRecipeScene, ingredientScene, recipeSearchScene;
    private String recipeName;
    private String searchFieldRecipeName;
    private Label menuLabel = new Label();

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));

        String database = properties.getProperty("database");
        String databasePath = properties.getProperty("databasePath");

        String databaseFile = databasePath + database;

        UserDao userDao = new DatabaseUserDao(databaseFile);
        RecipeDao recipeDao = new DatabaseRecipeDao(databaseFile);

        recipebook = new RecipebookService(recipeDao, userDao);
        if (!recipebook.connectToDatabase(databaseFile)) {
            System.out.println("ERROR! Database cannot be connected, shutting down!");

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        /* Setup the login window */
        VBox welcomePane = new VBox(20);
        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        Label welcomeLabel = new Label("Welcome to the Recipebook!");
        TextField usernameInput = new TextField();
        PasswordField usernamePassword = new PasswordField();

        inputPane.getChildren().addAll(loginLabel, usernameInput, passwordLabel, usernamePassword);
        welcomePane.getChildren().add(welcomeLabel);
        Label loginMessage = new Label();

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create new user");

        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = usernamePassword.getText();
            menuLabel.setText(username + " logged in..");

            if (recipebook.login(username, password)) {
                loginMessage.setText("");
                primaryStage.setScene(mainScene);
                usernameInput.clear();
            } else {
                loginMessage.setText("Invalid username or password!");
                loginMessage.setTextFill(Color.RED);
            }

            usernameInput.clear();
            usernamePassword.clear();
        });

        createButton.setOnAction(e -> {
            usernameInput.clear();
            usernamePassword.clear();
            primaryStage.setScene(newUserScene);
        });

        loginPane.getChildren().addAll(welcomePane, loginMessage, inputPane, loginButton, createButton);

        loginScene = new Scene(loginPane, 750, 500);

        /* Setup for creation of a new user */
        HBox menuPaneNewUser = new HBox(10);
        menuPaneNewUser.setPadding(new Insets(10));
        Region menuSpacerNewUser = new Region();
        HBox.setHgrow(menuSpacerNewUser, Priority.ALWAYS);
        Button backButtonCreateUser = new Button("Back");

        backButtonCreateUser.setOnAction(e -> {
            primaryStage.setScene(loginScene);
        });

        menuPaneNewUser.getChildren().addAll(menuSpacerNewUser, backButtonCreateUser);

        Label createNewUserLabel = new Label("The username must be at least 3 characters long and the password 5.");
        VBox newUserPane = new VBox(10);
        HBox newUsernamePane = new HBox(10);

        newUsernamePane.setPadding(new Insets(10));
        TextField newUsername = new TextField();
        Label newUsernameLabel = new Label("Username");
        newUsernameLabel.setPrefWidth(150);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsername);

        HBox newUserPasswordPane = new HBox(10);
        newUserPasswordPane.setPadding(new Insets(10));
        PasswordField newUserPassword = new PasswordField();
        Label newUserPasswordLabel = new Label("Password");
        newUserPasswordLabel.setPrefWidth(150);
        newUserPasswordPane.getChildren().addAll(newUserPasswordLabel, newUserPassword);

        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("Create a new user");

        createNewUserButton.setOnAction((ActionEvent e) -> {
            String username = newUsername.getText();
            String password = newUserPassword.getText();

            if (username.length() < 3) {
                userCreationMessage.setText("Username is too short!");
            } else if (!username.matches("[a-zA-Z0-9]")) {
                userCreationMessage.setText("Invalid username, only letters and numbers allowed!");
            }
            if (password.length() < 5) {
                userCreationMessage.setText("Password too short!");
            } else if (recipebook.createUser(username, password)) {
                userCreationMessage.setText("");
                newUsername.setText("");
                newUserPassword.setText("");
                loginMessage.setText("New user created!");
                loginMessage.setTextFill(Color.GREEN);
                primaryStage.setScene(loginScene);
            } else {
                userCreationMessage.setText("Username already in use. Username has to be unique");
                userCreationMessage.setTextFill(Color.RED);
            }
        });

        newUsername.clear();
        newUserPassword.clear();

        newUserPane.getChildren().addAll(menuPaneNewUser, createNewUserLabel, userCreationMessage, newUsernamePane, newUserPasswordPane, createNewUserButton);

        newUserScene = new Scene(newUserPane, 750, 500);

        /* Setup the main scene */
        VBox mainScenePane = new VBox(10);
        mainScenePane.setPadding(new Insets(10));

        HBox menuPane = new HBox(10);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");

        menuPane.getChildren().addAll(menuLabel, menuSpacer, logoutButton);

        logoutButton.setOnAction(e -> {
            recipebook.logout();
            primaryStage.setScene(loginScene);
        });

        Label searchRecipeLabel = new Label("Search a recipe by name.");
        Label searchRecipeNameLabel = new Label("Recipe name: ");
        VBox searchRecipePane = new VBox(10);
        HBox searchRecipeNamePane = new HBox(10);
        Button searchRecipeButton = new Button("Search");
        TextField searchRecipeName = new TextField();
        searchRecipeName.prefWidth(50);

        searchRecipeNamePane.getChildren().addAll(searchRecipeNameLabel, searchRecipeName);

        searchRecipeButton.setOnAction(e -> {
            searchFieldRecipeName = searchRecipeName.getText();
            primaryStage.setScene(searchResultScene(searchFieldRecipeName, primaryStage));
        });

        searchRecipePane.getChildren().addAll(searchRecipeLabel, searchRecipeNamePane, searchRecipeButton);

        Button changeToAddRecipeSceneButton = new Button("Add a recipe");

        changeToAddRecipeSceneButton.setOnAction(e -> {
            searchRecipeName.clear();
            primaryStage.setScene(addRecipeScene);
        });

        mainScenePane.getChildren().addAll(menuPane, searchRecipePane, changeToAddRecipeSceneButton);

        mainScene = new Scene(mainScenePane, 750, 500);

        /* Setup the scene for adding a new recipe */
        VBox addNewRecipeScenePane = new VBox(10);
        addNewRecipeScenePane.setPadding(new Insets(10));

        HBox menuPaneNewRecipe = new HBox(10);
        Region menuSpacerNewRecipe = new Region();
        HBox.setHgrow(menuSpacerNewRecipe, Priority.ALWAYS);
        Button backButton = new Button("Back");
        Button newRecipeSceneLogoutButton = new Button("Logout");

        backButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);
        });

        newRecipeSceneLogoutButton.setOnAction(e -> {
            recipebook.logout();
            primaryStage.setScene(loginScene);
        });

        menuPaneNewRecipe.getChildren().addAll(menuSpacerNewRecipe, backButton, newRecipeSceneLogoutButton);

        HBox addNewRecipePane = new HBox(10);
        Label newRecipeNameLabel = new Label("Recipe name");
        TextField newRecipeName = new TextField();
        Button addNewRecipeButton = new Button("Add a new recipe");

        addNewRecipeButton.setOnAction(e -> {

            recipeName = newRecipeName.getText();
            recipebook.createNewRecipe(newRecipeName.getText(), recipebook.getLoggedUser().getName());
            primaryStage.setScene(ingredientScene);
            newRecipeName.clear();
        });

        addNewRecipePane.getChildren().addAll(newRecipeNameLabel, newRecipeName, addNewRecipeButton);

        addNewRecipeScenePane.getChildren().addAll(menuPaneNewRecipe, addNewRecipePane);

        addRecipeScene = new Scene(addNewRecipeScenePane, 750, 500);

        /* Setup for stage for adding ingredients to a recipe */
        HBox menuPaneNewIngredient = new HBox(10);
        Region menuSpacerNewIngredient = new Region();
        HBox.setHgrow(menuSpacerNewIngredient, Priority.ALWAYS);
        Button backButtonNewIngredient = new Button("Back");
        Button newIngredientSceneLogoutButton = new Button("Logout");

        backButtonNewIngredient.setOnAction(e -> {
            primaryStage.setScene(mainScene);
        });

        newIngredientSceneLogoutButton.setOnAction(e -> {
            recipebook.logout();
            primaryStage.setScene(loginScene);
        });

        menuPaneNewIngredient.getChildren().addAll(menuSpacerNewRecipe, backButton, newRecipeSceneLogoutButton);

        HBox ingredientRecipeNamePane = new HBox(10);
        Label ingredientRecipeName = new Label(recipeName);
        ingredientRecipeNamePane.getChildren().add(ingredientRecipeName);

        HBox ingredientNamePane = new HBox(10);
        HBox ingredientAmountPane = new HBox(10);
        HBox ingredientUnitPane = new HBox(10);
        VBox ingredientPane = new VBox(10);

        ingredientPane.setPadding(new Insets(10));

        Label ingredientInfo = new Label("Add a new ingredient to the recipe by pressing 'Add ingredient'. When ready, click 'Save' to save the recipe.");

        Label ingredientNameLabel = new Label("Ingredient name");
        TextField ingredientName = new TextField();

        Label ingredientAmountLabel = new Label("Amount");
        TextField ingredientAmount = new TextField();

        Label ingredientUnitLabel = new Label("Unit");
        TextField ingredientUnit = new TextField();

        Button addIngredientButton = new Button("Add ingredient");

        ingredientNamePane.getChildren().addAll(ingredientNameLabel, ingredientName);
        ingredientAmountPane.getChildren().addAll(ingredientAmountLabel, ingredientAmount);
        ingredientUnitPane.getChildren().addAll(ingredientUnitLabel, ingredientUnit);

        addIngredientButton.setOnAction(e -> {
            if (ingredientAmount.getText().matches("-?\\d+(\\.\\d+)?") && !recipeName.isEmpty()) {
                recipebook.addIngredient(recipeName, ingredientName.getText(), Integer.parseInt(ingredientAmount.getText()), ingredientUnit.getText());

                ingredientName.clear();
                ingredientAmount.clear();
                ingredientUnit.clear();
            } else {
                Label ingredientAmountNotNumeric = new Label("Ingredient amount is not a number!");
                ingredientPane.getChildren().add(ingredientAmountNotNumeric);
            }
        });

        Button saveRecipeToDatabaseButton = new Button("Save recipe");

        saveRecipeToDatabaseButton.setOnAction(e -> {

        });

        ingredientPane.getChildren().addAll(ingredientRecipeNamePane, menuPaneNewIngredient, ingredientInfo, ingredientNamePane, ingredientAmountPane, ingredientUnitPane, addIngredientButton, saveRecipeToDatabaseButton);

        ingredientScene = new Scene(ingredientPane, 750, 500);

        /* Setup the primary stage and scene */
        primaryStage.setTitle("Recipebook");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("closing");
            if (recipebook.getLoggedUser() != null) {
                e.consume();
            }

        });
    }

    /**
     * Method to build the scene for search results. Returns a Scene-object.
     *
     * @param recipeName The name of the recipe which is searched
     * @param primaryStage The PrimaryStage
     * @return Scene-object of the search result scene.
     */
    public Scene searchResultScene(String recipeName, Stage primaryStage) {
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);
        });

        menuPane.getChildren().addAll(menuSpacer, backButton);

        VBox searchResultPane = new VBox(10);
        searchResultPane.setPadding(new Insets(10));
        Label searchResultLabel = new Label("");
        Label searchResultIngredients = new Label("");

        Recipe recipe = recipebook.fetchRecipe(recipeName);

        if (recipe == null) {
            searchResultLabel.setText("No recipe with name " + recipeName + " found.");
        } else {
            searchResultLabel.setText(recipe.getName());

            recipe.getIngredients().values().forEach((ingredient) -> {
                searchResultIngredients.setText(searchResultIngredients.getText() + "\n" + ingredient.toString());
            });

        }

        searchResultPane.getChildren().addAll(menuPane, searchResultLabel, searchResultIngredients);

        Scene searchResultScene = new Scene(searchResultPane, 750, 500);

        return searchResultScene;

    }

    @Override
    public void stop() {
        System.out.println("The application is now closed. Thank you for using the recipebook.");
    }

}
