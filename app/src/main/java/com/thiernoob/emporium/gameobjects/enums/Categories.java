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
    }
}
