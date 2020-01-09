package com.thiernoob.emporium.gameobjects;

import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

import java.util.ArrayList;

public class InitialiseItems {

    private static InitialiseItems initItems = null;

    private static ArrayList<Item> allItems;

    private InitialiseItems() {
        this.allItems = new ArrayList<>();
    }

    public static InitialiseItems InitialiseItems() {
        if (initItems == null) {
            initItems = new InitialiseItems();
        }

        // Spells
        allItems.add(new Item("Enchirion", 100000, Categories.SPELL, Rarity.LEGENDARY, "The Enchiridion was an ancient book with codes of conduct, guidelines, and other helpful information for heroes. This book have great power which allow it to tear open worm holes between dimensions in the multiverse"));
        allItems.add(new Item("Draco Meteor", 50000, Categories.SPELL, Rarity.EPIC, "Comets are summoned down from the sky onto the target"));
        allItems.add(new Item("Word of Recovery", 45000, Categories.SPELL, Rarity.EPIC, "Completely heals your target"));
        allItems.add(new Item("Mana Drain", 10000, Categories.SPELL, Rarity.RARE, "A power that drains someone else's Mana Meter and gives it to the caster."));
        allItems.add(new Item("Gravity Shift", 15000, Categories.SPELL, Rarity.RARE, "You can change the direction of gravity (for yourself only) once every 6 seconds"));
        allItems.add(new Item("Earthquake", 9000, Categories.SPELL, Rarity.RARE, "The ground begins shaking violently. Structures may be damaged or collapse."));
        allItems.add(new Item("Illuminate", 1000, Categories.SPELL, Rarity.COMMON, "A floating light moves as you command it."));
        allItems.add(new Item("Adhere", 800, Categories.SPELL, Rarity.COMMON, "Object becomes extremely sticky."));
        allItems.add(new Item("Spider Climb", 850, Categories.SPELL, Rarity.COMMON, "You can climb surfaces like a spider."));
        allItems.add(new Item("Telepathy", 1900, Categories.SPELL, Rarity.COMMON, "You can hear each other’s thoughts now."));

        // Ingredients
        allItems.add(new Item("Crystal Gem Apple", 90000, Categories.INGREDIENT, Rarity.LEGENDARY, "Whoever eats the Crystal Gem Apple is teleported to the Crystal Dimension and given ultimate power over the Crystal Men until the gem leaves their body."));
        allItems.add(new Item("Glow Stone", 25000, Categories.INGREDIENT, Rarity.EPIC, "Unusually round stone which glows in a pale blue hue. Ancient people used it as a source of light"));
        allItems.add(new Item("Lycanthrope Blood", 10000, Categories.INGREDIENT, Rarity.RARE, "Blood taken from a lycanthropy victim. There\\'s no telling if you can get infected with it"));
        allItems.add(new Item("Ectoplasm", 30000, Categories.INGREDIENT, Rarity.EPIC, "Viscious substance with supernatural properties. A single drip gives you nightmares"));
        allItems.add(new Item("Mana Crystal", 8000, Categories.INGREDIENT, Rarity.RARE, "Shards of crystal containing magical energy. It emanates a faint bluish aura."));
        allItems.add(new Item("Focus Stone", 13000, Categories.INGREDIENT, Rarity.RARE, "Artificial stone used on constructions to generate a weak amount of mana. Might have other uses."));
        allItems.add(new Item("Honeycomb", 1000, Categories.INGREDIENT, Rarity.COMMON, "Chunk of raw honey collected by Stinger drones. I can tell it's very sweet."));
        allItems.add(new Item("Stubby Carrot", 900, Categories.INGREDIENT, Rarity.COMMON, "Giant piece of wild carrot. What on the realm made them to grow so big?"));
        allItems.add(new Item("Beef Jerky", 600, Categories.INGREDIENT, Rarity.COMMON, "Hefty chunk of raw meat. Looks tender and juicy, but I won\\'t eat it raw."));
        allItems.add(new Item("Regallium Flower", 400, Categories.INGREDIENT, Rarity.COMMON, "Small bell-shaped flower of a wild plant. Its fragrance is both soothing and relaxing"));
        allItems.add(new Item("Venom Extract", 500, Categories.INGREDIENT, Rarity.COMMON, "Extracted venom inside a tightly sealed vial. While poisonous, it's useless in this state."));


        // Consumable
        allItems.add(new Item("Panacea", 110000, Categories.CONSUMABLE, Rarity.LEGENDARY, "A mythical substance able to cure any illness or poison and even prolong one's life."));
        allItems.add(new Item("Gutrot Potion", 50000, Categories.CONSUMABLE, Rarity.EPIC, "This unpalatable potion will force you to make a crucial choice between drinking it to save your life in agony, or dying but suffering less."));
        allItems.add(new Item("Starry Potion", 15000, Categories.CONSUMABLE, Rarity.RARE, "Use this potion to travel to the stars without leaving your house."));
        allItems.add(new Item("Raid Rayve Potion", 10000, Categories.CONSUMABLE, Rarity.RARE, "This potion will carry you away from the spirit realm, and get you back on your feet."));
        allItems.add(new Item("Game Andouillette", 900, Categories.CONSUMABLE, Rarity.COMMON, "Eat this sausage with respect, because you might find yourself in its place after the next manhunt."));
        allItems.add(new Item("Hota", 500, Categories.CONSUMABLE, Rarity.COMMON, "It's like a pita, only not."));
        allItems.add(new Item("Beer", 100, Categories.CONSUMABLE, Rarity.COMMON, "A six pack of this classic brew is the only six pack you'll be seeing for a while. "));
        allItems.add(new Item("Mini Healing Potion", 800, Categories.CONSUMABLE, Rarity.COMMON, "This mini potion will make you feel a little better."));


        // WEAPONS
        allItems.add(new Item("Queen of Thieves' Sword", 110000, Categories.WEAPON, Rarity.LEGENDARY, "This imposing sword is very manageable. So much so that the Queen of Thieves always keeps it on her. Some even say it's her closest friend and confidant."));
        allItems.add(new Item("Ush's Sword", 80000, Categories.WEAPON, Rarity.EPIC, "Each of Ush's swords represents a part of himself. This one represents his flamboyant side, so you'll be leaving him with only his darker, serious side."));
        allItems.add(new Item("Koutoulou Bow", 59000, Categories.WEAPON, Rarity.EPIC, "The second in a long line of bows where an eye replaces the cord. Nobody really understands how it works, but it seems as if the arrows it fires come from another dimension."));
        allItems.add(new Item("Tomahorse", 25000, Categories.WEAPON, Rarity.RARE, "This axe will transform any enemy into a docile mount. Plant it firmly between their shoulder blades, use the handle as a saddle, and with a few cracks of a whip you'll be galloping along in no time."));
        allItems.add(new Item("Hammerture", 7000, Categories.WEAPON, Rarity.COMMON, "This hammer was created to cause suffering. First, visually, with its appearance. It also generates sounds imitating a cheap plastic flute, which makes your ears suffer. And, of course, it can generate massive physical damage."));
        allItems.add(new Item("Ragnarok", 8000, Categories.WEAPON, Rarity.COMMON, "Ragnarok is powerful enough to raise a storm. It can also raise the temper of your friends if you don't know how to use it properly"));
        allItems.add(new Item("God Rod", 10000, Categories.WEAPON, Rarity.COMMON, "The God Rod is a fearsome weapon. Warriors have been known to win battles in their sleep with the aid of this... fearsome weapon"));
        allItems.add(new Item("Wand Else?", 8000, Categories.WEAPON, Rarity.COMMON, "This wand belongs to a famous Grinegau who makes excellent coffee, but who just can't get the ladies."));

        // SHIELDS
        allItems.add(new Item("Crocoshield", 100000, Categories.SHIELD, Rarity.LEGENDARY, "Crocodyl scales are as hard as Iops' heads. This shield is covered in them, making it a must-have accessory for adventurers everywhere who like to jump carelessly into the fray. "));
        allItems.add(new Item("Seven Years Bad Luck ", 89000, Categories.SHIELD, Rarity.EPIC, "This shield is made of a very fragile mirror. It won't protect you against much of anything at all, but if your opponent breaks it, you can be sure they'll suffer seven years bad luck. Or at least, that's what you can tell yourself after your humiliating defeat."));
        allItems.add(new Item("Sponghield", 50000, Categories.SHIELD, Rarity.RARE, "This porous but absorbent yellow shield was made from the remains of a creature who used to live in a Pineapi Mask at the bottom of the sea."));
        allItems.add(new Item("Invisible Shield", 45000, Categories.SHIELD, Rarity.RARE, "This shield won't protect you from ridicule. "));
        allItems.add(new Item("Lumberjack Shield", 30000, Categories.SHIELD, Rarity.COMMON, "This shield used to protect lumberjacks from falling trees. Its function quickly changed, and now it is more often used to protect warriors from enemies who tend to land on them without warning!"));
        allItems.add(new Item("Water Carapace", 20000, Categories.SHIELD, Rarity.COMMON, "This Trophy protects you from Crocodyl Tears."));
        allItems.add(new Item("Air Carapace", 20000, Categories.SHIELD, Rarity.COMMON, "Taking refuge under this Trophy will protect you from wind, but will not prevent you from generating your own. This can lead to an uncomfortable case of trapped wind."));
        allItems.add(new Item("Neutral Carapace", 20000, Categories.SHIELD, Rarity.COMMON, "This Trophy protects you from all the ditherers who can't choose sides during a fight."));


        return initItems;
    }

    public ArrayList<Item> getAllItems(){
        return allItems;
    }
}
