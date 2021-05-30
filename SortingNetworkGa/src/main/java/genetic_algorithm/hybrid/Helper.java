package genetic_algorithm.hybrid;

public class Helper {
    int patience;

    public Helper(int patience) {
        this.patience = patience;
    }

    public void trim(Population population, double deadRate){
        int initialSize = population.getPopulation().size();
        int k=population.getPopulation().get(0).depth();
        int n=population.getPopulation().get(0).getWires();
        int d = population.getPopulation().get(0).getTargetDepth();
        int deaths=0;
        while (deaths<initialSize){
            population.replaceIndividual(deaths,new Chromosome(k,n,d));
            deaths++;
            if(((double)deaths)/population.getPopulation().size() >= deadRate){
                break;
            }
        }

        population.replaceIndividual(population.getBestPosition(),new Chromosome(k,n,d));
    }

    public int patience() {
        return patience;
    }
}
