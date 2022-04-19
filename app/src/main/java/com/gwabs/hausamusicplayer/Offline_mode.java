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

    // Use a background thread to check the progress of downloading
    private final ExecutorService executor = Executors.newFixedThreadPool(1);


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" WAKOKIN AL-AMIN");
        player = findViewById(R.id.jcplayer);
        songsArrayList= new ArrayList<>();

        Songlist = new ArrayList<>();

        File files = getExternalFilesDir(getString(R.string.app_name));
        File[] file = files.listFiles();

        for (File file1 : file){
            Songlist.add(JcAudio.createFromFilePath(file1.getName(),file1.getPath()));
           String name = file1.getName();
           SongModel songModel = new SongModel(name,"");
           songsArrayList.add(songModel);
        }
        player.initPlaylist(Songlist, null);






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
        audioAdapter = new audioAdapter(this, Songlist, songsArrayList, true, new titleClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                player.createNotification(R.drawable.headerimg);
                player.playAudio(player.getMyPlaylist().get(position));

            }
        });

        recyclerView.setAdapter(audioAdapter);
        audioAdapter.notifyDataSetChanged();

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(true);

    }


    private void addsongsname() {
        Songlist = new ArrayList<>();

        for (int i = 0 ; i < songsArrayList.size(); i++){

            Songlist.add(JcAudio.createFromURL(songsArrayList.get(i).getSongNname(),songsArrayList.get(i).getSongUrl()));
            // Songlist.add(JcAudio.)
        }

        /*
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


         */
        player.initPlaylist(Songlist, null);


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
                finishAffinity();
        }
        return true;
    }



    /*
    public void getDataFromFirebase(String sunandata,DatabaseReference myreff){

        Query query  = myreff.child(sunandata);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    String songName = snapshot.child("fileName").getValue().toString();
                    String songUrl = snapshot.child("fileURL").getValue().toString();
                    SongModel songModel = new SongModel(songName,songUrl);
                    songsArrayList.add(songModel);
                    Log.i("TAG",snapshot.toString());
                }

                Songlist = new ArrayList<>();

                for (int i = 0 ; i < songsArrayList.size(); i++){

                    Songlist.add(JcAudio.createFromURL(songsArrayList.get(i).getSongNname(),songsArrayList.get(i).getSongUrl()));
                }

                audioAdapter = new audioAdapter(Context,Songlist,songsArrayList,true, new titleClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        progressBar = itemView.findViewById(R.id.progressBar);
                        ImageButton btnDownload = itemView.findViewById(R.id.btnDownload);
                        btnDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Offline_mode.this, "am clicked", Toast.LENGTH_SHORT).show();
                                downloadFile(songsArrayList.get(position).getSongUrl(),songsArrayList.get(position).getSongNname());
                            }
                        });
                        player.createNotification(R.drawable.headerimg);
                        player.playAudio(player.getMyPlaylist().get(position));

                    }
                });
                recyclerView.setAdapter(audioAdapter);
                audioAdapter.notifyDataSetChanged();


                player.initPlaylist(Songlist, null);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                try {
                    Log.i("error","some issoes please refresh");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

     */
    @Override
    public void onPause() {
        super.onPause();
        player.createNotification(R.drawable.headerimg);
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
    public void onJcpError(Throwable throwable) {

    }

    @Override
    public void onPaused(JcStatus jcStatus) {

    }

    @Override
    public void onPlaying(JcStatus jcStatus) {
        player.createNotification(R.drawable.headerimg);
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

    @Override
    public void onContinueAudio(JcStatus jcStatus) {

    }



}