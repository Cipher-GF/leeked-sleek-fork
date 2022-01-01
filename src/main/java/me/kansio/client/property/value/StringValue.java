package me.kansio.client.property.value;

import me.kansio.client.property.Value;

public class StringValue extends Value<String> {

    String value;

    public StringValue(String name, Object owner, String value) {
        super(name, owner, value);

        this.value = value;
    }
}
