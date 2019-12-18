package com.budova.everhome.dto;

import com.budova.everhome.domain.Temperature;

import java.time.LocalDateTime;

public class TemperatureDto {

    private LocalDateTime time;
    private Float value;

    public TemperatureDto() { }

    public TemperatureDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public TemperatureDto(Temperature t) {
        this.time = t.getTime();
        this.value = t.getValue();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
