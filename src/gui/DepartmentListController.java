package gui;

import entities.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Program;
import services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    // Dependência que precisa ser injetada através de uma classe externa para evitar forte acoplamento.

    private DepartmentService service;

    private ObservableList<Department> obsList;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML // TableColumn<Tipo_entidade,Tipo_coluna>
    private TableColumn<Department,Integer> tableColumnInteger;

    @FXML
    private TableColumn<Department,String> tableColumnString;

    @FXML
    private Button buttonNovo;

    public void setService(DepartmentService service) {
        this.service = service;
    }

    public void updateTableView(){
        if (service == null) {
            throw new IllegalStateException("Service não instanciado");
        }
        obsList = FXCollections.observableArrayList(service.findAll());
        tableViewDepartment.setItems(obsList);
    }

    public void onButtonNovoAction(ActionEvent event){
        // event é uma referência para o controle que recebeu o evento. No caso o botão
        initDialogForm("/gui/DepartmentForm.fxml",Util.palcoAtual(event));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();

    }

    private void initializeNodes(){
        // Padrão do JavaFX para iniciar comportamento das colunas.

        tableColumnInteger.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnString.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Window é superclasse de Stage, portanto o downcasting.

        Stage stage = (Stage) Program.getPrincipal().getWindow();

        // Fazer TableView acompanhar a altura da janela.

        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    private void initDialogForm(String absolutString, Stage stage){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutString));

        try {
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira o nome do departamento: ");
            dialogStage.setScene(new Scene(pane));

            dialogStage.setResizable(false);
            //Método para inserir o palco Pai do palco que é o formulário de diálogo.
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            Util.showAlerts(Alert.AlertType.ERROR,"IO Exception", "Erro ao carregar janela", e.getMessage());
        }

    }
}
