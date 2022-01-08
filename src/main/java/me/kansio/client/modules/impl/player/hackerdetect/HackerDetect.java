package me.kansio.client.modules.impl.player.hackerdetect;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;

public class HackerDetect extends Module {

    public HackerDetect() {
        super("Hacker Detect", ModuleCategory.PLAYER);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        for (Check c : Client.getInstance().getCheckManager)
    }

}
