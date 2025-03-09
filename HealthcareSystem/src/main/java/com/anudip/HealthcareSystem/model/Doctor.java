package com.anudip.HealthcareSystem.model;
import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String speciality;
    private int experience;

    // Default Constructor (Required by JPA)
    public Doctor() {}

    // Parameterized Constructor (Properly Assigns Values)
    public Doctor(User user, String speciality, Integer experience) {
        this.user = user;
        this.speciality = speciality;
        this.experience = (experience != null) ? experience : 0; // Handle null case
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
