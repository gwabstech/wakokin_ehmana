package com.gwabs.wakokinal_aminehmana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements JcPlayerManagerListener, NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<JcAudio> Songlist;
    private ArrayList<String> songName;
    private RecyclerView recyclerView;
    private String url;
    private android.content.Context Context;
    private JcPlayerView player;
    audioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" WAKOKIN AL-AMIN");
        player = findViewById(R.id.jcplayer);
        addsongsname();

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(MainActivity.this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        actionBarDrawerToggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(Context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);

        audioAdapter = new audioAdapter(Context,Songlist,songName, new titleClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {


                player.playAudio(player.getMyPlaylist().get(position));


            }
        });





        recyclerView.setAdapter(audioAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }


    private void addsongsname() {
        Songlist = new ArrayList<>();
        Songlist.add(JcAudio.createFromAssets("HALIMATU SADIYYA", "HALIMATU SADIYYA.mp3"));
        Songlist.add(JcAudio.createFromAssets("MATA ADON GARI", "MATA ADON GARI-1.mp3"));
        Songlist.add(JcAudio.createFromAssets("NA FADA", "NA FADA.mp3"));
        Songlist.add(JcAudio.createFromAssets("Ni Daban Ne", "Ni Daban Ne.mp3"));
        Songlist.add(JcAudio.createFromAssets("No More Shaye Shaye", "No More Shaye Shaye_eq.mp3"));
        Songlist.add(JcAudio.createFromAssets("RABIN RAINA", "RABIN RAINA.mp3"));
        Songlist.add(JcAudio.createFromAssets("SALATIN ANNABI", "SALATIN ANNABI.mp3"));
        Songlist.add(JcAudio.createFromAssets("Tamburan Masoya", "Tamburan Masoya.mp3"));
        Songlist.add(JcAudio.createFromAssets("UWA BA DA MAMA ", "UWA BA DA MAMA .mp3"));
        Songlist.add(JcAudio.createFromAssets("WAI YA NE", "WAI YA NE.mp3"));

        player.initPlaylist(Songlist, null);


        songName = new ArrayList<>();
        songName.add(0,"HALIMATU SADIYYA");
        songName.add(1,"MATA ADON GARI");
        songName.add(2,"NA FADA");
        songName.add(3,"NI DABAN NE");
        songName.add(4,"No More Shaye Shaye");
        songName.add(5,"RABIN RAINA");
        songName.add(6,"SALATIN ANNABI");
        songName.add(7,"TAMBURAN MASOYA");
        songName.add(8,"UWA BA DA MAMA");
        songName.add(9,"WAI YA NE");


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.youtube:
                url = "https://www.youtube.com/channel/UCveU1ChIQAxVrpTazC_A0Fw";
                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.Contactus:
                break;
            case R.id.aboutalamin:
                break;
        }
        return true;
    }



    @Override
    public void onPause() {
        super.onPause();
        player.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onContinueAudio(JcStatus jcStatus) {

    }

    @Override
    public void onJcpError(Throwable throwable) {

    }

    @Override
    public void onPaused(JcStatus jcStatus) {

    }

    @Override
    public void onPlaying(JcStatus jcStatus) {

    }

    @Override
    public void onPreparedAudio(JcStatus jcStatus) {

    }

    @Override
    public void onStopped(JcStatus jcStatus) {

    }

    @Override
    public void onTimeChanged(JcStatus jcStatus) {

    }
}