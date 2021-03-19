package demosap.demosap.models.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity {
    private String name;
    private String password;
    private String email;
    private Administrator boss;
    private List<Client> clients;
    private List<Sale> sales;


    public Seller() {
    }

    @Column(nullable = false,unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false,unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
@OneToMany(mappedBy = "seller",targetEntity = Client.class)
@LazyCollection(LazyCollectionOption.FALSE)
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
@ManyToOne
@JoinColumn(name = "administrator_id")
    public Administrator getBoss() {
        return boss;
    }

    public void setBoss(Administrator boss) {
        this.boss = boss;
    }
@OneToMany(mappedBy = "seller")
@LazyCollection(LazyCollectionOption.FALSE)
    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
