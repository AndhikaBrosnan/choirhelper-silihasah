package choirhelper.silihasah.org.ui.pitch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Pitch;

public class PitchSongAdapter extends RecyclerView.Adapter<PitchSongAdapter.MyHolder> {

    private final Context mContext;
    ArrayList<Pitch> mData;

    public PitchSongAdapter(Context mContext, DatabaseReference mDb, final ArrayList<Pitch> mData) {
        this.mContext = mContext;
        this.mData = mData;
        notifyDataSetChanged();
//
//        mDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                mData.add(dataSnapshot.getValue(Pitch.class));
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tonepersec_list_item,parent,false);

        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView frequency;
        TextView secs;
        TextView pitch;

        public MyHolder(View itemView) {
            super(itemView);

            frequency = (TextView)itemView.findViewById(R.id.tv_freq_pitch);
            secs = (TextView)itemView.findViewById(R.id.tv_sec_pitch);
            pitch = (TextView)itemView.findViewById(R.id.tv_tone_pitch);
        }
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Pitch currentPitch = mData.get(position);
        holder.secs.setText(currentPitch.getSecs());
        holder.frequency.setText(String.valueOf(currentPitch.getFrequency()));
        holder.pitch.setText(currentPitch.getPitch());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
