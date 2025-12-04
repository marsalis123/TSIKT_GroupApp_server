package org.example.test_1_server;

import jakarta.validation.constraints.*;

public class UserDto {
    public int id;

    @NotBlank(message = "Username je povinný")
    @Size(min = 3, max = 20, message = "Username musí mať 3-20 znakov")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username môže obsahovať len písmená, čísla a _")
    public String username;

    @NotBlank(message = "Heslo je povinné")
    @Size(min = 8, message = "Heslo musí mať aspoň 8 znakov")
    public String password;

    @NotBlank(message = "Meno je povinné")
    public String name;

    @NotBlank(message = "Email je povinný")
    @Email(message = "Email nemá správny formát")
    public String email;

    @Min(value = 0, message = "Vek nemôže byť záporný")
    @Max(value = 120, message = "Vek nie je reálny")
    public int age;

    public String photoPath;

    public UserDto() {}

    public UserDto(int id,
                   String username,
                   String name,
                   String email,
                   int age,
                   String photoPath) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.age = age;
        this.photoPath = photoPath;
    }
}

