public class Main {

    public static void main(String[] args) {
        AminoacidSequencesAlignment aminoSeqAlignment = new AminoacidSequencesAlignment();
        NeedlmanWunschAlignmentMethodStrategy NWStrategy = new NeedlmanWunschAlignmentMethodStrategy();
        SmithWatermanAlignmentMethodStrategy SWStrategy = new SmithWatermanAlignmentMethodStrategy();

        aminoSeqAlignment.fillCostMatrixMap("BLOSUM62.csv");

        aminoSeqAlignment.setAlignmentMethodStrategy(NWStrategy);
        aminoSeqAlignment.countAlignmentCostsAndCheckBacktraces("aminoAcidResidues");

        aminoSeqAlignment.setAlignmentMethodStrategy(SWStrategy);
        aminoSeqAlignment.countAlignmentCostsAndCheckBacktraces("aminoAcidResidues");
    }

}
