package com.budova.everhome.controller;

import com.budova.everhome.domain.Temperature;
import com.budova.everhome.repos.TemperatureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private TemperatureRepo tempRepo;

    @GetMapping("/home")
    public String home(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            Model model
    ) {
        Temperature t = tempRepo.findFirstByOrderByTimeDesc();
        model.addAttribute("temperature", t.toString());
        return "home";
    }

}
