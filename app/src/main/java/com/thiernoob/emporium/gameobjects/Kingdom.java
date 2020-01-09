package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Location;

public class Kingdom {
    private Location kingdom;

    public Kingdom(Location kingdom){
        this.kingdom = kingdom;
    }

    public Location getKingdom() {
        return kingdom;
    }

    public void setKingdom(Location kingdom) {
        this.kingdom = kingdom;
    }
}
