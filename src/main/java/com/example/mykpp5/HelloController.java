package com.example.mykpp5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class HelloController {
    @FXML
    private AnchorPane main_form;
    @FXML
    private TableView<MeteorologicalObservations> AtmoshereData_TableView;
    @FXML
    private TableColumn<MeteorologicalObservations, Integer> ID_column;
    @FXML
    private TextField ID_textField, pressure_TextField, temperature_textField;
    @FXML
    private Button closeBtn, deleteBtn, hideBtn,showResultBtn, updateBtn;
    @FXML
    private DatePicker datePicker_date;
    @FXML
    private TableColumn<MeteorologicalObservations, Date> date_column;
    @FXML
    private TableColumn<MeteorologicalObservations, Double> pressure_column;
    @FXML
    private TableColumn<MeteorologicalObservations, Double> temperature_column;
    //////////////////////////////////////////////
    private ObservableList<MeteorologicalObservations> meteorologicalObservations;
    private MeteorologicalObservations meteorlogicalClass = new MeteorologicalObservations();


    public void initializeData(){
        MeteorologicalObservations.initializeObservations();
    }
    public void loadData(){
        meteorologicalObservations = FXCollections.observableArrayList(meteorlogicalClass.getAllObservations());

        ID_column.setCellValueFactory(new PropertyValueFactory<>("ID"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("observationDate"));
        temperature_column.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        pressure_column.setCellValueFactory(new PropertyValueFactory<>("pressure"));
        AtmoshereData_TableView.setItems(meteorologicalObservations);
    }
    public void addNewData(){
        if(temperature_textField.getText().isEmpty()
                || ID_textField.getText().isEmpty()
                || datePicker_date.getValue() == null
                || pressure_TextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, заповніть усі поля перед додаванням.");
            alert.showAndWait();
            return;
        }
        try {
            Double temperature = Double.parseDouble(temperature_textField.getText());
            Double pressure = Double.parseDouble(pressure_TextField.getText());
            LocalDate localDate = datePicker_date.getValue();
            java.sql.Date date = java.sql.Date.valueOf(localDate);
            meteorlogicalClass.addObservation(date,temperature,pressure);
            loadData();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, введіть числові значення для температури та тиску.");
            alert.showAndWait();
        }
    }
    public void deleteData(){
        if(temperature_textField.getText().isEmpty()
                || ID_textField.getText().isEmpty()
                || datePicker_date.getValue() == null
                || pressure_TextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, заповніть усі поля перед додаванням.");
            alert.showAndWait();
            return;
        }
        try{
        Boolean isValid = false;
        Integer statID = Integer.parseInt(ID_textField.getText());
        for(MeteorologicalObservations meteoData : meteorologicalObservations){
            if(meteoData.getID() == statID){
                isValid = true;
            }
        }
        if(isValid){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Підтвердження");
            alert.setHeaderText(null);
            alert.setContentText("Ви бажаєте видалити цей запис?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.get().equals(ButtonType.OK)){
                meteorlogicalClass.deleteObservationById(statID);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Інформація");
                alert.setHeaderText(null);
                alert.setContentText("Запис був успішно видалений.");
                alert.showAndWait();
                loadData();
            }
        }
    }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, введіть числове значення для ідентифікатора(ID).");
            alert.showAndWait();
        }
    }
    public void updateData(){
        if(temperature_textField.getText().isEmpty()
                || ID_textField.getText().isEmpty()
                || datePicker_date.getValue() == null
                || pressure_TextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, заповніть усі поля перед додаванням.");
            alert.showAndWait();
            return;
        }
        try{
            Boolean isValid = false;
            Integer statID = Integer.parseInt(ID_textField.getText());
            for(MeteorologicalObservations meteoData : meteorologicalObservations){
                if(meteoData.getID() == statID){
                    isValid = true;
                }
            }
            if(isValid){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Підтвердження");
                alert.setHeaderText(null);
                alert.setContentText("Ви бажаєте змінити цей запис?");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.get().equals(ButtonType.OK)){
                    LocalDate localDate = datePicker_date.getValue();
                    java.sql.Date date = java.sql.Date.valueOf(localDate);
                    Double temperature = Double.parseDouble(temperature_textField.getText());
                    Double pressure = Double.parseDouble(pressure_TextField.getText());

                    meteorlogicalClass.updateObservation(statID, date, temperature, pressure);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Інформація");
                    alert.setHeaderText(null);
                    alert.setContentText("Запис був успішно змінений.");
                    alert.showAndWait();
                    loadData();
                }
            }
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, введіть числове значення для ідентифікатора(ID).");
            alert.showAndWait();
        }
    }
    ///////////////////////////////
    public void introduceData(){
        List<MeteorologicalObservations> LargestNumsData = meteorlogicalClass.getDaysWithLargestPressureDifference();
        AtmoshereData_TableView.setItems(FXCollections.observableArrayList(LargestNumsData));
    }
    public void curretnDate(){
        Date joinDateUtil = new Date();
        java.sql.Date joinDateSql = new java.sql.Date(joinDateUtil.getTime());
        datePicker_date.setValue(joinDateSql.toLocalDate());
    }
    //////////////////////////////////
    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    public void initialize(){
        initializeData();
        loadData();
    }
}