package com.thiernoob.emporium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

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
                addToOffer(new Offer(listOffer.size(),15-rand.nextInt(30),new Item("test"+listOffer.size(),rand.nextInt(RANDOM_MAX_PRICE), cat, rar)));
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

        // BUGS BIZARRE
        //sch.scheduleAtFixedRate(offerMaker,2,10, TimeUnit.SECONDS); // BUG WHITE SCREEN
        //sch2.scheduleAtFixedRate(buyItems,0,10,TimeUnit.SECONDS);

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
        ((ShopFragment)this.shopFrag).buy(position);
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






}
