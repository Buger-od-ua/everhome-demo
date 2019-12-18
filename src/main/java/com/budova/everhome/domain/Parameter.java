package com.budova.everhome.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Parameter {
    TEMPERATURE_S1(1),
    TEMPERATURE_S2(2),
    SET_TEMPERATURE(3),
    VALVE_POSITION(4);

    private Integer id;
    private String name;

    private static final Map<Integer, Parameter> STATIC_HOLDER = Arrays
            .stream(Parameter.values())
            .collect(Collectors.toMap(Parameter::getId, Function.identity())
    );

    Parameter(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    Parameter(Integer id) {
        this.id = id;
        this.name = "";
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Parameter of(Integer id) {
        return STATIC_HOLDER.get(id);
    }
}
