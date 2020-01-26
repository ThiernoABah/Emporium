package com.thiernoob.emporium;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thiernoob.emporium.adapter.Offer;
import com.thiernoob.emporium.fragment.CollectionFragment;
import com.thiernoob.emporium.fragment.MapFragment;
import com.thiernoob.emporium.fragment.OffersFragment;
import com.thiernoob.emporium.fragment.ProfileFragment;
import com.thiernoob.emporium.fragment.ShopFragment;
import com.thiernoob.emporium.gameobjects.InitialiseItems;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Kingdom;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.gameobjects.enums.Align;
import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Location;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String SAVE_FILENAME = "save.json";
    private int PERCENTAGE_BONUS = 5;
    private int MAX_OFFERS = 15;
    private int FIRST_OFFERS = 5;
    private int SAVE_RATE = 25; // every 25 seconds there a save
    private int OFFER_RATE = 10; // new Offer every 10 seconds
    private int SHOP_TIC = 2; // tick to count the time a item have to stay in the shop (using his time in shop attribute)

    // Counter for the number of offers (using it to give an id to a offer)
    private int OFFER_CPT;

    // Variable used to get random value
    private Random rand;

    // The object that represent the player (Singleton)
    private Player player;

    // All possible Items
    private InitialiseItems items;

    // Fragments
    private Fragment offersFrag;
    private Fragment collectionFrag;
    private Fragment mapFrag;
    private Fragment shopFrag;
    private Fragment profileFrag;
    private Fragment selectedFrag;


    // Tutorials Boolean (true if you have to display tutorial)
    private boolean tutoCollection;
    private boolean tutoShop;
    private boolean tutoOffer;
    private boolean tutoMap;

    // Collections used to represent offers, collection, shop and map
    private List<Offer> listOffer;
    private List<Item> collection;
    private List<Item> shop;
    private List<Kingdom> map;

    // Remaining gold view
    TextView mainRemainingGold;

    // ThreadPoolExecutor charged to run the following thread to simulate a good behaviour
    ScheduledThreadPoolExecutor sch;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.destroy();
        if (this.sch != null) {
            this.sch.shutdownNow(); // to avoid thread running if the player is back to the logging screen and what to start over
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        File f = new File(this.getFilesDir().getAbsolutePath() + "/" + SAVE_FILENAME);
        if (!f.exists()) {
            this.firstConnection();
            this.tutoCollection = true;
            this.tutoShop = true;
            this.tutoMap = true;
            this.tutoOffer = true;

        } else {
            this.charge(f);
        }

        this.OFFER_CPT = listOffer.size(); // To set the id of new offers
        this.initMap(); // Initialising the map (always the same map, map is just use to travel)

        // To display the remaining gold of the player on screen
        this.majRemainingGold();

        // Initalise some process to make the game "alive"
        this.lauchBackgroundProcess();

    }

    // Navigation Bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment nextFrag = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_offer:
                            nextFrag = offersFrag;
                            break;
                        case R.id.nav_map:
                            nextFrag = mapFrag;
                            break;
                        case R.id.nav_shop:
                            nextFrag = shopFrag;
                            break;
                        case R.id.nav_collection:
                            nextFrag = collectionFrag;
                            break;
                        case R.id.nav_profile:
                            nextFrag = profileFrag;
                            break;

                    }
                    if (nextFrag == null) {
                        return false;
                    }
                    ft.hide(selectedFrag).show(nextFrag).commit();
                    selectedFrag = nextFrag;

                    if(selectedFrag.getId() == profileFrag.getId()){
                        ((ProfileFragment)profileFrag).refresh();
                    }

                    tutorial(menuItem.getItemId()); // if its the first time on a frag display a tutoriel to explain it
                    return true;
                }
            };

    // tutorial depending on the fragment the player is
    public void tutorial(int id) {
        if (id == R.id.nav_collection && tutoCollection) {
            tutorialDialog("Collection Tutorial", "This is your collection, all the items you buy comes here, this is your treasure and if you want to sell an item you can do it from here just click on it.\n\n" +
                    "Tips : Don't be too greedy when selling an item if you sell it at twice or more of his original price he can take a while to be sold");
            tutoCollection = false;
        } else if (id == R.id.nav_map && tutoMap) {
            tutorialDialog("Map Tutorial", "This is the map, here you can travel from kingdom to kingdom, traveling can take a while." +
                    "\n\nTips : Some kingdom like some kinds of items you will get a bonus if you sell the rights items in the right kingdom.");
            tutoMap = false;
        } else if (id == R.id.nav_shop && tutoShop) {
            tutorialDialog("Shop Tutorial", "This is your shop all the items that you are selling appears here you can take them back to your collection if you want." +
                    "\n\nTips : Your items will be selled over time just wait.");
            tutoShop = false;
        } else if (id == R.id.nav_offer && tutoOffer) {
            tutorialDialog("Offers Tutorial", "This is your offers here you receive offers from travellers, some times if you are lucky a rare items may be proposed to you this is where you buy items." +
                    "\n\nTips : The type of a item is defined by the icon next to his name and each item have a rarity : Common items are green, Rare items are blue, Epic Items are purple and Legendary items orange.");
            tutoOffer = false;
        }
    }

    // the method that fire the turorial dialog
    public void tutorialDialog(String titre, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogMessage);
        TextView title = new TextView(this);
        title.setText(titre);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(25);
        builder.setCustomTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // reset player save and let him start a new game from 0
    public void reset() {
        // delete save file and reset game
        String filename = SAVE_FILENAME;
        File file = new File(this.getFilesDir(), filename);

        file.delete();
        player.destroy();
        this.sch.shutdownNow();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    // save the player progress
    public void save() {
        if (player.getLocation() == Location.TRAVELING) {
            player.setLocation(player.getLastLocation());
        }


        // Save Game state to a Json Object
        JSONObject save = new JSONObject();
        try {
            JSONObject playerSave = new JSONObject();

            playerSave.put("pseudo", player.getPseudo());
            playerSave.put("gold", player.getGold());
            playerSave.put("karma", player.getKarma());
            playerSave.put("location", player.getLocation().toString());
            playerSave.put("alignment", player.getAlignment().toString());

            save.put("player", playerSave);

            JSONArray collection = new JSONArray();

            for (Item i : this.collection) {
                collection.put(saveItem(i));
            }

            save.put("collection", collection);

            JSONArray shop = new JSONArray();

            for (Item i : this.shop) {
                shop.put(saveItem(i));
            }

            save.put("shop", shop);

            JSONArray offers = new JSONArray();

            for (Offer i : this.listOffer) {
                offers.put(saveOffer(i));
            }

            save.put("offers", offers);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        writeJsonToFile(save);

    }

    // on connection if the player have a save file this method load it
    private void charge(File f) {

        try {
            // Init variables, inflate fragment and set nav bar
            this.initialise();

            JSONObject lastSave = new JSONObject(readJsonFromFile(f));

            JSONObject player = lastSave.getJSONObject("player");

            this.player.setPseudo(player.get("pseudo").toString());
            this.player.setLocation(Location.value(player.get("location").toString()));
            this.player.setGold(Integer.valueOf(player.get("gold").toString()));
            this.player.setKarma(Integer.valueOf(player.get("karma").toString()));
            this.player.setAlignment(Align.value(player.get("alignment").toString()));


            JSONArray offers = lastSave.getJSONArray("offers");


            for (int i = 0; i < offers.length(); i++) {
                JSONObject offer = offers.getJSONObject(i);
                Offer o = chargeOffer(offer);
                if (o != null) {
                    listOffer.add(o);
                }

            }


            JSONArray shop = lastSave.getJSONArray("shop");
            for (int i = 0; i < shop.length(); i++) {
                JSONObject item = shop.getJSONObject(i);
                Item it = chargeItem(item);
                if (it != null) {
                    this.shop.add(it);
                }
            }


            JSONArray collection = lastSave.getJSONArray("collection");
            for (int i = 0; i < collection.length(); i++) {
                JSONObject item = collection.getJSONObject(i);
                Item it = chargeItem(item);
                if (it != null) {
                    this.collection.add(it);
                }
            }


        } catch (JSONException js) {
            Toast.makeText(getApplicationContext(), "error while charging", Toast.LENGTH_SHORT).show();
        }

    }

    // Initialise the game if this is a first connection (no save file found)
    public void firstConnection() {
        // Init variables, inflate fragment and set nav bar
        this.initialise();

        // Generating random Offers for now
        this.firstOffers(FIRST_OFFERS);

        // This handler is here to delay the appearence of the first tutorial dialog (making some time for the keybord to disappear and have a nice dispaly)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tutorialDialog("Profile tutorial", "Hello " + player.getPseudo() + " Welcome !\nThis is your profile section here you can save your progression and reset it if you want to start again." +
                        "\nYou can also check some information about your progression i let you discover it.");
            }
        }, 250);

    }

    // Initialise the game parameters
    public void initialise() {
        /////////// INITIALISATION //////////
        this.rand = new Random();

        this.player = Player.getPlayer();
        this.player.setPseudo(getIntent().getStringExtra("PSEUDO"));

        this.items = InitialiseItems.InitialiseItems();

        this.listOffer = new ArrayList<>();
        this.collection = new ArrayList<>();
        this.shop = new ArrayList<>();
        this.map = new ArrayList<>();

        this.offersFrag = new OffersFragment();
        this.collectionFrag = new CollectionFragment();
        this.mapFrag = new MapFragment();
        this.shopFrag = new ShopFragment();
        this.profileFrag = new ProfileFragment();


        this.mainRemainingGold = findViewById(R.id.mainRemainingGold);

        /////////////////////////////////////////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, collectionFrag);
        ft.hide(collectionFrag);
        ft.add(R.id.fragment_container, shopFrag);
        ft.hide(shopFrag);
        ft.add(R.id.fragment_container, offersFrag);
        ft.hide(offersFrag);
        ft.add(R.id.fragment_container, mapFrag);
        ft.hide(mapFrag);
        ft.add(R.id.fragment_container, profileFrag);
        ft.show(profileFrag); // first fragment to be shown to the player
        ft.commit();
        this.selectedFrag = profileFrag;
    }

    // Initialise the map
    public void initMap() {

        for (Location l : Location.values()) {
            if (l == Location.TRAVELING) {
                continue;
            }
            map.add(new Kingdom((l)));
        }


    }

    // Launch 3 process that will make the game more alive
    private void lauchBackgroundProcess() {

        sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);

        // This runnable is charged to make new offers as time passes and remove old offers if the player didnt took his chance
        Runnable offerMaker = new Runnable() {
            @Override
            public void run() {
                // Randoms item for now
                OFFER_CPT++;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listOffer.size() <= MAX_OFFERS) {
                            Item item = items.getItems();
                            ((OffersFragment) offersFrag).getAdapter().add(new Offer(OFFER_CPT, computeKarma(item), item));
                        } else {
                            ((OffersFragment) offersFrag).getAdapter().remove(((OffersFragment) offersFrag).getAdapter().getItem(0));
                        }
                    }
                });

            }
        };

        // This runnable is charged to buy the item that the player put in his shop as time passes
        Runnable buyItems = new Runnable() {
            @Override
            public void run() {

                if (shop.size() > 0) {
                    List<Item> toBuy = new ArrayList<>();
                    for (Item i : shop) {
                        i.setTimeInShop(i.getTimeInShop() - 1);
                        if (i.getTimeInShop() <= 0) {
                            i.setTimeInShop(0);
                            toBuy.add(i);
                        }
                    }
                    if (!toBuy.isEmpty()) {
                        buying(toBuy);
                    }
                }
            }
        };

        // This runnable is charged to save the state of the game
        Runnable autoSave = new Runnable() {
            @Override
            public void run() {
                save();
            }
        };

        sch.scheduleAtFixedRate(offerMaker, 2, OFFER_RATE, TimeUnit.SECONDS);
        sch.scheduleAtFixedRate(buyItems, 2, SHOP_TIC, TimeUnit.SECONDS);
        sch.scheduleAtFixedRate(autoSave, 2, SAVE_RATE, TimeUnit.SECONDS);

    }

    // Used by the buying thread to buy items in the shop
    public void buying(final List<Item> toBuy) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Item item : toBuy) {
                    if (Kingdom.bonusOn(player.getLocation(), item.getType())) {
                        int money = item.getPrice() + Math.round((item.getPrice() * PERCENTAGE_BONUS) / 100);
                        player.addGold(money);

                    } else {
                        player.addGold(item.getPrice());
                    }
                    ((ShopFragment) shopFrag).getAdapter().remove(item);
                }
                majRemainingGold();
            }
        });

    }

    // Add an item to collection
    public void addToCollection(final Item i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((CollectionFragment) collectionFrag).getAdapter().add(i);
                ((CollectionFragment) collectionFrag).refreshWorth();
            }
        });

    }

    // Add an item to shop
    public void addToShop(final Item i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ShopFragment) shopFrag).getAdapter().add(i);
            }
        });
    }

    public void majRemainingGold() {
        this.mainRemainingGold.setText(this.player.getGold() + " $");
    }

    // First offers when you start the game
    public void firstOffers(int nb) {
        for (int i = 0; i < nb; i++) {
            Item item = items.getItems();
            this.listOffer.add(new Offer(i, this.computeKarma(item), item));
        }
    }

    // Tools to see how much karma buying a item gets you
    public int computeKarma(Item i) {

        switch (i.getType()) {
            case CONSUMABLE:
                return 8;
            case SPELL:
                return rand.nextInt(20) - 10;
            case SHIELD:
                return -3;
            case INGREDIENT:
                return 1;
            case WEAPON:
                return -8;
        }
        return 0;
    }

    // Write an JsonObject into the save file
    private void writeJsonToFile(JSONObject save) {
        File file = new File(this.getFilesDir(), SAVE_FILENAME);

        FileWriter writer = null;
        BufferedWriter buffWriter = null;

        if (!(file.exists())) {
            try {
                file.createNewFile();
                writer = new FileWriter(file.getAbsoluteFile());
                buffWriter = new BufferedWriter(writer);
                buffWriter.write(save.toString());
                buffWriter.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "error catch while saving", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            try {
                file.delete();
                file.createNewFile();
                writer = new FileWriter(file.getAbsoluteFile());
                buffWriter = new BufferedWriter(writer);
                buffWriter.write(save.toString());
                buffWriter.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "error catch while saving", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    // Read the save file and return its content (json format)
    private String readJsonFromFile(File f) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "save charge failed", Toast.LENGTH_SHORT).show();
        }
        return text.toString();
    }

    // Turn an Item into a JSON object
    private JSONObject saveItem(Item i) {
        // Transform an item is a JsonObject
        try {

            JSONObject saveItem = new JSONObject();
            saveItem.put("id", i.getId());
            saveItem.put("name", i.getName());
            saveItem.put("original_price", i.getOriginalPrice());
            saveItem.put("price", i.getPrice());
            saveItem.put("type", i.getType().toString());
            saveItem.put("rarity", i.getRarity().toString());
            saveItem.put("description", i.getDescription());
            saveItem.put("timeInShop", i.getTimeInShop());
            return saveItem;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Turn an Offer int a JSON Object
    private JSONObject saveOffer(Offer o) {
        // Transform an offer is a JsonObject
        try {
            JSONObject saveOffer = new JSONObject();
            saveOffer.put("id", o.getId());
            saveOffer.put("karma", o.getKarma());
            saveOffer.put("item", saveItem(o.getItem()));
            return saveOffer;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Turn a JSON Object representing an item into a Item
    private Item chargeItem(JSONObject jsItem) {
        try {
            int id = Integer.valueOf(jsItem.get("id").toString());
            String name = jsItem.get("name").toString();
            int original_price = Integer.valueOf(jsItem.get("original_price").toString());
            int price = Integer.valueOf(jsItem.get("price").toString());
            Categories type = Categories.value(jsItem.get("type").toString());
            Rarity rarity = Rarity.value(jsItem.get("rarity").toString());
            String description = jsItem.get("description").toString();
            int timeInShop = Integer.valueOf(jsItem.get("timeInShop").toString());
            return new Item(id, name, price, type, rarity, description, original_price, timeInShop);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Turn a JSON Object representing an offer into a Offer
    private Offer chargeOffer(JSONObject offer) {
        try {
            int id = Integer.valueOf(offer.get("id").toString());
            int karma = Integer.valueOf(offer.get("karma").toString());
            Item item = chargeItem(offer.getJSONObject("item"));
            return new Offer(id, karma, item);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public List<Offer> getOffers() {
        return this.listOffer;
    }

    public List<Item> getCollection() {
        return this.collection;
    }

    public List<Item> getShop() {
        return this.shop;
    }

    public List<Kingdom> getMap() {
        return map;
    }

}
