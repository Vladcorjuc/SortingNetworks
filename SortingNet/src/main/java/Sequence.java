import Util.Constants;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Sequence {
    private int n;
    private boolean[] bitSet;
    private int value;

    private int zeros=-1;


    private static Sequence[][] CACHE;

    static {
        int n = 16;
        CACHE = new Sequence[n + 1][];
        for (int k = 1; k <= n; k++) {
            int m = (int) Constants.POW_2[k+1];
            CACHE[k] = new Sequence[m];
        }
    }

    //region Constructors
    public Sequence(int size){
        this.n=size;
        this.bitSet=new boolean[n];
    }
    public  Sequence(Sequence copy){
        this.n=copy.n;
        this.value=copy.value;
        this.bitSet=new boolean[n];
        this.zeros=copy.zeros;
        for(int i=0;i<bitSet.length;i++){
            bitSet[i]=copy.get(i);
        }
    }
    public Sequence(int wires, int value) {
        this(wires);
        this.n=wires;
        this.bitSet=new boolean[n];
        this.setValue(value);

        computeZeros();
    }



    private void computeZeros() {
        zeros=0;
        for(int i=0;i<n;i++){
            if(!bitSet[i]){
                zeros++;
            }
        }
    }
    //endregion
    //region Getters, Setters and Updaters
    public int size(){return n;}
    public boolean get(int index){
        return bitSet[index];
    }
    public int getZerosNumber(){
        return zeros;
    }
    public int getOnesNumber(){
        return n-zeros;
    }
    public List<Integer> getZeros(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!bitSet[i]) {
                list.add(i+1);
            }
        }
        return list;
    }
    public List<Integer> getOnes(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (bitSet[i]) {
                list.add(i + 1);
            }
        }
        return list;
    }

    public boolean areSwappable(int index1,int index2){
        return bitSet[index1] && !bitSet[index2];
    }
    public boolean isBad(int index){
        return bitSet[index] ? isBad1(index) : isBad0(index);
    }
    public boolean isSorted(){
        for(int i=0;i<n-1;i++){
            if(bitSet[i]&&!bitSet[i+1]) {
                return false;
            }
        }
        return false;
    }
    private boolean isBad0(int index) {
        return index>zeros;
    }
    private boolean isBad1(int index) {
        return index<=zeros;
    }

    public void setValue(int value){
        this.value = value;
        int index = 0;
        while (value != 0) {

            boolean bit = (value % 2) != 0;
            bitSet[this.n - index - 1] =bit;
            index++;
            value = value /2;
        }
    }
    public int getValue(){return value;}
    //endregion


    public void swap(int i,int j) {
        boolean aux=bitSet[i];
        bitSet[i]=bitSet[j];
        bitSet[j]=aux;
    }



    public static Sequence getSwapped(Sequence sequence,int i, int j) {
        Sequence newSequence =new Sequence(sequence);
        newSequence.bitSet[i]=sequence.bitSet[j];
        newSequence.bitSet[j]=sequence.bitSet[i];
        return newSequence;
    }
    public static Sequence getInstance(int wires, int value) {
        if(CACHE.length<=wires) {
            return null;
        }
        if(CACHE[wires][value]!=null){
            return CACHE[wires][value];

        }
        Sequence s=new Sequence(wires,value);
        CACHE[wires][value]= new Sequence(s);
        return s;
    }


    @Override
    public String toString() {
        return "Sequence{" +
                "n=" + n +
                ", bitSet=" + toString(bitSet) +
                '}';
    }
    public  String toString(boolean[] bitSet){
        StringBuilder s=new StringBuilder();
        for(int i=0;i<bitSet.length;i++) {
            s.append(bitSet[i]?"1":"0");
        }
        return s.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sequence)) return false;
        Sequence sequence = (Sequence) o;
        return n == sequence.n &&
                Arrays.equals(bitSet, sequence.bitSet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(n);
        result = 31 * result + Arrays.hashCode(bitSet);
        return result;
    }

    public static void main(String[] args) {
        for(int i=0;i< Constants.POW_2[4+1];i++){
            System.out.println(getInstance(4,i));
        }
    }
}
