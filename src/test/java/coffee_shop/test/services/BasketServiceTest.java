package coffee_shop.test.services;

import coffee_shop.repositories.CoffeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import coffee_shop.models.Coffee;
import coffee_shop.models.User;
import coffee_shop.repositories.UserRepository;
import coffee_shop.services.BasketService;

import java.util.*;

import static org.mockito.Mockito.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class BasketServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CoffeeRepository coffeeRepository;

    @Captor
    private ArgumentCaptor<Coffee> captor;

    private BasketService basketService;

    @Autowired
    public void setBasketService(BasketService basketService) {
        this.basketService = basketService;
    }

    @BeforeEach
    public void init(){
        User user = new User();
        user.setLogin("test");
        user.setPassword("password");
        user.setId(1L);
        user.setCoffees(new ArrayList<>());

        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
    }

    @Test
    public void getCoffees() {
        Coffee coffee1 = new Coffee();
        coffee1.setId(1);
        coffee1.setName("Мясо");
        coffee1.setQuantity(5);
        coffee1.setPrice(158);
        coffee1.setSort("mutton");
        coffee1.setWeight(2);

        Coffee coffee2 = new Coffee();
        coffee2.setId(1);
        coffee2.setName("Мясо");
        coffee2.setQuantity(5);
        coffee2.setPrice(158);
        coffee2.setSort("mutton");
        coffee2.setWeight(2);

        basketService.addCoffee(coffee1);
        basketService.addCoffee(coffee2);

        List<Coffee> fetched = basketService.getCoffees();

        User user = userRepository.findByLogin("test");

        Assertions.assertEquals(user.getCoffees().size(), fetched.size());
    }

    @Test
    public void addCoffee() {
        Coffee coffee = new Coffee();
        coffee.setId(1);
        coffee.setName("Мясо");
        coffee.setQuantity(5);
        coffee.setPrice(158);
        coffee.setSort("mutton");
        coffee.setWeight(2);

        basketService.addCoffee(coffee);

        verify(coffeeRepository).save(captor.capture());
        Coffee captured = captor.getValue();
        Assertions.assertEquals(coffee.getId(), captured.getId());
        Assertions.assertEquals(coffee.getName(), captured.getName());
        Assertions.assertEquals(coffee.getQuantity(), captured.getQuantity());
        Assertions.assertEquals(coffee.getPrice(), captured.getPrice());
        Assertions.assertEquals(coffee.getSort(), captured.getSort());
        Assertions.assertEquals(coffee.getWeight(), captured.getWeight());

        User user = userRepository.findByLogin("test");
        Assertions.assertNotEquals(user.getCoffees().indexOf(coffee),-1);
    }

    @Test
    public void deleteCoffee(){
        Coffee coffee = new Coffee();
        coffee.setId(1);
        coffee.setName("Мясо");
        coffee.setQuantity(5);
        coffee.setPrice(158);
        coffee.setSort("mutton");
        coffee.setWeight(2);

        basketService.addCoffee(coffee);

        verify(coffeeRepository).save(captor.capture());
        Coffee captured = captor.getValue();

        when(coffeeRepository.findByName(coffee.getName()))
                .thenReturn(coffee);

        basketService.deleteCoffee(coffee.getName());

        Assertions.assertEquals(basketService.getCoffees().indexOf(captured), -1);
    }

    @Test
    public void buy(){
        Coffee coffee = new Coffee();
        coffee.setId(1);
        coffee.setName("Мясо");
        coffee.setQuantity(5);
        coffee.setPrice(158);
        coffee.setSort("mutton");
        coffee.setWeight(2);

        basketService.addCoffee(coffee);

        verify(coffeeRepository).save(captor.capture());
        Coffee captured = captor.getValue();

        basketService.buy();

        User user = userRepository.findByLogin("test");

        Assertions.assertEquals(basketService.getCoffees().indexOf(captured), -1);
        Assertions.assertFalse(coffee.getBaskets().contains(user));
    }


}
