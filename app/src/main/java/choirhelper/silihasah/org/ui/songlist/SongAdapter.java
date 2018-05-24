package choirhelper.silihasah.org.ui.songlist;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder_> implements MediaPlayer.OnPreparedListener {

    private final Context mContext;
    private final onClickHandler mClickHandler;

    private ArrayList<Song> mData;

    //variable tambahan
    private final View emptyView;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private StorageReference mStorage;

    @Override
    public ViewHolder_ onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.song_list_item,parent,false);


        return new ViewHolder_(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder_ holder, final int position) {
        final Song currentSong = mData.get(position);
        holder.mTitle.setText(currentSong.getmTitle());
        holder.mArranger.setText(currentSong.getmArranger());
        holder.mPembagianSuara.setText(currentSong.getJns_Suara());

        holder.mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(currentSong.getmSongURL());
                mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url = uri.toString();
                        try {

                            mediaPlayer.setDataSource(url);
                            mediaPlayer.setOnPreparedListener(SongAdapter.this);
                            mediaPlayer.prepareAsync();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "GAGAL PLAY", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //tambahan
        holder.itemView.setSelected(mSelectedId.contains(mDataId.get(position)));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void setEmptyView(){ //tambahan
        if(mData.isEmpty())emptyView.setVisibility(View.VISIBLE);
        else emptyView.setVisibility(View.GONE);
    }

    SongAdapter(Context context, DatabaseReference mDb, View empty, onClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
        mData = new ArrayList<>();

        //tambahan
        mDataId = new ArrayList<>();
        mSelectedId = new ArrayList<>();
        emptyView = empty;

        mDb.addChildEventListener(new ChildEventListener() { //2 tambahan disetiap child
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mData.add(dataSnapshot.getValue(Song.class));
                mDataId.add(dataSnapshot.getKey());
                notifyDataSetChanged();
                setEmptyView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int pos = mDataId.indexOf(dataSnapshot.getKey());
                mData.set(pos, dataSnapshot.getValue(Song.class));
                notifyDataSetChanged();
                setEmptyView();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int pos = mDataId.indexOf(dataSnapshot.getKey());
                mDataId.remove(pos);
                mData.remove(pos);
                notifyDataSetChanged();
                setEmptyView();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    interface onClickHandler{
        void onClick(String song_id, Song currentSong);
        boolean onLongClick(String song_id);
    }

    class ViewHolder_ extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        final TextView mTitle;
        final TextView mArranger;
        final TextView mPembagianSuara;
        final ImageView mPlayButton;

        ViewHolder_ (View view){
            super(view);
            mTitle = (TextView)view.findViewById(R.id.song_title);
            mArranger = (TextView)view.findViewById(R.id.song_arranger);
            mPembagianSuara = (TextView)view.findViewById(R.id.song_voicetypes);
            mPlayButton = (ImageView)view.findViewById(R.id.playbutton);
            view.setOnClickListener(this);
            //tambahan
            view.setOnLongClickListener(this);
            view.setLongClickable(true);
        }
        @Override
        public void onClick(View view) {
            //tambahin parameter mDataId dibawah ini
            mClickHandler.onClick(mDataId.get(getAdapterPosition()), mData.get(getAdapterPosition()));

        }

        @Override
        public boolean onLongClick(View view) {
            return mClickHandler.onLongClick(mDataId.get(getAdapterPosition())); //tambahin
        }
    }

    //tambahin class baru
    public void toggleSelection(String id){
        if(mSelectedId.contains(id)) mSelectedId.remove(id);
        else mSelectedId.add(id);
        notifyDataSetChanged();
    }

    //tambahin class baru
    public int selectionCount(){
        return mSelectedId.size();
    }

    //tambahin class baru
    public  void resetSelection(){
        mSelectedId = new ArrayList<>();
        notifyDataSetChanged();
    }

    public Song getSong(String song_id){
        return mData.get(mDataId.indexOf(song_id));
    }

    public ArrayList<String> getmSelectedId(){
        return mSelectedId;
    }

}
