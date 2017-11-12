package com.myapp.data.model;

import javax.validation.constraints.NotBlank;

public class Address {

    private Long id;

    @NotBlank(message = "address不能为空！")
    private String address;

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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }
}
