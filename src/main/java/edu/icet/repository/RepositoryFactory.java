package edu.icet.repository;

import edu.icet.repository.impl.MedicineRepositoryImpl;
import edu.icet.repository.impl.SaleItemRepositoryImpl;
import edu.icet.repository.impl.SaleRepositoryImpl;
import edu.icet.repository.impl.SupplierRepositoryImpl;
import edu.icet.util.RepositoryType;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    
    private RepositoryFactory() {}
    
    public static RepositoryFactory getInstance() {
        return instance == null ? instance = new RepositoryFactory() : instance;
    }
    
    public <T extends SuperRepository> T getRepository(RepositoryType type) {
        switch (type) {
            case MEDICINE:
                return (T) new MedicineRepositoryImpl();
            case SUPPLIER:
                return (T) new SupplierRepositoryImpl();
            case SALE:
                return (T) new SaleRepositoryImpl();
            case SALE_ITEM:
                return (T) new SaleItemRepositoryImpl();
            default:
                return null;
        }
    }
}
