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
        findValidXes(target);
        return 0;
    }

    public static int arcCount(BufferedReader reader) throws IOException {
        return 0;
    }

    private static void findValidXes(Target target) {
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
        int xVelocityCur = (int) Math.sqrt((target.getXMin() * 2) - 1);
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
                //            xVelocityCur--;
                //            xLandsAt = (xVelocityCur * (xVelocityCur + 1)) / 2;
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
