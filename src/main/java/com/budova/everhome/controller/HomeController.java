package com.budova.everhome.controller;

import com.budova.everhome.domain.*;
import com.budova.everhome.repos.ConnectionRepo;
import com.budova.everhome.repos.SetTemperatureRepo;
import com.budova.everhome.repos.TemperatureRepo;
import com.budova.everhome.repos.ValvePosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TemperatureRepo tempRepo;
    @Autowired
    private ValvePosRepo valvePosRepo;
    @Autowired
    private SetTemperatureRepo setTemperatureRepo;
    @Autowired
    private ConnectionRepo connectionRepo;

    @GetMapping("/home")
    public String home(Model model) {
        Temperature t1 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE_S1);
        Temperature t2 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE_S2);
        ValvePos v = valvePosRepo.findFirstByParamIsOrderByTimeDesc(Parameter.VALVE_POSITION);
        SetTemperature st = setTemperatureRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_TEMPERATURE);
        Connection c = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
        model.addAttribute("temperature1", t1 != null ? t1.getValue() : "null");
        model.addAttribute("temperature2", t2 != null ? t2.getValue() : "null");
        model.addAttribute("set_temperature", st != null ? st.getValue() : "null");
        model.addAttribute("valve", v != null ? v.getValue() : "null");
        model.addAttribute("connection", c != null ? c.getValue() : "null");
        List<Temperature> temps = tempRepo.findTop10ByParamIsOrderByTimeDesc(Parameter.TEMPERATURE_S1);
        StringBuilder sb = new StringBuilder();
        for (Temperature t : temps) {
            sb.append(t.toString());
        }
        model.addAttribute("temperature3", sb.toString());
        return "home";
    }

}
