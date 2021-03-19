package demosap.demosap.models.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

public class UserRegisterDTO {
    @Pattern(regexp = "\\w+@+.+",message = "Incorrect email")
    private String email;
    @Length(min = 6, message = "Password length should be at least 6 symbols")
    @Pattern(regexp = "[A-Z]+[a-z]+[0-9]+",message = "Password incorrect")
    private String password;
    private String confirmPassword;
    private String name;

    public UserRegisterDTO(String email, String password, String confirmPassword, String name) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
    }

    public UserRegisterDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
