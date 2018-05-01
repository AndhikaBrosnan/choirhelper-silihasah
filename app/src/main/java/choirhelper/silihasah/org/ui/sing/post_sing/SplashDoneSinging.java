package choirhelper.silihasah.org.ui.sing.post_sing;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.ui.sing.post_sing.score.ScoreActivity;

public class SplashDoneSinging extends AppCompatActivity {

    private String voiceType;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //menghilangkan ActionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_done_singing);

        voiceType = getIntent().getStringExtra("voicetype");
        uid = getIntent().getStringExtra("uid");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(),ScoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("voicetype",voiceType);
                bundle.putString("uid", uid);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, 2000L); //2000 L = 2 detik
    }
}
