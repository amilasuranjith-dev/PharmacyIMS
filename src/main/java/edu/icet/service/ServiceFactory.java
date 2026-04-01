package edu.icet.service;

import edu.icet.service.impl.DashboardServiceImpl;
import edu.icet.service.impl.MedicineServiceImpl;
import edu.icet.service.impl.SaleServiceImpl;
import edu.icet.service.impl.SupplierServiceImpl;
import edu.icet.util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;
    
    private ServiceFactory() {}
    
    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }
    
    public <T extends SuperService> T getService(ServiceType type) {
        switch (type) {
            case MEDICINE:
                return (T) new MedicineServiceImpl();
            case SUPPLIER:
                return (T) new SupplierServiceImpl();
            case SALE:
                return (T) new SaleServiceImpl();
            case DASHBOARD:
                return (T) new DashboardServiceImpl();
            default:
                return null;
        }
    }
}
