package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private IngredientRepository ingredientRepo;
    private TacoRepository tacoRepo;
    private IngredientByIdConverter converter;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo,
                                IngredientByIdConverter converter) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
        this.converter = converter;
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
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    /*
        When @ModelAttribute is used as a method request,
        it indicates the argument should be retrieved from the model
     */

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors,
                                @ModelAttribute Order order){
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

        model.addAttribute("design", new Taco());
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
