package coffee_shop.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coffee")
@Getter
@Setter
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "sort")
    String sort;
    @Column(name = "name")
    String name;
    @Column(name = "image")
    String image;
    @Column(name = "weight")
    int weight;
    @Column(name = "price")
    int price;
    @Column(name = "description")
    String description;
    @Column(name = "quantity")
    int quantity;
    @ManyToMany(mappedBy = "coffees")
    private Set<User> baskets=new HashSet<>();
}
