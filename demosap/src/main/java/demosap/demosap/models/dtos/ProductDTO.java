package demosap.demosap.models.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class ProductDTO {
    @Length(min = 6,message = "Name should be at least 6 symbols.")
    private String name;
    @DecimalMin(value = "0",message = "Price shouldn't be a negative number.")
    private BigDecimal price;

    public ProductDTO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
