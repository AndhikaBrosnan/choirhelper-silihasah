package choirhelper.silihasah.org.ui.sing.post_sing.score;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Song;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.MyHolder> {

    private ArrayList<Song> songList;
    private Context context;

    public ScoreListAdapter(ArrayList<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ScoreListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_list_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreListAdapter.MyHolder holder, int position) {
        holder.title.setText(songList.get(position).getmTitle());
        holder.voiceType.setText(songList.get(position).getVoiceType());
        holder.score.setText(songList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView voiceType;
        TextView score;

        public MyHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tv_songtitle);
            voiceType = (TextView)itemView.findViewById(R.id.tv_songvoicetype);
            score = (TextView)itemView.findViewById(R.id.tv_songscore);
        }
    }
}
