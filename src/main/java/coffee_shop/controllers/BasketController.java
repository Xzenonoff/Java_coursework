package coffee_shop.controllers;

import coffee_shop.models.Coffee;
import coffee_shop.services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("/basket")
    @ResponseBody
    public ModelAndView getBasket(){
        ModelAndView mav = new ModelAndView("basket");
        List<Coffee> coffees = basketService.getSetCoffee(basketService.getCoffees());
        coffees.sort(Comparator.comparing(Coffee::getId));
        mav.addObject("coffee", coffees);
        mav.addObject("num", basketService.getCounts(coffees, basketService.getCoffees()));
        mav.addObject("price", basketService.getPrice(basketService.getCoffees()));
        mav.addObject("discount", (int) Math.round(basketService.getStock(basketService.getCoffees())));
        return mav;
    }
    @GetMapping("/basket/delete/{name}")
    public String deleteBasket(@PathVariable String name){
        basketService.deleteCoffee(name);
        return "redirect:/basket";
    }
    @GetMapping("/basket/buy")
    public String deleteBasket(){
        basketService.buy();
        return "redirect:/";
    }
}
