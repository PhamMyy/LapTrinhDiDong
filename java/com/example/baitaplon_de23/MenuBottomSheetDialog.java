package com.example.baitaplon_de23;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MenuBottomSheetDialog extends BottomSheetDialogFragment {
    private String songTitle;
    private String artistName;
    private int albumImageResId;

    // Phương thức để truyền thông tin bài hát vào BottomSheet
    public static MenuBottomSheetDialog newInstance(String songTitle, String artistName, int albumImageResId) {
        MenuBottomSheetDialog fragment = new MenuBottomSheetDialog();
        Bundle args = new Bundle();
        args.putString("songTitle", songTitle);
        args.putString("artistName", artistName);
        args.putInt("albumImageResId", albumImageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songTitle = getArguments().getString("songTitle");
            artistName = getArguments().getString("artistName");
            albumImageResId = getArguments().getInt("albumImageResId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_menu, container, false);

        // Lấy ImageView và TextView để hiển thị thông tin bài hát
        ImageView songImageView = view.findViewById(R.id.songImage);
        TextView songTitleTextView = view.findViewById(R.id.songTitle);
        TextView artistNameTextView = view.findViewById(R.id.artistName);
        TextView addToPlaylistTextView = view.findViewById(R.id.addToPlaylist);

        // Đặt giá trị cho ImageView và TextView
        songImageView.setImageResource(albumImageResId);
        songTitleTextView.setText(songTitle);
        artistNameTextView.setText(artistName);
        addToPlaylistTextView.setOnClickListener(v -> addToLikedSongs(songTitle, artistName, albumImageResId));

        return view;
    }

    private void addToLikedSongs(String title, String artist, int imageId) {
        Intent intent = new Intent(getContext(), LikedActivity.class);
        intent.putExtra("songTitle", title);
        intent.putExtra("artistName", artist);
        intent.putExtra("imageResourceId", imageId);
        getContext().startActivity(intent);
    }
}
