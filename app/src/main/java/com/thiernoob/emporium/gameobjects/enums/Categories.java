package com.thiernoob.emporium.gameobjects.enums;

public enum Categories {
    CONSUMABLE {
        public String toString() {
            return "Consumable";
        }
    },
    INGREDIENT {
        public String toString() {
            return "Ingredients";
        }
        },
    WEAPON{
        public String toString() {
            return "Weapon";
        }
    },
    SHIELD{
        public String toString() {
            return "Shield";
        }
    },
    SPELL{
        public String toString() {
            return "Spell";
        }
    };

    public static Categories value(String v) {
        if (v.equals("Shield")) {
            return SHIELD;
        }
        else if (v.equals("Spell")) {
            return SPELL;
        }
        else if (v.equals("Ingredient")) {
            return INGREDIENT;
        }
        else if (v.equals("Weapon")) {
            return WEAPON;
        }
        else if (v.equals("Consumable")) {
            return CONSUMABLE;
        }
        else{
            return CONSUMABLE;
        }
    }
}
