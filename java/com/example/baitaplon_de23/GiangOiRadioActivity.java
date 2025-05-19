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

public class GiangOiRadioActivity extends AppCompatActivity {

    private ImageView themeImageView1;
    private TextView artistNameTextView;
    private ArrayList<String> musicList1;
    private ArrayList<String> artistList1;
    private ArrayList<Integer> imageResourceIds1;
    private int[] songList1;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        themeImageView1 = findViewById(R.id.themeImageView);
        artistNameTextView = findViewById(R.id.artistName);
        int imageResource = getIntent().getIntExtra("imageResource", R.drawable.theme_giangoiradio); // Giá trị mặc định
        themeImageView1.setImageResource(imageResource);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo danh sách ID hình ảnh từ drawable
        imageResourceIds1 = new ArrayList<>();
        imageResourceIds1.add(R.drawable.image_a1);
        imageResourceIds1.add(R.drawable.image_a2);
        imageResourceIds1.add(R.drawable.image_a3);
        imageResourceIds1.add(R.drawable.image_a4);
        imageResourceIds1.add(R.drawable.image_a5);
        imageResourceIds1.add(R.drawable.image_a6);
        imageResourceIds1.add(R.drawable.image_a7);
        imageResourceIds1.add(R.drawable.image_a8);
        imageResourceIds1.add(R.drawable.image_a9);
        imageResourceIds1.add(R.drawable.image_a10);

        DatabaseGiang dbHelper = new DatabaseGiang(this);
        musicList1 = dbHelper.getMusicList1();
        artistList1 = dbHelper.getArtistList1();

//        TextView txtSearch = findViewById(R.id.txt_search);
//        txtSearch.setOnClickListener(v -> {
//            // Tạo Intent để mở SearchActivity khi nhấn vào txt_search
//            Intent intent = new Intent(GiangOiRadioActivity.this, SearchActivity.class);
//            startActivity(intent);
//        });

        // Thiết lập tên nghệ sĩ từ danh sách artistList1
        if (!artistList1.isEmpty()) {
            artistNameTextView.setText(artistList1.get(0)); // Hiển thị tên nghệ sĩ đầu tiên
        }



        // Khởi tạo mảng chứa các bài hát
        songList1 = new int[] {
                R.raw.nguoi_lon_chua_lanh,
                R.raw.nguoi_lon_di_du_hoc,
                R.raw.nguoi_lon_song_de_theo_duoi,
                R.raw.nguoi_lon_va_ap_luc,
                R.raw.nguoi_lon_duoc_va_mat,
                R.raw.nguoi_lon_ra_o_rieng,
                R.raw.nguoi_lon_co_don,
                R.raw.nguoi_lon_fomo,
                R.raw.nguoi_lon_qua_tai,
                R.raw.nguoi_lon_cam_thay_gia
        };

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sử dụng adapter nội bộ để hiển thị danh sách bài hát
        MusicAdapter1 adapter = new MusicAdapter1(this, musicList1, artistList1, imageResourceIds1, songList1);
        recyclerView.setAdapter(adapter);
    }

    // Adapter nội bộ trong GiangOiRadioActivity
    private class MusicAdapter1 extends RecyclerView.Adapter<MusicAdapter1.MusicViewHolder> {
        private final Context context;
        private final ArrayList<String> musicList1;
        private final ArrayList<String> artistList1;
        private final ArrayList<Integer> imageResourceIds1;
        private final int[] songList1;  // Biến để lưu mảng các bài hát

        public MusicAdapter1(Context context, ArrayList<String> musicList1, ArrayList<String> artistList1, ArrayList<Integer> imageResourceIds1, int[] songList1) {
            this.context = context;
            this.musicList1 = musicList1;
            this.artistList1 = artistList1;
            this.imageResourceIds1 = imageResourceIds1;
            this.songList1 = songList1;
        }

        @NonNull
        @Override
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MusicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
            holder.songTitle.setText(musicList1.get(position));
            holder.artistName.setText(artistList1.get(position));
            holder.songImage.setImageResource(imageResourceIds1.get(position));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songTitle", musicList1.get(position));
                intent.putExtra("artistName", artistList1.get(position));
                intent.putExtra("currentSongIndex", position);

                intent.putStringArrayListExtra("songTitles", musicList1);
                intent.putStringArrayListExtra("artistNames", artistList1);
                intent.putIntegerArrayListExtra("imageResourceIds", imageResourceIds1);
                intent.putExtra("songList", songList1);  // Truyền mảng songList vào Intent
                context.startActivity(intent);
            });

            holder.menuIcon.setOnClickListener(v -> {
                // Hiển thị BottomSheetDialogFragment khi nhấn vào icon menu
                MenuBottomSheetDialog bottomSheet = MenuBottomSheetDialog.newInstance(
                        musicList1.get(position),
                        artistList1.get(position),
                        imageResourceIds1.get(position)
                );
                bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "MenuBottomSheet");
            });
        }

        @Override
        public int getItemCount() {
            return musicList1.size();
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
