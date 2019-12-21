package com.budova.everhome.dto;

import com.budova.everhome.domain.Connection;

import java.time.LocalDateTime;

public class ConnectionDto {

    private LocalDateTime time;
    private Boolean value;

    public ConnectionDto(LocalDateTime time, Boolean value) {
        this.time = time;
        this.value = value;
    }

    public ConnectionDto(Connection c) {
        this.time = c.getTime();
        this.value = c.getValue();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
