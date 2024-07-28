package it.ifm.springbootfirstapp;

import java.time.LocalDate;

class PersonInfo {
    private String name;
    private String lastName;
    private String sex;
    private LocalDate dateOfBirth;
    private String hometown;
    private String countryOfBirth;    

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }
}

