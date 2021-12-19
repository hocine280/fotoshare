package com.hocine.fotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hocine.fotoshare.Fragment.HomeFragment;
import com.hocine.fotoshare.Fragment.NotificationFragment;
import com.hocine.fotoshare.Fragment.ProfileFragment;
import com.hocine.fotoshare.Fragment.SearchFragment;

import java.util.Calendar;

/**
 * Classe appellée à l'ouverture de l'application
 *
 * @author Hocine
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Variables
     */
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    /**
     * Méthode permettant la création de l'activité + Mise en place du broadcastReceiver qui envoie une notifie une fois par jour
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        /**
         * Permet de définir l'heure à laquelle la notification démarre + lancement de la notif
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Méthode permettant de gérer le menu an bas de l'activité et en fonction de l'appuie de l'utilisateur un fragment correspond à la demande effectue par l'utilisateur
     * s'ouvre
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Affiche la page d'accueil quand on est connecté, c'est-à-dire le fragment où l'on retrouve tous les posts des personnes que l'on suit
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                // Affiche le fragment permettant de rechercher une personne
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                // Affiche l'activité qui permet d'ajouter un post
                case R.id.nav_add:
                    selectedFragment = null;
                    startActivity(new Intent(MainActivity.this, PostActivity.class));
                    break;
                // Affiche le fragment qui permet de visualiser les notifications
                case R.id.nav_favorite:
                    selectedFragment = new NotificationFragment();
                    break;
                // Affiche le fragment qui permet de visualiser son profil
                case R.id.nav_profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };


}