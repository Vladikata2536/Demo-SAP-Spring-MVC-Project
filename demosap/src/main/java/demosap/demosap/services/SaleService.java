package demosap.demosap.services;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface SaleService {
    String addSale(LocalDate date, String clientName, String productName) throws NoLoggedUserException, IllegalDataException;
    String viewSalesByGivenDate(LocalDate firstDate,LocalDate lastDate) throws NoLoggedUserException;
    String viewSalesByGivenSellerName(String sellerName) throws NoLoggedUserException;
}
