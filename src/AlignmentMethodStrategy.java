import java.util.Map;

public interface AlignmentMethodStrategy {

    int multiMax(int... numbers);

    void trackBacktrace(int[][] levensteinDistanceMatrix, Map<String, Integer> costMatrixMap,
                        char[] firstSequenceEntries, char[] secondSequenceEntries, int GAP_PENALTY);

    int retrieveAlignmentCost(int[][] levensteinDistanceMatrix, int n, int m);
}
