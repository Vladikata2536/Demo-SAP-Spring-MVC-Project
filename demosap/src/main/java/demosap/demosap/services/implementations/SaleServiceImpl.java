package demosap.demosap.services.implementations;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.entities.Client;
import demosap.demosap.models.entities.Product;
import demosap.demosap.models.entities.Sale;
import demosap.demosap.models.entities.Seller;
import demosap.demosap.repositories.ClientRepo;
import demosap.demosap.repositories.ProductRepo;
import demosap.demosap.repositories.SaleRepo;
import demosap.demosap.repositories.SellerRepo;
import demosap.demosap.services.SaleService;
import demosap.demosap.util.UserLoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepo saleRepo;
    private final SellerRepo sellerRepo;
    private final ProductRepo productRepo;
    private final ClientRepo clientRepo;

    @Autowired
    public SaleServiceImpl(SaleRepo saleRepo, SellerRepo sellerRepo, ProductRepo productRepo, ClientRepo clientRepo) {
        this.saleRepo = saleRepo;
        this.sellerRepo = sellerRepo;
        this.productRepo = productRepo;
        this.clientRepo = clientRepo;
    }




    @Override
    public String addSale(LocalDate date, String clientName, String productName) throws NoLoggedUserException, IllegalDataException {
        StringBuilder sb = new StringBuilder();

        Optional<Client> client = this.clientRepo.findByName(clientName);
        Optional<Product> product = this.productRepo.findByName(productName);

        if (!UserLoginBean.LOGGED_SELLER) {
            throw new NoLoggedUserException("Invalid logged user / No user logged in (Should be Seller)");
        }else if(client.isPresent() && product.isPresent()){
            Optional<Seller> seller = this.sellerRepo.findByName(UserLoginBean.SELLER_NAME);

            Sale sale = new Sale();

            sale.setDate(date);
            sale.setClient(client.get());
            sale.setProduct(product.get());
            sale.setSeller(seller.get());

            this.saleRepo.saveAndFlush(sale);

            sb.append(String.format("Successfully added sale: Date: %s, Client: %s, Product: %s, Seller: %s"
                    ,date
                    ,clientName
                    ,productName
                    ,seller.get().getName()));
        }else {
            if(!client.isPresent()){
                throw new IllegalDataException("Invalid client name");

            }else {
                throw new IllegalDataException("Invalid product name");
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String viewSalesByGivenDate(LocalDate firstDate, LocalDate lastDate) throws NoLoggedUserException {
        StringBuilder sb = new StringBuilder();

        List<Sale> sales = this.saleRepo.findAllByDateAfterAndDateBefore(firstDate, lastDate);

        sb = fillBuilderWithData(sales);

        return sb.toString().trim();
    }

    @Override
    public String viewSalesByGivenSellerName(String sellerName) throws NoLoggedUserException {
        StringBuilder sb = new StringBuilder();

        List<Sale> sales = this.saleRepo.findAllBySellerName(sellerName);

        sb = fillBuilderWithData(sales);

        return sb.toString();
    }

    private StringBuilder fillBuilderWithData(List<Sale> sales) throws NoLoggedUserException {
        StringBuilder sb = new StringBuilder();
        if (!UserLoginBean.LOGGED_ADMIN) {
            throw new NoLoggedUserException("Invalid logged user / No user logged in (Should be Admin)");
        }else {
            if(sales.size() == 0){
                throw new IllegalStateException("No found data");
            }else {
                for (Sale sale : sales) {
                    sb.append(String.format("| Date: %s |\t| Client: %s |\t| Product: %s |\t| Seller: %s |",sale.getDate()
                            ,sale.getClient().getName()
                            ,sale.getProduct().getName()
                            ,sale.getSeller().getName()))
                            .append(System.lineSeparator());
                }
            }
        }
        return sb;
    }
}
