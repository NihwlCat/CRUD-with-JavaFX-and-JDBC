package gui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class Util {

    public static Stage palcoAtual(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
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

}
