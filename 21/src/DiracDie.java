import java.util.List;

public class DiracDie implements Die {

    @Override
    public List<Integer> roll() {
        return List.of(1, 2, 3);
    }
}
