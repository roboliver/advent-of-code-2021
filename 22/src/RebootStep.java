public class RebootStep {
    private final boolean isOnNotOff;
    private final Volume volume;

    public RebootStep(boolean isOnNotOff, Volume volume) {
        this.isOnNotOff = isOnNotOff;
        this.volume = volume;
    }

    public boolean isOnNotOff() {
        return isOnNotOff;
    }

    public Volume volume() {
        return volume;
    }
    
}
