package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    @FXML
    private Button btCancel;

    @FXML
    private Button btSave;

    @FXML
    private Label labelErro;

    @FXML
    private TextField nameInsert;

    @FXML
    private TextField idInsert;

    public void onBtCancelAction(){
        System.out.println("CANCELAR");
    }

    public void onBtSaveAction(){
        System.out.println("SALVAR");
    }

    private void initializeNodes(){
        Util.setTextFieldLength(nameInsert,30);
        Util.setTextFieldInt(idInsert);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }
}
