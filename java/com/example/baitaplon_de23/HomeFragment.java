package com.example.baitaplon_de23;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {
    private LinearLayout giangOiRadio;
    private LinearLayout viSaoTheNhi;
    private LinearLayout thuanPodcast;
    private LinearLayout likedPodcasts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các LinearLayout của "Giang ơi Radio" và "Vì sao thế nhỉ!"
        giangOiRadio = view.findViewById(R.id.giangOiRadio);
        viSaoTheNhi = view.findViewById(R.id.viSaoTheNhi);
        thuanPodcast = view.findViewById(R.id.thuanPodcast);
        likedPodcasts = view.findViewById(R.id.likedPodcasts);

        // Thiết lập sự kiện click cho "Giang ơi Radio"
        giangOiRadio.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), GiangOiRadioActivity.class);
            intent.putExtra("imageResource", R.drawable.theme_giangoiradio);
            startActivity(intent);
        });

        // Thiết lập sự kiện click cho "Vì sao thế nhỉ!"
        viSaoTheNhi.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ViSaoTheNhiActivity.class);
            intent.putExtra("imageResource", R.drawable.theme_visaothenhi);
            startActivity(intent);
        });

        thuanPodcast.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ThuanPodcastActivity.class);
            intent.putExtra("imageResource", R.drawable.theme_thuanpodcast);
            startActivity(intent);
        });

        likedPodcasts.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), LikedActivity.class);
            intent.putExtra("imageResource", R.drawable.theme_liked);
            startActivity(intent);
        });






        return view;
    }

}
