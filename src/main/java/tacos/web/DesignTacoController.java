package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.data.Ingredient;
import tacos.data.Ingredient.Type;
import tacos.data.Order;
import tacos.data.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private IngredientRepository ingredientRepo;
    private TacoRepository tacoRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
    }
    /*
        When @ModelAttribute annotation is used at the method level it indicates the
        purpose of that method is to add one or more model attributes.

        Spring MVC will always make a call first to @ModelAttribute annotated methods
        before it calls any request handles methods
        @ModelAttribute methods are invoked before the controller methods
        annotated with @RequestMapping are invoked
     */
    @ModelAttribute(name = "order")
    public Order order(Model model) {
        if(!model.containsAttribute("order"))
            return new Order();
        return (Order) model.getAttribute("order");
    }

    /*@ModelAttribute(name = "taco")
    public Taco taco(Model model){
        if(!model.containsAttribute("taco"))
            return new Taco();
        return (Taco) model.getAttribute("taco");
    }*/

    /*
        When @ModelAttribute is used as a method request,
        it indicates the argument should be retrieved from the model
     */

    @PostMapping
    public String processDesign(@Valid @ModelAttribute Taco design, Errors errors,
                                @ModelAttribute Order order, Model model){
        if(errors.hasErrors()){
            log.info("Got the error");
            return "design";
        }

        Taco saved = tacoRepo.save(design);

        order.addDesign(saved);
        log.info("Name" + design.getName());
        return "redirect:/orders/current";
    }

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ing -> ingredients.add(ing));

        Type[] types = Type.values();
        for(Type type : types){
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());
        log.info("Everything is fine so far");
        return "design";
    }

    public List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
