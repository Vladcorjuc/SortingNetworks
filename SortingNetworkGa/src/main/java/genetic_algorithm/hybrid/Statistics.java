package genetic_algorithm.hybrid;

public class Statistics {
    public static int[] found;
    public static int rounds;
    public static int mutations;
    public static int added;

    public static void initialize(int runs){
        found = new int[runs];
        rounds = runs;
        mutations=0;
        added=0;
    }
    public static void foundInRound(int i){
        found[i] = 1;
    }

    public static int getGoodRoundsNumber(){
        int s=0;
        for(Integer i:found){
            if(i==1){
                s+=1;
            }
        }
        return s;
    }
    public static void mutationOccurred(){
        mutations++;
    }
    public static void mutationAdded(){
        added++;
    }

    public static String get(){
        return "FOUND : " + getGoodRoundsNumber() + "/" + rounds + "\n" + "MUTATIONS: " + mutations + "\tAdded: " + added;
    }
}
