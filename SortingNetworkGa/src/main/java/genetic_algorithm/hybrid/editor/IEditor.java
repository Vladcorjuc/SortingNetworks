package genetic_algorithm.hybrid.editor;

import genetic_algorithm.hybrid.Chromosome;

public interface IEditor {
    public void edit(Chromosome offspring, boolean verbose);
}

