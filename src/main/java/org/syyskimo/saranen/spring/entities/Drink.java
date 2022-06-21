package org.syyskimo.saranen.spring.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Drink implements Comparable<Drink> {
    private double vol;
    private double alcVol;

    private LocalDateTime drinkingMoment;
    private final String uuid;

    public static final double ONESHOT = 0.01551;

    public Drink(double vol, double alcVol)
    {
        this.vol = vol;
        this.alcVol = alcVol;
        this.drinkingMoment = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
    }

    public String toString()
    {
        return String.format("%.2f", this.vol) + "l (" + String.format("%.1f", this.alcVol) + "%)";
    }

    public double getAlc()
    {
        return (this.vol * (this.alcVol / 100.0));
    }

    public double getAlcVol() {
        return this.alcVol;
    }

    public double getVolume() {
        return this.vol;
    }

    public long getSecondsSince() {
        return (ChronoUnit.SECONDS.between(this.drinkingMoment, LocalDateTime.now()));
    }

    public double getShotAmount()
    {
        return (this.getAlc() / Drink.ONESHOT);
    }

    public String getId() {
        return this.uuid;

    }

    @Override
    public int compareTo(Drink o) {
        return this.drinkingMoment.compareTo(o.drinkingMoment);
    }
}