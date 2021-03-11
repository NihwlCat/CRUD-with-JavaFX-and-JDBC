package gui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;


public class Util {

    public static Stage palcoAtual(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Double tryParsetoDouble(String str){
        try{
            return Double.parseDouble(str);
        } catch (NumberFormatException e){
            return null;
        }
    }
    public static Integer tryParsetoInt(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e){
            return null;
        }
    }

    public static void showAlerts (AlertType type, String ... args){
        Alert alert = new Alert(type);
        alert.setTitle(args[0]);
        alert.setHeaderText(args[1]);
        alert.setContentText(args[2]);

        alert.show();
    }


    // Alerta de confirmação para remover departamento. Interface Optional<>
    public static Optional<ButtonType> showConfirmation(AlertType type, String ... args){
        Alert alert = new Alert(type);

        alert.setTitle(args[0]);
        alert.setHeaderText(null);
        alert.setContentText(args[1]);

        return alert.showAndWait();
    }

    public static void setTextFieldInt(TextField txt){
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null && !newValue.matches("\\d*")){
                txt.setText(oldValue);
            }
        });
    }

    public static void setTextFieldDouble(TextField txt) {
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null && !newValue.matches("\\d*([.]\\d*)?")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void setTextFieldLength(TextField txt, int max){
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null && newValue.length() > max)
                txt.setText(oldValue);
        });
    }

    /////////

    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
        tableColumn.setCellFactory(column -> {
            TableCell<T, Date> cell = new TableCell<T, Date>() {
                private SimpleDateFormat sdf = new SimpleDateFormat(format);
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(sdf.format(item));
                    }
                }
            };
            return cell;
        });
    }
    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
        tableColumn.setCellFactory(column -> {
            TableCell<T, Double> cell = new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Locale.setDefault(Locale.US);
                        setText(String.format("%."+decimalPlaces+"f", item));
                    }
                }
            };
            return cell;
        });
    }

    public static void formatDatePicker(DatePicker datePicker, String format) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);{
                datePicker.setPromptText(format.toLowerCase());
            } // Mas o que seria isso?
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

}
