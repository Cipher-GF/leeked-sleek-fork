package me.kansio.client.modules.impl.player.hackerdetect;

import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
//import me.kansio.client.modules.impl.player.hackerdetect.checks.combat.*;
import me.kansio.client.modules.impl.player.hackerdetect.checks.movement.*;
import me.kansio.client.modules.impl.player.hackerdetect.checks.exploit.*;
import java.util.ArrayList;

public class CheckManager {
    private ArrayList<Check> checks = new ArrayList<>();

    public CheckManager() {
        // Combat
        // Movement
        checks.add(new SpeedA());
        checks.add(new FlightA());
        //Exploit
        checks.add(new PhaseA());
    }

    @SuppressWarnings("all")
    public ArrayList<Check> getChecks() {
        return this.checks;
    }
}
