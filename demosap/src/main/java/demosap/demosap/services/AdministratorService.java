package demosap.demosap.services;

import demosap.demosap.exceptions.AlreadyLoggedUserException;
import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.UserLoginDTO;
import demosap.demosap.models.dtos.UserRegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdministratorService {
    String registerAdministrator(UserRegisterDTO userRegisterDTO) throws IllegalDataException;
    String loginAdministrator(UserLoginDTO userLoginDTO) throws AlreadyLoggedUserException, IllegalDataException;
    String logoutAdministrator() throws NoLoggedUserException;
}
