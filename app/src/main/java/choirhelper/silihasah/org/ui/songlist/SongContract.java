package choirhelper.silihasah.org.ui.songlist;

import java.util.List;

import choirhelper.silihasah.org.BasePresenter;
import choirhelper.silihasah.org.BaseView;
import choirhelper.silihasah.org.data.Song;

/**
 * Created by BrosnanUhYeah on 27/03/2018.
 */

public class SongContract {

    interface View extends BaseView<Presenter>{
        void showSongs(List<Song> songs);

        void showError(String msg);

        void onListClicked(Song song);

        void onDeleteClicked(Song song);
    }

    interface Presenter extends BasePresenter{
        void loadCatalog();

        void addSong();

        void editSong();

        void deleteSong();

    }


}
