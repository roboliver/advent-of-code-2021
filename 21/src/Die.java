import java.util.ArrayList;
import java.util.List;

public interface Die {

    List<Integer> roll();

    default List<Integer> rollNTimes(int n) {
        List<Integer> rollSums = new ArrayList<>();
        rollSums.add(0);
        for (int i = 0; i < n; i++) {
            List<Integer> rolls = roll();
            List<Integer> rollSumsNew = new ArrayList<>();
            for (int rollSum : rollSums) {
                for (int roll : rolls) {
                    rollSumsNew.add(rollSum + roll);
                }
            }
            rollSums = rollSumsNew;
        }
        return rollSums;
    }
}
