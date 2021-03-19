package demosap.demosap.services;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    String addProduct(ProductDTO productDTO) throws NoLoggedUserException, IllegalDataException;
    String editProduct(String oldName, ProductDTO productDTO) throws IllegalDataException, NoLoggedUserException;
    String deleteProduct(String name) throws NoLoggedUserException, IllegalDataException;
}
