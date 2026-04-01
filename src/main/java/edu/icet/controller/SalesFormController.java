package edu.icet.controller;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.model.dto.SaleDTO;
import edu.icet.model.dto.SaleItemDTO;
import edu.icet.service.interfaces.MedicineService;
import edu.icet.service.interfaces.SaleService;
import edu.icet.service.ServiceFactory;
import edu.icet.util.ServiceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SalesFormController implements Initializable {
    
    @FXML
    private TextField txtInvoice;
    
    @FXML
    private TextField txtNotes;
    
    @FXML
    private ComboBox<MedicineDTO> cmbMedicine;
    
    @FXML
    private TextField txtQuantity;
    
    @FXML
    private TableView<SaleItemDTO> tblItems;
    
    @FXML
    private TableColumn<SaleItemDTO, String> colItemMedicine;
    
    @FXML
    private TableColumn<SaleItemDTO, Integer> colItemQuantity;
    
    @FXML
    private TableColumn<SaleItemDTO, Double> colItemPrice;
    
    @FXML
    private TableColumn<SaleItemDTO, Double> colItemSubtotal;
    
    @FXML
    private Label lblSubtotal;
    
    @FXML
    private TextField txtDiscount;
    
    @FXML
    private Label lblTotal;
    
    @FXML
    private TextField txtPaid;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    private SaleService saleService;
    private MedicineService medicineService;
    private SalesController parentController;
    private ObservableList<SaleItemDTO> saleItems;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saleService = ServiceFactory.getInstance().getService(ServiceType.SALE);
        medicineService = ServiceFactory.getInstance().getService(ServiceType.MEDICINE);
        
        txtInvoice.setText(saleService.generateInvoiceNumber());
        
        colItemMedicine.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colItemSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        saleItems = FXCollections.observableArrayList();
        tblItems.setItems(saleItems);
        
        loadMedicines();
        
        btnSave.setOnAction(event -> handleSave());
        btnCancel.setOnAction(event -> handleCancel());
    }
    
    private void loadMedicines() {
        cmbMedicine.setItems(FXCollections.observableArrayList(medicineService.getAllMedicines()));
        cmbMedicine.setConverter(new StringConverter<MedicineDTO>() {
            @Override
            public String toString(MedicineDTO medicine) {
                return medicine == null ? "" : medicine.getName() + " - " + medicine.getBrand();
            }
            
            @Override
            public MedicineDTO fromString(String string) {
                return null;
            }
        });
    }
    
    public void setParentController(SalesController parentController) {
        this.parentController = parentController;
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    @FXML
    private void handleAddItem() {
        MedicineDTO medicine = cmbMedicine.getValue();
        String qtyText = txtQuantity.getText().trim();
        
        if (medicine == null || qtyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please select medicine and enter quantity.");
            return;
        }
        
        try {
            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Quantity must be greater than 0.");
                return;
            }
            
            if (quantity > medicine.getQuantity()) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock", 
                         "Available quantity: " + medicine.getQuantity());
                return;
            }
            
            SaleItemDTO item = new SaleItemDTO();
            item.setMedicineId(medicine.getId());
            item.setMedicineName(medicine.getName() + " - " + medicine.getBrand());
            item.setQuantity(quantity);
            item.setUnitPrice(medicine.getUnitPrice());
            item.setSubtotal(quantity * medicine.getUnitPrice());
            
            saleItems.add(item);
            
            cmbMedicine.setValue(null);
            txtQuantity.clear();
            
            calculateTotal();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid quantity.");
        }
    }
    
    @FXML
    private void handleRemoveItem() {
        SaleItemDTO selected = tblItems.getSelectionModel().getSelectedItem();
        if (selected != null) {
            saleItems.remove(selected);
            calculateTotal();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to remove.");
        }
    }
    
    @FXML
    public void calculateTotal() {
        double subtotal = saleItems.stream()
            .mapToDouble(SaleItemDTO::getSubtotal)
            .sum();
        
        lblSubtotal.setText(String.format("%.2f", subtotal));
        
        double discount = 0.0;
        try {
            discount = Double.parseDouble(txtDiscount.getText().trim());
        } catch (NumberFormatException e) {
            discount = 0.0;
            txtDiscount.setText("0.00");
        }
        
        double total = subtotal - discount;
        lblTotal.setText(String.format("%.2f", total));
    }
    
    private void handleSave() {
        if (saleItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please add at least one item.");
            return;
        }
        
        if (txtPaid.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please enter paid amount.");
            return;
        }
        
        try {
            double paidAmount = Double.parseDouble(txtPaid.getText().trim());
            double total = Double.parseDouble(lblTotal.getText());
            
            if (paidAmount < total) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Paid amount must be at least the total amount.");
                return;
            }
            
            SaleDTO sale = new SaleDTO();
            sale.setInvoiceNumber(txtInvoice.getText());
            sale.setTotalAmount(total);
            sale.setDiscount(Double.parseDouble(txtDiscount.getText().trim()));
            sale.setPaidAmount(paidAmount);
            sale.setNotes(txtNotes.getText().trim());
            sale.setItems(new ArrayList<>(saleItems));
            
            if (saleService.saveSale(sale)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Sale completed successfully.");
                if (parentController != null) {
                    parentController.handleRefresh();
                }
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save sale.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid amounts.");
        }
    }
    
    private void handleCancel() {
        closeWindow();
    }
    
    private void closeWindow() {
        if (contentArea != null) {
            // Navigate back to sales list view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sales-view.fxml"));
                Parent view = loader.load();
                
                SalesController controller = loader.getController();
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
