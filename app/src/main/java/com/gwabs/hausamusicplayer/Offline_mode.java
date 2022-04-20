package com.gwabs.hausamusicplayer;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tonyodev.fetch2.Fetch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Offline_mode extends AppCompatActivity  implements JcPlayerManagerListener, NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<JcAudio> Songlist;
    private ArrayList<SongModel> songsArrayList;
    private RecyclerView recyclerView;
    private String url;
    private android.content.Context Context;
    private JcPlayerView player;
    audioAdapter audioAdapter;
    private ProgressBar progressBar;


    private static final int UPDATE_DOWNLOAD_PROGRESS = 1;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Download");
        player = findViewById(R.id.jcplayer);
        songsArrayList= new ArrayList<>();

        Songlist = new ArrayList<>();







        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(Offline_mode.this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        actionBarDrawerToggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(Context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);

        try{

            File files = getExternalFilesDir(getString(R.string.app_name));
            File[] file = files.listFiles();

            for (File file1 : file){
                Songlist.add(JcAudio.createFromFilePath(file1.getName(),file1.getPath()));
                String name = file1.getName();
                SongModel songModel = new SongModel(name,"");
                songsArrayList.add(songModel);
            }
            player.initPlaylist(Songlist, null);


            audioAdapter = new audioAdapter(this, Songlist, songsArrayList, true, new titleClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    if (player.isPlaying()){
                        player.kill();
                        player.createNotification(R.drawable.headerimg);
                        player.setVisibility(View.VISIBLE);
                        player.playAudio(player.getMyPlaylist().get(position));

                    }else {
                        player.createNotification(R.drawable.headerimg);
                        player.setVisibility(View.VISIBLE);
                        player.playAudio(player.getMyPlaylist().get(position));
                    }
                   //

                }
            });

            recyclerView.setAdapter(audioAdapter);
            audioAdapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "No Song found ", Toast.LENGTH_SHORT).show();
        }

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(true);

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

                Intent intent2 = new Intent(getApplicationContext(),AboutDev.class);
                startActivity(intent2);
                break;
            case R.id.aboutalamin:
                Intent intent1 = new Intent(getApplicationContext(),AboutAlamin.class);
                startActivity(intent1);

                break;

            case R.id.Close:
                this.finish();
                finishAffinity();
                player.kill();
        }
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        player.createNotification(R.drawable.headerimg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onContinueAudio(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onJcpError(@NonNull Throwable throwable) {

    }

    @Override
    public void onPaused(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onPlaying(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onPreparedAudio(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onStopped(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onTimeChanged(@NonNull JcStatus jcStatus) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setVisibility(View.VISIBLE);
    }
}