package me.kansio.client.property.value;

import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.Value;

public class StringValue extends Value<String> {

    String value;

    public StringValue(String name, Module owner, String value) {
        super(name, owner, value);

        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
