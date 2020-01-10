package com.budova.everhome.controller;

import com.budova.everhome.domain.Temperature;
import com.budova.everhome.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @GetMapping("/api/temperature/day")
    public List<Temperature> getTemperature() {
        return temperatureService.getTemperatureForLastDay();
    }

}
