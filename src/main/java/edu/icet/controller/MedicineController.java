package edu.icet.controller;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.service.interfaces.MedicineService;
import edu.icet.service.ServiceFactory;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedicineController implements Initializable {
    
    @FXML
    private TextField txtSearch;
    
    @FXML
    private TableView<MedicineDTO> tblMedicines;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colId;
    
    @FXML
    private TableColumn<MedicineDTO, String> colName;
    
    @FXML
    private TableColumn<MedicineDTO, String> colBrand;
    
    @FXML
    private TableColumn<MedicineDTO, String> colCategory;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colQuantity;
    
    @FXML
    private TableColumn<MedicineDTO, Double> colPrice;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colReorderLevel;
    
    @FXML
    private TableColumn<MedicineDTO, LocalDate> colExpiry;
    
    @FXML
    private TableColumn<MedicineDTO, String> colSupplier;
    
    @FXML
    private Button btnEdit;
    
    @FXML
    private Button btnDelete;
    
    private MedicineService medicineService;
    private ObservableList<MedicineDTO> medicineList;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicineService = ServiceFactory.getInstance().getService(ServiceType.MEDICINE);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colReorderLevel.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));
        colExpiry.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        
        loadMedicines();
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    private void loadMedicines() {
        List<MedicineDTO> medicines = medicineService.getAllMedicines();
        medicineList = FXCollections.observableArrayList(medicines);
        tblMedicines.setItems(medicineList);
    }
    
    @FXML
    public void handleSearch() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadMedicines();
        } else {
            List<MedicineDTO> medicines = medicineService.searchMedicines(searchText);
            medicineList = FXCollections.observableArrayList(medicines);
            tblMedicines.setItems(medicineList);
        }
    }
    
    @FXML
    private void handleAdd() {
        showMedicineForm(null);
    }
    
    @FXML
    private void handleEdit() {
        MedicineDTO selected = tblMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showMedicineForm(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a medicine to edit.");
        }
    }
    
    @FXML
    private void handleDelete() {
        MedicineDTO selected = tblMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Medicine");
            confirmAlert.setContentText("Are you sure you want to delete this medicine?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (medicineService.deleteMedicine(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine deleted successfully.");
                    loadMedicines();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete medicine.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a medicine to delete.");
        }
    }
    
    @FXML
    public void handleRefresh() {
        loadMedicines();
        txtSearch.clear();
    }
    
    private void showMedicineForm(MedicineDTO medicine) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/medicine-form.fxml"));
            Parent root = loader.load();
            
            MedicineFormController controller = loader.getController();
            controller.setMedicine(medicine);
            controller.setParentController(this);
            controller.setContentArea(contentArea);
            
            if (contentArea != null) {
                // Load in same pane
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                // Fallback to modal window if contentArea not set
                Stage stage = new Stage();
                stage.setTitle(medicine == null ? "Add Medicine" : "Edit Medicine");
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
