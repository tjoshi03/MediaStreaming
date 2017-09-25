package govirtual360.mediastreaming;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener  {
        Button play ,pause;
        SeekBar seekBar;
    Player player;
    int flag;
    String url="https://paymentdone.000webhostapp.com/despacito.mp3";
    ProgressDialog progressDialog;
        private MediaPlayer mediaPlayer;
        private int lengthOfAudio;
    private final Handler handler = new Handler();

    private final Runnable r = new Runnable()
    {
        @Override
        public void run() {
            updateSeekProgress();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        flag=0;
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        mediaPlayer=new MediaPlayer();

        play=(Button)findViewById(R.id.play);
        pause=(Button)findViewById(R.id.pause);
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        seekBar.setOnTouchListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        mediaPlayer.setOnInfoListener(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        seekBar.setSecondaryProgress(i);
        System.out.println(i);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        seekBar.setProgress(0);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                 progressDialog.show();

                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                 progressDialog.dismiss();

                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play:
                if (flag==0){
                    //  new Player().execute(URL);
                    player=new Player();
                    player.execute(url);

                }
                else {
                    if (mediaPlayer!=null){

                        playAudio();

                    }
                }
                break;
            case R.id.pause:
                    pauseAudio();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        if (mediaPlayer.isPlaying())
        {
            SeekBar tmpSeekBar = (SeekBar)v;
            mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
        }
        else {
            SeekBar tmpSeekBar = (SeekBar)v;
            mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
        }
        return false;
    }

    private void updateSeekProgress() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / lengthOfAudio) * 100));


                handler.postDelayed(r, 1000);
            }
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean prepared;

            try
            {
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepare();
                lengthOfAudio = mediaPlayer.getDuration();

                prepared = true;

            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
                progressDialog.dismiss();
            if (aBoolean){
                flag=1;
            }
            else {
                flag=0;
            }
            playAudio();

        }
    }

    private void playAudio() {

        if(mediaPlayer!=null)
        {
            mediaPlayer.start();
            updateSeekProgress();



        }
    }

    private void pauseAudio() {
        if(mediaPlayer!=null)
        {
            mediaPlayer.pause();
        }
    }
}
