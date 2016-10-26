import java.io.*;

/**
 * This is the class that students need to implement. The code skeleton is provided.
 * Students need to implement rtinit(), rtupdate() and linkhandler().
 * printdt() is provided to pretty print a table of the current costs for reaching
 * other nodes in the network.
 */ 
public class Node { 
    
    public static final int INFINITY = 9999;
    
    int[] lkcost;  /*The link cost between node 0 and other nodes*/
    int[][] costs;    /*Define distance table*/
    int nodename;               /*Name of this node*/
    
    /* Class constructor */
    public Node() { }
    
    //***********************************************************************************************************
    /* students to write the following two routines, and maybe some others */
    void rtinit(int nodename, int[] initial_lkcost) { 
      System.out.printf("System is Initializing Node#%d at Time %f\n", nodename, NetworkSimulator.clocktime);
      //Update the Node information for this node
      this.nodename = nodename;
      int n = initial_lkcost.length; // number of vertices
      
      lkcost = new int[n];// copy the link cost from the parameter
      for(int i = 0; i < n; i ++ ){
        lkcost[i] = initial_lkcost[i];
      }
      
      //According to the link cost, update the distence table
      //The diagnoal value is the distance Node 0 to Node i via Node j.
      costs = new int[n][n];
      for (int i = 0; i < n; i ++){
        for (int j = 0; j < n; j ++){
          if (j == nodename) {
            costs[i][j] = initial_lkcost[i];
          }else{
            costs[i][j] = INFINITY;
          }
        }
      }
      
      //************After initialize the node***********
      //We will send the mincost to the directly connected node
      int source = nodename;
      int[] mincost = lkcost;
      notify(source, mincost);
      printResult(costs);
    }    
    
    
    
    
    
    //***********************************************************
    //updata distence table when every get a packet from other node
    //****************************************************************

    void rtupdate(Packet rcvdpkt) {  
      System.out.printf("System is updating Node#%d at Time %f\n", nodename,  NetworkSimulator.clocktime);
      int via = rcvdpkt.sourceid;
      int self = rcvdpkt.destid;
      int[] minc = rcvdpkt.mincost;
      int n = minc.length;
      System.out.printf("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU\n");
      boolean update = false; // check if we need to send neibohor a copy
      //update the distance table
      for (int i = 0; i < n; i ++){
        if (costs[i][via] > minc[i]){
          System.out.printf("updating costs table: cost[%d][%d] = minc[%d]= %d\n", i,via,i,minc[i]);
          costs[i][via] = minc[i];
        }
      }
      
      //Find the shortest path
      //If the distance through Node j is shorter, we will update the distance table
      for (int i = 0; i < n; i ++){
        if (i != self) {
          if (costs[via][self] + costs[i][via] < costs[i][self]) {
            System.out.printf("Updating distance table at Node %d.\n",nodename);
            System.out.printf("It is closer to go through Node %d with distance %d\n",via,costs[via][self] + costs[i][via]);
            costs[i][self] = costs[via][self] + costs[i][via];
            update = true;
          }
        }
      }
      
      //If the distance table is updated, we will notify this Node's direct Neighbor.
      int[] newmin = new int[n];
      for(int i = 0; i < n; i ++){
        newmin[i] = costs[i][self];
      }
      printdt();
      if (update) {
        System.out.printf("There is an update in the distance table\n");
        notify(self, newmin);
      }else{
        System.out.printf("There is no change in the distance table\n");
      }
      printResult(costs);
      table(costs);
    
    }
    
    
    //***********************************************************************************
    /* called when cost from the node to linkid changes from current value to newcost*/
    void linkhandler(int linkid, int newcost) { 
      int oldcost = costs[linkid][nodename];
      int dif = newcost - oldcost;
      //add the diffrerenct to every edge
      for(int i =0; i< 4; i++){
        if(costs[linkid][i] != INFINITY){
          costs[linkid][i] += dif;
        }
      }
      int[] mincost = {costs[nodename][0],costs[nodename][1],costs[nodename][2],costs[nodename][3]};
      notify(nodename,mincost);
    }    


    /* Prints the current costs to reaching other nodes in the network */
    void printdt() {
        switch(nodename) {
 case 0:
     System.out.printf("                via     \n");
     System.out.printf("   D0 |    1     2    3 \n");
     System.out.printf("  ----|-----------------\n");
     System.out.printf("     1|  %3d   %3d   %3d\n",costs[1][1], costs[1][2],costs[1][3]);
     System.out.printf("dest 2|  %3d   %3d   %3d\n",costs[2][1], costs[2][2],costs[2][3]);
     System.out.printf("     3|  %3d   %3d   %3d\n",costs[3][1], costs[3][2],costs[3][3]);
     break;
 case 1:
     System.out.printf("                via     \n");
     System.out.printf("   D1 |    0     2 \n");
     System.out.printf("  ----|-----------------\n");
     System.out.printf("     0|  %3d   %3d \n",costs[0][0], costs[0][2]);
     System.out.printf("dest 2|  %3d   %3d \n",costs[2][0], costs[2][2]);
     System.out.printf("     3|  %3d   %3d \n",costs[3][0], costs[3][2]);
     break;
     
 case 2:
     System.out.printf("                via     \n");
     System.out.printf("   D2 |    0     1    3 \n");
     System.out.printf("  ----|-----------------\n");
     System.out.printf("     0|  %3d   %3d   %3d\n",costs[0][0], costs[0][1],costs[0][3]);
     System.out.printf("dest 1|  %3d   %3d   %3d\n",costs[1][0], costs[1][1],costs[1][3]);
     System.out.printf("     3|  %3d   %3d   %3d\n",costs[3][0], costs[3][1],costs[3][3]);
     break;
 case 3:
     System.out.printf("                via     \n");
     System.out.printf("   D3 |    0     2 \n");
     System.out.printf("  ----|-----------------\n");
     System.out.printf("     0|  %3d   %3d\n",costs[0][0],costs[0][2]);
     System.out.printf("dest 1|  %3d   %3d\n",costs[1][0],costs[1][2]);
     System.out.printf("     2|  %3d   %3d\n",costs[2][0],costs[2][2]);
     break;
        }
    }
    
    //*********************************************************************
    //Send node's shortest path to it's neighbor.
    void notify (int source, int[] mincost){
      if (source == 0) {
        System.out.printf("Sending node %d's directly connected neighbor its mincost\n", source);
        NetworkSimulator.tolayer2(new Packet(source, 1, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 2, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 3, mincost));
      }else if (source == 1){
        System.out.printf("Sending node %d's directly connected neighbor its mincost\n", source);
        NetworkSimulator.tolayer2(new Packet(source, 0, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 2, mincost));
      }else if (source == 2){
        System.out.printf("Sending node %d's directly connected neighbor its mincost\n", source);
        NetworkSimulator.tolayer2(new Packet(source, 0, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 1, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 3, mincost));
      }else {//source == 3
        System.out.printf("Sending node %d's directly connected neighbor its mincost\n", source);
        NetworkSimulator.tolayer2(new Packet(source, 0, mincost));
        NetworkSimulator.tolayer2(new Packet(source, 2, mincost));
      }
    }
    
    //************************************************************************************************
    //print out the mincost or the result of the distance table.
    //This method will output a 1X4 array which indicates the shortest path to each node.
    //This method will print out another 1X4 array to indicate the direction which gives the shortest path.
    void printResult (int[][] costs){
      int[] result = {999,999,999,999};
      int[] dir = new int[4];
      for (int row = 0; row < 4; row++){
        for(int col = 0; col < 4; col++){
          if (row != col){
            if (costs[row][col] < result[row]){
              result[row] = costs[row][col];
              dir [row] = col;
            }
          }
        }
      }
      System.out.printf("************************************************************************************\n");
      System.out.printf("The shortest distance to each Node is following:\n");
      System.out.printf("Node#0: %d, Node#1: %d, Node#2: %d, Node#3: %d\n", result[0],result[1],result[2],result[3]);
      System.out.printf("The Node from which the distance is the shortest:\n");
      System.out.printf("Node#%d, Node#%d, Node#%d, Node#%d\n", dir[0],dir[1],dir[2],dir[3]);
      System.out.printf("************************************************************************************\n");
      System.out.println("");
    }
    
    
    //print out the whole distance table
    void table(int[][] costs){
      System.out.printf("                via          \n");
      System.out.printf("   D0 |    0    1     2    3 \n");
      System.out.printf("  ----|----------------------\n");
      System.out.printf("     0|  %3d   %3d   %3d  %3d\n",costs[0][0], costs[0][1],costs[0][2],costs[0][3]);
      System.out.printf("     1|  %3d   %3d   %3d  %3d\n",costs[1][0], costs[1][1],costs[1][2],costs[1][3]);
      System.out.printf("dest 2|  %3d   %3d   %3d  %3d\n",costs[2][0], costs[2][1],costs[2][2],costs[2][3]);
      System.out.printf("     3|  %3d   %3d   %3d  %3d\n",costs[3][0], costs[3][1],costs[3][2],costs[3][3]);
      System.out.printf("  ------------------------------------------------------------------------\n");
    }
    
}