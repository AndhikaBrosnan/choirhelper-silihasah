package choirhelper.silihasah.org.ui.pitch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Pitch;
import choirhelper.silihasah.org.ui.pitch.tarsosDSPpitchvoice.AudioDispatcherFactory;
import choirhelper.silihasah.org.ui.sing.SingActivity;

public class PitchSongActivity extends AppCompatActivity {

    //handler for runnable
    Handler handler = new Handler();
    long startTime = 0L, timeinMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;

    //timer runnable
    Runnable updateRecordTimerThread = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void run() {
            timeinMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff+timeinMilliseconds;
            int secs = (int)(updateTime/1000);
            int mins = secs/60;
            pitchTrigg = (int)(updateTime/1000);
            secs%=60;
            int milliseconds = (int)(updateTime%1000);
            timerecord.setText(String.format("%2d",mins)+":"+String.format("%2d",secs));
            handler.postDelayed(this,0);

            if(pitchTrigg == 45){
                handler.removeCallbacks(updateRecordTimerThread);
            }
        }
    };


    int i = 0;
    //pitch runnable
    Runnable updateSongPitchThread = new Runnable() {
        @Override
        public void run() {
            i++;
            data.add(new Pitch(pitchinHzDisplay,pitch.getText().toString(),timerecord.getText().toString()));
            mDb.child(voiceType + i).setValue(new Pitch(pitchinHzDisplay,pitch.getText().toString(),timerecord.getText().toString()));
            mAdapter.notifyDataSetChanged();
            handler.postDelayed(this,1000);

            if(pitchTrigg == 45){
                handler.removeCallbacks(updateSongPitchThread);
            }
        }
    };

    private String songTitle;
    private String voiceType;
    private String uid;
    private DatabaseReference mDb;
    private Button singbutton;

    RecyclerView rv;
    PitchSongAdapter mAdapter;

    private float pitchinHzDisplay ;
    private TextView titleinfo;
    private TextView pitch;
    private TextView freq;
    private ImageView record;
    private TextView timerecord;


    int pitchTrigg = 0;

    ArrayList<Pitch> data = fillData();

    //the ArrayList data
    ArrayList<Pitch> fillData(){
        ArrayList<Pitch> data = new ArrayList<>();

        startTime = SystemClock.uptimeMillis();
        //list handler
        handler.postDelayed(updateSongPitchThread,0);

        return data;
    }


    AudioDispatcher dispatcher =
            AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_song);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        initPitch();

        //database
        mDb = FirebaseDatabase.getInstance().getReference().child("song_list").child(uid).child("song_data").child(voiceType);

        //Check data if has data or not. If data empty, add data. If data exist don't add data.

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



//        mDb.setValue("Brosnan Ganteng");

    }

    //menghitung frekuensi dan nada suara (Android)
    private void initPitch(){
        //pitch handler
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();
                pitchinHzDisplay = pitchInHz;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };

        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    private void initView(){
        pitch = (TextView)findViewById(R.id.tv_songpitch);
        freq = (TextView)findViewById(R.id.tv_songfrequencies);
        record = (ImageView)findViewById(R.id.iv_record);
        timerecord = (TextView)findViewById(R.id.tv_recordtime);
        rv = (RecyclerView)findViewById(R.id.rv_tonepersec);
        titleinfo = (TextView)findViewById(R.id.info_record);
        singbutton = (Button)findViewById(R.id.b_sing);

        songTitle = getIntent().getStringExtra("title");
        voiceType = getIntent().getStringExtra("voicetype");
        uid = getIntent().getStringExtra("uid");

        setTitle(songTitle + " - " + voiceType);

        singbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingActivity.class);
                Bundle b = new Bundle();
                b.putString("uid",uid);
                b.putString("title",songTitle);
                b.putString("voicetype",voiceType);
                intent.putExtras(b);
                startActivity(intent);

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        mAdapter = new PitchSongAdapter(this,mDb,data);
        mAdapter.notifyDataSetChanged();

        //start the time and record the frequency
        startTime = SystemClock.uptimeMillis();

        //timer handler
        handler.postDelayed(updateRecordTimerThread, 0);
        rv.setAdapter(mAdapter);
    }

    @SuppressLint("SetTextI18n")
    public void processPitch(float pitchInHz) {

        freq.setText("" + pitchInHz);

        if(pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
            pitch.setText("A");
        }
        else if(pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            pitch.setText("B");
        }
        else if(pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            pitch.setText("C");
        }
        else if(pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            pitch.setText("D");
        }
        else if(pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            pitch.setText("E");
        }
        else if(pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            pitch.setText("F");
        }
        else if(pitchInHz >= 185 && pitchInHz < 196) {
            //G
            pitch.setText("G");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
