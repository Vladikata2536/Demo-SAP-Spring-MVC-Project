package demosap.demosap.models.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

public class SellerDTO {
    private String name;
    @Length(min = 6, message = "Password length should be at least 6 symbols")
    @Pattern(regexp = "[A-Z]+[a-z]+[0-9]+",message = "Password incorrect")
    private String password;
    @Pattern(regexp = "\\w+@+.+",message = "Incorrect email")
    private String email;

    public SellerDTO(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
