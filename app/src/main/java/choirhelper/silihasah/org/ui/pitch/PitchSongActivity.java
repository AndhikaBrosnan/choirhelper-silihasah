package choirhelper.silihasah.org.ui.pitch;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Pitch;
import choirhelper.silihasah.org.ui.pitch.TarsosDSPstuffs.AudioDispatcherFactory;

public class PitchSongActivity extends AppCompatActivity {

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
            timerecord.setText(String.format("%2d",mins)+":"+String.format("%2d",secs)+":"+String.format("%3d",milliseconds));
            handler.postDelayed(this,0);
        }
    };

    //pitch runnable
    Runnable updateSongPitchThread = new Runnable() {
        @Override
        public void run() {
            data.add(new Pitch(pitchinHzDisplay,pitch.getText().toString(),timerecord.getText().toString()));
            Log.d("Data ditambahkan: ",String.valueOf(pitchTrigg));
            mAdapter.notifyDataSetChanged();
            handler.postDelayed(this,1000);
        }
    };

    //the ArrayList data
    ArrayList<Pitch> fillData(){
        ArrayList<Pitch> data = new ArrayList<>();

        startTime = SystemClock.uptimeMillis();
        //list handler
        handler.postDelayed(updateSongPitchThread,0);

        return data;
    }

    ArrayList<Pitch> data = fillData();
    RecyclerView rv;
    PitchSongAdapter mAdapter;

    private float pitchinHzDisplay ;

    int pitchTrigg = 0;

    AudioDispatcher dispatcher =
            AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
    private TextView pitch;
    private TextView freq;
    private ImageView record;
    private TextView timerecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_song);

        initView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        mAdapter = new PitchSongAdapter(this,data);
        mAdapter.notifyDataSetChanged();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();

                //timer handler
                handler.postDelayed(updateRecordTimerThread,0);

                rv.setAdapter(mAdapter);
            }
        });

        //recyclerview pitch song list


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
    }

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
