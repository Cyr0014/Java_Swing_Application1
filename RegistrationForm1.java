import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class RegistrationForm1 extends Application {

    // Database credentials and URL
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/registration_db";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "Cyr011nq#N!29";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registration Form");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPrefWidth(300);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        Label mobileLabel = new Label("Mobile:");
        TextField mobileField = new TextField();
        gridPane.add(mobileLabel, 0, 1);
        gridPane.add(mobileField, 1, 1);

        Label genderLabel = new Label("Gender:");
        RadioButton maleRadio = new RadioButton("Male");
        RadioButton femaleRadio = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
        gridPane.add(genderLabel, 0, 2);
        gridPane.add(maleRadio, 1, 2);
        gridPane.add(femaleRadio, 2, 2);

        Label dobLabel = new Label("DOB:");
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Select Date");
        gridPane.add(dobLabel, 0, 3);
        gridPane.add(dobPicker, 1, 3);

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        gridPane.add(addressLabel, 0, 4);
        gridPane.add(addressField, 1, 4);

        CheckBox termsBox = new CheckBox("Accept Terms and Conditions");
        gridPane.add(termsBox, 0, 5, 2, 1);

        Button submitButton = new Button("Submit");
        Button resetButton = new Button("Reset");
        gridPane.add(submitButton, 0, 6);
        gridPane.add(resetButton, 1, 6);

        VBox displayBox = new VBox(10);
        displayBox.setPadding(new Insets(10));
        displayBox.setStyle("-fx-border-color: #c0c0c0; -fx-border-width: 1; -fx-background-color: #f0f0f0;");

        Label displayTitle = new Label("Entered Information:");
        displayTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label nameDisplay = new Label();
        Label mobileDisplay = new Label();
        Label genderDisplay = new Label();
        Label dobDisplay = new Label();
        Label addressDisplay = new Label();
        Label termsDisplay = new Label();

        displayBox.getChildren().addAll(displayTitle, nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        mobileField.textProperty().addListener((observable, oldValue, newValue) -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        maleRadio.setOnAction(event -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        femaleRadio.setOnAction(event -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        dobPicker.valueProperty().addListener((observable, oldValue, newValue) -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        addressField.textProperty().addListener((observable, oldValue, newValue) -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));
        termsBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateDisplay(nameDisplay, mobileDisplay, genderDisplay, dobDisplay, addressDisplay, termsDisplay, nameField, mobileField, genderGroup, dobPicker, addressField, termsBox));

        submitButton.setOnAction(event -> {
            if (termsBox.isSelected()) {
                saveDetailsToFile(nameField.getText(), mobileField.getText(), ((RadioButton) genderGroup.getSelectedToggle()).getText(), dobPicker.getValue() != null ? dobPicker.getValue().toString() : "Not selected", addressField.getText(), termsBox.isSelected());
                saveDetailsToDatabase(nameField.getText(), mobileField.getText(), ((RadioButton) genderGroup.getSelectedToggle()).getText(), dobPicker.getValue() != null ? dobPicker.getValue().toString() : null, addressField.getText(), termsBox.isSelected());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Details saved successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please accept terms and conditions to submit.");
                alert.showAndWait();
            }
        });

        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(gridPane, displayBox);

        Scene scene = new Scene(mainLayout, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveDetailsToDatabase(String name, String mobile, String gender, String dob, String address, boolean termsAccepted) {
        String insertSQL = "INSERT INTO users (name, mobile, gender, dob, address, terms_accepted) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                pstmt.setString(1, name);
                pstmt.setString(2, mobile);
                pstmt.setString(3, gender);
                if (dob != null) {
                    pstmt.setDate(4, java.sql.Date.valueOf(dob));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setString(5, address);
                pstmt.setBoolean(6, termsAccepted);

                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveDetailsToFile(String name, String mobile, String gender, String dob, String address, boolean termsAccepted) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write("Name: " + name);
            writer.newLine();
            writer.write("Mobile: " + mobile);
            writer.newLine();
            writer.write("Gender: " + gender);
            writer.newLine();
            writer.write("DOB: " + dob);
            writer.newLine();
            writer.write("Address: " + address);
            writer.newLine();
            writer.write("Accepted Terms: " + (termsAccepted ? "Yes" : "No"));
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDisplay(Label nameDisplay, Label mobileDisplay, Label genderDisplay, Label dobDisplay, Label addressDisplay, Label termsDisplay, TextField nameField, TextField mobileField, ToggleGroup genderGroup, DatePicker dobPicker, TextField addressField, CheckBox termsBox) {
        String name = "Name: " + nameField.getText();
        String mobile = "Mobile: " + mobileField.getText();
        String gender = "Gender: " + ((RadioButton) genderGroup.getSelectedToggle()).getText();
        String dob = "DOB: " + (dobPicker.getValue() != null ? dobPicker.getValue().toString() : "Not selected");
        String address = "Address: " + addressField.getText();
        String termsAccepted = "Accepted Terms: " + (termsBox.isSelected() ? "Yes" : "No");

        nameDisplay.setText(name);
        mobileDisplay.setText(mobile);
        genderDisplay.setText(gender);
        dobDisplay.setText(dob);
        addressDisplay.setText(address);
        termsDisplay.setText(termsAccepted);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
