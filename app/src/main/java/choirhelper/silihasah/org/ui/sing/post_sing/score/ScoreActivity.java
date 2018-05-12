package choirhelper.silihasah.org.ui.sing.post_sing.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Song;
import choirhelper.silihasah.org.ui.sing.post_sing.score.score_history.DatabaseHelper;

public class ScoreActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private TextView score;
    public String scoreValue;
    private String voiceType;
    private String uid;
    private RecyclerView rv_scorelist;

    private Context context = this;
    private DatabaseHelper databaseHelper;
    private String songTitle;

    ArrayList<Song> listScore;
    private ScoreListAdapter scoreAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        if(songTitle == null || voiceType == null || uid == null){
            score.setText("SCORE HISTORY");
            setTitle("Improve yourself!");
        }else{
            initFirebase();
        }

        initObject();
    }

    public void initView(){
        score = (TextView)findViewById(R.id.tv_songscore);
        rv_scorelist = (RecyclerView)findViewById(R.id.rv_score);

        songTitle = getIntent().getStringExtra("songTitle");
        voiceType = getIntent().getStringExtra("voicetype");
        uid = getIntent().getStringExtra("uid");

        setTitle(songTitle + " - " +voiceType);
    }

    public void initFirebase(){
        mDb =  FirebaseDatabase.getInstance().getReference().child("song_list")
                .child(uid).child("song_data").child(voiceType).child("score");


        mDb.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreValue = dataSnapshot.getValue().toString();
//                postDataToSQLite();
                score.setText("Score: " + scoreValue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initObject(){


        listScore = new ArrayList<>();
        scoreAdapter = new ScoreListAdapter(listScore,this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_scorelist.setLayoutManager(layoutManager);
        rv_scorelist.setHasFixedSize(true);
        rv_scorelist.setAdapter(scoreAdapter);
        databaseHelper = new DatabaseHelper(context);

        getDataFromSQLite();
    }

    //deleted soon
//    public void postDataToSQLite(){
//        i++;
//        song.setSongId(i);
//        song.setmTitle(songTitle);
//        song.setVoiceType(voiceType);
//        song.setScore(scoreValue);
//
//        databaseHelper.addTodo(song);
//    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listScore.clear();
                listScore.addAll(databaseHelper.getAllScore());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                scoreAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
