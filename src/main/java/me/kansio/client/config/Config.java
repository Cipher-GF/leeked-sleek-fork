package me.kansio.client.config;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class Config {

    @Getter @Setter private String name;
    @Getter @Setter private File file;

    public Config(String name, File file) {
        this.name = name;
        this.file = file;
    }

}
