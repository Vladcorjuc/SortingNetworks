import Util.Graph;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkExpander implements Callable<Integer> {

    private final Network baseNet;
    private final NetworkGenerator generator;
    private final List<Network> currentSpace;
    private final List<Network> deadNetworks;
    private int addedNetworks;

    public NetworkExpander(Network network, NetworkGenerator generator) {
        this.baseNet = network;
        this.generator = generator;
        this.currentSpace = generator.currentSpace;
        this.deadNetworks = generator.deadNetworks;
    }

    @Override
    public Integer call() {
        addedNetworks = 0;
        int n = baseNet.getWires();
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (baseNet.isRedundant(i, j)) {
                    continue;
                }
                Network netStar = new Network(baseNet, i, j);

                if(spaceIsEmpty()){
                    addNetwork(netStar);
                    continue;
                }
                if (isSubsumed(netStar)) {
                    continue;
                }

                Pair<Network, Graph> existSubsum = findFirstSubsumedNetwork(netStar);
                if (existSubsum != null) {
                    List<Network> toDelete = Subsumption.getSubsumedNetworks(existSubsum);
                    removeSubsumedAndLower(toDelete,netStar);
                }

                addNetwork(netStar);
            }
        }
        return addedNetworks;
    }

    public boolean spaceIsEmpty() {
        generator.spaceLock.readLock().lock();
        try {
            return currentSpace.isEmpty();
        }finally {
            generator.spaceLock.readLock().unlock();
        }
    }

    public void addNetwork(Network network){
        generator.spaceLock.writeLock().lock();
        try {
            currentSpace.add(network);
            addedNetworks++;
        }finally {
            generator.spaceLock.writeLock().unlock();
        }
    }

    public boolean isSubsumed(Network cStar) {
        generator.spaceLock.readLock().lock();
        try {
            for (Network net:currentSpace) {
                if(net==cStar){continue;}
                Graph subsumptionGraph = Subsumption.createGraph(net,cStar);
                if(!Subsumption.canSubsum(net,cStar)){continue;}
                if(subsumptionGraph.hasPerfectMatching()){
                    return true;
                }
            }
            return false;
        } finally {
            generator.spaceLock.readLock().unlock();

        }
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
