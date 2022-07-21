import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.DisplayUtils.printMatrix;

public class AminoAcidSeqAfinneFuncSeqAlignment {

    private static final int D_PENALTY = -4;
    private static final int E_PENALTY = -1;
    private static final int MATCH_PENALTY = 1;
    private static final int MISMATCH_PENALTY = -1;
    private static final String SEQUENCES_DELIMETER = ":";
    private static final String M_LABEL = "M";
    private static final String I_X_LABEL = "X";
    private static final String I_Y_LABEL = "Y";
    private static final String PREV_STEP_TEMPLATE = "%s%d%d";
    private char[] firstSequenceEntries;
    private char[] secondSequenceEntries;
    private int[][] M;
    private int[][] Ix;
    private int[][] Iy;
    private String[][] M_previousStepsHistory;
    private String[][] Ix_previousStepsHistory;
    private String[][] Iy_previousStepsHistory;
    private int choicePointer;

    public void countAlignmentCostsAndCheckBacktraces(String sequencesFilePath) {
        List<String[]> sequencesList = new ArrayList<>();
        try (FileReader fr = new FileReader(sequencesFilePath);
             BufferedReader bfr = new BufferedReader(fr)) {
            String line;
            String[] tempSequencesArray;
            int i = 0;
            while ((line = bfr.readLine()) != null) {
                tempSequencesArray = line.split(SEQUENCES_DELIMETER);
                for (i = 0; i < tempSequencesArray.length; i++) {
                    tempSequencesArray[i] = (tempSequencesArray[i]).trim();
                }
                sequencesList.add(tempSequencesArray);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        sequencesList.forEach(sequenceArray -> countAlignmentCost(sequenceArray));
    }

    public void countAlignmentCost(String[] sequencesArray) {
        String firstSequence = sequencesArray[0];
        String secondSequence = sequencesArray[1];
        System.out.println("Initial sequences:");
        System.out.println(firstSequence);
        System.out.println(secondSequence);
        System.out.println();
        firstSequenceEntries = firstSequence.toCharArray();
        secondSequenceEntries = secondSequence.toCharArray();
        int i;
        int j;
        int n = firstSequenceEntries.length;
        int m = secondSequenceEntries.length;
        M = new int[n + 1][m + 1];
        Ix = new int[n + 1][m + 1];
        Iy = new int[n + 1][m + 1];
        M_previousStepsHistory = new String[n + 1][m + 1];
        Ix_previousStepsHistory = new String[n + 1][m + 1];
        Iy_previousStepsHistory = new String[n + 1][m + 1];
        for (i = 0; i < n + 1; i++) {
            for (j = 0; j < m + 1; j++) {
                M[i][j] = Integer.MIN_VALUE;
                Ix[i][j] = Integer.MIN_VALUE;
                Iy[i][j] = Integer.MIN_VALUE;
            }
        }

        M[0][0] = 0;

        for (i = 0; i < n + 1; i++) {
            Ix[i][0] = D_PENALTY + i * E_PENALTY;
            if (i > 0) {
                Ix_previousStepsHistory[i][0] = String.format(PREV_STEP_TEMPLATE, I_X_LABEL, i - 1, 0);
            }
        }

        for (j = 0; j < m + 1; j++) {
            Iy[0][j] = D_PENALTY + j * E_PENALTY;
            if (j > 0) {
                Iy_previousStepsHistory[0][j] = String.format(PREV_STEP_TEMPLATE, I_X_LABEL, 0, j - 1);
            }
        }

        printMatrix(M, n + 1, m + 1);
        printMatrix(Ix, n + 1, m + 1);
        printMatrix(Iy, n + 1, m + 1);

        int s;
        for (i = 1; i < n + 1; i++) {
            for (j = 1; j < m + 1; j++) {
                if (firstSequenceEntries[i - 1] == secondSequenceEntries[j - 1]) {
                    s = MATCH_PENALTY;
                } else {
                    s = MISMATCH_PENALTY;
                }

                M[i][j] = multiMax(M[i - 1][j - 1] + s, Ix[i - 1][j - 1] + s, Iy[i - 1][j - 1] + s);

                switch (choicePointer) {
                    case 0:
                        M_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, M_LABEL, i - 1, j - 1);
                        break;
                    case 1:
                        M_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, I_X_LABEL, i - 1, j - 1);
                        break;
                    case 2:
                        M_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, I_Y_LABEL, i - 1, j - 1);
                        break;
                }

                Ix[i][j] = multiMax(M[i - 1][j] + D_PENALTY + E_PENALTY, Ix[i - 1][j] + E_PENALTY);

                switch (choicePointer) {
                    case 0:
                        Ix_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, M_LABEL, i - 1, j);
                        break;
                    case 1:
                        Ix_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, I_X_LABEL, i - 1, j);
                        break;
                }

                Iy[i][j] = multiMax(M[i][j - 1] + D_PENALTY + E_PENALTY, Iy[i][j - 1] + E_PENALTY);

                switch (choicePointer) {
                    case 0:
                        Iy_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, M_LABEL, i, j - 1);
                        break;
                    case 1:
                        Iy_previousStepsHistory[i][j] = String.format(PREV_STEP_TEMPLATE, I_Y_LABEL, i, j - 1);
                        break;
                }
            }
        }

        printMatrix(M, n + 1, m + 1);
        printMatrix(Ix, n + 1, m + 1);
        printMatrix(Iy, n + 1, m + 1);
    }

    private int multiMax(int... costs) {
        int maxCost = Integer.MIN_VALUE;
        int counter = 0;
        for (int curCost : costs) {
            if (curCost > maxCost) {
                maxCost = curCost;
                choicePointer = counter;
            }
            counter++;
        }
        return maxCost;
    }
}
