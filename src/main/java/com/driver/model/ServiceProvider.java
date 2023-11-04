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
    private List<Country> countryList = new ArrayList<>();

    @OneToMany
    private List<Connection> connectionList = new ArrayList<>();

    @ManyToMany(mappedBy = "serviceProviderList" , cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

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

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean findCountry(String countryName){
        for(Country country : countryList){
            if(country.getCountryName().name().equals(countryName)) return true;
        }
        return false;
    }
}
