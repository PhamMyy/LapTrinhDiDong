package com.example.baitaplon_de23;

import android.content.Context;
import android.content.Intent;
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

public class ViSaoTheNhiActivity extends AppCompatActivity {

    private ImageView themeImageView2;
    private TextView artistNameTextView;
    private ArrayList<String> musicList2;
    private ArrayList<String> artistList2;
    private ArrayList<Integer> imageResourceIds2;
    private int[] songList2;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        themeImageView2 = findViewById(R.id.themeImageView);
        artistNameTextView = findViewById(R.id.artistName);
        int imageResource = getIntent().getIntExtra("imageResource", R.drawable.theme_visaothenhi); // Giá trị mặc định
        themeImageView2.setImageResource(imageResource);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());


        // Khởi tạo danh sách ID hình ảnh từ drawable
        imageResourceIds2 = new ArrayList<>();
        imageResourceIds2.add(R.drawable.image_b1);
        imageResourceIds2.add(R.drawable.image_b2);
        imageResourceIds2.add(R.drawable.image_b3);
        imageResourceIds2.add(R.drawable.image_b4);
        imageResourceIds2.add(R.drawable.image_b5);
        imageResourceIds2.add(R.drawable.image_b6);
        imageResourceIds2.add(R.drawable.image_b7);
        imageResourceIds2.add(R.drawable.image_b8);
        imageResourceIds2.add(R.drawable.image_b9);
        imageResourceIds2.add(R.drawable.image_b10);

        DatabaseViSao dbHelper = new DatabaseViSao(this);
        musicList2 = dbHelper.getMusicList2();
        artistList2 = dbHelper.getArtistList2();

        // Thiết lập tên nghệ sĩ từ danh sách artistList2
        if (!artistList2.isEmpty()) {
            artistNameTextView.setText(artistList2.get(0)); // Hiển thị tên nghệ sĩ đầu tiên
        }


        // Khởi tạo mảng chứa các bài hát
        songList2 = new int[]{
                R.raw.va_rang_nhung_nguoi,
                R.raw.nhung_chuyen_buon_hom_ay,
                R.raw.vi_sao_thang_7,
                R.raw.gia_ma_con_nguoi_ta,
                R.raw.vi_sao_nguoi_sang_tao,
                R.raw.co_con_nguoi_song,
                R.raw.co_mot_noi_dau,
                R.raw.nhung_dua_tre_co_ngoai_hinh,
                R.raw.co_dieu_gi_an_nap,
                R.raw.minh_kiem_loi
        };

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sử dụng adapter nội bộ để hiển thị danh sách bài hát
        MusicAdapter2 adapter = new MusicAdapter2(this, musicList2, artistList2, imageResourceIds2, songList2);
        recyclerView.setAdapter(adapter);
    }

    // Adapter nội bộ trong ViSaoTheNhi
    private class MusicAdapter2 extends RecyclerView.Adapter<MusicAdapter2.MusicViewHolder> {
        private final Context context;
        private final ArrayList<String> musicList2;
        private final ArrayList<String> artistList2;
        private final ArrayList<Integer> imageResourceIds2;
        private final int[] songList2;  // Biến để lưu mảng các bài hát

        public MusicAdapter2(Context context, ArrayList<String> musicList2, ArrayList<String> artistList2, ArrayList<Integer> imageResourceIds2, int[] songList2) {
            this.context = context;
            this.musicList2 = musicList2;
            this.artistList2 = artistList2;
            this.imageResourceIds2 = imageResourceIds2;
            this.songList2 = songList2;
        }

        @NonNull
        @Override
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MusicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
            holder.songTitle.setText(musicList2.get(position));
            holder.artistName.setText(artistList2.get(position));
            holder.songImage.setImageResource(imageResourceIds2.get(position));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songTitle", musicList2.get(position));
                intent.putExtra("artistName", artistList2.get(position));
                intent.putExtra("currentSongIndex", position);
                intent.putStringArrayListExtra("songTitles", musicList2);
                intent.putStringArrayListExtra("artistNames", artistList2);
                intent.putIntegerArrayListExtra("imageResourceIds", imageResourceIds2);
                intent.putExtra("songList", songList2);  // Truyền mảng songList vào Intent
                context.startActivity(intent);
            });

            holder.menuIcon.setOnClickListener(v -> {
                // Hiển thị BottomSheetDialogFragment khi nhấn vào icon menu
                MenuBottomSheetDialog bottomSheet = MenuBottomSheetDialog.newInstance(
                        musicList2.get(position),
                        artistList2.get(position),
                        imageResourceIds2.get(position)
                );
                bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "MenuBottomSheet");
            });
        }

        @Override
        public int getItemCount() {
            return musicList2.size();
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
