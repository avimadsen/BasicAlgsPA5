
import java.util.Scanner;

// Data structure for a node in a linked list
class Item {
    int data;
    Item next;

    Item(int data, Item next) {
        this.data = data;
        this.next = next;
    }
}

// Data structure for representing a graph
class Graph {
    int n; // # of nodes in the graph

    Item[] A;
    // For u in [0..n), A[u] is the adjecency list for u

    Graph(int n) {
        // initialize a graph with n vertices and no edges
        this.n = n;
        A = new Item[n];
    }

    void addEdge(int u, int v) {
        // add an edge u -> v to the graph

        A[u] = new Item(v, A[u]);
    }
}

// Data structure holding data computed by DFS
class DFSInfo {
    int k;
    // # of trees in DFS forest

    int[] T;
    // For u in [0..n), T[u] is initially 0, but when DFS discovers
    // u, T[u] is set to the index (which is in [1..k]) of the tree
    // in DFS forest in which u belongs.

    int[] L;
    // List of nodes in order of decreasing finishing time

    int count;
    // initially set to n, and is decremented every time
    // DFS finishes with a node and is recorded in L

    DFSInfo(Graph graph) {
        int n = graph.n;
        k = 0;
        T = new int[n];
        L = new int[n];
        count = n;
    }
}

// your "main program" should look something like this:

class SCCStarter {

    static void recDFS(int u, Graph graph, DFSInfo info) {
        // perform a recursive DFS, starting at u
        Item currentItem = graph.A[u];
        info.T[u] = info.k; 
        while (currentItem != null) {
            if (info.T[currentItem.data] == 0) {
                
                recDFS(currentItem.data, graph, info);
            }
            currentItem = currentItem.next;
        }
        info.count--;
        info.L[info.count] = u;

    }

    static DFSInfo DFS(int[] order, Graph graph) {
        // performs a "full" DFS on given graph, processing
        // nodes in the order specified (i.e., order[0], order[1], ...)
        // in the main loop.
        DFSInfo cityInfo = new DFSInfo(graph);
        for (int i = 0; i < graph.n; i++) {
            if (cityInfo.T[order[i]] == 0) {
                cityInfo.k++;
                recDFS(order[i], graph, cityInfo);
            }
        }
        return cityInfo;
    }

    static boolean[] computeSafeNodes(Graph graph, DFSInfo info) {
        // returns a boolean array indicating which nodes
        // are safe nodes. The DFSInfo is that computed from the
        // second DFS.

        boolean[] booleanNodes = new boolean[graph.n];
        for(int i=0; i<booleanNodes.length; i++)
        {
            booleanNodes[i] = true;
        }

        
        boolean [] trees = new boolean[info.k+1];

        for(int i=0; i<trees.length; i++)
        {
            trees[i]=true;
        }
        for(int i=0; i<graph.n;i++)
        {
            Item z = graph.A[i];

            while(z!=null)
            {
                if(info.T[i] != info.T[z.data])
            {
                booleanNodes[i]=false;
                trees[info.T[i]]=false;
            }
            z=z.next;

            }
            
        
           }
           for(int i=0;i<trees.length;i++)
           {
               for(int z=0; z<graph.A.length; z++)
               {
                   if(trees[i] == false && (info.T[z]==i))
                   booleanNodes[z]=false;
               }
           }
        
        /*
        int zeroTree=-1;
        for(int i=0;i<trees.length;i++)
        {
            if(trees[i]==0)
            zeroTree=i;
        }
*/
      /*  for(int i=0; i<graph.A.length;i++)
        {
            System.out.println(graph.A[i].data);
            booleanNodes[graph.A[i].data] = false;
        }
        
        for(int i=0; i<graph.n;i++)
        {
            for(int z=0; z<trees.length; z++)
            {
                if(info.T[i] == z && trees[z] ==0)
                booleanNodes[i]= true;
            }
        }

        */
        /*
        //set everything equal to true 
        for (int i = 0; i < graph.n; i++) {
            Item z = graph.A[i];

            while(z!=null)
            {
                //System.out.println(info.T[i] +" " +  info.T[z.data]);
            if (info.T[i] != info.T[z.data])
            {
                
                //setting it to false
                //System.out.println(graph.A[i].data);
                booleanNodes[i] = false;
            }   

                z=z.next;

        }
    }
    */


    
        return booleanNodes;
    }

    static Graph reverse(Graph graph) {
        // returns the reverse of the given graph
        Graph reverseGraph = new Graph(graph.n);
        for (int i = 0; i < graph.A.length; i++) {
            System.out.println(graph.A[i].data);
            int first = graph.A[i].data;
            int second = graph.A[i].next.data;
            reverseGraph.addEdge(second, first);
        }
        return reverseGraph;
    }

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int numOfRooms = sc.nextInt();
        int numOfPassages = sc.nextInt();
        Graph city = new Graph(numOfRooms + 1);
        Graph reversedGraph = new Graph(numOfRooms +1);

        for (int i = 0; i < numOfPassages; i++) {
            int firstRoom = sc.nextInt();
            int secondRoom = sc.nextInt();
            city.addEdge(firstRoom, secondRoom);
            reversedGraph.addEdge(secondRoom, firstRoom);
        }
        DFSInfo SCCGraphInfo = new DFSInfo(city);

        // Initialize SCCGraphInfo.T to 0

        for (int i = 0; i < city.n; i++) {
            SCCGraphInfo.T[i] = 0;
        }

        // Get reversed graph
     //   Graph reverseGraph = reverse(city);

        DFSInfo reversedGraphInfo = new DFSInfo(reversedGraph);

        // Initialize reversedGraphInfo.T to 0
        for (int i = 0; i < city.n; i++) {
            reversedGraphInfo.T[i] = 0;
        }

        // DFS on reversedGraph

        //DOES THIS MAKE SENSE
        int[] regularOrder = new int[city.n];
        for (int i = 0; i < city.n; i++) {
            regularOrder[i] = i;
        }
        reversedGraphInfo = DFS(regularOrder, reversedGraph);

        // Find SCCs from reversed graph order

        SCCGraphInfo = DFS(reversedGraphInfo.L, city);

        // Get boolean array of safe nodes
        boolean[] booleanNodes = computeSafeNodes(city, SCCGraphInfo);

        for(int i=0;i<booleanNodes.length-1; i++)
        {
            if(booleanNodes[i]==true)
            System.out.print(i+" ");
        }
        // scan through boolean list by k value to determine if every value of SCC is
        // present by any value of k
        /*
        int[] kScan = new int[SCCGraphInfo.k + 1];
        for (int i = 1; i < kScan.length; i++) {
            kScan[i] = i;
        }

        for (int z = 0; z < city.n; z++) {
            if (booleanNodes[z] == false) {
                kScan[SCCGraphInfo.T[z]] = -1;
            }
        }
        for (int i = 1; i < kScan.length; i++) {
            if (kScan[i] != -1) {
                for (int z = 0; z < city.n; z++) {
                    if (SCCGraphInfo.T[z] == kScan[i])
                        System.out.println("hi" + city.A[z].data);
                }
            }
        }
        */

    }

}