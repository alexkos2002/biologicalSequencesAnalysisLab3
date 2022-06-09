import java.util.Map;

import static utils.DisplayUtils.printMatrix;

public class NeedlmanWunschAlignmentMethodStrategy implements AlignmentMethodStrategy {
    @Override
    public int multiMax(int... numbers) {
        double maxNumber = Double.NEGATIVE_INFINITY;
        for (int curNumber : numbers) {
            if (curNumber > maxNumber) {
                maxNumber = curNumber;
            }
        }
        return (int) maxNumber;
    }

    @Override
    public void trackBacktrace(int[][] levensteinDistanceMatrix, Map<String, Integer> costMatrixMap,
                               char[] firstSequenceEntries, char[] secondSequenceEntries, int GAP_PENALTY) {
        int i = firstSequenceEntries.length;
        int j = secondSequenceEntries.length;
        String firstAlignedSequence = "";
        String secondAlignedSequence = "";
        String replaceCostKey;
        int replaceCostValue;
        String firstSequenceCurEntry;
        String secondSequenceCurEntry;
        while (i > 0 && j > 0) {
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
        if (i > 0) {
            while (i > 0) {
                firstSequenceCurEntry = String.valueOf(firstSequenceEntries[i - 1]);
                firstAlignedSequence = firstSequenceCurEntry + firstAlignedSequence;
                secondAlignedSequence = "-" + secondAlignedSequence;
                i -= 1;
            }
        } else {
            while (j > 0) {
                secondSequenceCurEntry = String.valueOf(firstSequenceEntries[j - 1]);
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
        return levensteinDistanceMatrix[n][m];
    }
}
