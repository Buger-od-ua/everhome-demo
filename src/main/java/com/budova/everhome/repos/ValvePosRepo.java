package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.ValvePos;
import org.springframework.data.repository.CrudRepository;

public interface ValvePosRepo extends CrudRepository<ValvePos, Long> {

    public ValvePos findFirstByParamIsOrderByTimeDesc(Parameter param);

}
