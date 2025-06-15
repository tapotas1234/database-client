package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    @Getter
    Integer id;

    @Column
    @NotNull
    String name;

    @Column
    @NotNull
    @Setter
    Integer age;

    @Column
    @Setter
    String email;

    @Column
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdAt;

    public User(){}

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ID: " + id + " " + "name: " + name + ", age: " + age + ", created at: " + createdAt + ", email: " + email + "\n";
    }
}
