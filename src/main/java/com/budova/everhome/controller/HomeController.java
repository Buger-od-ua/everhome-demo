package com.budova.everhome.controller;

import com.budova.everhome.domain.Temperature;
import com.budova.everhome.repos.TemperatureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableScheduling
public class HomeController {

    @Autowired
    private TemperatureRepo tempRepo;

    @GetMapping("/home")
    public String home(Model model) {
        Temperature t = tempRepo.findFirstByOrderByTimeDesc();
        model.addAttribute("temperature", t != null ? t.getValue() : "null");
        return "home";
    }

}
