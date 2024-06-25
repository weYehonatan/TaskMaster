package yehonatan.weitzman.taskmaster;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SoundService extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        mediaPlayer=MediaPlayer.create(this,R.raw.background);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

}