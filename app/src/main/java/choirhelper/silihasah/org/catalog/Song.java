package choirhelper.silihasah.org.catalog;

public class Song {
    private String mTitle;
    private String mArranger;
    private String mSongURL;


    public Song(String mTitle, String mArranger, String mSongURL) {
        this.mTitle = mTitle;
        this.mArranger = mArranger;
        this.mSongURL = mSongURL;
    }

    public Song(){}

    public void setmSongURL(String mSongURL) {
        this.mSongURL = mSongURL;
    }

    public String getmSongURL() {
        return mSongURL;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmArranger(String mArranger)
    {
        this.mArranger = mArranger;
    }

    public String getmArranger() {
        return mArranger;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }



}
