package kr.co.polycube.backendtest.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(name = "name")
    @NotNull(message = "Name is a required field.")
    @Size(min = 5, message = "Name must be at least 5 characters long.")
    private String name;
}
