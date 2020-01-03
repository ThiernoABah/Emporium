package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Align;
import com.thiernoob.emporium.gameobjects.enums.Location;

import java.util.Random;

public class Player {

    private final int STARTUP_GOLD = 10000;
    private final int STARTUP_KARMA = 50;

    private static Player player = null;

    private String pseudo;
    private int gold;
    private int karma;
    private Location location;
    private Align alignment;


    private Player(){

        this.gold = STARTUP_GOLD;
        this.karma = STARTUP_KARMA;
        this.alignment = Align.NEUTRAL;
        Random rand = new Random();
        this.location = Location.values()[rand.nextInt(Location.values().length )];
    }

    public static Player getPlayer()
    {
        if (player == null)
            player = new Player();

        return player;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void giveGold(int gold) {
        this.gold -= gold;
    }

    public void updateKarma(int karma) {
        this.karma += karma;
        this.updateAlign();
    }


    public void updateAlign() {
        if(this.karma < 0){
            this.alignment = Align.CHAOTIC;
        }
        else{
            if(this.karma < 100){
                this.alignment = Align.NEUTRAL;
            }
            else{
                this.alignment = Align.LAWFUL;
            }
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getPseudo() {
        return pseudo;
    }

    public int getGold() {
        return gold;
    }

    public int getKarma() {
        return karma;
    }

    public Location getLocation() {
        return location;
    }

    public Align getAlignment() {
        return alignment;
    }


}