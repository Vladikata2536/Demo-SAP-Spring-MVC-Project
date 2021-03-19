package demosap.demosap.repositories;

import demosap.demosap.models.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepo extends JpaRepository<Sale,Long> {

    List<Sale> findAllByDateAfterAndDateBefore(LocalDate firstDate, LocalDate lastDate);
    List<Sale> findAllBySellerName(String sellerName);
}
