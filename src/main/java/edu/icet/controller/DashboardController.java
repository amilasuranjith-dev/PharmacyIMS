package edu.icet.controller;

import edu.icet.model.dto.DashboardDTO;
import edu.icet.model.dto.MedicineDTO;
import edu.icet.service.interfaces.DashboardService;
import edu.icet.service.ServiceFactory;
import edu.icet.util.ServiceType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML
    private Label lblTotalMedicines;
    
    @FXML
    private Label lblLowStock;
    
    @FXML
    private Label lblExpiring;
    
    @FXML
    private Label lblTodaySales;
    
    @FXML
    private TableView<MedicineDTO> tblLowStock;
    
    @FXML
    private TableColumn<MedicineDTO, String> colLowStockName;
    
    @FXML
    private TableColumn<MedicineDTO, String> colLowStockBrand;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colLowStockQty;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colLowStockReorder;
    
    @FXML
    private TableView<MedicineDTO> tblExpiring;
    
    @FXML
    private TableColumn<MedicineDTO, String> colExpiringName;
    
    @FXML
    private TableColumn<MedicineDTO, String> colExpiringBrand;
    
    @FXML
    private TableColumn<MedicineDTO, LocalDate> colExpiringDate;
    
    @FXML
    private TableColumn<MedicineDTO, Integer> colExpiringQty;
    
    private DashboardService dashboardService;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboardService = ServiceFactory.getInstance().getService(ServiceType.DASHBOARD);
        
        colLowStockName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLowStockBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colLowStockQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colLowStockReorder.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));
        
        colExpiringName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExpiringBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colExpiringDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        colExpiringQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        DashboardDTO dashboard = dashboardService.getDashboardData();
        
        lblTotalMedicines.setText(String.valueOf(dashboard.getTotalMedicines()));
        lblLowStock.setText(String.valueOf(dashboard.getLowStockCount()));
        lblExpiring.setText(String.valueOf(dashboard.getExpiringCount()));
        lblTodaySales.setText(String.format("%.2f", dashboard.getTotalSalesToday()));
        
        List<MedicineDTO> lowStock = dashboardService.getLowStockMedicines();
        tblLowStock.setItems(FXCollections.observableArrayList(lowStock));
        
        List<MedicineDTO> expiring = dashboardService.getExpiringMedicines();
        tblExpiring.setItems(FXCollections.observableArrayList(expiring));
    }
    
    @FXML
    private void handleRefresh() {
        loadDashboardData();
    }
}
