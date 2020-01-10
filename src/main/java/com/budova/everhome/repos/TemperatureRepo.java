package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.Temperature;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface TemperatureRepo extends CrudRepository<Temperature, Long> {

    Temperature findFirstByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<Temperature> findTop10ByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<Temperature> findByParamAndTimeAfterOrderByTimeDesc(Parameter param, LocalDateTime after);

}
