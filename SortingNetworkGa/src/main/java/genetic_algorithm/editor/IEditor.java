package genetic_algorithm.editor;

import genetic_algorithm.hybrid.Chromosome;

public interface IEditor {
    public void edit(Chromosome offspring, boolean verbose);
}

