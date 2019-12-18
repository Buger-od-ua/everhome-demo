package com.budova.everhome.dto;

import com.budova.everhome.domain.SetTemperature;

import java.time.LocalDateTime;

public class SetTemperatureDto {

    private LocalDateTime time;
    private Float value;

    public SetTemperatureDto() { }

    public SetTemperatureDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetTemperatureDto(SetTemperature t) {
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
