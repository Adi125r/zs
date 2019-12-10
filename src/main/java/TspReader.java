import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TspReader
{
    private int countOfVert;
    private int graph[][];

    public TspReader(String fileLocation) throws FileNotFoundException
    {
        File fileWithGraph = new File(fileLocation);
        Scanner tspScanner = new Scanner(fileWithGraph);
        countOfVert = tspScanner.nextInt();
        graph = new int[countOfVert][countOfVert];
        for (int i = 0; i < countOfVert; i++)
        {
            for (int j = 0; j < countOfVert; j++)
            {
                graph[i][j] = tspScanner.nextInt();
            }
        }
    }

    public void writeGraph()
    {
        for (int i = 0; i < countOfVert; i++)
        {
            for (int j = 0; j < countOfVert; j++)
            {
                System.out.print(graph[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public int getCountOfVert()
    {
        return countOfVert;
    }

    public int[][] getGraph()
    {
        return graph;
    }
}
