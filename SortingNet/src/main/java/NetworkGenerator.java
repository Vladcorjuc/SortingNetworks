import Util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkGenerator {

    public List<Network> currentSpace;
    public List<Network> deadNetworks;

    private ExecutorService executor;
    public ReadWriteLock spaceLock=new ReentrantReadWriteLock();
    public ReadWriteLock deadNetworksLock = new ReentrantReadWriteLock();
    private int threads;

    public int bound=-1;
    private boolean done=false;

    public NetworkGenerator(int threads) {
        this.threads=threads;
    }

    public List<Network> generateSpace(int n, int k, int bound, List<Network> F){
        this.bound=bound;
        List<List<Network>> space = new ArrayList<>();
        space.add(F);
        int q = F.get(0).getComparatorList().size();
        int index =0;

        executor = Executors.newFixedThreadPool(threads);
        currentSpace=new ArrayList<>();
        deadNetworks=new ArrayList<>();

        for(int p= q; p<k; p++){
            currentSpace.clear();
            index++;
            List<Network> lastSpace = new ArrayList<>(space.get(index-1));


            List<Callable<Integer>> calls = new LinkedList<>();
            for (Network net:lastSpace) {
                calls.add(new NetworkExpander(net,this));
            }


            List<Future<Integer>> futures = null;
            try {
                futures = executor.invokeAll(calls);
                for (Future<Integer> f : futures) {
                    int added = f.get();
                }
                calls.clear();

                for(Network network:currentSpace){
                    calls.add(new NetworkTrimmer(network,this));
                }
                futures = executor.invokeAll(calls);
                for (Future<Integer> f : futures) {
                    f.get();

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            
            removeDead();
            space.add(currentSpace);
        }

        executor.shutdown();

        for(Network net:currentSpace){
            if(net.isSorting()){
                System.out.println("Bingo!! ->> "+ k);
                System.out.println(net);
                done=true;
                break;
            }
        }

        return space.get(index);
    }

    private void removeDead() {
        currentSpace.removeIf(Network::isDead);
    }


    public Network createGreenFilter(int n){
        Network filter = new Network(n);
        int length=1;
        while(length<n){

            for(int k=1;k<=length;k++){
                for(int i=k;i<=n-length;i+=2*length){
                    filter.addComparator(new Comparator(i,i+length));
                }
            }
            length *= 2;
        }
        return filter;
    }
    public double eval(@NotNull Network network){
        int n =network.getWires();
        return (1.0 / ((n + 1) * (Constants.POW_2[n] - 1))) *
                ((network.getBad().size() + network.outputs().size()) - n - 1);
    }

    public boolean isDone(){
        return done;
    }



    public static void main(String[] args) {
       // int threads = Runtime.getRuntime().availableProcessors();
        int threads=8;
        System.out.println("Threads :"+threads);
        NetworkGenerator networkGenerator=new NetworkGenerator(threads);

        int n=9;

        Network filter = networkGenerator.createGreenFilter(n);
        List<Network> filterSpace = new ArrayList<>();
        filterSpace.add(filter);

        int k= filterSpace.get(0).getComparatorList().size();
        while(true){
            System.out.println(k);
            List<Network> space = networkGenerator.generateSpace(n,k,1000,filterSpace);
            if(networkGenerator.isDone()){
                break;
            }
            k++;
            filterSpace=new ArrayList<>(space);
        }

    }

}