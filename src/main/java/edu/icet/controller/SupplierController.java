package edu.icet.controller;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.service.ServiceFactory;
import edu.icet.service.interfaces.SupplierService;
import edu.icet.util.ServiceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {
    
    @FXML
    private TextField txtSearch;
    
    @FXML
    private TableView<SupplierDTO> tblSuppliers;
    
    @FXML
    private TableColumn<SupplierDTO, Integer> colId;
    
    @FXML
    private TableColumn<SupplierDTO, String> colName;
    
    @FXML
    private TableColumn<SupplierDTO, String> colContact;
    
    @FXML
    private TableColumn<SupplierDTO, String> colPhone;
    
    @FXML
    private TableColumn<SupplierDTO, String> colEmail;
    
    @FXML
    private TableColumn<SupplierDTO, String> colAddress;
    
    @FXML
    private Button btnEdit;
    
    @FXML
    private Button btnDelete;
    
    private SupplierService supplierService;
    private ObservableList<SupplierDTO> supplierList;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supplierService = ServiceFactory.getInstance().getService(ServiceType.SUPPLIER);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        loadSuppliers();
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    private void loadSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        supplierList = FXCollections.observableArrayList(suppliers);
        tblSuppliers.setItems(supplierList);
    }
    
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadSuppliers();
        } else {
            List<SupplierDTO> suppliers = supplierService.searchSuppliers(searchText);
            supplierList = FXCollections.observableArrayList(suppliers);
            tblSuppliers.setItems(supplierList);
        }
    }
    
    @FXML
    public void handleRefresh() {
        loadSuppliers();
        txtSearch.clear();
    }
    
    @FXML
    private void handleAdd() {
        showSupplierForm(null);
    }
    
    @FXML
    private void handleEdit() {
        SupplierDTO selected = tblSuppliers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showSupplierForm(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a supplier to edit.");
        }
    }
    
    @FXML
    private void handleDelete() {
        SupplierDTO selected = tblSuppliers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Supplier");
            confirmAlert.setContentText("Are you sure you want to delete this supplier?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (supplierService.deleteSupplier(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully.");
                    loadSuppliers();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a supplier to delete.");
        }
    }

    
    private void showSupplierForm(SupplierDTO supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplier-form.fxml"));
            Parent root = loader.load();
            
            SupplierFormController controller = loader.getController();
            controller.setSupplier(supplier);
            controller.setParentController(this);
            controller.setContentArea(contentArea);
            
            if (contentArea != null) {
                // Load in same pane
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                // Fallback to modal window if contentArea not set
                Stage stage = new Stage();
                stage.setTitle(supplier == null ? "Add Supplier" : "Edit Supplier");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load form: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
