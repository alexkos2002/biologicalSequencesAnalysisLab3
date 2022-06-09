import java.util.Map;

public class SmithWatermanAlignmentMethodStrategy implements AlignmentMethodStrategy {

    @Override
    public int multiMax(int... numbers) {
        double maxNumber = Double.NEGATIVE_INFINITY;
        for (int curNumber : numbers) {
            if (curNumber > maxNumber) {
                maxNumber = curNumber;
            }
        }
        int intMaxNumber = (int) maxNumber;
        return intMaxNumber > 0 ? intMaxNumber : 0;
    }

    @Override
    public void trackBacktrace(int[][] levensteinDistanceMatrix, Map<String, Integer> costMatrixMap,
                               char[] firstSequenceEntries, char[] secondSequenceEntries, int GAP_PENALTY) {
        int firstSequenceEntriesNum = firstSequenceEntries.length;
        int secondSequenceEntriesNum = secondSequenceEntries.length;
        int i;
        int j;
        int maxNumberI = 0;
        int maxNumberJ = 0;
        double maxAlignmentCost = Double.NEGATIVE_INFINITY;
        for (i = 0; i < firstSequenceEntriesNum; i++) {
            for (j = 0; j < secondSequenceEntriesNum; j++) {
                if (levensteinDistanceMatrix[i][j] > maxAlignmentCost) {
                    maxAlignmentCost = levensteinDistanceMatrix[i][j];
                    maxNumberI = i;
                    maxNumberJ = j;
                }
            }
        }
        String firstAlignedSequence = "";
        String secondAlignedSequence = "";
        String replaceCostKey;
        int replaceCostValue;
        i = maxNumberI;
        j = maxNumberJ;
        String firstSequenceCurEntry;
        String secondSequenceCurEntry;
        while (i > 0 && j > 0) {
            if (levensteinDistanceMatrix[i][j] == 0) {
                break;
            }
            firstSequenceCurEntry = String.valueOf(firstSequenceEntries[i - 1]);
            secondSequenceCurEntry = String.valueOf(secondSequenceEntries[j - 1]);
            replaceCostKey = firstSequenceCurEntry + secondSequenceCurEntry;
            replaceCostValue = costMatrixMap.get(replaceCostKey);
            if (i > 0 && j > 0 && levensteinDistanceMatrix[i][j] == levensteinDistanceMatrix[i - 1][j - 1] ||
                    levensteinDistanceMatrix[i][j] == levensteinDistanceMatrix[i - 1][j - 1] + replaceCostValue) {
                firstAlignedSequence = firstSequenceCurEntry + firstAlignedSequence;
                secondAlignedSequence = secondSequenceCurEntry + secondAlignedSequence;
                i -= 1;
                j -= 1;
            } else if (i > 0 && levensteinDistanceMatrix[i][j] == levensteinDistanceMatrix[i - 1][j] + GAP_PENALTY) {
                firstAlignedSequence = firstSequenceCurEntry + firstAlignedSequence;
                secondAlignedSequence = "-" + secondAlignedSequence;
                i -= 1;
            } else if (j > 0 && levensteinDistanceMatrix[i][j] == levensteinDistanceMatrix[i][j - 1] + GAP_PENALTY) {
                firstAlignedSequence = "-" + firstAlignedSequence;
                secondAlignedSequence = secondSequenceCurEntry + secondAlignedSequence;
                j -= 1;
            }
        }
        System.out.println("Aligned sequences:");
        System.out.println(firstAlignedSequence);
        System.out.println(secondAlignedSequence);
        System.out.println();
    }

    @Override
    public int retrieveAlignmentCost(int[][] levensteinDistanceMatrix, int n, int m) {
        double maxNumber = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (levensteinDistanceMatrix[i][j] > maxNumber) {
                    maxNumber = levensteinDistanceMatrix[i][j];
                }
            }
        }
        return (int) maxNumber;
    }


}
