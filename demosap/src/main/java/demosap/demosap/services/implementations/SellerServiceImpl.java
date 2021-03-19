package demosap.demosap.services.implementations;

import demosap.demosap.exceptions.AlreadyLoggedUserException;
import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.SellerDTO;
import demosap.demosap.models.dtos.UserLoginDTO;
import demosap.demosap.models.entities.Administrator;
import demosap.demosap.models.entities.Seller;
import demosap.demosap.repositories.AdministratorRepo;
import demosap.demosap.repositories.SellerRepo;
import demosap.demosap.services.SellerService;
import demosap.demosap.util.UserLoginBean;
import demosap.demosap.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepo sellerRepo;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final AdministratorRepo administratorRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SellerServiceImpl(SellerRepo sellerRepo, ModelMapper modelMapper, ValidationUtil validationUtil, AdministratorRepo administratorRepo, PasswordEncoder passwordEncoder) {
        this.sellerRepo = sellerRepo;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.administratorRepo = administratorRepo;

        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String loginSeller(UserLoginDTO userLoginDTO) throws AlreadyLoggedUserException, IllegalDataException {
        StringBuilder sb = new StringBuilder();

        Optional<Seller> seller = this.sellerRepo.findByEmailAndPassword(userLoginDTO.getEmail(), userLoginDTO.getPassword());

        if (seller.isPresent()) {
            if (UserLoginBean.LOGGED_SELLER) {
                throw new AlreadyLoggedUserException("Seller is already logged in");
            } else {
                UserLoginBean.loginSeller(seller.get().getName());
                sb.append(String.format("%s successfully logged in", seller.get().getName()));
            }

        } else {
            throw new IllegalDataException("Incorrect email/password");
        }

        return sb.toString().trim();
    }

    @Override
    public String logoutSeller() throws NoLoggedUserException {
        StringBuilder sb = new StringBuilder();
        if (!UserLoginBean.LOGGED_SELLER) {
            throw new NoLoggedUserException("Cannot log out. No user was logged in");
        } else {
            sb.append(String.format("Seller %s successfully logged out", UserLoginBean.SELLER_NAME));
            UserLoginBean.logoutSeller();
        }

        return sb.toString().trim();
    }

    @Override
    public String addSeller(SellerDTO sellerDTO) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();
        Optional<Seller> checkSeller = this.sellerRepo.findByEmail(sellerDTO.getEmail());
        if (checkSeller.isPresent()) {
            throw new IllegalDataException("There is already a seller with that info");
        } else if (this.validationUtil.isValid(sellerDTO)) {
            Seller seller = this.modelMapper.map(sellerDTO, Seller.class);
            seller.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));
            Optional<Administrator> administrator = this.administratorRepo.findByName(UserLoginBean.ADMIN_NAME);
            seller.setBoss(administrator.get());

            this.sellerRepo.saveAndFlush(seller);
            sb.append(String.format("Successfully added seller with name %s to the database", seller.getName()));

        } else {
            this.validationUtil.violations(sellerDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
        }


        return sb.toString().trim();
    }


    @Override
    public String editSeller(String oldName, SellerDTO sellerDTO) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();

        Optional<Seller> sellerRepo = this.sellerRepo.findByName(oldName);
        if (!sellerRepo.isPresent()) {
            throw new IllegalDataException(String.format("There is no such seller with name %s", oldName));

        } else {

            if (this.validationUtil.isValid(sellerDTO)) {
                Seller seller = this.modelMapper.map(sellerDTO, Seller.class);
                sellerRepo.get().setName(seller.getName());
                sellerRepo.get().setPassword(passwordEncoder.encode(seller.getPassword()));
                sellerRepo.get().setEmail(seller.getEmail());
                Optional<Administrator> administrator = this.administratorRepo.findByName(UserLoginBean.ADMIN_NAME);
                sellerRepo.get().setBoss(administrator.get());

                this.sellerRepo.save(sellerRepo.get());
                sb.append(String.format("Seller %s was edited with new Name: %s , new Password: %s and new Email: %s",
                        oldName
                        , sellerDTO.getName()
                        , sellerDTO.getPassword()
                        , sellerDTO.getEmail()));
            } else {
                this.validationUtil.violations(sellerDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
            }

        }


        return sb.toString().trim();
    }

    @Override
    public String deleteSeller(String name) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();

        Optional<Seller> seller = this.sellerRepo.findByName(name);

        if (seller.isPresent()) {
            this.sellerRepo.delete(seller.get());

            sb.append(String.format("Seller with name %s was deleted from the database", name));
        } else {
            throw new IllegalDataException(String.format("There is no such seller with name %s", name));
        }


        return sb.toString().trim();
    }

    public static void checkIfUserExists() throws NoLoggedUserException {
        if (!UserLoginBean.LOGGED_ADMIN) {
            throw new NoLoggedUserException("Invalid logged user / No user logged in (Should be Administrator)");
        }
    }
}
