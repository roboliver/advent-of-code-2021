import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Region {
    private static final int MAX_VOLUMES_BEFORE_SUBDIVIDE = 500;
    private final Volume regionVolume;
    private List<Volume> volumes = new ArrayList<>();
    private List<Region> subRegions = null;

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
        if (!isSubdivided()) {
            executeStepOnWhole(volumeAdd);
        } else {
            executeStepOnSubdivided(volumeAdd);
        }
    }

    private void executeStepOnWhole(Volume volumeAdd) {
        Volume volumeAddInRegion = volumeAdd.intersection(regionVolume);
        List<Volume> volumeAddParts = (volumeAddInRegion == Volume.NULL)
                ? Collections.emptyList() : List.of(volumeAddInRegion);
        for (Volume volume : volumes) {
            List<Volume> volumeAddPartsNew = new ArrayList<>();
            for (Volume volumeAddPart : volumeAddParts) {
                volumeAddPartsNew.addAll(volumeAddPart.subtract(volume));
            }
            volumeAddParts = volumeAddPartsNew;
        }
         volumes.addAll(volumeAddParts);
        subdivideIfNeeded();
    }

    private void executeStepOnSubdivided(Volume volumeAdd) {
        for (Region subRegion : subRegions) {
            subRegion.executeStepOn(volumeAdd);
        }
    }

    private void executeStepOff(Volume volumeRemove) {
        if (!isSubdivided()) {
            executeStepOffWhole(volumeRemove);
        } else {
            executeStepOffSubdivided(volumeRemove);
        }
    }

    private void executeStepOffWhole(Volume volumeRemove) {
        List<Volume> volumeRemainingParts = new ArrayList<>();
        for (Volume volume : volumes) {
            volumeRemainingParts.addAll(volume.subtract(volumeRemove));
        }
        volumes = volumeRemainingParts;
        subdivideIfNeeded();
    }

    private void executeStepOffSubdivided(Volume volumeRemove) {
        for (Region subRegion : subRegions) {
            subRegion.executeStepOff(volumeRemove);
        }
    }

    private void subdivideIfNeeded() {
        if (!isSubdivided() && volumes.size() > MAX_VOLUMES_BEFORE_SUBDIVIDE) {
            List<Volume> subRegionVolumes = regionVolume.subdivide();
            List<Region> subRegions = new ArrayList<>();
            for (Volume subRegionVolume : subRegionVolumes) {
                subRegions.add(new Region(subRegionVolume));
            }
            this.subRegions = Collections.unmodifiableList(subRegions);
            for (Volume volume : volumes) {
                for (Region subRegion : subRegions) {
                    subRegion.addVolume(subRegion.regionVolume.intersection(volume));
                }
            }
            volumes = null;
        }
    }

    private void addVolume(Volume volume) {
        if (volume != Volume.NULL) {
            volumes.add(volume);
        }
    }

    private boolean isSubdivided() {
        return subRegions != null;
    }

    public long cubesOn() {
        if (!isSubdivided()) {
            return volumes.stream()
                    .map(Volume::size)
                    .reduce(0L, Long::sum);
        } else {
            return subRegions.stream()
                    .map(Region::cubesOn)
                    .reduce(0L, Long::sum);
        }
    }
}
