package org.worksale.models.users;

import java.io.Serializable;
import java.util.UUID;
import org.worksale.models.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String surname;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 128)
    private String passwordHash;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean darkMode = true;

    @Column(nullable = false)
    private Boolean isLanguageGerman = true;

    public User() {
    }

    public User(String name, String surname, String email, String passwordHash, Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean dark_mode) {
        this.darkMode = dark_mode;
    }

    public Boolean isLanguageGerman() {
        return isLanguageGerman;
    }

    public void setLanguageGerman(Boolean isLanguageGerman) {
        this.isLanguageGerman = isLanguageGerman;
    }
}
