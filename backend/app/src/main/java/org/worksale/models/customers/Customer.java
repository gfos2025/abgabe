package org.worksale.models.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.Generated;

import org.worksale.models.appointments.AppointmentParticipant;
import org.worksale.models.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "assigned_manager_id", referencedColumnName = "id")
    private User assignedManager;

    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String surname;

    @Column(nullable = true, unique = true, length = 255)
    private String email;

    @Column(nullable = true, length = 64)
    private String phoneNumber;

    @Column(nullable = true, length = 255)
    private String address;

    @Column(nullable = false, length = 255)
    private String company;

    @Column(nullable = false, length = 255)
    private ContactStatus contactStatus = ContactStatus.NotContacted;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<CustomerNotes> notes;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ContactStatus getContactStatus() {
        return this.contactStatus;
    }

    public void setContactStatus(ContactStatus contactStatus) {
        this.contactStatus = contactStatus;
    }

    public List<CustomerNotes> getNotes() {
        return this.notes;
    }

    public void setNotes(List<CustomerNotes> notes) {
        this.notes = notes;
    }

    public User getAssignedManager() {
        return this.assignedManager;
    }

    public void setAssignedManager(User assignedManager) {
        this.assignedManager = assignedManager;
    }
}