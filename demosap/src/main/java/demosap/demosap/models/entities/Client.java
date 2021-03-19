package demosap.demosap.models.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity {
    private String name;
    private String email;
    private Seller seller;
    private List<Sale> sales;

    public Client() {
    }

    @Size(max = 25)
    @Column(nullable = false, length = 25,unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
@ManyToOne
@JoinColumn(name = "seller_id")
    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
@OneToMany(mappedBy = "client")
    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
