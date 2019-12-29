package com.thiernoob.emporium.adapter;

import com.thiernoob.emporium.gameobjects.Item;

public class Offer {

    private int id;
    private int karma;
    private Item item;

    public Offer(int id, int karma , Item item){
        this.id = id;
        this.karma = karma;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
