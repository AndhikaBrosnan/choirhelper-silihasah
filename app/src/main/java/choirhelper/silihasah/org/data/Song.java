package choirhelper.silihasah.org.data;

public class Song {
    private String mTitle;
    private String mArranger;
    private String mSongURL;

    private boolean isSopran;
    private boolean isAlto;
    private boolean isTenor;
    private boolean isBass;

    private String score;
    private int songId;

    private String voiceType;

    public Song(String mTitle, String mArranger, String mSongURL, boolean isSopran, boolean isAlto, boolean isTenor, boolean isBass) {
        this.mTitle = mTitle;
        this.mArranger = mArranger;
        this.mSongURL = mSongURL;
        this.isSopran = isSopran;
        this.isAlto = isAlto;
        this.isTenor = isTenor;
        this.isBass = isBass;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setSopran(boolean sopran) {
        isSopran = sopran;
    }

    public void setAlto(boolean alto) {
        isAlto = alto;
    }

    public void setTenor(boolean tenor) {
        isTenor = tenor;
    }

    public void setBass(boolean bass) {
        isBass = bass;
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

    public boolean isSopran() {
        return isSopran;
    }

    public boolean isAlto() {
        return isAlto;
    }

    public boolean isTenor() {
        return isTenor;
    }

    public boolean isBass() {
        return isBass;
    }

    public void setVoiceType(String voiceType) {
        this.voiceType = voiceType;
    }

    public String getVoiceType() {
        return voiceType;
    }

    public String getJns_Suara() {
        if(isSopran == true && isAlto == true &&isTenor == true &&  isBass == true){
            return "SATB";
        }else if(isTenor == true && isSopran == true && isAlto == true){
            return "SAT";
        }else if(isBass== true && isSopran == true && isAlto == true){
            return "SAB";
        }else if(isBass== true && isSopran == true && isTenor == true){
            return "STB";
        }else if(isBass== true && isAlto == true && isTenor == true){
            return "ATB";
        }else if(isSopran == true && isAlto == true){
            return "SA";
        }else if(isSopran == true && isTenor == true){
            return "ST";
        }else if(isSopran == true && isBass == true){
            return "SB";
        }else if(isAlto == true && isTenor == true){
            return "AT";
        }else if(isAlto == true && isBass == true){
            return "AB";
        }else if(isTenor == true && isBass == true){
            return "TB";
        }else if(isSopran == true){
            return "S";
        }else if(isAlto == true){
            return "A";
        }else if(isTenor == true){
            return "T";
        }else if(isBass == true){
            return "B";
        }else
            return "Tidak Ada";
    }
}
