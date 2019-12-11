import org.apache.commons.collections4.iterators.PermutationIterator;

import java.io.FileNotFoundException;
import java.util.*;

public class TspBf
{
    private static TspReader tspReader;
    private static int numberthread=6;
    private static int temp=1;
    private static int numpoints;
    private static int numFactorial ;
    private static Map<Integer,List<Integer>> theBest = new HashMap<Integer, List<Integer>>();
    private static PermutationIterator permutationGenerator;

    private static void generateData() {
        ArrayList<Integer> verts = new ArrayList<Integer>();
        for (int i = 1; i < tspReader.getCountOfVert(); i++) {
            verts.add(i);
        }
        numFactorial = factorial(verts.size());
        permutationGenerator = new PermutationIterator(verts);

    }

    private static int factorial(int num) {
        int result = 1;
        for (int i = 1; i < (num-1); i++) {
            result *= i;
        }
        System.out.println(result);
        return result;
    }


    private static void algorithmBf() throws InterruptedException {

        Runnable runnableBf = new Runnable() {
            @Override
            public void run() {
                List<Integer> possibleRoad;
                int theBestRoadValue=Integer.MAX_VALUE;
                for (int i = numpoints *(temp-1); i < numpoints*temp; i++)
                {


                    possibleRoad = permutationGenerator.next();
                    int temporaryRoadValue =0;
                    temporaryRoadValue = temporaryRoadValue + tspReader.getGraph()[0][possibleRoad.get(0)];
                    for (int j = 0; j < possibleRoad.size() - 1; j++) {
                        temporaryRoadValue = temporaryRoadValue + tspReader.getGraph()[possibleRoad.get(j)][possibleRoad.get(j + 1)];
                    }
                    temporaryRoadValue = temporaryRoadValue + tspReader.getGraph()[possibleRoad.get(possibleRoad.size() - 1)][0];

                    if (temporaryRoadValue < theBestRoadValue) {
                        theBestRoadValue = temporaryRoadValue;
                        synchronized (this) {
                            theBest.put(temporaryRoadValue, possibleRoad);

                        }
                    }

                }}

        };
        Thread[] threadBf = new Thread[numberthread];
        for (int i=0;i<numberthread;i++){
            Thread thread = new Thread(runnableBf);
            threadBf[i]=thread;
        }


for (int i =0;i<numberthread;i++) {
    numpoints =( numFactorial/ numberthread);
    threadBf[i].start();
    temp++;

}
        for (int i =0;i<numberthread;i++) {
          threadBf[i].join();}
temp=1;
    }

    private static void printScore()
    {
        Map<Integer, List<Integer>> sort = new TreeMap<Integer, List<Integer>>(theBest);
        Map.Entry<Integer,List<Integer>> entry =sort.entrySet().iterator().next();
        System.out.println(entry.getKey());
        System.out.print("0 ");
        for (int i = 0; i < sort.get(entry.getKey()).size(); i++)
        {

            System.out.print(entry.getValue().get(i)+" ");
        }
        System.out.print("0\n");
    }



    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        tspReader = new TspReader("14.txt");
       // tspReader.writeGraph();
        generateData();
        for (int i = 0; i < 1; i++)
        {
            long firstTime = System.nanoTime();
            algorithmBf();
            long secondTime = System.nanoTime();
            printScore();
            long timeScore = secondTime - firstTime;
            System.out.println("Czas: " + (timeScore));
            System.out.println();
        }
    }
}