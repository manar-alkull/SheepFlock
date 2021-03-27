package com.example.sheepflock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.sheepflock.alarm.AlarmHandler;
import com.example.sheepflock.system.MyLocationManager;
import com.example.sheepflock.system.SheepContentManager;
import com.example.sheepflock.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static MyLocationManager locationManager;
    public static MainActivity mainActivityContext=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            }
        });

        mainActivityContext=this;

        demoSheeps();
        locationManager=new MyLocationManager(this);
    }

    public SheepContentManager sheepContentManager=new SheepContentManager(this);
    private void demoSheeps() {
        if(sheepContentManager.getSheeps().size()==0){
            sheepContentManager.demo(this);
            HomeFragment.refresh();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClickSettings(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        //intent.putExtra("EXTRA_SESSION_ID", sessionId);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }






    private BroadcastReceiver reciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            //HomeFragment homeFragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.example_fragment);
            HomeFragment.refresh();


        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter  filter1 = new IntentFilter("com.example.sheepflock.alarm");
        registerReceiver(reciever,filter1);
    }
    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(reciever);
    }
}
