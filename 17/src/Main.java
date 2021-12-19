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

    private static void findValidYs(Target target, Map<Integer, List<Integer>> hits) {
        // The maximum y is whatever positive value causes the probe to immediately pass into the window at as low a
        // point as possible upon dropping back to zero. The minimum y is whatever value causes the probe to pass
        // through the bottom of the target on the first frame. Between these, inclusive, will be all the valid Y
        // values.
        //
        // for positive Y values, we will first of all calculate how many frames until the probe is at zero and will
        // start to drop from the next frame -- this is 2Y + 1. hold onto this.
        //
        // then, we need to figure out -- at this initial velocity, how many frames until it is at or lower than the
        // target's minimum Y position?
        //
        // from here, we will keep deducting "a frame's worth of drop" until the probe is above the frame.
        //
        // for negative Y values, we will do the same as the latter part of the above. in fact -- we can just save the
        // above answers both with and without the flight frames! (i.e., multiplying the Y by -1 and then deducting 1
        // from it. so e.g., if 9 is a valid value, and takes flight+drop frames, then -10 is also valid, and it takes
        // drop frames.


        // the frame at which we will start at in each iteration -- wherever we are just above (or in) the target.
        int initFrame = inverseTriangleNumber(-1 * target.getYMax());
        // the position this starting frame will put us at.
        int initLandsAt = triangleNumber(initFrame) * -1;
        for (int velocity = -1; velocity >= target.getYMin(); velocity--) {
            boolean pastTarget = false;
            int frame = initFrame;
            int landsAt = initLandsAt;
            int framesCheckedBeforePassedTargetStart = 0;
            while (!pastTarget) {
                System.out.println("velocity is " + velocity);
                // we haven't passed the target entirely yet
                if (landsAt <= target.getYMax()) {
                    System.out.println("passed the start");
                    // we are below the start of the target
                    if (target.isYOnTarget(landsAt)) {
                        System.out.println("on target");
                        // we are within the target
                        List<Integer> negHitFrames = hits.computeIfAbsent(velocity, k -> new ArrayList<>());
                        negHitFrames.add(frame);
                        int posVelocity = (velocity * -1) - 1;
                        int flightFrames = posVelocity * 2 + 1;
                        List<Integer> positiveHitFrames = hits.computeIfAbsent(posVelocity, k -> new ArrayList<>());
                        positiveHitFrames.add(frame + flightFrames);
                    } else {
                        System.out.println("and passed the end.");
                        pastTarget = true;
                    }
                } else {
                    if (framesCheckedBeforePassedTargetStart > 0) {
                        initFrame = frame;
                        initLandsAt = landsAt;
                    }
                    framesCheckedBeforePassedTargetStart++;
                }
                // update where we're going to land -- the frame indicates how much bigger than the initial velocity
                // this increment is going to be
                landsAt -= (frame + (velocity * -1));
                frame++;
            }
            // we'll start one frame earlier each loop (barring any additional adjustment we made), and the position
            // this will have us start at is determined by removing the current velocity from the previous calculated
            // position, since we will skip this element of the triangle number.
            if (initFrame > 0) {
                initFrame--;
                initLandsAt -= velocity;
            }
        }
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
