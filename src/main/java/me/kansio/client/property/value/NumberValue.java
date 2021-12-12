package me.kansio.client.property.value;

import lombok.Getter;
import me.kansio.client.property.Unit;
import me.kansio.client.property.Value;

@Getter
public final class NumberValue extends Value<Double> {

    private final double min, max;
    private boolean isInt;
    private double increment;

    public NumberValue(String name, Object owner, double value, double min, double max, double increment, boolean isInt) {
        super(name, owner, value);
        checkRetardMoment(value);
        this.min = min;
        this.isInt = isInt;
        this.max = max;
        this.increment = increment;
    }

    private void checkRetardMoment(double value) {
        if (value < min) {
            try {
                throw new Exception("Retard Exception: Default Value < Min Value");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (min < 0 || max < 0) {
            try {
                throw new Exception("Retard Exception: Min or Max Value below zero!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    @SuppressWarnings("all")
    public <T extends Number> T getCastedValue() {
        return (T) super.getValue();
    }

    @Override
    public Double getValue() {
        if (isInt) {
            return (double) super.getValue().intValue();
        }
        return super.getValue();
    }

    @Override
    public void setValueAutoSave(Double value) {
        super.setValueAutoSave(Math.min(Math.max(min, value), max));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || getValue().equals(o);
    }

    public boolean isInteger() {
        return isInt;
    }

}