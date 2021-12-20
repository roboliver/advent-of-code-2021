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
        Map<Integer, List<Integer>> xHitsMoving = new HashMap<>();
        Set<Integer> xHitsStopped = new HashSet<>();
        findValidXs(target, xHitsMoving, xHitsStopped);
        Map<Integer, List<Integer>> yHits = new HashMap<>();
        findValidYs(target, yHits);
        Map<Integer, List<Integer>> xFramesMoving = reverseMap(xHitsMoving);
        Map<Integer, List<Integer>> yFrames = reverseMap(yHits);
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
        // We will try each possible valid velocity -- the range spans from the value that causes the probe to stop just
        // inside the target, to that value causes it to pass through the end of the target in the first frame. For each
        // velocity, we will start at the end of the target, and decrement the frame we're considering until we have
        // passed through the front of the target.
        //
        // the number of end frames we will skip in our backwards iteration
        int framesSkip = 0;
        for (int velocity = inverseTriangleNumber(target.getXMin()); velocity <= target.getXMax(); velocity++) {
            int frame = velocity - framesSkip;
            // find the first (furthest) position we're interested in for this velocity -- it's wherever the probe would
            // stop if allowed to run until it does, minus however far it would travel in the frames we're skipping
            int landsAt = triangleNumber(velocity) - triangleNumber(velocity - frame);
            boolean foundHit = false;
            while (landsAt >= target.getXMin()) {
                if (target.isXOnTarget(landsAt)) {
                    if (frame == velocity) {
                        // this is the frame when the probe stops entirely -- so for this velocity the Y frame doesn't
                        // have to exactly match, it only needs to be at least this high, so add this X velocity to the
                        // separate results set used to track these
                        hitsStopped.add(velocity);
                    } else {
                        // save the frame we hit the target at for this velocity
                        List<Integer> hitFrames = hitsMoving.computeIfAbsent(velocity, k -> new ArrayList<>());
                        hitFrames.add(frame);
                    }
                    if (!foundHit) {
                        // our first (furthest) hit -- since the next velocity will be higher, there's no way we will
                        // possibly need to consider any later frames than this for it
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
        // We will try each possible valid velocity -- the range of negative velocities spans from -1 (just barely
        // faster than dropping the probe) to whatever value causes it to pass through the bottom of the target in the
        // first frame, and each of these has a corresponding positive velocity that will cause it to track the same
        // path towards and past the target, but delayed by some number of frames to account for its upward flight.
        //
        // For each negative velocity, we will start at the bottom of the target, and decrement the frame we're
        // considering until the probe has risen above the top of the target. For each hit, we will also calculate and
        // include the corresponding positive velocity.
        //
        // the frame we will start at in each iteration -- we initialise this to the last frame for velocity -1 before
        // the probe has passed the target
        int frameInit = inverseTriangleNumber(-1 * target.getYMin());
        for (int velocity = -1; velocity >= target.getYMin(); velocity--) {
            int frame = frameInit;
            // our initial landing point for this velocity -- it's whatever distance we'd travel to this frame if the
            // velocity were 1, plus one at each frame for however much faster than one this velocity is
            int landsAt = triangleNumber(frame) * -1 + (velocity + 1) * frame;
            boolean foundHit = false;
            while (landsAt <= target.getYMax()) {
                if (target.isYOnTarget(landsAt)) {
                    List<Integer> negHitFrames = hits.computeIfAbsent(velocity, k -> new ArrayList<>());
                    negHitFrames.add(frame);
                    // we need to add the positive velocity that goes along with this negative velocity -- i.e., the
                    // velocity we could fire the probe upwards at, such that when it gets back to the origin it's
                    // // moving at the negative velocity
                    int posVelocity = (velocity * -1) - 1;
                    int flightFrames = posVelocity * 2 + 1;
                    List<Integer> positiveHitFrames = hits.computeIfAbsent(posVelocity, k -> new ArrayList<>());
                    positiveHitFrames.add(frame + flightFrames);
                    if (!foundHit) {
                        // our first hit -- since the next velocity will be higher, there's no way we will possibly need
                        // to consider any later frames than this for it
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
