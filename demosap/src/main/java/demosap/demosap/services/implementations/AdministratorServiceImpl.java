package demosap.demosap.services.implementations;

import demosap.demosap.exceptions.AlreadyLoggedUserException;
import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.UserLoginDTO;
import demosap.demosap.models.dtos.UserRegisterDTO;
import demosap.demosap.models.entities.Administrator;
import demosap.demosap.repositories.AdministratorRepo;
import demosap.demosap.services.AdministratorService;
import demosap.demosap.util.UserLoginBean;
import demosap.demosap.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepo administratorRepo;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public AdministratorServiceImpl(AdministratorRepo administratorRepo, ModelMapper modelMapper, ValidationUtil validationUtil, PasswordEncoder passwordEncoder) {
        this.administratorRepo = administratorRepo;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;


        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerAdministrator(UserRegisterDTO userRegisterDTO) throws IllegalDataException {
        StringBuilder sb = new StringBuilder();
        Optional<Administrator> administratorCheck = this.administratorRepo.findByName(userRegisterDTO.getName());

        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())){
            throw new IllegalDataException("Passwords don't match");

        }else if(administratorCheck.isPresent()){
            throw new IllegalStateException(String.format("There is already an administrator with name %s and email: %s"
                    ,userRegisterDTO.getName(),userRegisterDTO.getEmail()));

        }
        else if(this.validationUtil.isValid(userRegisterDTO)){
            Administrator administrator = this.modelMapper.map(userRegisterDTO, Administrator.class);
            administrator.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            this.administratorRepo.saveAndFlush(administrator);
            sb.append(String.format("%s was registered successfully",userRegisterDTO.getName()));

        }else {
            this.validationUtil.violations(userRegisterDTO).forEach(e -> sb.append(String.format("%s%n",e.getMessage())));
        }

        return sb.toString().trim();
    }

    @Override
    public String loginAdministrator(UserLoginDTO userLoginDTO) throws AlreadyLoggedUserException, IllegalDataException {
        StringBuilder sb = new StringBuilder();

        Optional<Administrator> administrator = this.administratorRepo.findByEmail(userLoginDTO.getEmail());

        if(administrator.isPresent()){
            if(UserLoginBean.LOGGED_ADMIN){
                throw new AlreadyLoggedUserException("Administrator is already logged in");
            }else {
                UserLoginBean.loginAdmin(administrator.get().getName());
                sb.append(String.format("%s successfully logged in",administrator.get().getName()));
            }
        }else {
            throw new IllegalDataException("Incorrect email/password");
        }
        return sb.toString().trim();
    }

    @Override
    public String logoutAdministrator() throws NoLoggedUserException {
        StringBuilder sb = new StringBuilder();

        if(!UserLoginBean.LOGGED_ADMIN){
            throw new NoLoggedUserException("Cannot log out. No user was logged in");
        }
            sb.append(String.format("User %s successfully logged out",UserLoginBean.ADMIN_NAME));
            UserLoginBean.logoutAdmin();


        return sb.toString().trim();
    }

}
