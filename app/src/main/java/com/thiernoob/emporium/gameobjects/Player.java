package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Align;
import com.thiernoob.emporium.gameobjects.enums.Location;

import java.util.Random;

public class Player {

    private final int STARTUP_GOLD = 20000;
    private final int STARTUP_KARMA = 50;

    private static Player player = null;

    private String pseudo;
    private int gold;
    private int karma;
    private Location location;
    private Align alignment;


    private Player() {

        this.gold = STARTUP_GOLD;
        this.karma = STARTUP_KARMA;
        this.alignment = Align.NEUTRAL;
        Random rand = new Random();
        Location l = Location.TRAVELING;
        while(l == Location.TRAVELING){
            l =  Location.values()[rand.nextInt(Location.values().length)];
        }
        this.location = l ;
    }

    public static Player getPlayer() {
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

        if(this.karma > 100){
            this.karma = 100;
        }
        if(this.karma < -50){
            this.karma = -50;
        }
        this.updateAlign();
    }


    public void updateAlign() {
        if (this.karma < 0) {
            this.alignment = Align.CHAOTIC;
        } else {
            if (this.karma < 50) {
                this.alignment = Align.NEUTRAL;
            } else {
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

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public void setAlignment(Align alignment) {
        this.alignment = alignment;
    }

    public Align getAlignment() {
        return alignment;
    }


}
