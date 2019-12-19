package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.Temperature;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepo extends CrudRepository<Temperature, Long> {

    Temperature findFirstByParamIsOrderByTimeDesc(Parameter param);

}
