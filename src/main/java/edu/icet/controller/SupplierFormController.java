package edu.icet.controller;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.service.ServiceFactory;
import edu.icet.service.interfaces.SupplierService;
import edu.icet.util.ServiceType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtContactPerson;
    
    @FXML
    private TextField txtPhone;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextArea txtAddress;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    private SupplierService supplierService;
    private SupplierDTO supplier;
    private SupplierController parentController;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supplierService = ServiceFactory.getInstance().getService(ServiceType.SUPPLIER);
        
        btnSave.setOnAction(event -> handleSave());
        btnCancel.setOnAction(event -> handleCancel());
    }
    
    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
        if (supplier != null) {
            txtName.setText(supplier.getName());
            txtContactPerson.setText(supplier.getContactPerson());
            txtPhone.setText(supplier.getPhone());
            txtEmail.setText(supplier.getEmail());
            txtAddress.setText(supplier.getAddress());
        }
    }
    
    public void setParentController(SupplierController parentController) {
        this.parentController = parentController;
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        if (supplier == null) {
            supplier = new SupplierDTO();
        }
        
        supplier.setName(txtName.getText().trim());
        supplier.setContactPerson(txtContactPerson.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());
        
        boolean success;
        if (supplier.getId() == null) {
            success = supplierService.saveSupplier(supplier);
        } else {
            success = supplierService.updateSupplier(supplier);
        }
        
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier saved successfully.");
            if (parentController != null) {
                parentController.handleRefresh();
            }
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier.");
        }
    }
    
    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name is required.");
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone is required.");
            return false;
        }
        return true;
    }
    
    private void handleCancel() {
        closeWindow();
    }
    
    private void closeWindow() {
        if (contentArea != null) {
            // Navigate back to supplier list view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplier-view.fxml"));
                Parent view = loader.load();
                
                SupplierController controller = loader.getController();
                controller.setContentArea(contentArea);
                
                contentArea.getChildren().clear();
                contentArea.getChildren().add(view);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Close modal window
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
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
