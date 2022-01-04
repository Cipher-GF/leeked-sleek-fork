package me.kansio.client.property.value;

import lombok.Getter;
import lombok.Setter;
import me.kansio.client.property.Value;
import java.util.ArrayList;
import java.util.Collections;

public class SubCategory {

    @Getter private ArrayList<Value<?>> subSettings = new ArrayList<>();
    @Getter @Setter private String name;

    public SubCategory(String name, Value<?>... values) {
        this.name = name;
        Collections.addAll(subSettings, values);
    }

}
