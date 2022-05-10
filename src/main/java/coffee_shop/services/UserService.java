package coffee_shop.services;

import coffee_shop.controllers.UserRegistrationDto;
import coffee_shop.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByLogin(String login);

    void save(UserRegistrationDto registration, String cookie);
}
