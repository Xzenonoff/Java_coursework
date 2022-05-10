package coffee_shop.services;

import coffee_shop.models.Coffee;
import coffee_shop.models.User;
import coffee_shop.repositories.CoffeeRepository;
import coffee_shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Transactional
    public void addCoffee(Coffee coffee) {
        User user = getUser();
        if(coffee.getQuantity() > 0){
            coffee.setQuantity(coffee.getQuantity()-1);
            user.addCoffee(coffee);
            coffeeRepository.save(coffee);
        }

    }

    @Transactional(readOnly = true)
    public List<Coffee> getCoffees() {
        User user = getUser();
        return user.getCoffees();
    }

    public List<Coffee> getSetCoffee(List<Coffee> coffees1) {
        Set<Coffee> set = new HashSet<>(coffees1);
        return new ArrayList<>(set);
    }

    public List<String> getCounts(List<Coffee> coffees1, List<Coffee> coffee2) {
        List<Integer> nums = new ArrayList<Integer>();
        for (Coffee coffee : coffees1) {
            nums.add(Collections.frequency(coffee2, coffee));
        }
        List<String> str = new ArrayList<>();
        for (int i = 0; i < coffees1.size(); i++) {
            str.add(nums.get(i) + " x " + coffees1.get(i).getPrice());
        }
        return str;
    }

    public Double getPrice(List<Coffee> coffees) {
        double price = 0;
        for (Coffee coffee : coffees) {
            price += coffee.getPrice();
        }
        price -= price*getStock(coffees) /100;
        return price;
    }

    public Double getStock(List<Coffee> coffees) {
        double discount = 0;
        if (coffees.size() >=6){
            discount +=10;
        }
        List<String> sorts = new ArrayList<>();
        for (Coffee coffee : coffees) {
            sorts.add(coffee.getSort());
        }
        Set<String> stocks = new HashSet<>(sorts);
        if (stocks.size() >=2){
            discount +=5;
        }
        return discount;
    }

    @Transactional
    public void deleteCoffee(String name){
        User user = getUser();
        Coffee coffee = coffeeRepository.findByName(name);
        user.removeCoffee(coffee);
        coffee.setQuantity(coffee.getQuantity()+1);
        userRepository.save(user);
        coffeeRepository.save(coffee);
    }

    @Transactional
    public void buy(){
        User user = getUser();
        List<Coffee> coffees = user.getCoffees();
        for (Coffee coffee : coffees) {
            Set<User> users = coffee.getBaskets();
            users.remove(user);
            coffee.setBaskets(users);
            coffeeRepository.save(coffee);
        }
        coffees.clear();
        user.setCoffees(coffees);
        userRepository.save(user);
    }

    private User getUser(){
        String username = "";
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        } catch (NullPointerException e){
            username = "test";
        }

        return userRepository.findByLogin(username);
    }
}
