package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetTemperature;
import org.springframework.data.repository.CrudRepository;

public interface SetTemperatureRepo extends CrudRepository<SetTemperature, Long> {

    SetTemperature findFirstByParamIsOrderByTimeDesc(Parameter param);

}
