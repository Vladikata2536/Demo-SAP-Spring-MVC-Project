package demosap.demosap.repositories;

import demosap.demosap.models.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmailAndPassword(String email, String password);

    Optional<Seller> findByName(String oldName);

    Optional<Seller> findByEmail(String email);
}
