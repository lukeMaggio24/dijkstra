import java.util.*;
import java.io.*;
//Code written by Luke Maggio
//date finished: 7/15/2023

public class dijkstra
{
    private class identityMatrix
    {
        int weight = 0;
        int nodeNum = 0;
        int parent = 0;

        identityMatrix prev = null;
        boolean flag = false;

        public identityMatrix()
        {

        }
    }

    //reads in the input file, creating and returning a scanner
    private static Scanner readFile(String filename)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            return in;
        }
        catch(Exception e) //file does not exist
        {
            System.out.println("Could not find file " + e);
            return null;
        }        
    }

    //creates the graph
    public void createGraph(int numVertice, int numEdges, identityMatrix graph[][], Scanner in)
    {
        //used to keep create the graph
        int node = 0;
        int node2 = 0;
        int edgeWeight = 0;
        
        for(int j = 0; j < numVertice+1; j++)
            for(int k = 0; k < numVertice+1; k++)
                graph[j][k] = new identityMatrix();

        //creates the graph from the input file
        for(int i = 0; i < numEdges; i++)
        {
            node = in.nextInt();
            node2 = in.nextInt();
            edgeWeight = in.nextInt();
            graph[node][node2].weight = edgeWeight;
            graph[node2][node].weight = edgeWeight;
        }
    
    }   
    
    //perfomrs dijkstras algorithm
    public void diAlgorithm(int sourceVertex, int numVertice, identityMatrix graph[][])
    {
        //sets all weights on nodes to infinity, except current node
        int tracker = 0; //used to know when we have marked every node
        int [] flagged = new int[numVertice];

        //sets all nodes weights to infinity
        for(int i = 1; i <= numVertice; i++)
        {
            if(i != sourceVertex)
            {
                graph[i][i].weight = Integer.MAX_VALUE;
                graph[i][i].nodeNum = i;
            }
            else
                graph[i][i].nodeNum = i;
        }
        
        while(tracker < numVertice)
        {
           
            //adds weights with all adjacanet vertices
            for(int j = 1; j <= numVertice; j++)
            {
                if(graph[sourceVertex][j].weight > 0 && sourceVertex != j && graph[j][j].flag == false) //an edge is present
                {
                    if((graph[sourceVertex][sourceVertex].weight + graph[sourceVertex][j].weight) < graph[j][j].weight)
                    {
                        graph[j][j].weight = graph[sourceVertex][sourceVertex].weight + graph[sourceVertex][j].weight; //adds the current vertex+edge weight
                        graph[j][j].parent = graph[sourceVertex][sourceVertex].nodeNum;  
                        graph[j][j].prev = graph[sourceVertex][sourceVertex];
                    }
                }
            }

            int lowest = 0;
            graph[sourceVertex][sourceVertex].flag = true;
            flagged[tracker] = sourceVertex; //holds nodes we have flagged
            //determines what our next current node will be
            for(int l = 0; l <= tracker; l++)
            {
                for(int k = 1; k <= numVertice; k++)
                {
                    if(graph[flagged[l]][k].weight > 0 && flagged[l] != k && graph[k][k].flag == false) //an edge is present
                    {
                        if(lowest == 0) //if this is the first edge we found
                            lowest = k;
                        else //not the first edge we found
                            if(graph[lowest][lowest].weight > graph[k][k].weight)
                                lowest = k;                                
                    }
                }
            }

            
            //Moves along to the next node
            //new current node
            sourceVertex = lowest;
            tracker++;
        }
    }
    
    //prints out the the result of dijkstra algorithm
    public static void printPath(int sourceVertex, int numVertice, identityMatrix graph[][])
    {
        try
        {
            FileWriter out = new FileWriter("cop3503-asn2-output-maggio-luke.txt");
            
            out.write("" + numVertice + "\n");
            //iterates through all the nodes
            for(int i = 1; i <= numVertice; i++)
            {
                if(i != sourceVertex)
                    out.write("" + i + " " + graph[i][i].weight + " " + graph[i][i].parent);
                else
                    out.write(i + " " + "-1" + " " + "-1"); //we just literally print -1, instead of the value from the current node
                    //since the current node value needs to stay 0, for correct calculations.

                if(i != numVertice)
                    out.write("\n");
            }
            out.close();
        }
        catch(Exception e)
        {
            System.out.println("ERROR WRITING TO FILE");
        }
    }

    public static void main(String args[])
    {
        //go our input file and read in our first three values
        dijkstra temp = new dijkstra();

        Scanner in = readFile("cop3503-asn2-input.txt");
        if(in == null) //if a file not found exception was thrown earlier than we will exit the code
            return;

        int numVertice = in.nextInt(); //number of vertices
        int sourceVertex = in.nextInt(); //the starting vertices
        int numEdges = in.nextInt(); //the number of edges

        identityMatrix [][] graph = new identityMatrix[numVertice+1][numVertice+1]; //used to represent our graph
        temp.createGraph(numVertice, numEdges, graph, in);
        temp.diAlgorithm(sourceVertex, numVertice, graph);
        printPath(sourceVertex, numVertice, graph);
    }
}
