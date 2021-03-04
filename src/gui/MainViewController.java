package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import main.Program;
import services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemVendedor;
    @FXML
    private MenuItem menuItemDepartamento;
    @FXML
    private MenuItem menuItemSobre;

    public void onMenuItemVendedor() {
        System.out.print("onMenuItemVendedor");
    }

    public void onMenuItemDepartamento() {
        loadView2("/gui/ListDepartamento.fxml");
    }

    // Método Provisório
    private void loadView2(String s) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(s));
        try {
            VBox newVbox = loader.load();

            Scene aux_mainScene = Program.getPrincipal();

            VBox mainVbox = (VBox) ((ScrollPane) aux_mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().get(0);

            mainVbox.getChildren().clear();

            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVbox.getChildren());

            DepartmentListController controller = loader.getController();

            // Injetando dependência
            controller.setService(new DepartmentService());
            controller.updateTableView();

        } catch (IOException e){
            Util.showAlerts(Alert.AlertType.ERROR,"IO Exception", "Erro ao carregar janela", e.getMessage());
        }
    }

    public void onMenuItemSobre() {
        loadView("/gui/Sobre.fxml");
    }

    // A palavra synchronized garante que o processamento da interface gráfica não seja interrompido durante o multithreading do processador.

    private synchronized void loadView(String nomeAbsoluto){

        /*
        * O que faremos aqui é exibir o conteúdo do arquivo 'Sobre' dentro da Vbox da janela (cena) principal.
        *
        * Para isso é necessário puxar uma referência da cena principal para a classe na qual será usada.
        * O método getPrincipal(); retorna a cena principal.
        *
        * A variável newVbox recebe o conteúdo do arquivo FXML.
        * É necessário criar uma referência para a VBox da cena principal.
        *
        * Agora é necessário excluir todos <children> do mainVbox e em seguida incluir os elementos do MenuBar
        * e os <children> da nova cena (arquivo).
        */

        FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
        try {
            VBox newVbox = loader.load();

            Scene aux_mainScene = Program.getPrincipal();

            /*
            * O método getRoot(); retorna o elemento inicial da cena (que no caso é um ScrollPane) e logo em seguida
            * o método getContent(); retorna o conteúdo do ScrollPane (que no caso é uma VBox). Isso explica os typecasts utilizados.
            */

            VBox mainVbox = (VBox) ((ScrollPane) aux_mainScene.getRoot()).getContent();

            // Cria-se um Node auxiliar para receber o conteúdo do MenuBar.
            Node mainMenu = mainVbox.getChildren().get(0);

            // Comando para limpar todos os filhos de MainVbox.
            mainVbox.getChildren().clear();

            // Adicionando mainMenu e conteúdo do VBox de Sobre.fxml para o VBox da cena principal.
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVbox.getChildren());


        } catch (IOException e){
            Util.showAlerts(Alert.AlertType.ERROR,"IO Exception", "Erro ao carregar janela", e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
