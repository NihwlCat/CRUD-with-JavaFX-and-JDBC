package gui;

import db.DBIntegrityException;
import entities.Seller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listener.DataChangeListener;
import main.Program;
import services.DepartmentService;
import services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;


// DataChangeListener : interface para implementar o Observer do Subject

public class SellerListController implements Initializable, DataChangeListener {

    // Dependência que precisa ser injetada através de uma classe externa para evitar forte acoplamento.

    private SellerService service;

    private ObservableList<Seller> obsList;

    @FXML
    private TableColumn<Seller,String> tableColumnemail;

    @FXML
    private TableColumn<Seller,Date> tableColumnDate;

    @FXML
    private TableColumn<Seller,Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller,Seller> tableColumnREMOVE;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML // TableColumn<Tipo_entidade,Tipo_coluna>
    private TableColumn<Seller,Integer> tableColumnInteger;

    @FXML
    private TableColumn<Seller,String> tableColumnString;

    @FXML
    private Button buttonNovo;

    @FXML
    private TableColumn<Seller,Seller> tableColumnEDIT;

    public void setService(SellerService service) {
        this.service = service;
    }



    public void updateTableView(){
        if (service == null) {
            throw new IllegalStateException("Service não instanciado");
        }
        obsList = FXCollections.observableArrayList(service.findAll());
        tableViewSeller.setItems(obsList);
        initButtons();
        removeButtons();
    }

    public void onButtonNovoAction(ActionEvent event){
        // event é uma referência para o controle que recebeu o evento. No caso o botão

        // instanciação provisório de Seller que é passada como argumento para a função de iniciar a tela.

        Seller obj = new Seller();
        obj.setName("");
        initDialogForm(obj,"/gui/SellerForm.fxml",Util.palcoAtual(event));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();

    }

    private void removeButtons(){
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller,Seller>(){
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Seller obj, boolean empty){
                super.updateItem(obj,empty);

                if(obj == null){
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj){
        // Optional é usado para carregar um objeto. É uma classe que armazena um objeto.
        Optional<ButtonType> res = Util.showConfirmation(Alert.AlertType.CONFIRMATION,"Remover departamento","Confirme para remover");
        if(res.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Serviço não injetado");
            }

            try {
                service.removeSeller(obj);
            } catch (DBIntegrityException e){
                Util.showAlerts(Alert.AlertType.ERROR,"ERRO",null,e.getMessage());
            }
            updateTableView();

        }
    }
    private void initButtons (){
        // Não entendi porra nenhuma desse boilerplate

        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller,Seller>(){
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Seller obj, boolean empty){
                super.updateItem(obj,empty);

                if(obj == null){
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> initDialogForm(obj,"/gui/SellerForm.fxml",Util.palcoAtual(event)));
            }
        });
    }

    private void initializeNodes(){
        // Padrão do JavaFX para iniciar comportamento das colunas.

        tableColumnInteger.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnString.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Util.formatTableColumnDate(tableColumnDate,"dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Util.formatTableColumnDouble(tableColumnBaseSalary,2);
        tableColumnemail.setCellValueFactory(new PropertyValueFactory<>("email")); /*O argumento precisa ser o nome do atributo da classe*/

        // Window é superclasse de Stage, portanto o downcasting.

        Stage stage = (Stage) Program.getPrincipal().getWindow();

        // Fazer TableView acompanhar a altura da janela.

        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    private void initDialogForm(Seller obj, String absolutString, Stage stage){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutString));

        try {
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira o nome do vendedor: ");
            dialogStage.setScene(new Scene(pane));

            //Injetando dependência de obj para o Controller da tela SellerForm.
            SellerFormController controller = loader.getController();
            controller.setEntidade(obj);
            controller.setServices(new SellerService(),new DepartmentService());
            controller.loadDepartmentList();
            controller.subscribeListener(this); // Inscrevendo essa classe na lista de listeners da Subject
            controller.updateFormData();

            dialogStage.setResizable(false);
            //Método para inserir o palco Pai do palco que é o formulário de diálogo.
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            Util.showAlerts(Alert.AlertType.ERROR,"IO Exception", "Erro ao carregar janela", e.getMessage());
        }

    }

    @Override
    public void onChanged() {
        updateTableView();
    }
}
