package cs5500.cs5500.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HelloController {

  @GetMapping({"/", "/index"})
  public String index(Model model, @RequestParam(value="message", required=false, defaultValue="Greetings from CS5500 Group 11!") String message) {
    model.addAttribute("message", message);
    return "index";
  }

}