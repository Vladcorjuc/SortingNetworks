import Util.Graph;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkTrimmer implements Callable<Integer> {

    private final Network baseNet;
    private final NetworkGenerator generator;
    private final List<Network> currentSpace;
    private final List<Network> deadNetworks;

    private int removed=0;

    public NetworkTrimmer(Network baseNet, NetworkGenerator generator) {
        this.baseNet = baseNet;
        this.generator = generator;
        this.currentSpace = generator.currentSpace;
        this.deadNetworks = generator.deadNetworks;
    }


    @Override
    public Integer call() {
        if(baseNet.isDead()){
            return removed;
        }

        Pair<Network, Graph> existSubsum = findFirstSubsumedNetwork(baseNet);
        if (existSubsum != null) {
            List<Network> toDelete = Subsumption.getSubsumedNetworks(existSubsum);
            removeSubsumedAndLower(toDelete,baseNet);
        }else {
            removeLowerValue();
        }
        return removed;
    }
    public Pair<Network,Graph> findFirstSubsumedNetwork(Network network) {
        generator.spaceLock.readLock().lock();
        try {
            for (Network net:currentSpace) {
                if(network==net){continue;}
                Graph subsumptionGraph = Subsumption.createGraph(net,network);
                if(subsumptionGraph.hasPerfectMatching()){
                    return new Pair<>(net,subsumptionGraph);
                }
            }
            return null;
        }finally {
            generator.spaceLock.readLock().unlock();
        }
    }
    public void removeSubsumedAndLower(List<Network> subsumedNetworks,Network addedNetwork){
        generator.spaceLock.readLock().lock();
        try {
            for (Network network : currentSpace) {
                //region REMOVE SUBSUMED NETWORKS
                if (subsumedNetworks.contains(network)) {
                    network.die();
                    generator.deadNetworksLock.writeLock().lock();
                    try {
                        deadNetworks.add(network);
                    } finally {
                        generator.deadNetworksLock.writeLock().unlock();
                    }
                    removed++;
                }
                //endregion

                //region REMOVE LOWER VALUE NETWORK
                double evalCStar = generator.eval(addedNetwork);
                double evalCPrime = generator.eval(network);
                if (currentSpace.size() > generator.bound && evalCStar < evalCPrime){
                    double x = ThreadLocalRandom.current().nextDouble();
                    if (evalCPrime < x && evalCStar > x) {
                        network.die();
                        generator.deadNetworksLock.writeLock().lock();
                        try {
                            deadNetworks.add(network);
                        } finally {
                            generator.deadNetworksLock.writeLock().unlock();
                        }
                        removed++;

                    }
                }
                //endregion
            }
        } finally {
            generator.spaceLock.readLock().unlock();
        }
        removeDead();
    }
    public void removeLowerValue(){
        generator.spaceLock.readLock().lock();
        try {
            for (Network network : currentSpace) {
                //region REMOVE LOWER VALUE NETWORK
                double evalCStar = generator.eval(baseNet);
                double evalCPrime = generator.eval(network);

                if (currentSpace.size() > generator.bound && evalCStar < evalCPrime){
                    double x = ThreadLocalRandom.current().nextDouble();
                    if (evalCPrime < x && evalCStar > x) {
                        network.die();
                        generator.deadNetworksLock.writeLock().lock();
                        try {
                            deadNetworks.add(network);
                        } finally {
                            generator.deadNetworksLock.writeLock().unlock();
                        }
                        removed++;

                    }
                }
                //endregion
            }
        } finally {
            generator.spaceLock.readLock().unlock();
        }
        removeDead();
    }
    public void removeDead(){
        generator.deadNetworksLock.writeLock().lock();
        try {
            if (deadNetworks.size() > 1000) {
                generator.spaceLock.writeLock().lock();
                try {
                    currentSpace.removeAll(deadNetworks);
                } finally {
                    generator.spaceLock.writeLock().unlock();
                }
                deadNetworks.clear();
            }
        } finally {
            generator.deadNetworksLock.writeLock().unlock();
        }
    }

}
