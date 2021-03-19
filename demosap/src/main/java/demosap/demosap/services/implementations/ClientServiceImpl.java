package demosap.demosap.services.implementations;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.ClientDTO;
import demosap.demosap.models.entities.Client;
import demosap.demosap.models.entities.Seller;
import demosap.demosap.repositories.ClientRepo;
import demosap.demosap.repositories.SellerRepo;
import demosap.demosap.services.ClientService;
import demosap.demosap.util.UserLoginBean;
import demosap.demosap.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ModelMapper modelMapper;
    private final ClientRepo clientRepo;
    private final ValidationUtil validationUtil;
    private final SellerRepo sellerRepo;

    @Autowired
    public ClientServiceImpl(ModelMapper modelMapper, ClientRepo clientRepo, ValidationUtil validationUtil, SellerRepo sellerRepo) {
        this.modelMapper = modelMapper;
        this.clientRepo = clientRepo;
        this.validationUtil = validationUtil;
        this.sellerRepo = sellerRepo;
    }


    @Override
    public String addClient(ClientDTO clientDTO) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();
        Optional<Client> clientCheck = this.clientRepo.findByEmail(clientDTO.getEmail());

        if (clientCheck.isPresent()) {

            throw new IllegalDataException(String.format("There is already a client with name: %s and email: %s", clientDTO.getName(), clientDTO.getEmail()));

        } else if (this.validationUtil.isValid(clientDTO)) {
            Client client = this.modelMapper.map(clientDTO, Client.class);

            Optional<Seller> seller = this.sellerRepo.findByName(UserLoginBean.SELLER_NAME);
            client.setSeller(seller.get());

            this.clientRepo.saveAndFlush(client);

            sb.append(String.format("Client added to the database with name: %s and email: %s"
                    , clientDTO.getName()
                    , clientDTO.getEmail()));
        } else {
            this.validationUtil.violations(clientDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
        }


        return sb.toString().trim();
    }

    @Override
    public String editClient(String oldName, ClientDTO clientDTO) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();
        StringBuilder sb = new StringBuilder();
        Optional<Client> client = this.clientRepo.findByName(oldName);
        if (!client.isPresent()) {
            throw new IllegalDataException(String.format("There is no such client with name %s", oldName));
        } else {
            if (this.validationUtil.isValid(clientDTO)) {
                Client clientMapper = this.modelMapper.map(clientDTO, Client.class);
                client.get().setName(clientMapper.getName());
                client.get().setEmail(clientMapper.getEmail());

                Optional<Seller> seller = this.sellerRepo.findByName(UserLoginBean.SELLER_NAME);
                client.get().setSeller(seller.get());

                this.clientRepo.save(client.get());
                sb.append(String.format("Client with name %s was edited with new Name: %s and new Email: %s"
                        , oldName
                        , clientDTO.getName()
                        , clientDTO.getEmail()));
            } else {
                this.validationUtil.violations(clientDTO).forEach(e -> sb.append(e.getMessage()).append(System.lineSeparator()));
            }
        }


        return sb.toString().trim();
    }

    @Override
    public String deleteClient(String name) throws NoLoggedUserException, IllegalDataException {
        checkIfUserExists();

        StringBuilder sb = new StringBuilder();
        Optional<Client> client = this.clientRepo.findByName(name);
        if (client.isPresent()) {
            this.clientRepo.delete(client.get());
            sb.append(String.format("Client with name %s was deleted from the database", name));
        } else {
            throw new IllegalDataException(String.format("There is no such client with name %s", name));
        }


        return sb.toString().trim();
    }

    public static void checkIfUserExists() throws NoLoggedUserException {
        if (!UserLoginBean.LOGGED_SELLER) {
            throw new NoLoggedUserException("Invalid logged user / No user logged in (Should be Seller)");
        }
    }
}
