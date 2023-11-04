package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);

        serviceProviderRepository1.save(serviceProvider);

        Optional<Admin> optionalUser = adminRepository1.findById(adminId);

        Admin admin = optionalUser.get();

        serviceProvider.setAdmin(admin);

        admin.getServiceProviders().add(serviceProvider);

        adminRepository1.save(admin);

        return admin;

    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        String code = null;
        HashMap<String , String> map = new HashMap<>();
        map.put("USA" , "002");
        map.put("AUS" , "003");
        map.put("CHI" , "004");
        map.put("JPN" , "005");
        map.put("IND" , "001");

        code = map.getOrDefault(countryName.toUpperCase() , "000");
        if(code.equals("000")) throw new Exception("Country not found");

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository1.findById(serviceProviderId);
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        Country country = new Country();
        country.setCountryName(CountryName.valueOf(countryName.toUpperCase()));
        country.setCode(code);

        countryRepository1.save(country);
        country.setServiceProvider(serviceProvider);

        serviceProvider.getCountryList().add(country);

        countryRepository1.save(country);

        return serviceProvider;
    }
}
