package com.gwabs.hausamusicplayer;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements JcPlayerManagerListener, NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<JcAudio> Songlist;
    private ArrayList<SongModel> songsArrayList;
    private RecyclerView recyclerView;
    private String url;
    private DatabaseReference myreff;
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
        myreff = FirebaseDatabase.getInstance().getReference();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Wakokin Ehmana");
        player = findViewById(R.id.jcplayer);
        player.setVisibility(View.VISIBLE);
        songsArrayList = new ArrayList<>();

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(MainActivity.this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        actionBarDrawerToggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        getDataFromFirebase("EaskPneUFjgmd7NG8W3RNCjP6aR2", myreff);

       player.setJcPlayerManagerListener(this);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(true);

    }

/*
    private void addsongsname() {
        Songlist = new ArrayList<>();

        for (int i = 0 ; i < songsArrayList.size(); i++){

            Songlist.add(JcAudio.createFromURL(songsArrayList.get(i).getSongNname(),songsArrayList.get(i).getSongUrl()));
            Songlist.add(JcAudio.)
        }


        player.initPlaylist(Songlist, null);


    }

     */


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

                Intent intent2 = new Intent(getApplicationContext(), AboutDev.class);
                startActivity(intent2);
                break;
            case R.id.aboutalamin:
                Intent intent1 = new Intent(getApplicationContext(), AboutAlamin.class);
                startActivity(intent1);

                break;

            case R.id.Close:
                this.finish();
                finishAffinity();
                player.kill();
        }
        return true;
    }


    public void getDataFromFirebase(String sunandata, DatabaseReference myreff) {

        Query query = myreff.child(sunandata);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String songName = snapshot.child("fileName").getValue().toString();
                    String songUrl = snapshot.child("fileURL").getValue().toString();
                    SongModel songModel = new SongModel(songName, songUrl);
                    songsArrayList.add(songModel);
                    Log.i("TAG", snapshot.toString());
                }

                Songlist = new ArrayList<>();

                for (int i = 0; i < songsArrayList.size(); i++) {

                    Songlist.add(JcAudio.createFromURL(songsArrayList.get(i).getSongNname(), songsArrayList.get(i).getSongUrl()));
                }

                audioAdapter = new audioAdapter(MainActivity.this, Songlist, songsArrayList, false, new titleClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        progressBar = itemView.findViewById(R.id.progressBar);
                        ImageButton btnDownload = itemView.findViewById(R.id.btnDownload);
                        btnDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "am clicked", Toast.LENGTH_SHORT).show();
                                downloadFile(songsArrayList.get(position).getSongUrl(), songsArrayList.get(position).getSongNname());
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
                    Log.i("error", "some issoes please refresh");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Tag","Paused");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // player.kill();
    }

    @Override
    public void onCompletedAudio() {
        player.continueAudio();
    }


    @Override
    public void onJcpError(Throwable throwable) {
        Log.i("Tag",throwable.toString());
    }

    @Override
    public void onPaused(JcStatus jcStatus) {
        player.createNotification(R.drawable.headerimg);
    }

    @Override
    public void onPlaying(JcStatus jcStatus) {
        player.createNotification(R.drawable.headerimg);
       // player.createNotification(R.drawable.headerimg);
    }

    @Override
    public void onPreparedAudio(JcStatus jcStatus) {
        Log.i("Tag",jcStatus.toString());
    }

    @Override
    public void onStopped(JcStatus jcStatus) {

    }

    @Override
    public void onTimeChanged(JcStatus jcStatus) {
    }

    @Override
    public void onContinueAudio(JcStatus jcStatus) {
        Log.i("Tag",jcStatus.toString());
    }

    private final Handler mainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == UPDATE_DOWNLOAD_PROGRESS) {
                int downloadProgress = msg.arg1;

                // Update your progress bar here.
                progressBar.setProgress(downloadProgress, true);
            }
            return true;
        }
    });

    public void downloadFile(String Url, String fileName) {

        /*
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);


        String url = "http:www.example.com/test.txt";
        String file = "/downloads/test.txt";

        final Request request = new Request(url, file);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");

        fetch.enqueue(request, updatedRequest -> {
            //Request was successfully enqueued for download.
        }, error -> {
            //An error occurred enqueuing the request.
        });

         */


        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Url);

// concatinate above fileExtension to fileName


        fileName += ".mp3";

        ArrayList<String> songsNames = new ArrayList<>();

        File files = getExternalFilesDir(getString(R.string.app_name));
        File[] file = files.listFiles();
        for (File file1 : file) {
            Log.i("File", file1.getName().toString());
            songsNames.add(file1.getName().toString());

        }


        if (songsNames.contains(fileName)) {
            Toast.makeText(this, "File Already Exist", Toast.LENGTH_SHORT).show();
        } else {

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url))
                    .setTitle(fileName)
                    .setDescription("Downloading " + fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    //.setDestinationUri(Uri.fromFile(new File(destinationDirectory,fileName + fileExtension)));
                    .setDestinationInExternalFilesDir(this, getString(R.string.app_name), fileName);
            //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);


            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            long downloadId = dm.enqueue(request);


            executor.execute(new Runnable() {
                @Override
                public void run() {

                    int progress = 0;
                    boolean isDownloadFinished = false;
                    while (!isDownloadFinished) {
                        Cursor cursor = dm.query(new DownloadManager.Query().setFilterById(downloadId));
                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            switch (downloadStatus) {
                                case DownloadManager.STATUS_RUNNING:
                                    @SuppressLint("Range") long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (totalBytes > 0) {
                                        @SuppressLint("Range") long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        progress = (int) (downloadedBytes * 100 / totalBytes);
                                    }

                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    progress = 100;
                                    isDownloadFinished = true;
                                    executor.shutdown();
                                    mainHandler.removeCallbacksAndMessages(null);
                                    break;
                                case DownloadManager.STATUS_PAUSED:
                                case DownloadManager.STATUS_PENDING:
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    isDownloadFinished = true;
                                    break;
                            }
                            Message message = Message.obtain();
                            message.what = UPDATE_DOWNLOAD_PROGRESS;
                            message.arg1 = progress;
                            mainHandler.sendMessage(message);
                        }
                    }
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        new android.view.MenuInflater(this).inflate(R.menu.op_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.offline) {
            try {
                Intent intent = new Intent(MainActivity.this, Offline_mode.class);
                startActivity(intent);
               // finish();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("Tag","Resumed");
    }


}