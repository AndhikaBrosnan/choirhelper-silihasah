package choirhelper.silihasah.org.ui.sing;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import choirhelper.silihasah.org.R;

public class SingBassActivity extends AppCompatActivity {

    Runnable updateTimerThread = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            timeinMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff+timeinMilliseconds;
            secs = (int)(updateTime/1000);
            priceTrigg = (int)(updateTime/1000);
            mins = secs/60;

            secs%=60;
            int milliseconds = (int)(updateTime%1000);
            tv_timer.setText(String.format("%2d",mins)+":"+String.format("%2d",secs)+":"+String.format("%3d",milliseconds));

            //Seekbar handler
            seekBar.setMax(50);
            seekBar.setProgress(0);
            seekBar.setProgress(secs);

            tuning();

            handler.postDelayed(this,0);
        }
    };

    private TextView tv_timer;
    private TextView tv_freq;
    private DatabaseReference mDb;

    Handler handler = new Handler();
    long startTime = 0L, timeinMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;

    int secs = 0;
    int priceTrigg = 0;
    int mins = 0;

    private SeekBar seekBar;
    private TextView ketepatannada;

    private int frequency_suara;

    private ImageView tunegood;
    private ImageView tunehigh;
    private ImageView tunelow;
    private TextView goodfreq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        mDb = FirebaseDatabase.getInstance().getReference().child("frequency");

        mDb.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                frequency_suara = Integer.valueOf(dataSnapshot.getValue().toString());
                tv_freq.setText(frequency_suara +" Hz");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Time Handler
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimerThread,0);
    }

    private void initView(){
        tv_freq = (TextView)findViewById(R.id.tv_freq_practice);
        seekBar = (SeekBar)findViewById(R.id.sb_song);
        tv_timer = (TextView)findViewById(R.id.tv_timer);
        ketepatannada = (TextView)findViewById(R.id.tv_ketepatannada_practice);
        tunegood = (ImageView)findViewById(R.id.iv_tunegood_practice);
        tunehigh = (ImageView)findViewById(R.id.iv_tunehigh_practice);
        tunelow = (ImageView)findViewById(R.id.iv_tunelow_practice);
        goodfreq = (TextView)findViewById(R.id.tv_goodfreq_practice);
    }

    private void tuning(){


        //using C3 NOTE, 3rd Octave
        switch (secs){
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                goodfreq.setText("121");
                if(frequency_suara>=116 && frequency_suara<= 126){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                goodfreq.setText("165");
                if(frequency_suara>=160 && frequency_suara<= 171){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<165){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;

            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                goodfreq.setText("121");
                if(frequency_suara>=116 && frequency_suara<= 126){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                goodfreq.setText("165");
                if(frequency_suara>=160 && frequency_suara<= 171){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<165){
                    tunelow();
                }else {
                    tunehigh();
                }
            break;
            case 23:
                goodfreq.setText("121");
                if(frequency_suara>=116 && frequency_suara<= 126){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            default:
                idle();
                break;
        }
    }

    private void idle(){
        ketepatannada.setText("diam");
    }

    private void tunegood(){
        ketepatannada.setText("NADA TEPAT");

        tunehigh.setImageResource(R.color.grey);
        tunegood.setImageResource(R.color.green);
        tunelow.setImageResource(R.color.grey);
    }

    private void tunehigh(){
        ketepatannada.setText("NADA TERLALU TINGGI");

        tunehigh.setImageResource(R.color.red);
        tunegood.setImageResource(R.color.grey);
        tunelow.setImageResource(R.color.grey);
    }

    private void tunelow(){
        ketepatannada.setText("NADA TERLALU RENDAH");

        tunegood.setImageResource(R.color.grey);
        tunehigh.setImageResource(R.color.grey);
        tunelow.setImageResource(R.color.red);
    }
}
