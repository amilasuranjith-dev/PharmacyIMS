package edu.icet.controller;

import edu.icet.model.dto.SaleDTO;
import edu.icet.service.interfaces.SaleService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class SalesController implements Initializable {
    
    @FXML
    private TextField txtSearch;
    
    @FXML
    private TableView<SaleDTO> tblSales;
    
    @FXML
    private TableColumn<SaleDTO, Integer> colId;
    
    @FXML
    private TableColumn<SaleDTO, String> colInvoice;
    
    @FXML
    private TableColumn<SaleDTO, Double> colTotal;
    
    @FXML
    private TableColumn<SaleDTO, Double> colDiscount;
    
    @FXML
    private TableColumn<SaleDTO, Double> colPaid;
    
    @FXML
    private TableColumn<SaleDTO, LocalDateTime> colDate;
    
    @FXML
    private TableColumn<SaleDTO, String> colNotes;
    
    @FXML
    private Button btnView;
    
    private SaleService saleService;
    private ObservableList<SaleDTO> saleList;
    private StackPane contentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saleService = ServiceFactory.getInstance().getService(ServiceType.SALE);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        
        loadSales();
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    private void loadSales() {
        List<SaleDTO> sales = saleService.getAllSales();
        saleList = FXCollections.observableArrayList(sales);
        tblSales.setItems(saleList);
    }
    
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadSales();
        } else {
            List<SaleDTO> sales = saleService.searchSales(searchText);
            saleList = FXCollections.observableArrayList(sales);
            tblSales.setItems(saleList);
        }
    }
    
    @FXML
    private void handleNewSale() {
        showSalesForm();
    }
    
    @FXML
    private void handleViewDetails() {
        SaleDTO selected = tblSales.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SaleDTO fullSale = saleService.getSaleById(selected.getId());
            showSaleDetails(fullSale);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a sale to view details.");
        }
    }
    
    @FXML
    public void handleRefresh() {
        loadSales();
        txtSearch.clear();
    }
    
    private void showSalesForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sales-form.fxml"));
            Parent root = loader.load();
            
            SalesFormController controller = loader.getController();
            controller.setParentController(this);
            controller.setContentArea(contentArea);
            
            if (contentArea != null) {
                // Load in same pane
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                // Fallback to modal window if contentArea not set
                Stage stage = new Stage();
                stage.setTitle("New Sale");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load form: " + e.getMessage());
        }
    }
    
    private void showSaleDetails(SaleDTO sale) {
        StringBuilder details = new StringBuilder();
        details.append("Invoice: ").append(sale.getInvoiceNumber()).append("\n");
        details.append("Date: ").append(sale.getSaleDate()).append("\n");
        details.append("Total: ").append(sale.getTotalAmount()).append("\n");
        details.append("Discount: ").append(sale.getDiscount()).append("\n");
        details.append("Paid: ").append(sale.getPaidAmount()).append("\n\n");
        details.append("Items:\n");
        
        if (sale.getItems() != null) {
            sale.getItems().forEach(item -> {
                details.append("- ").append(item.getMedicineName())
                       .append(" x").append(item.getQuantity())
                       .append(" @ ").append(item.getUnitPrice())
                       .append(" = ").append(item.getSubtotal())
                       .append("\n");
            });
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sale Details");
        alert.setHeaderText("Sale Information");
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
