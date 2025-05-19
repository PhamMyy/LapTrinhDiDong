package com.example.baitaplon_de23;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private ImageView albumArtImageView;

    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private ImageButton btnBack10s, btnForward10s;
    private SeekBar musicSeekBar;

    private AudioManager audioManager;
    private ImageButton btnVolDown, btnVolUp;

    private TextView currentTime, totalTime, songTitleTextView, artistNameTextView;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();
    private int currentSongIndex;
    private int[] songList;

    private ArrayList<String> songTitles;
    private ArrayList<String> artistNames;
    private ArrayList<Integer> albumImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        TextView marqueeText = findViewById(R.id.song_title);
        marqueeText.setSelected(true);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        currentSongIndex = intent.getIntExtra("currentSongIndex", 0);

        // Nhận các danh sách từ Intent, và kiểm tra `null`
        songTitles = intent.getStringArrayListExtra("songTitles");
        artistNames = intent.getStringArrayListExtra("artistNames");
        albumImages = intent.getIntegerArrayListExtra("imageResourceIds");
        songList = intent.getIntArrayExtra("songList");  // Nhận mảng bài hát từ Intent



        // Kiểm tra nếu dữ liệu truyền vào không đầy đủ, thông báo lỗi và kết thúc
        if (songTitles == null || artistNames == null || albumImages == null || songList == null ||
                songTitles.isEmpty() || artistNames.isEmpty() || albumImages.isEmpty() || songList.length == 0) {
            finish();
            return;
        }

        // Tham chiếu các TextView để hiển thị tiêu đề bài hát và tên nghệ sĩ
        songTitleTextView = findViewById(R.id.song_title);
        artistNameTextView = findViewById(R.id.artist_name);
        albumArtImageView = findViewById(R.id.album_art);

        // Cập nhật giao diện với thông tin bài hát ban đầu
        updateSongInfo(currentSongIndex);

        // Khởi tạo các thành phần còn lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        playPauseButton = findViewById(R.id.btn_play_pause);
        nextButton = findViewById(R.id.btn_next);
        previousButton = findViewById(R.id.btn_previous);
        musicSeekBar = findViewById(R.id.music_seekbar);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);

        // Tham chiếu các nút tua lại và tua tới
        btnBack10s = findViewById(R.id.btn_back10s);
        btnForward10s = findViewById(R.id.btn_forward10s);

        // Khởi tạo MediaPlayer với bài hát dựa trên chỉ số
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex]);
        totalTime.setText(formatTime(mediaPlayer.getDuration()));

        // Xử lý sự kiện nút Play/Pause
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseMusic();
            } else {
                playMusic();
            }
        });

        // Xử lý sự kiện nút Next
        nextButton.setOnClickListener(v -> {
            if (currentSongIndex < songList.length - 1) {
                currentSongIndex++;
            } else {
                currentSongIndex = 0;
            }
            playNewSong();
        });

        // Xử lý sự kiện nút Previous
        previousButton.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else {
                currentSongIndex = songList.length - 1;
            }
            playNewSong();
        });

        // Xử lý khi thay đổi SeekBar
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Cập nhật SeekBar và thời gian phát
        updateSeekBar();

        // Xử lý sự kiện nút "Back"
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_down);
        });

        // Xử lý sự kiện tua lại 10 giây
        btnBack10s.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (currentPosition - 10000 > 0) {
                mediaPlayer.seekTo(currentPosition - 10000);
            } else {
                mediaPlayer.seekTo(0);
            }
        });

        // Xử lý sự kiện tua tới 10 giây
        btnForward10s.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (currentPosition + 10000 < mediaPlayer.getDuration()) {
                mediaPlayer.seekTo(currentPosition + 10000);
            } else {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        });

        // Lấy AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Tham chiếu đến các nút điều chỉnh âm lượng
        btnVolDown = findViewById(R.id.btn_volDown);
        btnVolUp = findViewById(R.id.btn_volUp);

        // Xử lý sự kiện nút giảm âm lượng
        btnVolDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseVolume();
            }
        });

        // Xử lý sự kiện nút tăng âm lượng
        btnVolUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseVolume();
            }
        });

        // Trong onCreate của PlayActivity.java
        ImageButton btnSpeed = findViewById(R.id.btn_speed);
        btnSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeedDialog();
            }
        });
        // Trong onCreate
        ImageButton btnTimer = findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(v -> showTimerDialog());


    }




    private void playNewSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex]);
        totalTime.setText(formatTime(mediaPlayer.getDuration()));

        // Cập nhật thông tin giao diện với bài hát mới
        updateSongInfo(currentSongIndex);
        playMusic();
    }

    private void updateSongInfo(int songIndex) {
        songTitleTextView.setText(songTitles.get(songIndex));
        artistNameTextView.setText(artistNames.get(songIndex));
        albumArtImageView.setImageResource(albumImages.get(songIndex));
    }

    private void playMusic() {
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.ic_pause);
        isPlaying = true;
        updateSeekBar();
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        playPauseButton.setImageResource(R.drawable.ic_play);
        isPlaying = false;
    }

    private void updateSeekBar() {
        musicSeekBar.setMax(mediaPlayer.getDuration());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    private void decreaseVolume() {
        // Giảm âm lượng 1 đơn vị
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    private void increaseVolume() {
        // Tăng âm lượng 1 đơn vị
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    private void showSpeedDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_speed, null);
        dialog.setContentView(view);

        // Xử lý sự kiện khi chọn từng tốc độ
        view.findViewById(R.id.speed_1x).setOnClickListener(v -> {
            setPlaybackSpeed(1.0f);
            dialog.dismiss();
        });
        view.findViewById(R.id.speed_1_2x).setOnClickListener(v -> {
            setPlaybackSpeed(1.2f);
            dialog.dismiss();
        });
        view.findViewById(R.id.speed_1_5x).setOnClickListener(v -> {
            setPlaybackSpeed(1.5f);
            dialog.dismiss();
        });
        view.findViewById(R.id.speed_1_8x).setOnClickListener(v -> {
            setPlaybackSpeed(1.8f);
            dialog.dismiss();
        });
        view.findViewById(R.id.speed_2x).setOnClickListener(v -> {
            setPlaybackSpeed(2.0f);
            dialog.dismiss();
        });

        dialog.show();
    }

    // Phương thức thay đổi tốc độ phát nhạc
    private void setPlaybackSpeed(float speed) {
        if (mediaPlayer != null) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
    }

    private void showTimerDialog() {
        BottomSheetDialog timerDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_timer, null);
        timerDialog.setContentView(view);

        // Xử lý sự kiện khi chọn từng tùy chọn hẹn giờ
        view.findViewById(R.id.option_5_minutes).setOnClickListener(v -> {
            setSleepTimer(5 * 60 * 1000); // Hẹn giờ 5 phút
            timerDialog.dismiss();
        });

        view.findViewById(R.id.option_10_minutes).setOnClickListener(v -> {
            setSleepTimer(10 * 60 * 1000); // Hẹn giờ 10 phút
            timerDialog.dismiss();
        });

        view.findViewById(R.id.option_15_minutes).setOnClickListener(v -> {
            setSleepTimer(15 * 60 * 1000); // Hẹn giờ 15 phút
            timerDialog.dismiss();
        });

        view.findViewById(R.id.option_end_of_episode).setOnClickListener(v -> {
            setSleepTimerAtEndOfEpisode(); // Hẹn giờ khi hết tập
            timerDialog.dismiss();
        });

        timerDialog.show();
    }
    private void setSleepTimer(int milliseconds) {
        handler.postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.ic_play);
                isPlaying = false;
            }
        }, milliseconds);
    }
    private void setSleepTimerAtEndOfEpisode() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.ic_play);
                isPlaying = false;
            });
        }
    }






}
