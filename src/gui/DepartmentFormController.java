package gui;

import entities.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import listener.DataChangeListener;
import services.DepartmentService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

        obj.setId(Util.tryParsetoInt(idInsert.getText()));
        obj.setName(nameInsert.getText());

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

    public void onBtSaveAction(){

        entidade = getFormData();
        service.saveOrUpdate(entidade);
        notifyListeners();
        // Notificando que houve alteração.
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }
}
