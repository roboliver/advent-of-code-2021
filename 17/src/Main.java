import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("highest arc: " + highestArc(reader));
        }
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("number of valid arcs: " + arcCount(reader));
        }
    }

    public static int highestArc(BufferedReader reader) throws IOException {
        int maxY = validVelocities(target(reader)).stream()
                .map(Point::y)
                .reduce(Integer.MIN_VALUE, Integer::max);
        return triangleNumber(maxY);
    }

    public static int arcCount(BufferedReader reader) throws IOException {
        return validVelocities(target(reader)).size();
    }

    private static Set<Point> validVelocities(Target target) {
        // get maps from X and Y velocities to the frames at which they hit the target, and also separately, the X
        // velocities that stop within the target, which will happen after the same number of frames as the velocity
        Map<Integer, List<Integer>> xHitsMoving = new HashMap<>();
        Set<Integer> xHitsStopped = new HashSet<>();
        findValidXs(target, xHitsMoving, xHitsStopped);
        Map<Integer, List<Integer>> yHits = new HashMap<>();
        findValidYs(target, yHits);
        // reverse the maps, so they map hit frames to velocities that hit the target at that frame
        Map<Integer, List<Integer>> xFramesMoving = reverseMap(xHitsMoving);
        Map<Integer, List<Integer>> yFrames = reverseMap(yHits);
        // find frame matches and add the velocity pairs
        Set<Point> velocities = new HashSet<>();
        for (Map.Entry<Integer, List<Integer>> xFrame : xFramesMoving.entrySet()) {
            List<Integer> yVelocities = yFrames.get(xFrame.getKey());
            if (yVelocities != null) {
                for (Integer xVelocity : xFrame.getValue()) {
                    for (Integer yVelocity : yVelocities) {
                        velocities.add(new Point(xVelocity, yVelocity));
                    }
                }
            }
        }
        // add the pairs for the X velocities that stop inside the target -- for these, any Y velocity that takes at
        // least that many frames to hit the target is valid
        for (Integer xVelocity : xHitsStopped) {
            for (Map.Entry<Integer, List<Integer>> yFrame : yFrames.entrySet()) {
                if (yFrame.getKey() >= xVelocity) {
                    for (Integer yVelocity : yFrame.getValue()) {
                        velocities.add(new Point(xVelocity, yVelocity));
                    }
                }
            }
        }
        return velocities;
    }

    private static Map<Integer, List<Integer>> reverseMap(Map<Integer, List<Integer>> original) {
        Map<Integer, List<Integer>> reversed = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : original.entrySet()) {
            for (Integer value : entry.getValue()) {
                List<Integer> reverseValues = reversed.computeIfAbsent(value, k -> new ArrayList<>());
                reverseValues.add(entry.getKey());
            }
        }
        return reversed;
    }

    private static void findValidXs(Target target, Map<Integer, List<Integer>> hitsMoving, Set<Integer> hitsStopped) {
        // iterate backwards through the target for every viable X velocity, saving the hit frames we find
        int framesSkip = 0;
        // viable velocities are from the value that stops at the start of the target, to the value that hits the end of
        // the target in the first frame
        for (int velocity = inverseTriangleNumber(target.getXMin()); velocity <= target.getXMax(); velocity++) {
            int frame = velocity - framesSkip;
            int landsAt = triangleNumber(velocity) - triangleNumber(velocity - frame);
            boolean foundHit = false;
            while (landsAt >= target.getXMin()) {
                if (target.isXOnTarget(landsAt)) {
                    if (frame == velocity) {
                        // the probe stops moving at this point, so any Y velocity that hits on at least this X velocity
                        // frame will be valid
                        hitsStopped.add(velocity);
                    } else {
                        // the probe is still moving, so the corresponding Y velocity will need to hit in the same frame
                        List<Integer> hitFrames = hitsMoving.computeIfAbsent(velocity, k -> new ArrayList<>());
                        hitFrames.add(frame);
                    }
                    if (!foundHit) {
                        // first hit at this velocity -- since the next one will be higher, the probe will have moved
                        // further at this many frames before it stops, so we won't need to consider any later frames
                        // in future
                        framesSkip = (velocity - frame);
                        foundHit = true;
                    }
                }
                frame--;
                landsAt -= (velocity - frame);
            }
        }
    }

    private static void findValidYs(Target target, Map<Integer, List<Integer>> hits) {
        // iterate backwards through the target for every viable Y velocity, saving the hit frames we find
        int frameInit = inverseTriangleNumber(-1 * target.getYMin());
        // viable velocities are from -1 to the value that hits the end of the target in the first frame -- note also
        // that each of these negative velocities has a corresponding positive velocity too, where the probe is first
        // launched upwards and is moving at the negative velocity once it gets back to the origin
        for (int velocity = -1; velocity >= target.getYMin(); velocity--) {
            int frame = frameInit;
            int landsAt = triangleNumber(frame) * -1 + (velocity + 1) * frame;
            boolean foundHit = false;
            while (landsAt <= target.getYMax()) {
                if (target.isYOnTarget(landsAt)) {
                    // add the negative velocity
                    List<Integer> negHitFrames = hits.computeIfAbsent(velocity, k -> new ArrayList<>());
                    negHitFrames.add(frame);
                    // calculate and add the corresponding positive velocity
                    int posVelocity = (velocity * -1) - 1;
                    int flightFrames = posVelocity * 2 + 1;
                    List<Integer> positiveHitFrames = hits.computeIfAbsent(posVelocity, k -> new ArrayList<>());
                    positiveHitFrames.add(frame + flightFrames);
                    if (!foundHit) {
                        // first hit at this velocity -- since the next one will be higher, the probe will have moved
                        // further after this many frames, so we won't need to consider any later frames in future
                        frameInit = frame;
                        foundHit = true;
                    }
                }
                frame--;
                landsAt += (frame + (velocity * -1));
            }
        }
    }

    private static int triangleNumber(int n) {
        return (n * (n + 1)) / 2;
    }

    private static int inverseTriangleNumber(int t) {
        return (int) (Math.sqrt(1 + (t * 8)) - 1) / 2;
    }

    private static Target target(BufferedReader reader) throws IOException {
        String[] parts = reader.readLine().split(",");
        int xMin = Integer.parseInt(parts[0].substring(parts[0].indexOf("=") + 1, parts[0].indexOf("..")));
        int xMax = Integer.parseInt(parts[0].substring(parts[0].indexOf("..") + 2));
        int yMin = Integer.parseInt(parts[1].substring(parts[1].indexOf("=") + 1, parts[1].indexOf("..")));
        int yMax = Integer.parseInt(parts[1].substring(parts[1].indexOf("..") + 2));
        return new Target(xMin, xMax, yMin, yMax);
    }
}
