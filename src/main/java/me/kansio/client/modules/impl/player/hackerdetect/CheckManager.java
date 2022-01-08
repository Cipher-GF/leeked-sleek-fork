package me.kansio.client.modules.impl.player.hackerdetect;

import lombok.Getter;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import me.kansio.client.modules.impl.player.hackerdetect.checks.phase.CagePhaseCheck;

import java.util.ArrayList;

@Getter
public class CheckManager {

    private ArrayList<Check> checks = new ArrayList<>();

    public CheckManager() {
        checks.add(new CagePhaseCheck());
    }
}
