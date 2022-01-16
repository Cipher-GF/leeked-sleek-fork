package me.kansio.client.commands.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kansio.client.Client;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.network.HttpUtil;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

public class UsersCommand extends Command {
    public UsersCommand() {
        super("users");
    }

    @Override
    public void run(String[] args) {

        try {
            JsonElement node = new JsonParser().parse(HttpUtil.get("http://zerotwoclient.xyz:13337/api/v1/getclientplayers"));


            if (node.isJsonArray()) {
                Client.getInstance().getUsers().clear();
                for (JsonElement ele : node.getAsJsonArray()) {
                    JsonObject obj = ele.getAsJsonObject();

                    Client.getInstance().getUsers().put(obj.get("ign").getAsString(), obj.get("name").getAsString());

                    ChatUtil.log(MessageFormat.format(ChatUtil.translateColorCodes("&b{0} [{1}] &7({2})&b - {3}"), obj.get("name").getAsString(), obj.get("uid").getAsString(), obj.get("ign").getAsString(), obj.get("serverIP").getAsString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
