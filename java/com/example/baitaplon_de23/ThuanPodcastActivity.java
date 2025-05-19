package com.example.baitaplon_de23;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

public class ThuanPodcastActivity extends AppCompatActivity {

    private ImageView themeImageView3;
    private TextView artistNameTextView;
    private ArrayList<String> musicList3;
    private ArrayList<String> artistList3;
    private ArrayList<Integer> imageResourceIds3;
    private int[] songList3;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        themeImageView3 = findViewById(R.id.themeImageView);
        artistNameTextView = findViewById(R.id.artistName);
        int imageResource = getIntent().getIntExtra("imageResource", R.drawable.theme_thuanpodcast); // Giá trị mặc định
        themeImageView3.setImageResource(imageResource);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());


        // Khởi tạo danh sách ID hình ảnh từ drawable
        imageResourceIds3 = new ArrayList<>();
        imageResourceIds3.add(R.drawable.theme_thuanpodcast);
        imageResourceIds3.add(R.drawable.theme_thuanpodcast);
        imageResourceIds3.add(R.drawable.image_c3);
        imageResourceIds3.add(R.drawable.image_c4);
        imageResourceIds3.add(R.drawable.image_c5);
        imageResourceIds3.add(R.drawable.image_c6);
        imageResourceIds3.add(R.drawable.image_c7);
        imageResourceIds3.add(R.drawable.image_c8);
        imageResourceIds3.add(R.drawable.image_c9);
        imageResourceIds3.add(R.drawable.image_c10);

        DatabaseThuan dbHelper = new DatabaseThuan(this);
        musicList3 = dbHelper.getMusicList3();
        artistList3 = dbHelper.getArtistList3();

        // Thiết lập tên nghệ sĩ từ danh sách artistList2
        if (!artistList3.isEmpty()) {
            artistNameTextView.setText(artistList3.get(0)); // Hiển thị tên nghệ sĩ đầu tiên
        }


        // Khởi tạo mảng chứa các bài hát
        songList3 = new int[] {
                R.raw.doc_lap_tu_do,
                R.raw.toi_dung_o_ria,
                R.raw.co_nhung_mua_he,
                R.raw.cai_toi,
                R.raw.chuyen_cai_chet,
                R.raw.tinh_yeu,
                R.raw.duoi_mai_hien,
                R.raw.mot_minh,
                R.raw.thanh_xuan,
                R.raw.ai_cung_di_qua
        };

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sử dụng adapter nội bộ để hiển thị danh sách bài hát
        MusicAdapter1 adapter = new MusicAdapter1(this, musicList3, artistList3, imageResourceIds3, songList3);
        recyclerView.setAdapter(adapter);
    }

    // Adapter nội bộ trong GiangOiRadioActivity
    private class MusicAdapter1 extends RecyclerView.Adapter<MusicAdapter1.MusicViewHolder> {
        private final Context context;
        private final ArrayList<String> musicList3;
        private final ArrayList<String> artistList3;
        private final ArrayList<Integer> imageResourceIds3;
        private final int[] songList3;  // Biến để lưu mảng các bài hát

        public MusicAdapter1(Context context, ArrayList<String> musicList3, ArrayList<String> artistList3, ArrayList<Integer> imageResourceIds3, int[] songList3) {
            this.context = context;
            this.musicList3 = musicList3;
            this.artistList3 = artistList3;
            this.imageResourceIds3 = imageResourceIds3;
            this.songList3 = songList3;
        }

        @NonNull
        @Override
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MusicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
            holder.songTitle.setText(musicList3.get(position));
            holder.artistName.setText(artistList3.get(position));
            holder.songImage.setImageResource(imageResourceIds3.get(position));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songTitle", musicList3.get(position));
                intent.putExtra("artistName", artistList3.get(position));
                intent.putExtra("currentSongIndex", position);

                intent.putStringArrayListExtra("songTitles", musicList3);
                intent.putStringArrayListExtra("artistNames", artistList3);
                intent.putIntegerArrayListExtra("imageResourceIds", imageResourceIds3);
                intent.putExtra("songList", songList3);  // Truyền mảng songList vào Intent
                context.startActivity(intent);
            });

            holder.menuIcon.setOnClickListener(v -> {
                // Hiển thị BottomSheetDialogFragment khi nhấn vào icon menu
                MenuBottomSheetDialog bottomSheet = MenuBottomSheetDialog.newInstance(
                        musicList3.get(position),
                        artistList3.get(position),
                        imageResourceIds3.get(position)
                );
                bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "MenuBottomSheet");
            });
        }

        @Override
        public int getItemCount() {
            return musicList3.size();
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
