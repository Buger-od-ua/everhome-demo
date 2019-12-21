package com.budova.everhome.repos;

import com.budova.everhome.domain.Connection;
import com.budova.everhome.domain.Parameter;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionRepo extends CrudRepository<Connection, Long> {

    Connection findFirstByParamIsOrderByTimeDesc(Parameter param);

}
