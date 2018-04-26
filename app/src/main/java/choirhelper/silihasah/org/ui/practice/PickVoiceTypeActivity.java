package choirhelper.silihasah.org.ui.practice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.ui.pitch.PitchSongActivity;
import choirhelper.silihasah.org.ui.sing.SingSopranActivity;
import choirhelper.silihasah.org.ui.sing.singpractice.ListennSingActivity;

public class PickVoiceTypeActivity extends AppCompatActivity {

    int FILL_DATA_CODE = 0;

    private Button sopran;
    private Button alto;
    private Button tenor;
    private Button bass;
    private boolean isSopran;
    private boolean isAlto;
    private boolean isTenor;
    private boolean isBass;
    private String songTitle;
    private String uid;
    private DatabaseReference mDb;
    Context context = this;
    private View inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_voice_type);

        initView();
        initOnClicks();
        setTitle(songTitle);

        mDb = FirebaseDatabase.getInstance().getReference().child("song_list").child(uid).child("song_data");
        mDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Kalo datanya ada, tanya, mau nyanyi atau masukin data baru?
                if (dataSnapshot.child("sopran").exists()){
                    sopran.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    FILL_DATA_CODE = 1;
                }

                if(dataSnapshot.child("alto").exists()){
                    alto.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    FILL_DATA_CODE = 1;
                }

                if(dataSnapshot.child("tenor").exists()){
                    tenor.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    FILL_DATA_CODE = 1;
                }

                if(dataSnapshot.child("bass").exists()){
                    bass.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    FILL_DATA_CODE = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initOnClicks(){
        final Bundle bundle = new Bundle();
        bundle.putString("title",songTitle);
        bundle.putString("uid",uid);

        sopran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILL_DATA_CODE==0){
                    Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                    bundle.putString("voicetype","sopran");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                //alert tanya yg Record song Data
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setView(inflater)
                        .setTitle(R.string.recordsongdata)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Record Ulang
                                Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                                bundle.putString("voicetype","sopran");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Ga record ulang, langsung nyanyi.
                                Intent intent = new Intent(getApplicationContext(), SingSopranActivity.class);
                                bundle.putString("voicetype","sopran");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                alert.create().show();

            }
        });

        alto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FILL_DATA_CODE==0){
                    Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                    bundle.putString("voicetype","alto");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setView(inflater)
                        .setTitle(R.string.recordsongdata)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Record Ulang
                                Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                                bundle.putString("voicetype","alto");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Ga record ulang, langsung nyanyi.
                                Intent intent = new Intent(getApplicationContext(), SingSopranActivity.class);
                                bundle.putString("voicetype","alto");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                alert.create().show();
            }
        });

        tenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FILL_DATA_CODE==0){
                    Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                    bundle.putString("voicetype","tenor");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setView(inflater)
                        .setTitle(R.string.recordsongdata)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Record Ulang
                                Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                                bundle.putString("voicetype","tenor");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Ga record ulang, langsung nyanyi.
                                Intent intent = new Intent(getApplicationContext(), SingSopranActivity.class);
                                bundle.putString("voicetype","tenor");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                alert.create().show();
            }
        });

        bass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FILL_DATA_CODE==0){
                    Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                    bundle.putString("voicetype","bass");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setView(inflater)
                        .setTitle(R.string.recordsongdata)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Record Ulang
                                Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                                bundle.putString("voicetype","bass");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Ga record ulang, langsung nyanyi.
                                Intent intent = new Intent(getApplicationContext(), SingSopranActivity.class);
                                bundle.putString("voicetype","bass");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                alert.create().show();
            }
        });
    }


    private void initView(){
        sopran = (Button)findViewById(R.id.b_sopran);
        alto = (Button)findViewById(R.id.b_alto);
        tenor = (Button)findViewById(R.id.b_tenor);
        bass = (Button)findViewById(R.id.b_bass);

        sopran.setVisibility(View.GONE);
        alto.setVisibility(View.GONE);
        tenor.setVisibility(View.GONE);
        bass.setVisibility(View.GONE);

        songTitle = getIntent().getStringExtra("title");
        isSopran = getIntent().getBooleanExtra("sopran",false);
        isAlto = getIntent().getBooleanExtra("alto",false);
        isTenor = getIntent().getBooleanExtra("tenor",false);
        isBass = getIntent().getBooleanExtra("bass",false);
        uid = getIntent().getStringExtra("uid");

        inflater = getLayoutInflater().inflate(R.layout.song_data_editor,null,false);

        //if sopran shows sopran button, alto shows alto and so on
        if(isSopran == true && isAlto == true && isTenor == true &&  isBass == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.VISIBLE);

        }else if(isTenor == true && isSopran == true && isAlto == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.GONE);
        }else if(isBass== true && isSopran == true && isAlto == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.VISIBLE);
        }else if(isBass== true && isSopran == true && isTenor == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.VISIBLE);
        }else if(isBass== true && isAlto == true && isTenor == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.VISIBLE);
        }else if(isSopran == true && isAlto == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.GONE);
        }else if(isSopran == true && isTenor == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.GONE);
        }else if(isSopran == true && isBass == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.VISIBLE);
        }else if(isAlto == true && isTenor == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.GONE);
        }else if(isAlto == true && isBass == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.VISIBLE);
        }else if(isTenor == true && isBass == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.VISIBLE);
        }else if(isSopran == true){
            sopran.setVisibility(View.VISIBLE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.GONE);
        }else if(isAlto == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.VISIBLE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.GONE);
        }else if(isTenor == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.VISIBLE);
            bass.setVisibility(View.GONE);
        }else if(isBass == true){
            sopran.setVisibility(View.GONE);
            alto.setVisibility(View.GONE);
            tenor.setVisibility(View.GONE);
            bass.setVisibility(View.VISIBLE);
        }else
            Toast.makeText(this, "Anda tidak menginput jenis suara", Toast.LENGTH_SHORT).show();
    }

}
