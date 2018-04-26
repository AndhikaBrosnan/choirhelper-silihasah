/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package choirhelper.silihasah.org.ui.songlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import choirhelper.silihasah.org.R;
import choirhelper.silihasah.org.data.Song;
import choirhelper.silihasah.org.ui.practice.PickVoiceTypeActivity;
import choirhelper.silihasah.org.ui.sing.SingSopranActivity;

//import android.view.ActionMode;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class SongActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private StorageReference mStorage;
    private SongAdapter mAdapter;

    private static final int MP3_REQUEST = 1;

    ProgressDialog progressDialog;
    UploadTask uploadTask;
    Uri mResulUri;
    ImageView songImage;

    //tambahin variable
    private ActionMode actionMode;
    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(choirhelper.silihasah.org.R.menu.context_song_catalog,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            menu.findItem(R.id.action_edit).setVisible(mAdapter.selectionCount()==1);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_edit:
                    editSong();
                    return true;
                case R.id.action_delete:
                    deleteSong();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            mAdapter.resetSelection();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        songImage = (ImageView)findViewById(R.id.uploadmp3);

        progressDialog = new ProgressDialog(this);

        mDb = FirebaseDatabase.getInstance().getReference().child("song_list");
        mStorage = FirebaseStorage.getInstance().getReference();


        mAdapter = new SongAdapter(this, mDb, findViewById(R.id.empty_view), new SongAdapter.onClickHandler() {
            @Override
            public void onClick(String song_id, Song currentSong) {
                if(actionMode != null){
                    mAdapter.toggleSelection(song_id);
                    if(mAdapter.selectionCount()==0)actionMode.finish();
                    else actionMode.invalidate();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), PickVoiceTypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",currentSong.getmTitle());
                bundle.putBoolean("sopran",currentSong.isSopran());
                bundle.putBoolean("alto",currentSong.isAlto());
                bundle.putBoolean("tenor",currentSong.isTenor());
                bundle.putBoolean("bass",currentSong.isBass());
                bundle.putString("uid",song_id);
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(SongActivity.this, currentSong.getmTitle(),Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onLongClick(String song_id) {
                if(actionMode != null)return false;
                mAdapter.toggleSelection(song_id);
                actionMode = SongActivity.this.startSupportActionMode(callback);
                return true;
            }
        });//tambahin parameter sama hapus new sampe kurung kurawal

        rv.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSong();
            }
        });
    }

    //biar bisa add
    private void addSong() {
        View view = getLayoutInflater().inflate(R.layout.song_editor,null, false);
        final EditText title = (EditText)view.findViewById(R.id.edit_song_title);
        final EditText arranger = (EditText)view.findViewById(R.id.edit_song_arranger);

        final CheckBox cek_Sopran = (CheckBox)view.findViewById(R.id.cb_sopran);
        final CheckBox cek_Alto = (CheckBox)view.findViewById(R.id.cb_alto);
        final CheckBox cek_Tenor = (CheckBox)view.findViewById(R.id.cb_tenor);
        final CheckBox cek_Bass = (CheckBox)view.findViewById(R.id.cb_bass);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(R.string.editor_activity_title_new_song)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        progressDialog.setMessage("Harap Tunggu...");
                        progressDialog.show();

                        Uri urisong = mResulUri;
                        StorageReference storagePath = mStorage.child("Songs").child(mResulUri.getLastPathSegment());
                        uploadTask = storagePath.putFile(urisong);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_SHORT).show();
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                String key = mDb.push().getKey();
                                mDb.child(key).setValue(new Song(
                                        title.getText().toString(),
                                        arranger.getText().toString(),
                                        downloadUrl.toString(),
                                        cek_Sopran.isChecked(),
                                        cek_Alto.isChecked(),
                                        cek_Tenor.isChecked(),
                                        cek_Bass.isChecked()
                                ));
                                progressDialog.dismiss();
                                return;
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();

    }

    private void editSong(){
        View view = getLayoutInflater().inflate(R.layout.   song_editor,null, false);
        final EditText title = (EditText)view.findViewById(R.id.edit_song_title);
        final EditText arranger = (EditText)view.findViewById(R.id.edit_song_arranger);
        final CheckBox cek_Sopran = (CheckBox)view.findViewById(R.id.cb_sopran);
        final CheckBox cek_Alto = (CheckBox)view.findViewById(R.id.cb_alto);
        final CheckBox cek_Tenor = (CheckBox)view.findViewById(R.id.cb_tenor);
        final CheckBox cek_Bass = (CheckBox)view.findViewById(R.id.cb_bass);

        //copy terus tambahin dibawah ini
        final String currentSongId = mAdapter.getmSelectedId().get(0);
        Song currentSong = mAdapter.getSong(currentSongId);
        title.setText(currentSong.getmTitle());
        arranger.setText(currentSong.getmArranger());
        cek_Sopran.setChecked(currentSong.isSopran());
        cek_Alto.setChecked(currentSong.isAlto());
        cek_Tenor.setChecked(currentSong.isTenor());
        cek_Bass.setChecked(currentSong.isBass());


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(R.string.editor_activity_title_new_song)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setMessage("Harap Tunggu...");
                        progressDialog.show();

                        Uri urisong = mResulUri;
                        StorageReference storagePath = mStorage.child("Songs").child(mResulUri.getLastPathSegment());
                        uploadTask = storagePath.putFile(urisong);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_SHORT).show();
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                String key = mDb.push().getKey();
                                mDb.child(currentSongId).setValue(new Song(
                                        title.getText().toString(),
                                        arranger.getText().toString(),
                                        downloadUrl.toString(),
                                        cek_Sopran.isChecked(),
                                        cek_Alto.isChecked(),
                                        cek_Tenor.isChecked(),
                                        cek_Bass.isChecked()
                                ));
                                progressDialog.dismiss();
                                actionMode.finish();
                                return;
                            }
                        });


                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        actionMode.finish(); //tambahin finish
                    }
                });
        builder.create().show();
    }

    private void deleteSong(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_dialog_msg)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(String id:mAdapter.getmSelectedId()){
                            mDb.child(id).removeValue();
                        }
                        actionMode.finish(); //tambahin finish
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        actionMode.finish(); //tambahin finish
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sing:
                Intent intent = new Intent (getApplicationContext(),SingSopranActivity.class);
                //send data to bernyanyi activity
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadMp3(View view) {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE}, MP3_REQUEST);
        }else{

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("audio/*");
            startActivityForResult(intent,MP3_REQUEST);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MP3_REQUEST && resultCode == RESULT_OK) {
            final Uri songUri = data.getData();
            mResulUri = songUri;
            Log.d("media","onActivityResult"+songUri.toString());
        }
    }
}
