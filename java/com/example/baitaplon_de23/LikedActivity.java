package com.example.baitaplon_de23;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {

    private ImageView themeImageView;
    private TextView artistNameTextView;
    private ArrayList<String> musicListx;
    private ArrayList<String> artistListx;
    private ArrayList<Integer> imageResourceIdsx;

    private ImageButton btnBack;

    private static final String SHARED_PREFS_NAME = "LikedSongsPrefs";
    private static final String MUSIC_LIST_KEY = "musicListx";
    private static final String ARTIST_LIST_KEY = "artistListx";
    private static final String IMAGE_LIST_KEY = "imageResourceIdsx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        themeImageView = findViewById(R.id.themeImageView);
        artistNameTextView = findViewById(R.id.artistName);
        int imageResource = getIntent().getIntExtra("imageResource", R.drawable.theme_liked);
        themeImageView.setImageResource(imageResource);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo danh sách
        loadLikedSongs();

        // Kiểm tra nếu có dữ liệu bài hát mới để thêm vào danh sách yêu thích
        Intent intent = getIntent();
        if (intent.hasExtra("songTitle") && intent.hasExtra("artistName") && intent.hasExtra("imageResourceId")) {
            String songTitle = intent.getStringExtra("songTitle");
            String artistName = intent.getStringExtra("artistName");
            int imageResourceId = intent.getIntExtra("imageResourceId", R.drawable.image_play);

            // Thêm bài hát mới vào danh sách và lưu lại
            musicListx.add(songTitle);
            artistListx.add(artistName);
            imageResourceIdsx.add(imageResourceId);
            saveLikedSongs();
        }

        artistNameTextView.setText("Tập của bạn");

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MusicAdapter adapter = new MusicAdapter(this, musicListx, artistListx, imageResourceIdsx);
        recyclerView.setAdapter(adapter);
    }

    private void saveLikedSongs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Chuyển đổi danh sách thành chuỗi JSON để lưu vào SharedPreferences
        editor.putString(MUSIC_LIST_KEY, gson.toJson(musicListx));
        editor.putString(ARTIST_LIST_KEY, gson.toJson(artistListx));
        editor.putString(IMAGE_LIST_KEY, gson.toJson(imageResourceIdsx));

        editor.apply();
    }

    private void loadLikedSongs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Lấy dữ liệu từ SharedPreferences và chuyển đổi lại thành ArrayList
        String musicJson = sharedPreferences.getString(MUSIC_LIST_KEY, null);
        String artistJson = sharedPreferences.getString(ARTIST_LIST_KEY, null);
        String imageJson = sharedPreferences.getString(IMAGE_LIST_KEY, null);

        Type stringType = new TypeToken<ArrayList<String>>() {}.getType();
        Type integerType = new TypeToken<ArrayList<Integer>>() {}.getType();

        musicListx = musicJson == null ? new ArrayList<>() : gson.fromJson(musicJson, stringType);
        artistListx = artistJson == null ? new ArrayList<>() : gson.fromJson(artistJson, stringType);
        imageResourceIdsx = imageJson == null ? new ArrayList<>() : gson.fromJson(imageJson, integerType);
    }

    private class MusicAdapter extends RecyclerView.Adapter<LikedActivity.MusicAdapter.MusicViewHolder> {
        private final Context context;
        private final ArrayList<String> musicListx;
        private final ArrayList<String> artistListx;
        private final ArrayList<Integer> imageResourceIdsx;

        public MusicAdapter(Context context, ArrayList<String> musicListx, ArrayList<String> artistListx, ArrayList<Integer> imageResourceIdsx) {
            this.context = context;
            this.musicListx = musicListx;
            this.artistListx = artistListx;
            this.imageResourceIdsx = imageResourceIdsx;
        }

        @NonNull
        @Override
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MusicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
            holder.songTitle.setText(musicListx.get(position));
            holder.artistName.setText(artistListx.get(position));
            holder.songImage.setImageResource(imageResourceIdsx.get(position));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songTitle", musicListx.get(position));
                intent.putExtra("artistName", artistListx.get(position));
                intent.putExtra("currentSongIndex", position);

                intent.putStringArrayListExtra("songTitles", musicListx);
                intent.putStringArrayListExtra("artistNames", artistListx);
                intent.putIntegerArrayListExtra("imageResourceIds", imageResourceIdsx);
                context.startActivity(intent);
            });

            holder.menuIcon.setOnClickListener(v -> {
                // Hiển thị BottomSheetDialogFragment khi nhấn vào icon menu
                MenuBottomSheetDialog bottomSheet = MenuBottomSheetDialog.newInstance(
                        musicListx.get(position),
                        artistListx.get(position),
                        imageResourceIdsx.get(position)
                );
                bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "MenuBottomSheet");
            });
        }

        @Override
        public int getItemCount() {
            return musicListx.size();
        }

        public class MusicViewHolder extends RecyclerView.ViewHolder {
            TextView songTitle;
            TextView artistName;
            ImageView menuIcon;
            ImageView songImage;

            public MusicViewHolder(View itemView) {
                super(itemView);
                songTitle = itemView.findViewById(R.id.songTitle);
                artistName = itemView.findViewById(R.id.artistName);
                menuIcon = itemView.findViewById(R.id.menuIcon);
                songImage = itemView.findViewById(R.id.songImage);
            }
        }
    }
}
