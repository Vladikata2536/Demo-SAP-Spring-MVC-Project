package demosap.demosap.services.implementations;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.ProductDTO;
import demosap.demosap.models.entities.Product;
import demosap.demosap.repositories.ProductRepo;
import demosap.demosap.services.ProductService;
import demosap.demosap.util.UserLoginBean;
import demosap.demosap.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;


    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String addProduct(ProductDTO productDTO) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();
        Optional<Product> productCheck = this.productRepo.findByName(productDTO.getName());
        if (productCheck.isPresent()) {
            throw new IllegalDataException(String.format("There is already a product with name: %s and price: %.2f", productDTO.getName(), productDTO.getPrice()));

        } else if (this.validationUtil.isValid(productDTO)) {
            Product product = this.modelMapper.map(productDTO, Product.class);
            this.productRepo.saveAndFlush(product);
            sb.append(String.format("Product added to the database with name: %s and price: %.2f", productDTO.getName(), productDTO.getPrice()));
        } else {
            this.validationUtil.violations(productDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
        }


        return sb.toString().trim();
    }


    @Override
    public String editProduct(String oldName, ProductDTO productDTO) throws IllegalDataException, NoLoggedUserException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();

        Optional<Product> product = this.productRepo.findByName(oldName);

        if (!product.isPresent()) {
            throw new IllegalDataException(String.format("There is no such product with name %s", oldName));

        } else {
            if (this.validationUtil.isValid(productDTO)) {
                Product newProduct = this.modelMapper.map(productDTO, Product.class);
                product.get().setName(newProduct.getName());
                product.get().setPrice(newProduct.getPrice());

                this.productRepo.save(product.get());
                sb.append(String.format("Product %s was edited with new Name: %s and new Price: %.2f"
                        , oldName
                        , product.get().getName()
                        , product.get().getPrice()));
            } else {
                this.validationUtil.violations(productDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
            }
        }


        return sb.toString().trim();
    }

    @Override
    public String deleteProduct(String name) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();

        Optional<Product> product = this.productRepo.findByName(name);
        if (product.isPresent()) {
            this.productRepo.delete(product.get());
            sb.append(String.format("Product with name %s was deleted from the database", name));
        } else {
            throw new IllegalDataException(String.format("There is no such product with name %s", name));

        }

        return sb.toString().trim();
    }

    public static void checkIfUserExists() throws NoLoggedUserException {
        if (!UserLoginBean.LOGGED_ADMIN) {
            throw new NoLoggedUserException("Invalid logged user / No user logged in (Should be Administrator)");
        }
    }
}
