package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Location;

public class Kingdom {
    private Location kingdom;


    public Kingdom(Location kingdom) {
        this.kingdom = kingdom;
    }

    public Location getKingdom() {
        return kingdom;
    }

    public static boolean bonusOn(Location location, Categories type) {
        switch (location) {
            case AZIR:
                if (type == Categories.WEAPON || type == Categories.CONSUMABLE) {
                    return true;
                } else {
                    return false;
                }
            case AIMIA:
                if (type == Categories.CONSUMABLE || type == Categories.SPELL) {
                    return true;
                } else {
                    return false;
                }
            case ALETHKAR:
                if (type == Categories.WEAPON || type == Categories.SHIELD) {
                    return true;
                } else {
                    return false;
                }
            case JAH_KEVED:
                if (type == Categories.SPELL || type == Categories.INGREDIENT) {
                    return true;
                } else {
                    return false;
                }
            case NARAK:
                if (type == Categories.SHIELD || type == Categories.INGREDIENT) {
                    return true;
                } else {
                    return false;
                }
            case RESHI:
                if (type == Categories.CONSUMABLE || type == Categories.SHIELD) {
                    return true;
                } else {
                    return false;
                }
            case SHINOVAR:
                if (type == Categories.SPELL || type == Categories.WEAPON) {
                    return true;
                } else {
                    return false;
                }
            case BONTA:
                if (type == Categories.CONSUMABLE || type == Categories.INGREDIENT) {
                    return true;
                } else {
                    return false;
                }
            case BRAKMAR:
                if (type == Categories.INGREDIENT || type == Categories.WEAPON) {
                    return true;
                } else {
                    return false;
                }
            case ASTRUB:
                if (type == Categories.SPELL || type == Categories.SHIELD) {
                    return true;
                } else {
                    return false;
                }
            case TRAVELING:
                return false;
        }

        return false;
    }

    public static String kingdomBonus(Location location) {
        String msg = "Bonus in " + location.toString() + " is on : ";

        switch (location) {
            case AZIR:
                return msg + Categories.WEAPON.toString() + " and " + Categories.CONSUMABLE.toString();
            case AIMIA:
                return msg + Categories.CONSUMABLE.toString() + " and " + Categories.SPELL.toString();
            case ALETHKAR:
                return msg + Categories.WEAPON.toString() + " and " + Categories.SHIELD.toString();
            case JAH_KEVED:
                return msg + Categories.SPELL.toString() + " and " + Categories.INGREDIENT.toString();
            case NARAK:
                return msg + Categories.SHIELD.toString() + " and " + Categories.INGREDIENT.toString();
            case RESHI:
                return msg + Categories.CONSUMABLE.toString() + " and " + Categories.SHIELD.toString();
            case SHINOVAR:
                return msg + Categories.SPELL.toString() + " and " + Categories.WEAPON.toString();
            case BONTA:
                return msg + Categories.CONSUMABLE.toString() + " and " + Categories.INGREDIENT.toString();
            case BRAKMAR:
                return msg + Categories.INGREDIENT.toString() + " and " + Categories.WEAPON.toString();
            case ASTRUB:
                return msg + Categories.SPELL.toString() + " and " + Categories.SHIELD.toString();

            case TRAVELING:
                return "None and you are not supposed to see this";
        }
        return "None and you are not supposed to see this";
    }

}
