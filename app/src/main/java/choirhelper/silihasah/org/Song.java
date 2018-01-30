package choirhelper.silihasah.org;

public class Song {
    private String mTitle;
    private String mArranger;

    public Song(){}

    public Song(String mTitle, String mArranger) {
        this.mTitle = mTitle;
        this.mArranger = mArranger;
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
