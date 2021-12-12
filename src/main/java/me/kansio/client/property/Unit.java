package me.kansio.client.property;


/**
 * @author - Aidan#1337
 * @created 9/26/2020 at 11:22 AM
 * Do not distribute this code without credit
 * or ill get final on ur ass
 */

public enum Unit {
    BLOCKS("blocks"),
    MS("ms"),
    CPS("cps"),
    PERCENT("%"),
    BPT("bpt"),
    X("X"),
    Y("Y"),
    TPS("tps");


    final String displayText;

    Unit(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}