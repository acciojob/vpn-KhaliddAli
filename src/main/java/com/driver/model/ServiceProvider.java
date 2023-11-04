package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ServiceProvider")
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public ServiceProvider() {
    }

    private String name;

    @ManyToOne
    private Admin admin;

    @OneToMany(mappedBy = "serviceProvider" , cascade = CascadeType.ALL)
    private List<Country> countries = new ArrayList<>();

    @OneToMany
    private List<Connection> connectionList = new ArrayList<>();

    @ManyToMany(mappedBy = "serviceProviderList" , cascade = CascadeType.ALL)
    private List<User> userList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public boolean findCountry(String countryName){
        for(Country country : countries){
            if(country.getCountryName().name().equals(countryName)) return true;
        }
        return false;
    }
}
