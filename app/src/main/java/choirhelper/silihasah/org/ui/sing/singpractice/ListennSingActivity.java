package choirhelper.silihasah.org.ui.sing.singpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import choirhelper.silihasah.org.R;

public class ListennSingActivity extends AppCompatActivity {

    private ImageView playbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_n_sing);

        playbutton = (ImageView)findViewById(R.id.iv_listentune);

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
