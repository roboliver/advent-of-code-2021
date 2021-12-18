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

    private static void findValidYs(Target target, Map<Integer, List<Integer>> yVelsToHitFrames) {
        System.out.println("target is: " + target.getYMin() + ".." + target.getYMax());
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
        int startFrame = framesToReachPosition(-1 * target.getYMax());
        int startYLandsAt = (startFrame * (startFrame + 1)) / 2;
        for (int yVelocityCur = 1; yVelocityCur <= -1 * target.getYMin(); yVelocityCur++) {
            //System.out.println("velocity: " + yVelocityCur + ", starting in frame " + startFrame + ", landing at y=" + startYLandsAt);
            boolean pastStartOfTarget = false;
            boolean pastEndOfTarget = false;
            int currentFrame = startFrame;
            int currentLandsAt = startYLandsAt;
            int adding = yVelocityCur  + currentFrame;
            while (!pastEndOfTarget) {
                //System.out.println("currently landing at " + currentLandsAt + ", target is " + (target.getYMax() * -1));
                if (currentLandsAt >= target.getYMax() * -1) {
                    pastStartOfTarget = true;
                    if (currentLandsAt > target.getYMin() * -1) {
                        pastEndOfTarget = true;
                    } else {
                        //System.out.println("got a hit. vel: " + yVelocityCur +", frame: " + currentFrame + ", lands at: " + currentLandsAt);
                        int negativeVelocity = -1 * yVelocityCur;
                        int positiveVelocity = yVelocityCur - 1;
                        int negativeFrameHit = currentFrame;
                        int flightTime = positiveVelocity * 2 + 1;
                        int positiveFrameHit = negativeFrameHit + flightTime;
                        //System.out.println("if fired upwards at velocity " + positiveVelocity +", this will land at y=" + currentLandsAt + " in frame " + positiveFrameHit);
                        //System.out.println("if fired downwards at velocity " + negativeVelocity +", this will land at y=" + currentLandsAt + " in frame " + negativeFrameHit);
                        List<Integer> negatives = yVelsToHitFrames.computeIfAbsent(negativeVelocity, k -> new ArrayList<>());
                        negatives.add(negativeFrameHit);
                        List<Integer> positives = yVelsToHitFrames.computeIfAbsent(positiveVelocity, k -> new ArrayList<>());
                        positives.add(positiveFrameHit);
                    }
                } else {
                    if (adding - yVelocityCur > 0 || currentFrame == 0) {
                        //System.out.
                        // not in the zeroth or first frame, so make some updates...
                        startFrame++;
                        startYLandsAt = currentLandsAt + adding;
                    }
                }
                currentLandsAt += adding;
                currentFrame++;
                adding++;
            }
            startFrame--;
            startYLandsAt -= yVelocityCur;
        }
        System.out.println("y hits:");
        for (Map.Entry<Integer, List<Integer>> entry : yVelsToHitFrames.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }
    }

    private static int framesToReachPosition(int destination) {
        return (int) (Math.sqrt(1 + (destination * 8)) - 1) / 2; // reverse triangle formula
    }

    private static void findValidXs(Target target, Map<Integer, List<Integer>> xVelsToHitFramesMoving, Set<Integer> xVelsThatStopInTarget) {
        System.out.println("target is: " + target.getXMin() + ".." + target.getXMax());
        // The minimum x is whatever value causes the probe to stop moving inside the target's x window, as close to the
        // start as possible. The maximum x is whatever value causes the probe to pass through the end of the target on
        // the first frame. Between these, inclusive, will be all the valid X values.
        //
        // get the lowest valid X position by reversing the triangle number formula
        //
        // map from x velocities to frames at which the probe is within the target (but hasn't stopped)
        // set of x velocities that cause the probe to stop within the target (which will be after the velocity's number
        // of frames)
        int xVelocityCur = framesToReachPosition(target.getXMin());
        int minDeductions = 0;
        while (xVelocityCur <= target.getXMax()) {
            int xLandsAt = ((xVelocityCur * (xVelocityCur + 1)) / 2) - ((minDeductions * (minDeductions + 1) / 2));
            int deductions = minDeductions;
            boolean gotAHit = false;
            //System.out.println("checking velocity " + xVelocityCur);
            //System.out.println("when it stops, it will end at " + xLandsAt);
            while (xLandsAt >= target.getXMin()) {
                if (target.isXOnTarget(xLandsAt)) {
                    if (deductions == 0) {
                        xVelsThatStopInTarget.add(xVelocityCur);
                    } else {
                        List<Integer> frames = xVelsToHitFramesMoving.computeIfAbsent(xVelocityCur, k -> new ArrayList<>());
                        frames.add(xVelocityCur - deductions);
                    }
                    if (!gotAHit) {
                        minDeductions = deductions;
                        gotAHit = true;
                    }
                }
                deductions++;
                xLandsAt -= deductions;
            }
            xVelocityCur++;
        }
        System.out.println("moving:");
        for (Map.Entry<Integer, List<Integer>> entry : xVelsToHitFramesMoving.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }
        System.out.println("stopped:");
        for (Integer frame : xVelsThatStopInTarget) {
            System.out.println(frame);
        }
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
