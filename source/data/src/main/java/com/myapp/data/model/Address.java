package com.myapp.data.model;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;

public class Address {

    private Long id;

//    @NotBlank(message = "address不能为空！")
    private String address;

    private double randomNumber;

    @Value("#{systemProperties['user.timezone']}")
    private String defaultLocale;

    public Address() {
    }

    public Address(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(double randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", randomNumber=" + randomNumber +
                ", defaultLocale='" + defaultLocale + '\'' +
                '}';
    }
}
