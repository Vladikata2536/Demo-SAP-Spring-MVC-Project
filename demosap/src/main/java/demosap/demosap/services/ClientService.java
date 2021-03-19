package demosap.demosap.services;

import demosap.demosap.exceptions.IllegalDataException;
import demosap.demosap.exceptions.NoLoggedUserException;
import demosap.demosap.models.dtos.ClientDTO;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
    String addClient(ClientDTO clientDTO) throws NoLoggedUserException, IllegalDataException;
    String editClient(String oldEmail, ClientDTO clientDTO) throws NoLoggedUserException, IllegalDataException;
    String deleteClient(String name) throws NoLoggedUserException, IllegalDataException;
}
