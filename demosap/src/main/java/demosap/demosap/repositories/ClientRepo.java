package demosap.demosap.repositories;

import demosap.demosap.models.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByName(String oldName);
}
