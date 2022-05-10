package coffee_shop.controllers;

import coffee_shop.models.Coffee;
import coffee_shop.services.BasketService;
import coffee_shop.services.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class CoffeeController {

    @Autowired
    private CoffeeService coffeeService;

    @Autowired
    private BasketService basketService;

    @GetMapping("/{sort}")
    @ResponseBody
    public ModelAndView getcoffees(@PathVariable String sort){
        ModelAndView mav = new ModelAndView("shop");
        List<Coffee> coffees= coffeeService.getBySort(sort);
        mav.addObject("coffee", coffees);
        return mav;
    }

    @GetMapping("/{sort}/get/{name}")
    @ResponseBody
    public ModelAndView getCoffee(@PathVariable String sort, @PathVariable String name){
        List<Coffee> coffees = coffeeService.getBySort(sort);
        Coffee coffee = coffeeService.getByName(coffees, name);
        ModelAndView mav = new ModelAndView("coffee");
        mav.addObject("coffee", coffee);
        return mav;
    }

    @GetMapping("{sort}/add/{name}")
    @ResponseBody
    public ModelAndView addCoffee(@PathVariable String sort, @PathVariable String name){
        List<Coffee> coffees = coffeeService.getBySort(sort);
        Coffee coffee = coffeeService.getByName(coffees, name);
        ModelAndView mav = new ModelAndView("shop");
        basketService.addCoffee(coffee);
        mav.addObject("coffee", coffees);
        mav.addObject("product", coffee.getName());
        mav.addObject("purchase", true);
        return mav;
    }

}
