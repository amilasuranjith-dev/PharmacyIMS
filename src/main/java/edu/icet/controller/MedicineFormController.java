package edu.icet.controller;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.model.dto.SupplierDTO;
import edu.icet.service.interfaces.MedicineService;
import edu.icet.service.ServiceFactory;
import edu.icet.service.interfaces.SupplierService;
import edu.icet.util.ServiceType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MedicineFormController implements Initializable {
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtBrand;
    
    @FXML
    private TextField txtCategory;
    
    @FXML
    private TextField txtQuantity;
    
    @FXML
    private TextField txtPrice;
    
    @FXML
    private TextField txtReorderLevel;
    
    @FXML
    private DatePicker dpExpiry;
    
    @FXML
    private ComboBox<SupplierDTO> cmbSupplier;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    private MedicineService medicineService;
    private SupplierService supplierService;
    private MedicineDTO medicine;
    private MedicineController parentController;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicineService = ServiceFactory.getInstance().getService(ServiceType.MEDICINE);
        supplierService = ServiceFactory.getInstance().getService(ServiceType.SUPPLIER);
        
        loadSuppliers();
        
        btnSave.setOnAction(event -> handleSave());
        btnCancel.setOnAction(event -> handleCancel());
    }
    
    private void loadSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        cmbSupplier.setItems(FXCollections.observableArrayList(suppliers));
        
        cmbSupplier.setConverter(new StringConverter<SupplierDTO>() {
            @Override
            public String toString(SupplierDTO supplier) {
                return supplier == null ? "" : supplier.getName();
            }
            
            @Override
            public SupplierDTO fromString(String string) {
                return null;
            }
        });
    }
    
    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
        if (medicine != null) {
            txtName.setText(medicine.getName());
            txtBrand.setText(medicine.getBrand());
            txtCategory.setText(medicine.getCategory());
            txtQuantity.setText(String.valueOf(medicine.getQuantity()));
            txtPrice.setText(String.valueOf(medicine.getUnitPrice()));
            txtReorderLevel.setText(String.valueOf(medicine.getReorderLevel()));
            dpExpiry.setValue(medicine.getExpiryDate());
            
            for (SupplierDTO supplier : cmbSupplier.getItems()) {
                if (supplier.getId().equals(medicine.getSupplierId())) {
                    cmbSupplier.setValue(supplier);
                    break;
                }
            }
        }
    }
    
    public void setParentController(MedicineController parentController) {
        this.parentController = parentController;
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            if (medicine == null) {
                medicine = new MedicineDTO();
            }

            medicine.setName(txtName.getText().trim());
            medicine.setBrand(txtBrand.getText().trim());
            medicine.setCategory(txtCategory.getText().trim());
            medicine.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
            medicine.setUnitPrice(Double.parseDouble(txtPrice.getText().trim()));

            String reorderText = txtReorderLevel.getText() == null ? "" : txtReorderLevel.getText().trim();
            int reorderLevel = reorderText.isEmpty() ? 0 : Integer.parseInt(reorderText);
            medicine.setReorderLevel(reorderLevel);

            medicine.setExpiryDate(dpExpiry.getValue());
            medicine.setSupplierId(cmbSupplier.getValue().getId());
            
            boolean success;
            if (medicine.getId() == null) {
                success = medicineService.saveMedicine(medicine);
            } else {
                success = medicineService.updateMedicine(medicine);
            }
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine saved successfully.");
                // Refresh parent list
                if (parentController != null) {
                    try {
                        parentController.handleRefresh();
                    } catch (Exception ignored) {
                        // If parent controller isn't fully initialized, it's safe to ignore.
                    }
                }
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save medicine.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for quantity, price, and reorder level.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name is required.");
            return false;
        }
        if (txtBrand.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Brand is required.");
            return false;
        }
        if (txtCategory.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Category is required.");
            return false;
        }

        String qtyText = txtQuantity.getText().trim();
        if (qtyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity is required.");
            return false;
        }
        try {
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a whole number.");
            return false;
        }

        String priceText = txtPrice.getText().trim();
        if (priceText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Unit price is required.");
            return false;
        }
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Unit price must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Unit price must be a number.");
            return false;
        }

        String reorderText = txtReorderLevel.getText() == null ? "" : txtReorderLevel.getText().trim();
        if (!reorderText.isEmpty()) {
            try {
                int reorder = Integer.parseInt(reorderText);
                if (reorder < 0) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Reorder level can't be negative.");
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Reorder level must be a whole number.");
                return false;
            }
        }

        if (dpExpiry.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Expiry date is required.");
            return false;
        }
        if (cmbSupplier.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier is required.");
            return false;
        }
        return true;
    }
    
    private void handleCancel() {
        closeWindow();
    }
    
    private void closeWindow() {
        if (contentArea != null) {
            // Navigate back to medicine list view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/medicine-view.fxml"));
                Parent view = loader.load();
                
                MedicineController controller = loader.getController();
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
