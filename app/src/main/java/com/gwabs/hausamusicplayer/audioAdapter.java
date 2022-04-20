package com.gwabs.hausamusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jean.jcplayer.model.JcAudio;

import java.util.ArrayList;
import java.util.Objects;


public class audioAdapter  extends RecyclerView.Adapter<audioAdapter.audioAdapterViewHolder> {

    Context mContext;
    private ArrayList<JcAudio> SongList;
    private ArrayList<SongModel> songsname;
    titleClickListener songeClickListener;
    private boolean fromOfflineMode;


    public audioAdapter(Context mContext, ArrayList<JcAudio> songlist, ArrayList<SongModel> songsname ,boolean fromOfflineMode,titleClickListener songeClickListener) {
        this.mContext = mContext;
        this.SongList = songlist;
        this.songsname = songsname;
        this.fromOfflineMode = fromOfflineMode;
        this.songeClickListener = songeClickListener;
    }



    @NonNull
    @Override
    public audioAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder,parent,false);
        return new audioAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull audioAdapterViewHolder holder, int position) {

        holder.songName.setText(songsname.get(position).getSongNname());
        if (fromOfflineMode){
            holder.btn_download.setVisibility(View.GONE);
        }

        Objects.requireNonNull(holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songeClickListener.onItemClick(v,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return SongList.size();
    }


    public static class audioAdapterViewHolder extends RecyclerView.ViewHolder {
       private ImageView songImage;
       private TextView songName;
       private ProgressBar progressBar;
       private ImageButton btn_download;

        public audioAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.imgmusicImage);
            songName = itemView.findViewById(R.id.songName);
            progressBar = itemView.findViewById(R.id.progressBar);
            btn_download = itemView.findViewById(R.id.btnDownload);
        }
    }
}
