package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Order;

@Slf4j
@RequestMapping("/orders")
@Controller
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model){
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    @PostMapping
    public String processOrder(Order order){
        log.info("Name: " + order.getName());
        log.info(order.getCity());
        log.info(order.getCcNumber());
        log.info(order.getState());
        return "redirect:/";
    }
}
