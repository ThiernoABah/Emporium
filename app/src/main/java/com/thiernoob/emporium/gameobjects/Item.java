package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

public class Item {

    private int id;
    private String name;
    private int original_price;
    private int price;
    private Categories type;
    private Rarity rarity;
    private String description;
    private int timeInShop;


    public Item(String name, int price, Categories type, Rarity rarity) {
        this.name = name;
        this.price = price;
        this.original_price = price;
        this.type = type;
        this.rarity = rarity;
        this.description = "Nothing much to say...";
        this.timeInShop = 0;
    }

    public Item(String name, int price, Categories type, Rarity rarity, String description) {
        this.name = name;
        this.price = price;
        this.original_price = price;
        this.type = type;
        this.rarity = rarity;
        if (description == null) {
            this.description = "Nothing much to say...";
        } else {
            this.description = description;
        }
        this.timeInShop = 0;
    }

    public Item(int id, String name, int price, Categories type, Rarity rarity, String description, int orginalPrice, int timeInShop) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.original_price = orginalPrice;
        this.type = type;
        this.rarity = rarity;
        if (description == null) {
            this.description = "Nothing much to say...";
        } else {
            this.description = description;
        }
        this.timeInShop = timeInShop;

    }

    public int getTimeInShop() {
        return timeInShop;
    }

    public void setTimeInShop(int timeInShop) {
        this.timeInShop = timeInShop;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(Categories type) {
        this.type = type;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getOriginalPrice() {
        return original_price;
    }

    public Categories getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getDescription() {
        return description;
    }


}
