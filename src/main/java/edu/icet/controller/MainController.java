package edu.icet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button btnDashboard;
    
    @FXML
    private Button btnMedicines;
    
    @FXML
    private Button btnSuppliers;
    
    @FXML
    private Button btnSales;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboard();
    }
    
    @FXML
    private void loadDashboard() {
        loadView("/view/dashboard-view.fxml");
        highlightButton(btnDashboard);
    }
    
    @FXML
    private void loadMedicines() {
        loadView("/view/medicine-view.fxml");
        highlightButton(btnMedicines);
    }
    
    @FXML
    private void loadSuppliers() {
        loadView("/view/supplier-view.fxml");
        highlightButton(btnSuppliers);
    }
    
    @FXML
    private void loadSales() {
        loadView("/view/sales-view.fxml");
        highlightButton(btnSales);
    }
    
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Pass contentArea reference to controllers that need it
            Object controller = loader.getController();
            if (controller instanceof MedicineController) {
                ((MedicineController) controller).setContentArea(contentArea);
            } else if (controller instanceof SupplierController) {
                ((SupplierController) controller).setContentArea(contentArea);
            } else if (controller instanceof SalesController) {
                ((SalesController) controller).setContentArea(contentArea);
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load view: " + fxmlPath + " - " + e.getMessage());
        }
    }
    
    private void highlightButton(Button activeButton) {
        btnDashboard.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        btnMedicines.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        btnSuppliers.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        btnSales.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        
        activeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
    }
}
