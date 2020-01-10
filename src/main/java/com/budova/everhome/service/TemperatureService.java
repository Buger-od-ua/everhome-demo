package com.budova.everhome.service;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.Temperature;
import com.budova.everhome.repos.TemperatureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemperatureService {

    @Autowired
    private TemperatureRepo temperatureRepo;

    public List<Temperature> getTemperatureForLastDay() {
        LocalDateTime dayBefore = LocalDateTime.now().minusDays(1);
        return temperatureRepo.findByParamAndTimeAfterOrderByTimeDesc(Parameter.TEMPERATURE_S1, dayBefore);
    }

}
