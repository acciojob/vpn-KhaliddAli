package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        User user  = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);

        userRepository3.save(user);

        Optional<Country> optionalCountry = Optional.ofNullable(countryRepository3.findByCountryName(CountryName.valueOf(countryName.toUpperCase())));
        if(!optionalCountry.isPresent()) throw new Exception("Country Not Found");
        Country country = optionalCountry.get();

        user.setOriginalIp(country.getCode()+"."+user.getId());

        country.setUser(user);
        user.setCountry(country);

        countryRepository3.save(country);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        Optional<User> optionalUser = userRepository3.findById(userId);
        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository3.findById(serviceProviderId);

        User user = optionalUser.get();
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        serviceProviderRepository3.save(serviceProvider);

        return user;

    }
}
