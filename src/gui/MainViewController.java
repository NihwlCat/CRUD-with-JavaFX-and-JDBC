package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
        System.out.print("onMenuItemDepartamento");
    }

    public void onMenuItemSobre() {
        System.out.print("onMenuItemSobre");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
