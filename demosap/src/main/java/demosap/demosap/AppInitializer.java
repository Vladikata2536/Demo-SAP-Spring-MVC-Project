package demosap.demosap;

import demosap.demosap.exceptions.AlreadyLoggedUserException;
import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.*;
import demosap.demosap.services.*;
import demosap.demosap.util.UserLoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AppInitializer implements CommandLineRunner {
    private final ProductService productService;
    private final ClientService clientService;
    private final AdministratorService administratorService;
    private final SellerService sellerService;
    private final SaleService saleService;
    private boolean breakLoop = false;


    @Autowired
    public AppInitializer(ProductService productService, ClientService clientService, AdministratorService administratorService, SellerService sellerService, SaleService saleService) {
        this.productService = productService;
        this.clientService = clientService;
        this.administratorService = administratorService;
        this.sellerService = sellerService;
        this.saleService = saleService;
    }

    @Override
    public void run(String... args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (!breakLoop) {
            System.err.println("ENTER A COMMAND -> FOR EXIT : PRESS `Exit` !");
            String[] tokens = reader.readLine().split("\\|");

            switch (tokens[0]) {
                case "Register":
                    UserRegisterDTO userRegisterDTO = new UserRegisterDTO(tokens[4], tokens[2], tokens[3], tokens[1]);

                    try {
                        System.out.println(this.administratorService.registerAdministrator(userRegisterDTO));
                    } catch (IllegalDataException | IllegalStateException e) {
                        System.err.println(e.getMessage());
                    }


                    break;
                case "Login":
                    switch (tokens[1]) {
                        case "Administrator":
                            if (!UserLoginBean.CURRENT_LOGGED_USER) {
                                UserLoginDTO administratorLoginDTO = new UserLoginDTO(tokens[2], tokens[3]);
                                try {
                                    System.out.println(this.administratorService.loginAdministrator(administratorLoginDTO));
                                } catch (AlreadyLoggedUserException | IllegalDataException e) {
                                    System.err.println(e.getMessage());
                                }
                            } else {
                                System.err.println("There is already a user logged in");
                            }
                            break;
                        case "Seller":
                            if (!UserLoginBean.CURRENT_LOGGED_USER) {
                                UserLoginDTO sellerLoginDTO = new UserLoginDTO(tokens[2], tokens[3]);
                                try {
                                    System.out.println(this.sellerService.loginSeller(sellerLoginDTO));
                                } catch (AlreadyLoggedUserException | IllegalDataException e) {
                                    System.err.println(e.getMessage());
                                }
                            } else {
                                System.err.println("There is already a user logged in");
                            }
                            break;
                    }
                    break;
                case "Logout":
                    if (UserLoginBean.LOGGED_ADMIN) {
                        try {
                            System.out.println(this.administratorService.logoutAdministrator());
                        } catch (NoLoggedUserException e) {
                            System.err.println(e.getMessage());
                        }
                    } else {
                        try {
                            System.out.println(this.sellerService.logoutSeller());
                        } catch (NoLoggedUserException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case "AddProduct":
                    ProductDTO productDTO = new ProductDTO(tokens[1], new BigDecimal(tokens[2]));
                    try {
                        System.out.println(this.productService.addProduct(productDTO));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "EditProduct":
                    ProductDTO editDTO = new ProductDTO(tokens[2], new BigDecimal(tokens[3]));
                    try {
                        System.out.println(this.productService.editProduct(tokens[1], editDTO));
                    } catch (IllegalDataException | NoLoggedUserException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "DeleteProduct":
                    try {
                        System.out.println(this.productService.deleteProduct(tokens[1]));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "AddSeller":
                    SellerDTO sellerDTO = new SellerDTO(tokens[1], tokens[2], tokens[3]);
                    try {
                        System.out.println(this.sellerService.addSeller(sellerDTO));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }

                    break;
                case "EditSeller":
                    SellerDTO editSeller = new SellerDTO(tokens[2], tokens[3], tokens[4]);
                    try {
                        System.out.println(this.sellerService.editSeller(tokens[1], editSeller));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "DeleteSeller":
                    try {
                        System.out.println(this.sellerService.deleteSeller(tokens[1]));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "AddClient":
                    ClientDTO clientDTO = new ClientDTO(tokens[1], tokens[2]);
                    try {
                        System.out.println(this.clientService.addClient(clientDTO));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "EditClient":
                    ClientDTO editClient = new ClientDTO(tokens[2], tokens[3]);
                    try {
                        System.out.println(this.clientService.editClient(tokens[1], editClient));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "DeleteClient":
                    try {
                        System.out.println(this.clientService.deleteClient(tokens[1]));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "AddSale":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    String date = tokens[1];
                    LocalDate localDate = LocalDate.parse(date, formatter);
                    try {
                        System.out.println(this.saleService.addSale(localDate, tokens[2], tokens[3]));
                    } catch (NoLoggedUserException | IllegalDataException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "ViewSalesForPeriod":
                    DateTimeFormatter Dateformat = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate firstDate = LocalDate.parse(tokens[1], Dateformat);
                    LocalDate lastDate = LocalDate.parse(tokens[2], Dateformat);
                    try {
                        System.out.println(this.saleService.viewSalesByGivenDate(firstDate, lastDate));
                    } catch (NoLoggedUserException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "ViewSalesBySeller":
                    try {
                        System.out.println(this.saleService.viewSalesByGivenSellerName(tokens[1]));
                    } catch (NoLoggedUserException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "Exit":
                    this.breakLoop = true;
                    break;
            }
        }
        System.err.println("Program closed");


    }

}
