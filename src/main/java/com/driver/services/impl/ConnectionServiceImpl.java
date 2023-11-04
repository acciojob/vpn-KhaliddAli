package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;
    @Autowired
    CountryRepository countryRepository;
    @Override
    public User connect(int userId, String countryName) throws Exception{

        HashMap<String , String> map = new HashMap<>();
        map.put("USA" , "002");
        map.put("AUS" , "003");
        map.put("CHI" , "004");
        map.put("JPN" , "005");
        map.put("IND" , "001");

        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();
        if(user.getCountry().getCountryName().name().equals(countryName)) return user;
        if(user.getConnected()) throw new Exception("Already connected");

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();

        List<ServiceProvider> validProviders = new ArrayList<>();

        for(ServiceProvider serviceProvider : serviceProviderList) {
            if(serviceProvider.findCountry(countryName)) validProviders.add(serviceProvider);
        }

        if(validProviders.isEmpty()) throw new Exception("Unable to connect");

        Collections.sort(validProviders , (a,b) -> {
            return a.getId() - b.getId();
        });
        //saving connection
        Connection connection = new Connection();
        connectionRepository2.save(connection);


        ServiceProvider serviceProvider = validProviders.get(0);

        connection.setServiceProvider(serviceProvider);
        connection.setUser(user);

        serviceProvider.getConnectionList().add(connection);

        user.setConnected(true);
        user.setMaskedIp(map.get(countryName.toUpperCase()) + "." +
                        serviceProvider.getId() + "." +
                        userId
                );
        user.getConnectionList().add(connection);

        userRepository2.save(user);

        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {
        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();
        if(!user.getConnected()) throw new Exception("Already disconnected");

        user.setConnected(false);
        user.setMaskedIp(null);

        userRepository2.save(user);
        return user;

    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        Optional<User> optionalUser1 = userRepository2.findById(senderId);

        User sender = optionalUser1.get();

        Optional<User> optionalUser = userRepository2.findById(receiverId);

        User receiver = optionalUser.get();

        Country senderCountry;
        Country receiverCountry;

        if(sender.getConnected()) {
            String countryCode = sender.getMaskedIp().substring(0,3);
            senderCountry = countryRepository.findByCode(countryCode);
        }
        else{
            senderCountry = sender.getCountry();
        }

        if(receiver.getConnected()) {
            String countryCode = receiver.getMaskedIp().substring(0,3);
            receiverCountry = countryRepository.findByCode(countryCode);
        }
        else{
            receiverCountry = receiver.getCountry();
        }

        if(senderCountry.equals(receiverCountry)){
            return sender;
        }
        else{
            try {
                sender = connect(senderId, receiverCountry.getCountryName().name());
                return sender;
            }catch (Exception e){
                throw new Exception("Cannot establish communication");
            }
        }
    }
}
