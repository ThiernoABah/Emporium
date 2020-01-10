package com.thiernoob.emporium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

    private int OFFER_CPT;

    private Random rand;

    // The object that represent the player (Singleton)
    private Player player;

    // All possible Items
    private ArrayList<Item> items;

    // Fragments
    private Fragment offersFrag;
    private Fragment collectionFrag;
    private Fragment mapFrag;
    private Fragment shopFrag;
    private Fragment profileFrag;

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
            Log.d("on destroy", "destroy sch");
            this.sch.shutdownNow();
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
        } else {
            this.charge(f);
        }

        this.OFFER_CPT = listOffer.size(); // To set the id of new offers
        this.initMap(); // Initialising the map (always the same map, map is just use to travel)

        this.majRemainingGold();

        // Initalise some process to make the game "alive"
        this.lauchBackgroundProcess();

    }

    // Nav Bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_client:
                            selectedFrag = offersFrag;
                            break;
                        case R.id.nav_map:
                            selectedFrag = mapFrag;
                            break;
                        case R.id.nav_shop:
                            selectedFrag = shopFrag;
                            break;
                        case R.id.nav_collection:
                            selectedFrag = collectionFrag;
                            break;
                        case R.id.nav_profile:
                            selectedFrag = profileFrag;
                            break;

                    }
                    if (selectedFrag == null) {
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
                    return true;
                }
            };

    public void reset() {
        // delete save file and reset game
        String filename = SAVE_FILENAME;
        File file = new File(this.getFilesDir(), filename);

        player.destroy();
        this.sch.shutdownNow();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

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

    public void firstConnection() {
        // Init variables, inflate fragment and set nav bar
        this.initialise();

        // Generating random Offers for now
        this.firstOffers(FIRST_OFFERS);
    }

    public void initialise() {
        /////////// INITIALISATION //////////
        this.rand = new Random();

        this.player = Player.getPlayer();
        this.player.setPseudo(getIntent().getStringExtra("PSEUDO"));

        this.items = InitialiseItems.InitialiseItems().getAllItems();

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

        // Just to inflate the fragment for the first time
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, shopFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, offersFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFrag).commit(); // This last one is to put the profile frag as the first fragment that the player will see


    }

    public void initMap() {
        for (Location l : Location.values()) {
            if (l == Location.TRAVELING) {
                continue;
            }
            this.map.add(new Kingdom((l)));
        }
    }

    private void lauchBackgroundProcess() {

        sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);

        // This runnable is charged to make new offers as time passes and remove old offers if the player didnt took his chance
        Runnable offerMaker = new Runnable() {
            @Override
            public void run() {
                // Randoms item for now
                OFFER_CPT++;

                if (!offersFrag.isVisible()) {
                    if (listOffer.size() <= MAX_OFFERS) {
                        Item item = items.get(rand.nextInt(items.size()));
                        listOffer.add(new Offer(OFFER_CPT, computeKarma(item), item));
                    } else {
                        listOffer.remove(0);
                    }

                }

            }
        };

        // This runnable is charged to buy the item that the player put in his shop as time passes
        Runnable buyItems = new Runnable() {
            @Override
            public void run() {
                if (shop.size() > 0) {
                    for (Item i : shop) {
                        i.setTimeInShop(i.getTimeInShop() - 1);
                    }
                    for (int i = 0; i < shop.size(); i++) {
                        if (shop.get(i).getTimeInShop() <= 0) {
                            shop.get(i).setTimeInShop(0);
                            buying(i);
                            i--;
                        }
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


        sch.scheduleAtFixedRate(offerMaker, 2, 10, TimeUnit.SECONDS);
        sch.scheduleAtFixedRate(buyItems, 2, 2, TimeUnit.SECONDS);
        sch.scheduleAtFixedRate(autoSave, 2, SAVE_RATE, TimeUnit.SECONDS);

        /*Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                majRemainingGold();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();*/
    }

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

    public void addToCollection(Item i) {
        this.collection.add(i);
    }

    public void addToShop(Item i) {
        this.shop.add(i);
    }

    public void buying(int position) {

        if (!shopFrag.isVisible()) {
            if (Kingdom.bonusOn(this.player.getLocation(), shop.get(position).getType())) {
                int money = shop.get(position).getPrice() + Math.round((shop.get(position).getPrice() * PERCENTAGE_BONUS) / 100);
                this.player.addGold(money);

            } else {
                this.player.addGold(shop.get(position).getPrice());
            }

            this.shop.remove(position);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    majRemainingGold();
                }
            });
        }

    }

    public void majRemainingGold() {
        this.mainRemainingGold.setText(this.player.getGold() + " $");
    }

    public void firstOffers(int nb) {
        Random rand = new Random();
        for (int i = 0; i < nb; i++) {
            Item item = this.items.get(rand.nextInt(this.items.size()));
            this.listOffer.add(new Offer(i, this.computeKarma(item), item));
        }
    }

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


}
