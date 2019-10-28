package by.it.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Power {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String name;
    @Column
    private int power;

    public Power(String name, int power) {
        this.name = name; this.power = power;
    }

    public Power(long id, String name, int power) {
        this.id = id;
        this.name = name;
        this.power = power;
    }

    public Power() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
