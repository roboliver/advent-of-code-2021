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

        return 0;
    }

    public static int arcCount(BufferedReader reader) throws IOException {
        int count = 0;
        Target target = target(reader);
        Map<Integer, List<Integer>> xVelsToHitFramesMoving = new HashMap<>();
        Set<Integer> xVelsThatStopInTarget = new HashSet<>();
        findValidXs(target, xVelsToHitFramesMoving, xVelsThatStopInTarget);
        Map<Integer, List<Integer>> yVelsToHitFrames = new HashMap<>();
        findValidYs(target, yVelsToHitFrames);
        Map<Integer, List<Integer>> xHitFramesMovingToVels = reverseMap(xVelsToHitFramesMoving);
        System.out.println("x hit frames to velocities:");
        for (Map.Entry<Integer, List<Integer>> entry : xHitFramesMovingToVels.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }
        System.out.println("x velocities that stop mid-target:");
        for (Integer frame : xVelsThatStopInTarget) {
            System.out.println(frame);
        }
        Map<Integer, List<Integer>> yHitFramesToVels = reverseMap(yVelsToHitFrames);
        System.out.println("y hit frames to velocities:");
        for (Map.Entry<Integer, List<Integer>> entry : yHitFramesToVels.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }

        Map<Integer, HashSet<Integer>> validXYVelocities = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> xHitFrameMovingToVels : xHitFramesMovingToVels.entrySet()) {
            List<Integer> yVelsThatHitInTheXFrame = yHitFramesToVels.get(xHitFrameMovingToVels.getKey());
            if (yVelsThatHitInTheXFrame != null) {
                for (Integer x : xHitFrameMovingToVels.getValue()) {
                    for (Integer y : yVelsThatHitInTheXFrame) {
                        Set<Integer> ysAtThisX = validXYVelocities.computeIfAbsent(x, k -> new HashSet<>());
                        ysAtThisX.add(y);
                    }
                }
            }
        }
        for (Integer xVelStopsInTarget : xVelsThatStopInTarget) {
            for (Map.Entry<Integer, List<Integer>> yHitFrameVels : yHitFramesToVels.entrySet()) {
                if (yHitFrameVels.getKey() >= xVelStopsInTarget) {
                    for (Integer y : yHitFrameVels.getValue()) {
                        Set<Integer> ysAtThisX = validXYVelocities.computeIfAbsent(xVelStopsInTarget, k -> new HashSet<>());
                        ysAtThisX.add(y);
                    }
                }
            }
        }
        for (HashSet<Integer> values : validXYVelocities.values()) {
            count += values.size();
        }
        return count;
    }

    private static Map<Integer, List<Integer>> reverseMap(Map<Integer, List<Integer>> unreversed) {
        Map<Integer, List<Integer>> reversed = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> unreversedEntry : unreversed.entrySet()) {
            for (Integer valueEntry : unreversedEntry.getValue()) {
                List<Integer> entryEntry = reversed.computeIfAbsent(valueEntry, k -> new ArrayList<>());
                entryEntry.add(unreversedEntry.getKey());
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
        // For each negative velocity, we will start at the top of the target, and increment the frame we're considering
        // until the probe has fallen past the bottom of the target. For each hit, we will also calculate and add the
        // corresponding positive velocity.
        //
        // the frame at which we will start at in each iteration -- wherever we are just above (or in) the target
        int frameInit = inverseTriangleNumber(-1 * target.getYMax());
        for (int velocity = -1; velocity >= target.getYMin(); velocity--) {
            int frame = frameInit;
            // our initial landing point for this velocity - it's whatever distance we'd travel to this frame if the
            // velocity were 1, plus one at each frame for however much faster than one this velocity is
            int landsAt = triangleNumber(frame) * -1 + (velocity + 1) * frame;
            boolean foundHit = false;
            while (landsAt >= target.getYMin()) {
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
                        if (frame - frameInit > 1) {
                            // our first (highest) hit -- and we checked at least two frames that were short of this one
                            // on our way here, so push the init frame forwards to avoid needlessly checking extra empty
                            // frames for the next velocity
                            frameInit = frame - 1;
                        }
                        foundHit = true;
                    }
                }
                // update where we're going to land -- the frame indicates how much bigger than the initial velocity
                // this increment is going to be
                landsAt -= (frame + (velocity * -1));
                frame++;
            }
            // we'll start one frame earlier for each velocity (barring any additional adjustment we made).
            if (frameInit > 0) {
                frameInit--;
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
