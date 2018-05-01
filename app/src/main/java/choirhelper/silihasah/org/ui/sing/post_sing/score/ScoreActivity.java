package choirhelper.silihasah.org.ui.sing.post_sing.score;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import choirhelper.silihasah.org.R;

public class ScoreActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private DatabaseReference childDb;
    private TextView score;
    private String scorevalue;
    private String voiceType;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (TextView)findViewById(R.id.tv_score);

        voiceType = getIntent().getStringExtra("voicetype");
        uid = getIntent().getStringExtra("uid");

        mDb =  FirebaseDatabase.getInstance().getReference().child("song_list")
                .child(uid).child("song_data").child(voiceType).child("score");

        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scorevalue = dataSnapshot.getValue().toString();
                score.setText("Score: " + scorevalue );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
