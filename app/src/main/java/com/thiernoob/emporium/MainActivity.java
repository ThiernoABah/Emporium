package com.thiernoob.emporium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thiernoob.emporium.adapter.Offer;
import com.thiernoob.emporium.fragment.ClientFragment;
import com.thiernoob.emporium.fragment.CollectionFragment;
import com.thiernoob.emporium.fragment.MapFragment;
import com.thiernoob.emporium.fragment.ProfileFragment;
import com.thiernoob.emporium.fragment.ShopFragment;
import com.thiernoob.emporium.gameobjects.Kingdom;
import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.gameobjects.enums.Location;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int NB_RANDOM_ITEMS = 5;
    private final int RANDOM_KARMA = 15;
    private final int RANDOM_MAX_PRICE = 10000;
    private int OFFER_CPT;

    private boolean active;
    private Random rand;

    private Player player;

    private Fragment clientFrag;
    private Fragment collectionFrag;
    private Fragment mapFrag;
    private Fragment shopFrag;
    private Fragment profileFrag;


    private List<Offer> listOffer;
    private List<Item> collection;
    private List<Item> shop;
    private List<Kingdom> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        this.active = true;
        this.rand = new Random();

        this.listOffer = new ArrayList<Offer>();
        this.collection = new ArrayList<Item>();
        this.shop = new ArrayList<Item>();
        this.map = new ArrayList<Kingdom>();

        //////////////////////////////////////////
        this.clientFrag = new ClientFragment();
        this.collectionFrag = new CollectionFragment();
        this.mapFrag = new MapFragment();
        this.shopFrag = new ShopFragment();
        this.profileFrag = new ProfileFragment();
        /////////////////////////////////////////

        this.player = Player.getPlayer();
        this.player.setPseudo(getIntent().getStringExtra("PSEUDO"));

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // first time we display profile fragment



        //////////////////////////// Juste pour les inflates, trouver mieux
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,collectionFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,shopFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,clientFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mapFrag).commit();
        /////////////////////////////////////
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFrag).commit();


        this.randomOffers(this.NB_RANDOM_ITEMS);
        this.OFFER_CPT = listOffer.size();
        this.initMap();

        /// Les taches qui font qu'on achete les items du shop ou qu'on recoit de nouvelle offre
        // Maybe faire des notifications later
        //ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        ScheduledThreadPoolExecutor sch2 = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

        Runnable offerMaker = new Runnable(){
            @Override
            public void run() {
                Categories cat = Categories.values()[rand.nextInt(Categories.values().length)];
                Rarity rar = Rarity.values()[rand.nextInt(Rarity.values().length)];
                listOffer.add(new Offer(OFFER_CPT,15-rand.nextInt(30),new Item("test"+OFFER_CPT,rand.nextInt(RANDOM_MAX_PRICE), cat, rar)));
                OFFER_CPT++;
            }
        };

        Runnable buyItems = new Runnable(){
            @Override
            public void run() {
                if(shop.size()>0){
                    for(Item i : shop){
                        i.setTimeInShop(i.getTimeInShop()-1);
                    }
                    for(int i = 0 ; i<shop.size();  i++){
                        if(shop.get(i).getTimeInShop()<=0){
                            shop.get(i).setTimeInShop(0);
                            buyedFromShop(i);
                            i--;
                        }
                    }
                }
            }
        };

        // PAS DYNAMIQUE
        sch2.scheduleAtFixedRate(offerMaker,2,20, TimeUnit.SECONDS);
        sch2.scheduleAtFixedRate(buyItems,0,2,TimeUnit.SECONDS);

            this.save();

    }

    @Override
    public void onStop() {
        super.onStop();
        this.active = false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_client:
                            selectedFrag = clientFrag;
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag).commit();

                    return true;

                }
            };

    // Maybe les links a player later
    public List<Offer> getOffers(){
        return this.listOffer;
    }
    public List<Item> getCollection(){
        return this.collection;
    }
    public List<Item> getShop(){
        return this.shop;
    }
    public List<Kingdom> getMap() {
        return map;
    }
    public boolean isActive() {
        return this.active;
    }

    public void addToCollection(Item i){
        ((CollectionFragment)this.collectionFrag).addItem(i);
    }
    public void addToShop(Item i){
        ((ShopFragment)this.shopFrag).addItem(i);
    }

    public void buyedFromShop(int position){
        // Buy selon la location du player sa donne plus d'argent
        player.addGold(shop.get(position).getPrice());
        shop.remove(position);
    }

    public void addToOffer(Offer o){
        ((ClientFragment)this.clientFrag).addOffer(o);
    }

    public void randomOffers(int nb){
        Random rand = new Random();
        Categories cat;
        Rarity rar;
        for(int i = 0 ; i < nb;i++){
            cat = Categories.values()[rand.nextInt(Categories.values().length)];
            rar = Rarity.values()[rand.nextInt(Rarity.values().length)];
            this.listOffer.add(new Offer(i,RANDOM_KARMA-rand.nextInt(2*RANDOM_KARMA),new Item("test"+i,rand.nextInt(RANDOM_MAX_PRICE), cat, rar)));
        }
    }

    public void initMap(){
        for(Location l :
            Location.values()){
            if(l == Location.TRAVELING){
                continue;
            }
            this.map.add(new Kingdom((l)));
        }
    }

    public void save() {
        JSONObject save = new JSONObject();
        try {
            // Here we convert Java Object to JSON


            JSONObject playerSave = new JSONObject();
            playerSave.put("pseudo", player.getPseudo() );
            playerSave.put("gold", player.getGold());
            playerSave.put("karma", player.getKarma());
            playerSave.put("location", player.getLocation().toString());
            playerSave.put("alignment", player.getAlignment().toString());

            save.put("player", playerSave);


            JSONArray collection = new JSONArray();

            for (Item i : this.collection ) {
                JSONObject pnObj = new JSONObject();
                pnObj.put(String.valueOf(i.getId()), saveItem(i));
                collection.put(pnObj);
            }

            save.put("collection", collection);

            JSONArray shop = new JSONArray();

            for (Item i : this.shop ) {
                JSONObject pnObj = new JSONObject();
                pnObj.put(String.valueOf(i.getId()), saveItem(i));
                shop.put(pnObj);
            }

            save.put("shop", shop);

            JSONArray offers = new JSONArray();

            for (Offer i : this.listOffer ) {
                JSONObject pnObj = new JSONObject();
                pnObj.put(String.valueOf(i.getId()), saveOffer(i));
                offers.put(pnObj);
            }

            save.put("collection", offers);

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        String path = Environment.getExternalStorageDirectory().toString()+ "/Emporium/save_file";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        path += "/save.json";
        try{
            Writer output = null;
            File data = new File(path);
            data.createNewFile();
            Toast.makeText(getApplicationContext(), String.valueOf(data.canWrite()), Toast.LENGTH_LONG).show();
            output = new BufferedWriter(new FileWriter(data)); // ERREUR ICI !!
            output.write(save.toString());
            output.close();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "error catch", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }




    }

    private JSONObject saveItem(Item i){
        try {
            // Here we convert Java Object to JSON
            JSONObject saveItem = new JSONObject();
            saveItem.put("id", i.getId() );
            saveItem.put("name", i.getName());
            saveItem.put("original_price", i.getOriginalPrice());
            saveItem.put("price", i.getPrice());
            saveItem.put("type", i.getType().toString());
            saveItem.put("rarity", i.getRarity().toString());
            saveItem.put("description", i.getDescription());
            saveItem.put("timeInShop", i.getTimeInShop());

            return saveItem;

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private JSONObject saveOffer(Offer o){
        try {
            // Here we convert Java Object to JSON

            JSONObject saveOffer = new JSONObject();
            saveOffer.put("id", o.getId() );
            saveOffer.put("karma", o.getKarma());
            saveOffer.put("item", saveItem(o.getItem()));

            return saveOffer;
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
