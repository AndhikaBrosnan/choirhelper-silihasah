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
package choirhelper.silihasah.org;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import android.view.ActionMode;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private SongAdapter mAdapter;

    //tambahin variable
    private ActionMode actionMode;
    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.context_catalog,menu);
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

        //TODO (9) setup RecyclerView's layout manager and adapter
        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        mDb = FirebaseDatabase.getInstance().getReference().child("song_list");
        mAdapter = new SongAdapter(this, mDb, findViewById(R.id.empty_view), new SongAdapter.onClickHandler() {
            @Override
            public void onClick(String song_id, Song currentSong) {
                if(actionMode != null){
                    mAdapter.toggleSelection(song_id);
                    if(mAdapter.selectionCount()==0)actionMode.finish();
                    else actionMode.invalidate();
                    return;
                }
                Toast.makeText(CatalogActivity.this, currentSong.getmTitle(),Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onLongClick(String song_id) {
                if(actionMode != null)return false;
                mAdapter.toggleSelection(song_id);
                actionMode = CatalogActivity.this.startSupportActionMode(callback);
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
        //TODO (10) insert dummy data to our real-time database
//        String key = mDb.push().getKey();
//        mDb.child(key).setValue(new Song("Ratatouille","Tapir"));
        View view = getLayoutInflater().inflate(R.layout.dialog_editor,null, false);
        final EditText title = (EditText)view.findViewById(R.id.edit_song_title);
        final EditText arranger = (EditText)view.findViewById(R.id.edit_song_arranger);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(R.string.editor_activity_title_new_song)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String key = mDb.push().getKey();
                        mDb.child(key).setValue(new Song(
                                title.getText().toString(),
                                arranger.getText().toString()
                        ));
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
        View view = getLayoutInflater().inflate(R.layout.dialog_editor,null, false);
        final EditText title = (EditText)view.findViewById(R.id.edit_song_title);
        final EditText arranger = (EditText)view.findViewById(R.id.edit_song_arranger);

        //copy terus tambahin dibawah ini
        final String currentSongId = mAdapter.getmSelectedId().get(0);
        Song currentSong = mAdapter.getSong(currentSongId);
        title.setText(currentSong.getmTitle());
        arranger.setText(currentSong.getmArranger());


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(R.string.editor_activity_title_new_song)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mDb.child(currentSongId).setValue(new Song(
                                title.getText().toString(),
                                arranger.getText().toString()));
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
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
}
