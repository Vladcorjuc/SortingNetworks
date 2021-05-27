import java.util.List;

public class Main {


   /* public static void main(String[] args) {
        if (args.length > 0) {
            int n = Integer.parseInt((args[0]));
            int minComparators,maxComparators;
            System.out.println("Generating sorting networks with " + n + " wires.");

            switch (args.length) {
                case 3:
                    minComparators = Integer.parseInt((args[1]));
                        maxComparators = Integer.parseInt((args[2]));
                        System.out.println("Comparators ranging from " + minComparators + " up to " + maxComparators);
                        break;
                    case 2:
                        minComparators = Integer.parseInt((args[1]));
                        maxComparators = minComparators;
                        System.out.println("Comparators: " + minComparators);
                        break;
                    default:
                        maxComparators=minComparators=1;
                        System.out.println("Invalid arguments.");
                        System.exit(0);
                        break;
                }
                generate(n, minComparators,maxComparators);
            }
    }

    public static void generate(int wires, int minComparators, int maxComparators){
        Permutations.get(0); //forced initialization
        Sequence.getInstance(nbWires, 0); //forced initialization

        long t0 = System.currentTimeMillis();
        NetworkGenerator generator = new NetworkGenerator(wires, minComparators, maxComparators, null, null);
        List<Network> list = generator.createAll();
        long t1 = System.currentTimeMillis();
        System.out.println("------- " + wires +"--------");
        for (Network net : list) {
            if (net.isSorting()) {
                System.out.println("---> Sorting!");
                System.out.println(list.get(0));
                time[nbWires] = t1 - t0;
            }
        }
        System.out.println("Running time: " + (t1 - t0));
        return gen.bestOutputSize();
    }
    */
}
