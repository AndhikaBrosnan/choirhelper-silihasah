package choirhelper.silihasah.org.ui.sing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import choirhelper.silihasah.org.R;

/**
 * Created by BrosnanUhYeah on 03/04/2018.
 */

public class SingSopranActivity extends AppCompatActivity {

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
            tv_timer.setText(String.format("%2d",mins)+":"+String.format("%2d",secs));

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
    int count = 0;

    private SeekBar seekBar;
    private TextView ketepatannada;

    private int frequency_suara;

    final Context context = this;

    private ImageView tunegood;
    private ImageView tunehigh;
    private ImageView tunelow;
    private TextView goodfreq;
    private EditText ipaddress;
    private Button startbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
//        serverConnect();

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
        ipaddress = (EditText)findViewById(R.id.et_ipaddr);
        startbutton = (Button)findViewById(R.id.b_start);
    }

    //realtime wifi client code

//    private void serverConnect(){
//        String serverAdress = ipaddress.getText().toString() + ":" + "80";
//        HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
//        requestTask.execute(ledStatus);
//    }
//
//    private class HttpRequestTask extends AsyncTask<String, Void, String> {
//
//        private String serverAdress;
//        private String serverResponse = "";
//        private AlertDialog dialog;
//
//        public HttpRequestTask(String serverAdress) {
//            this.serverAdress = serverAdress;
//
//            dialog = new AlertDialog.Builder(context)
//                    .setTitle("HTTP Response from Ip Address:")
//                    .setCancelable(true)
//                    .create();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            dialog.setMessage("Data sent , waiting response from server...");
//
//            if (!dialog.isShowing())
//                dialog.show();
//
//            String val = params[0];
//            final String url = "http://" + serverAdress + "/led/" + val;
//
//            try {
//                HttpClient client = new DefaultHttpClient();
//                HttpGet getRequest = new HttpGet();
//                getRequest.setURI(new URI(url));
//                HttpResponse response = client.execute(getRequest);
//
//                InputStream inputStream = null;
//                inputStream = response.getEntity().getContent();
//                BufferedReader bufferedReader =
//                        new BufferedReader(new InputStreamReader(inputStream));
//
//                serverResponse = bufferedReader.readLine();
//                inputStream.close();
//
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//                serverResponse = e.getMessage();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//                serverResponse = e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                serverResponse = e.getMessage();
//            }
//
//            return serverResponse;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            dialog.setMessage(serverResponse);
//
//            if (!dialog.isShowing())
//                dialog.show();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            dialog.setMessage("Sending data to server, please wait...");
//
//            if (!dialog.isShowing())
//                dialog.show();
//        }
//    }

    private void tuning(){

        switch (secs){
            case 3: //A3 (220.00)
                goodfreq.setText("220");
                if(frequency_suara>=214 && frequency_suara<= 224){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<214){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 4://C4 (261.63)
                goodfreq.setText("261");
                if(frequency_suara>=256 && frequency_suara<= 266){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 5: //A3 (220.00)
                goodfreq.setText("220");
                if(frequency_suara>=214 && frequency_suara<= 224){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<214){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 6://C4 (261.63)
                goodfreq.setText("261");
                if(frequency_suara>=256 && frequency_suara<= 266){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 7: //E4 (329.63)
                goodfreq.setText("329");
                if(frequency_suara>=323 && frequency_suara<= 332){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<323){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 8: //E4 (329.63)
                goodfreq.setText("329");
                if(frequency_suara>=323 && frequency_suara<= 332){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<323){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 9: //D4 (293.66)
                goodfreq.setText("293");
                if(frequency_suara>=323 && frequency_suara<= 332){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<323){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 10: // *diam*
            case 11: // *diam*
            case 12: // *diam*
            case 13: // *diam*

            case 14: //A3 (220.00)
                goodfreq.setText("220");
                if(frequency_suara>=214 && frequency_suara<= 224){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<214){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 15: //C4 (261.63)
                goodfreq.setText("261");
                if(frequency_suara>=256 && frequency_suara<= 266){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 16: //A3 (220.00)
                goodfreq.setText("220");
                if(frequency_suara>=214 && frequency_suara<= 224){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<214){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 17: //C4 (261.63)
                goodfreq.setText("261");
                if(frequency_suara>=256 && frequency_suara<= 266){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<116){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;
            case 18: //E4 (329.63)
                goodfreq.setText("329");
                if(frequency_suara>=323 && frequency_suara<= 332){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<323){
                    tunelow();
                }else {
                    tunehigh();
                }
                break;

            case 19: //D4 (293.66)
                goodfreq.setText("293");
                if(frequency_suara>=323 && frequency_suara<= 332){ //Data pembanding
                    tunegood();
                }else if(frequency_suara<323){
                    tunelow();
                }else {
                    tunehigh();
                }
            case 20: //*diam*
            case 21: //*diam*
            case 22: //*diam*
            case 23: //*diam*
            case 24: //*diam*
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
