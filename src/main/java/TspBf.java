import org.apache.commons.collections4.iterators.PermutationIterator;

import java.io.FileNotFoundException;
import java.util.*;

public class TspBf
{
    private static TspReader tspReader;
    private static ArrayList<List<Integer>> possibleRoads;

    private static int numberthread=3;
    private static int temp=1;
    private static int numpoints;
    private static Map<Integer,Integer> theBest = new HashMap<Integer, Integer>();

    private static void generateData()
    {
        ArrayList<Integer> verts = new ArrayList<Integer>();
        for (int i = 0; i < tspReader.getCountOfVert(); i++)
        {
            verts.add(i);
        }
        possibleRoads = new ArrayList<List<Integer>>();
        PermutationIterator permutationGenerator = new PermutationIterator(verts);
        while (permutationGenerator.hasNext())
        {
            possibleRoads.add(permutationGenerator.next());
        }
    }



    private static void algorithmBf()
    {

        Runnable runnableBf = new Runnable() {
            @Override
            public void run() {

                int theBestRoadValue=Integer.MAX_VALUE;
                int possibleRoadItr =0;
                for (int i = numpoints *(temp-1); i < numpoints*temp; i++)
                {
                    int temporaryRoadValue =0;

                    for (int j = 0; j < possibleRoads.get(i).size() - 1; j++) {
                        temporaryRoadValue = temporaryRoadValue + tspReader.getGraph()[possibleRoads.get(i).get(j)][possibleRoads.get(i).get(j + 1)];
                    }
                    temporaryRoadValue = temporaryRoadValue + tspReader.getGraph()[possibleRoads.get(i).get(possibleRoads.get(i).size() - 1)][possibleRoads.get(i).get(0)];

                        if (temporaryRoadValue < theBestRoadValue) {
                            theBestRoadValue = temporaryRoadValue;

                            theBest.put(temporaryRoadValue,i);
                        }

                }
            }
        };
        Thread[] threadBf = new Thread[numberthread];
        for (int i=0;i<numberthread;i++){
            Thread thread = new Thread(runnableBf);
            threadBf[i]=thread;
        }


for (int i =0;i<numberthread;i++) {
    numpoints =( possibleRoads.size()/ numberthread);
    threadBf[i].start();
    temp++;

}
temp=1;
    }

    private static void printScore()
    {
        Map<Integer, Integer> sort = new TreeMap<Integer, Integer>(theBest);
        Map.Entry<Integer,Integer> entry =sort.entrySet().iterator().next();
        System.out.println(entry.getKey());
        for (int i = 0; i < possibleRoads.get(entry.getValue()).size(); i++)
        {
            System.out.print(possibleRoads.get(entry.getValue()).get(i));
        }
        System.out.print("0\n");
    }



    public static void main(String[] args) throws FileNotFoundException
    {
        tspReader = new TspReader("graf.txt");
        tspReader.writeGraph();
        generateData();
        for (int i = 0; i < 10; i++)
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