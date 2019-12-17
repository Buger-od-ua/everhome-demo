package com.budova.everhome.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime time;
    private Float value;

    public Temperature() { }

    public Temperature(Long id, LocalDateTime time, Float value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }

    public Temperature(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public static boolean isModuled(Temperature t1, Temperature t2, Float module) {
        return Math.abs(t1.value - t2.value) > module || Duration.between(t1.time, t2.time).getSeconds() > 300;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "id=" + id +
                ", time=" + time +
                ", value=" + value +
                '}';
    }
}
