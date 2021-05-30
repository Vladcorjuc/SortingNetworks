package genetic_algorithm.network;

import genetic_algorithm.hybrid.Gene;

import java.util.*;

public class Utils {

    public static <T extends Comparable<T>> int argMin(List<T> list){
        T min = list.get(0);
        int minIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min)<0) {
                min = list.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }
    public static final int[] POW_2={0,1,2,4,8,16,32,64,128,256,512,1024,2048, 4096, 8192, 16384, 32768, 65536};

    public static List<Gene> ConvertComparatorSetToGeneList(Set<genetic_algorithm.network.Comparator> redundantComparators) {
        List<Gene> genes = new ArrayList<>(redundantComparators.size());
        for(Comparator comparator:redundantComparators){
            Gene gene=new Gene(comparator.getStartingWire(),comparator.getEndingWire());
            genes.add(gene);
        }
        return genes;
    }
}
