package gui;

import db.DBException;
import entities.Department;
import exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import listener.DataChangeListener;
import services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department entidade;

    private DepartmentService service;


    // Implementação do conceito de Subject do padrão Observer.
    // Lista de listeners, métodos para entrar na lista e notificar objetos inscritos sobre a mudança.

    private List<DataChangeListener> list = new ArrayList<>();

    public void subscribeListener(DataChangeListener listener){
        list.add(listener);
    }

    private void notifyListeners(){
        for(DataChangeListener listener : list){
            listener.onChanged();
        }
    }

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

    private Department getFormData(){
        Department obj = new Department();

        ValidationException excep = new ValidationException("Validation errors");

        obj.setId(Util.tryParsetoInt(idInsert.getText()));

        if (nameInsert.getText() == null || nameInsert.getText().trim().equals(""))
            excep.addErrors("name","Campo vazio");

        obj.setName(nameInsert.getText());

        if (excep.getErrors().size() > 0){
            throw excep;
        }

        return obj;
    }

    public void setEntidade(Department ent){
        entidade = ent;
    }

    public void setService(DepartmentService service){
        this.service = service;
    }

    public void onBtCancelAction(ActionEvent event){
        Util.palcoAtual(event).close();
    }

    public void onBtSaveAction(ActionEvent event){

        if(entidade == null){
            throw new IllegalStateException("Entidade = null");
        }
        if(service == null){
            throw new IllegalStateException("Service = null");
        }

        try {
            entidade = getFormData();
            service.saveOrUpdate(entidade);
            notifyListeners();
            Util.palcoAtual(event).close();
            // Notificando que houve alteração.
        } catch(ValidationException e){

            setErrorsMessage(e.getErrors());
        } catch(DBException e){

            Util.showAlerts(Alert.AlertType.ERROR,"DB Exception", null, e.getMessage());
        }

    }

    private void initializeNodes(){
        Util.setTextFieldLength(nameInsert,30);
        Util.setTextFieldInt(idInsert);
    }

    public void updateFormData(){

        if(entidade == null){
            throw new IllegalStateException("Entidade não instanciada");
        }

        nameInsert.setText(String.valueOf(entidade.getName()));
        idInsert.setText(String.valueOf(entidade.getId()));

    }

    private void setErrorsMessage(Map<String,String> errors){
        Set<String> keys = errors.keySet();

        if(keys.contains("name")){
            labelErro.setText(errors.get("name"));
        } else {
            labelErro.setText("");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }
}
