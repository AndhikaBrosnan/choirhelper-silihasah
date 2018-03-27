package choirhelper.silihasah.org.singing;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import choirhelper.silihasah.org.R;

public class SingActivity extends AppCompatActivity {

    private TextView tv_timer;
    private TextView tv_freq;
    private DatabaseReference mDb;

    Handler handler = new Handler();
    long startTime = 0L, timeinMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;

    int secs = 0;
    int priceTrigg = 0;
    int mins = 0;
    int count = 0;

    private SeekBar seekBar;
    private TextView ketepatannada;

    private String frequency_suara;

    Runnable updateTimerThread = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            timeinMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff+timeinMilliseconds;
            secs = (int)(updateTime/1000);
            priceTrigg = (int)(updateTime/1000);
            mins = secs/60;
            count++;

            secs%=60;
            int milliseconds = (int)(updateTime%1000);
            tv_timer.setText(String.format("%2d",mins)+":"+String.format("%2d",secs)+":"+String.format("%3d",milliseconds));

            //Seekbar handler
            seekBar.setMax(50);
            seekBar.setProgress(0);
            seekBar.setProgress(secs);

            switch (secs){
                case 7:
                    if(frequency_suara.equals("200")){ //Data pembanding
                        ketepatannada.setText("NADA TEPAT");
                    }else
                        ketepatannada.setText("NADA SUMBANG");
                    break;
                case 13:
                    if(frequency_suara.equals("130")){ //Data pembanding
                        ketepatannada.setText("NADA TEPAT");
                    }else
                        ketepatannada.setText("NADA SUMBANG");
                    break;
                case 17:
                    if(frequency_suara.equals("100")){ //Data pembanding
                        ketepatannada.setText("NADA TEPAT");
                    }else
                        ketepatannada.setText("NADA SUMBANG");
                default:
                    break;
            }

            handler.postDelayed(this,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        tv_freq = (TextView)findViewById(R.id.tv_freq);
        seekBar = (SeekBar)findViewById(R.id.sb_song);

        tv_timer = (TextView)findViewById(R.id.tv_timer);
        ketepatannada = (TextView)findViewById(R.id.tv_ketepatannada);

        mDb = FirebaseDatabase.getInstance().getReference().child("frequency");

        mDb.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                frequency_suara = dataSnapshot.getValue().toString();
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
}
