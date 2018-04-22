package choirhelper.silihasah.org.ui.practice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.ui.pitch.PitchSongActivity;
import choirhelper.silihasah.org.ui.sing.singpractice.ListennSingActivity;

public class PickVoiceTypeActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_voice_type);

        initView();
        initOnClicks();

        setTitle(songTitle);
    }

    private void initOnClicks(){

        final Bundle bundle = new Bundle();
        bundle.putString("title",songTitle);
        bundle.putString("uid",uid);

        sopran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PitchSongActivity.class);
                bundle.putString("voicetype","sopran");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        alto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PitchSongActivity.class);
                bundle.putString("voicetype","alto");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PitchSongActivity.class);
                bundle.putString("voicetype","tenor");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        bass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PitchSongActivity.class);
                bundle.putString("voicetype","bass");
                intent.putExtras(bundle);
                startActivity(intent);
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

        //if sopran shows sopran button, alto shows alto and so on
        if(isSopran == true && isAlto == true &&isTenor == true &&  isBass == true){
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

    public void voiceOnClicks(View view) {

//        View v = getLayoutInflater().inflate(R.layout.tone_editor,null,false);
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
//                .setView(v)
//                .setTitle("Pilih nada untuk dilatih")
//                .setIcon(R.mipmap.ic_launcher)
//                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(getApplicationContext(),ListennSingActivity.class);
//                        startActivity(intent);
//                    }
//                })
//                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        dialog.create().show();

    }
}
