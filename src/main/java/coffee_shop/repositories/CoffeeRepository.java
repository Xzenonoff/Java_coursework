package coffee_shop.repositories;

import coffee_shop.models.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
    List<Coffee> findAllBySort(String sort);
    Coffee findByName(String name);
}
