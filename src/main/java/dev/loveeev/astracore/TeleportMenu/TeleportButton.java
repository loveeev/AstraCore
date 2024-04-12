package dev.loveeev.astracore.TeleportMenu;

import dev.loveeev.astracore.data.Teleports;
import lombok.Getter;

@Getter
public class TeleportButton {
    private final String name;
    private final Integer x1;
    private final Integer y1;
    private final Integer x2;
    private final Integer y2;

    public TeleportButton(String name, Integer x1, Integer y1, Integer x2, Integer y2) {
        this.name = name;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Teleports.addAll(this);
    }
}
