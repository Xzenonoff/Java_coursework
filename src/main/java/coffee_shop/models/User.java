package coffee_shop.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "session_id")
    private String sessionID;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String password, Collection<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
    @ManyToMany (cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_basket",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coffee_id")
    )
    private List<Coffee> coffees;

    public void addCoffee(Coffee coffee){
        this.coffees.add(coffee);
        coffee.getBaskets().add(this);
    }

    public void removeCoffee(Coffee coffee){
        this.coffees.remove(coffee);
        coffee.getBaskets().remove(this);
    }
}
