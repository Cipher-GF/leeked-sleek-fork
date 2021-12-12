package me.kansio.client.property.value;


import me.kansio.client.property.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class ModeValue extends Value<String> {

    private final List<String> choices;
    private final List<String> choicesLowerCase;

    public ModeValue(String name, Object owner, String... values) {
        super(name, owner, values[0]);
        choices = Arrays.asList(values);
        choicesLowerCase = new ArrayList<>();
        for (String choice : values) {
            choicesLowerCase.add(choice.toLowerCase());
        }
    }

    @Override
    public void setValueAutoSave(String value) {
        if (choicesLowerCase.contains(value.toLowerCase())) {
            super.setValueAutoSave(value);
        }
    }

    public void forceSetValue(String value) {
        super.setValueAutoSave(value);
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getChoicesLowerCase() {
        return choicesLowerCase;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || getValue().equals(o);
    }

}