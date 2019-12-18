package com.budova.everhome.dto;

import com.budova.everhome.domain.ValvePos;

import java.time.LocalDateTime;

public class ValvePosDto {

    private LocalDateTime time;
    private Float value;

    public ValvePosDto() { }

    public ValvePosDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public ValvePosDto(ValvePos v) {
        this.time = v.getTime();
        this.value = v.getValue();
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
