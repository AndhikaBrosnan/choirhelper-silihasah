package choirhelper.silihasah.org.singing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import choirhelper.silihasah.org.R;

public class SingActivity extends AppCompatActivity {

    private TextView freq;
    private DatabaseReference mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        freq = (TextView)findViewById(R.id.tv_freq);

        mDb = FirebaseDatabase.getInstance().getReference().child("frequency");

        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                freq.setText(dataSnapshot.getValue().toString() +" Hz");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
