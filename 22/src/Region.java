import java.util.ArrayList;
import java.util.List;

public class Region {
    private Volume regionVolume;
    private List<Volume> volumes = new ArrayList<>();

    public Region(Volume regionVolume) {
        this.regionVolume = regionVolume;
    }

    public void executeStep(RebootStep step) {
        if (step.isOnNotOff()) {
            executeStepOn(step.volume());
        } else {
            executeStepOff(step.volume());
        }
    }

    private void executeStepOn(Volume volumeAdd) {
        Volume volumeAddInRegion = volumeAdd.intersection(regionVolume);
        // null check not necessary, it should still work without -- remove after optimising
        if (volumeAddInRegion != Volume.NULL) {
            List<Volume> volumeAddParts = List.of(volumeAddInRegion);
            for (Volume volume : volumes) {
                List<Volume> volumeAddPartsNew = new ArrayList<>();
                for (Volume volumeAddPart : volumeAddParts) {
                    volumeAddPartsNew.addAll(volumeAddPart.subtract(volume));
                }
                volumeAddParts = volumeAddPartsNew;
            }
            volumes.addAll(volumeAddParts);
        }
    }

    private void executeStepOff(Volume volumeRemove) {
        Volume volumeRemoveInRegion = volumeRemove.intersection(regionVolume);
        // see above re null check
        if (volumeRemoveInRegion != Volume.NULL) {
            List<Volume> volumeRemainingParts = new ArrayList<>();
            for (Volume volume : volumes) {
                volumeRemainingParts.addAll(volume.subtract(volumeRemove));
            }
            volumes = volumeRemainingParts;
        }
    }

    public long cubesOn() {
        return volumes.stream()
                .map(Volume::size)
                .reduce(0L, Long::sum);
    }
}
