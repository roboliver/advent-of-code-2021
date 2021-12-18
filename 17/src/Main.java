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
        Target target = target(reader);
        //findValidXs(target);
        findValidYs(target);
        return 0;
    }

    public static int arcCount(BufferedReader reader) throws IOException {
        return 0;
    }

    private static void findValidYs(Target target) {
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
        int startFrame = reverseTriangle(-1 * target.getYMax());
        int startYLandsAt = (startFrame * (startFrame + 1)) / 2;
        Map<Integer, List<Integer>> yVelsToHitFrames = new TreeMap<>();
        for (int yVelocityCur = 1; yVelocityCur <= -1 * target.getYMin(); yVelocityCur++) {
            System.out.println("velocity: " + yVelocityCur + ", starting in frame " + startFrame + ", landing at y=" + startYLandsAt);
            boolean pastStartOfTarget = false;
            boolean pastEndOfTarget = false;
            int currentFrame = startFrame;
            int currentLandsAt = startYLandsAt;
            int adding = yVelocityCur  + currentFrame;
            while (!pastEndOfTarget) {
                System.out.println("currently landing at " + currentLandsAt + ", target is " + (target.getYMax() * -1));
                if (currentLandsAt >= target.getYMax() * -1) {
                    pastStartOfTarget = true;
                    System.out.println("got a hit. vel: " + yVelocityCur +", frame: " + currentFrame + ", lands at: " + currentLandsAt);
                    if (currentLandsAt >= target.getYMin() * -1) {
                        pastEndOfTarget = true;
                    } else {
                        int negativeVelocity = -1 * yVelocityCur;
                        int positiveVelocity = yVelocityCur - 1;
                        int negativeFrameHit = currentFrame;
                        int flightTime = yVelocityCur * 2 + 1;
                        int positiveFrameHit = negativeFrameHit + flightTime;
                        List<Integer> negatives = yVelsToHitFrames.computeIfAbsent(negativeVelocity, k -> new ArrayList<>());
                        negatives.add(negativeFrameHit);
                        List<Integer> positives = yVelsToHitFrames.computeIfAbsent(positiveVelocity, k -> new ArrayList<>());
                        positives.add(positiveFrameHit);
                    }
                } else {
                    int tempCurrentLandsAt = currentLandsAt + adding;
                    int tempCurrentFrame = currentFrame + 1;
                    int tempAdding = adding + 1;
                    if (tempAdding - yVelocityCur > 1 || tempCurrentFrame == 1) {
                        //System.out.
                        // not in the zeroth or first frame, so make some updates...
                        startFrame++;
                        startYLandsAt = tempCurrentLandsAt;
                    }
                }
                currentLandsAt += adding;
                currentFrame++;
                adding++;
            }
            startFrame--;
            startYLandsAt -= yVelocityCur;
        }
        //System.out.println("y hits:");
        for (Map.Entry<Integer, List<Integer>> entry : yVelsToHitFrames.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }
    }

    private static int reverseTriangle(int destination) {
        return (int) (Math.sqrt(1 + (destination * 8)) - 1) / 2; // reverse triangle formula
    }

    private static void findValidXs(Target target) {
        System.out.println("target is: " + target.getXMin() + ".." + target.getXMax());
        // The minimum x is whatever value causes the probe to stop moving inside the target's x window, as close to the
        // start as possible. The maximum x is whatever value causes the probe to pass through the end of the target on
        // the first frame. Between these, inclusive, will be all the valid X values.
        //
        // get the lowest valid X position by reversing the triangle number formula
        //
        // map from x velocities to frames at which the probe is within the target (but hasn't stopped)
        Map<Integer, List<Integer>> xVelsToHitFramesMoving = new HashMap<>();
        // set of x velocities that cause the probe to stop within the target (which will be after the velocity's number
        // of frames)
        Set<Integer> xVelsThatStopInTarget = new HashSet<>();
        int xVelocityCur = reverseTriangle(target.getXMin());
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
            System.out.println("moving:");
            for (Map.Entry<Integer, List<Integer>> entry : xVelsToHitFramesMoving.entrySet()) {
                System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
            }
            System.out.println("stopped:");
            for (Integer frame : xVelsThatStopInTarget) {
                System.out.println(frame);
            }
            xVelocityCur++;
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
