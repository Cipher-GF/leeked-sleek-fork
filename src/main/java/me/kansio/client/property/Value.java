package me.kansio.client.property;

import lombok.Getter;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Value<Type> {

    private final String name;
    @Getter
    protected List<String> modes = new ArrayList<>();
    private final Object owner;
    protected Value<?> parent;
    protected Type value;

    public Value(String name, Object owner, Type value) {
        this.name = name;
        this.owner = owner;
        this.value = value;
    }

    public Value(String name, Object owner, Type value, ModeValue parent, String... modes) {
        this(name, owner, value);
        this.parent = parent;
        Collections.addAll(this.modes, modes);
    }

    public Value(String name, Object owner, Type value, BooleanValue parent) {
        this(name, owner, value);
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Object getOwner() {
        return owner;
    }

    public Type getValue() {
        return value;
    }

    public void setValueAutoSave(Type value) {
        this.value = value;
        /**
         *
        ////Sulfur.getInstance().getFileManager().getObjects().forEach(MFile::save);
        */
    }


    public void setValue(Type value) {
        this.value = value;
    }

    public Value getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public String getValueAsString() {
        return String.valueOf(value);
    }

}