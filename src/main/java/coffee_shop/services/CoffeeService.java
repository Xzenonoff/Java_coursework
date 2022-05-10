package coffee_shop.services;

import coffee_shop.models.Coffee;
import coffee_shop.repositories.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Transactional(readOnly = true)
    public List<Coffee> getBySort(String sort) {
        List<Coffee> coffees = coffeeRepository.findAllBySort(sort);
        coffees.sort(Comparator.comparing(Coffee::getId));
        return coffees;
    }

    @Transactional(readOnly = true)
    public Coffee getByName(List<Coffee> coffees, String name){
        for (Coffee coffee : coffees) {
            if (coffee.getName().equals(name))
                return coffee;
        }
        return null;
    }
}
