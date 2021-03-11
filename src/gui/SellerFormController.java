package gui;

import db.DBException;
import entities.Department;
import entities.Seller;
import exceptions.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import listener.DataChangeListener;
import services.DepartmentService;
import services.SellerService;

import java.net.URL;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entidade;

    private SellerService service;
    private DepartmentService dpService;


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
    private Label labelErroDate;

    @FXML
    private Label labelErroEmail;

    @FXML
    private Label labelBaseSalary;

    @FXML
    private TextField nameInsert;

    @FXML
    private TextField idInsert;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField baseSalary;

    @FXML
    private ComboBox<Department> departmentComboBox;

    public void loadDepartmentList(){
        departmentComboBox.setItems(FXCollections.observableArrayList(dpService.findAll()));
    }

    private Seller getFormData(){
        Seller obj = new Seller();

        ValidationException excep = new ValidationException("Validation errors");

        obj.setId(Util.tryParsetoInt(idInsert.getText()));

        if (nameInsert.getText() == null || nameInsert.getText().trim().equals(""))
            excep.addErrors("name","Campo obrigatório");

        obj.setName(nameInsert.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals(""))
            excep.addErrors("email","Campo obrigatório");

        obj.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() == null)
            excep.addErrors("date","Campo obrigatório");
        else {
            Instant instante = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthDate(Date.from(instante));
        }

        if (baseSalary.getText() == null || baseSalary.getText().trim().equals(""))
            excep.addErrors("salary","Campo obrigatório");

        obj.setBaseSalary(Util.tryParsetoDouble(baseSalary.getText()));

        obj.setDepartment(departmentComboBox.getValue());


        if (excep.getErrors().size() > 0){
            throw excep;
        }

        return obj;
    }

    public void setEntidade(Seller ent){
        entidade = ent;
    }

    public void setServices(SellerService service, DepartmentService dpService){
        this.dpService = dpService;
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

        // Constraints
        Util.setTextFieldLength(nameInsert,30);
        Util.setTextFieldInt(idInsert);

        Util.setTextFieldDouble(baseSalary);
        Util.setTextFieldLength(txtEmail,50);

        Util.formatDatePicker(dpBirthDate,"dd/MM/yyyy");
    }

    public void updateFormData(){

        if(entidade == null){
            throw new IllegalStateException("Entidade não instanciada");
        }

        nameInsert.setText(String.valueOf(entidade.getName()));
        idInsert.setText(String.valueOf(entidade.getId()));

        txtEmail.setText(entidade.getEmail());
        baseSalary.setText(String.format("%.2f", entidade.getBaseSalary()));

        if(entidade.getBirthDate() != null)
        dpBirthDate.setValue(LocalDate.from(LocalDateTime.ofInstant(entidade.getBirthDate().toInstant(),ZoneId.systemDefault())));

        ////
        if(entidade.getDepartment() == null)
            departmentComboBox.getSelectionModel().selectFirst();
        else
            departmentComboBox.setValue(entidade.getDepartment());

    }

    private void setErrorsMessage(Map<String,String> errors){
        Set<String> keys = errors.keySet();

        /*if(keys.contains("name")){
            labelErro.setText(errors.get("name"));
        }
        if(keys.contains("salary")){
            labelBaseSalary.setText(errors.get("salary"));
        }
        if(keys.contains("email")){
            labelErroEmail.setText(errors.get("email"));
        }
        if(keys.contains("date")){
            labelErroDate.setText(errors.get("date"));
        }*/

        labelErro.setText(keys.contains("name") ? errors.get("name") : "");
        labelBaseSalary.setText(keys.contains("salary") ? errors.get("salary") : "");
        labelErroEmail.setText(keys.contains("email") ? errors.get("email") : "");
        labelErroDate.setText(keys.contains("date") ? errors.get("date") : "");

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        departmentComboBox.setCellFactory(factory);
        departmentComboBox.setButtonCell(factory.call(null));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        initializeComboBoxDepartment();
    }
}
