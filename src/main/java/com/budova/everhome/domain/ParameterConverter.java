package com.budova.everhome.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ParameterConverter implements AttributeConverter<Parameter, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Parameter parameter) {
        Integer id = null;
        if (parameter != null) {
            id = parameter.getId();
        }
        return id;
    }

    @Override
    public Parameter convertToEntityAttribute(Integer id) {
        Parameter parameter = null;
        if (id != null) {
            parameter = Parameter.of(id);
            if (parameter == null) {
                throw new IllegalArgumentException("No Parameter enum value for such id");
            }
        }
        return parameter;
    }
}
