package choirhelper.silihasah.org.data;

public class Pitch {
    private float frequency;
    private String pitch;
    private String secs;

    public Pitch(float frequency, String pitch, String secs) {
        this.frequency = frequency;
        this.pitch = pitch;
        this.secs = secs;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getSecs() {
        return secs;
    }

    public void setSecs(String secs) {
        this.secs = secs;
    }
}
