package demosap.demosap.services;

import demosap.demosap.exceptions.AlreadyLoggedUserException;
import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.SellerDTO;
import demosap.demosap.models.dtos.UserLoginDTO;
import org.springframework.stereotype.Service;

@Service
public interface SellerService {
    String loginSeller(UserLoginDTO userLoginDTO) throws AlreadyLoggedUserException, IllegalDataException;
    String logoutSeller() throws NoLoggedUserException;
    String addSeller(SellerDTO sellerDTO) throws NoLoggedUserException, IllegalDataException;
    String editSeller(String oldName, SellerDTO sellerDTO) throws NoLoggedUserException, IllegalDataException;
    String deleteSeller(String email) throws NoLoggedUserException, IllegalDataException;
}
