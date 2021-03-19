package demosap.demosap.models.dtos;

import javax.validation.constraints.Pattern;

public class ClientDTO {
    private String name;
    @Pattern(regexp = "\\w+@+.+",message = "Incorrect email")
    private String email;

    public ClientDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
