import java.util.*;
import java.io.*;

public class StudentNetworkSimulator extends NetworkSimulator
{
    /*
     * Predefined Constants (static member variables):
     *
     *   int MAXDATASIZE : the maximum size of the Message data and
     *                     Packet payload
     *
     *   int A           : a predefined integer that represents entity A
     *   int B           : a predefined integer that represents entity B 
     *
     * Predefined Member Methods:
     *
     *  void stopTimer(int entity): 
     *       Stops the timer running at "entity" [A or B]
     *  void startTimer(int entity, double increment): 
     *       Starts a timer running at "entity" [A or B], which will expire in
     *       "increment" time units, causing the interrupt handler to be
     *       called.  You should only call this with A.
     *  void toLayer3(int callingEntity, Packet p)
     *       Puts the packet "p" into the network from "callingEntity" [A or B]
     *  void toLayer5(String dataSent)
     *       Passes "dataSent" up to layer 5
     *  double getTime()
     *       Returns the current time in the simulator.  Might be useful for
     *       debugging.
     *  int getTraceLevel()
     *       Returns TraceLevel
     *  void printEventList()
     *       Prints the current event list to stdout.  Might be useful for
     *       debugging, but probably not.
     *
     *
     *  Predefined Classes:
     *
     *  Message: Used to encapsulate a message coming from layer 5
     *    Constructor:
     *      Message(String inputData): 
     *          creates a new Message containing "inputData"
     *    Methods:
     *      boolean setData(String inputData):
     *          sets an existing Message's data to "inputData"
     *          returns true on success, false otherwise
     *      String getData():
     *          returns the data contained in the message
     *  Packet: Used to encapsulate a packet
     *    Constructors:
     *      Packet (Packet p):
     *          creates a new Packet that is a copy of "p"
     *      Packet (int seq, int ack, int check, String newPayload)
     *          creates a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and a
     *          payload of "newPayload"
     *      Packet (int seq, int ack, int check)
     *          chreate a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and
     *          an empty payload
     *    Methods:
     *      boolean setSeqnum(int n)
     *          sets the Packet's sequence field to "n"
     *          returns true on success, false otherwise
     *      boolean setAcknum(int n)
     *          sets the Packet's ack field to "n"
     *          returns true on success, false otherwise
     *      boolean setChecksum(int n)
     *          sets the Packet's checksum to "n"
     *          returns true on success, false otherwise
     *      boolean setPayload(String newPayload)
     *          sets the Packet's payload to "newPayload"
     *          returns true on success, false otherwise
     *      int getSeqnum()
     *          returns the contents of the Packet's sequence field
     *      int getAcknum()
     *          returns the contents of the Packet's ack field
     *      int getChecksum()
     *          returns the checksum of the Packet
     *      int getPayload()
     *          returns the Packet's payload
     *
     */

    /*   Please use the following variables in your routines.
     *   int WindowSize  : the window size
     *   double RxmtInterval   : the retransmission timeout
     *   int LimitSeqNo  : when sequence number reaches this value, it wraps around
     */

    public static final int FirstSeqNo = 0;
    private int WindowSize;
    private double RxmtInterval;
    private int LimitSeqNo;
    
    // Add any necessary class variables here.  Remember, you cannot use
    // these variables to send messages error free!  They can only hold
    // state information for A or B.
    // Also add any necessary methods (e.g. checksum of a String)
    
    //return the increased sequence number and set the seq to 1 if it excced the limit
    protected int nextNum (int x){
      int next = x + 1;
      if (next == LimitSeqNo){
        next = FirstSeqNo;
      }
      return next;
    }
    
    //calculate the checksum for the packet, add up seq num, ack and data in the packet
    protected int checkSum(Packet p){
      int seq = p.getSeqnum();
      int ack = p.getAcknum();
      String data = p.getPayload();
      //calculate cheacksum of this packet, add up all the data with seq number and ACK
      int check = seq + ack;
      for(int i = 0; i < data.length(); i++){
        check += (int) data.charAt(i);
      }
      return check;
    }
    
    // Check wheater the packet is corrupted by comparing the cheacksum.
    //return true is the packet is corrupt and false otherwise.
    protected boolean isCorrupted(Packet p){
      int a = checkSum(p);
      int b = p.getChecksum();
      return (a != b);
    }
    
    //This method will sent out the packet in the queue
    //if the window has room.
    protected void checkQueue() {
      if (nexttoSend <= max_Seq){//check if the window has room
        if(nexttoSend < sentPackets.size()){
        
          Double t = getTime();
          toLayer3(A, sentPackets.get(nexttoSend));
          if (min_Seq == nexttoSend){
            startTimer(A,RxmtInterval);
          }
          nexttoSend ++;
          totalsent ++;
          rtt.add(nexttoSend, t);
        }
      }
    }
    
   //Variables that help track the state of A
    private int seqNum;                       //track the current sequence number
    private LinkedList<Packet> sentPackets;   //A list of sent packets
    private int min_Seq;                      //The first packet in the window
    private int max_Seq;                      // The last packet in the window
    private int nexttoSend;                   //actual non-duplicated packet sent
    
    //Variables for calculation and report.
    private int totalsent;                    //total packets sent,including retransmission
    private int numCorrupted;                 //NUmber of corruption
    private int numACK;                       // Number of ACK we received
    private LinkedList<Double> rtt;           //Store all the rtt we used for sending packets
    private int pktSuccess;                   // number of packets we successfully sent from A to B
    private int numtoL5;                      // number of packets delivered to level 5
    private int numSeed;                      //number of seed
    
    //Variables that help track the state of B
    private int exptSeq;                      // the sequence number of the the packet the B is expected to receive
    
    // This is the constructor.  Don't touch!
    public StudentNetworkSimulator(int numMessages,
                                   double loss,
                                   double corrupt,
                                   double avgDelay,
                                   int trace,
                                   int seed,
                                   int winsize,
                                   double delay)
    {
        super(numMessages, loss, corrupt, avgDelay, trace, seed);
        WindowSize = winsize;
        LimitSeqNo = winsize+1;
        RxmtInterval = delay;
        numSeed = seed;
    }

    
    // This routine will be called whenever the upper layer at the sender [A]
    // has a message to send.  It is the job of your protocol to insure that
    // the data in such a message is delivered in-order, and correctly, to
    // the receiving upper layer.
    protected void aOutput(Message message){
      //Construct a new packet with the data in message
      String payload = message.getData();
      Packet p = new Packet(seqNum, 0, -1, payload);
      int checksum = checkSum(p);
      p.setChecksum(checksum);
      seqNum = nextNum(p.getSeqnum());

      //Add to list of packets
      sentPackets.add(p);
      //Send the packet to B if the window size allows
      if(sentPackets.indexOf(p) <= max_Seq){
        toLayer3(A,p);
        System.out.println("Sending packet to B:" + p.toString());
        
        //every time we send a packet, we will store the time in the rtt
        rtt.add(sentPackets.indexOf(p), getTime());
        if (min_Seq == nexttoSend){
          startTimer(A,RxmtInterval);
        }
        totalsent ++;
        nexttoSend ++;
      }
    }
      
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by a B-side procedure)
    // arrives at the A-side.  "packet" is the (possibly corrupted) packet
    // sent from the B-side.
    protected void aInput(Packet packet)
    {
      Double time = getTime();
      //Check if the ACK is corrupted
      if (isCorrupted(packet)) {
        System.out.println("A recieves corrupted ACK! Ignoring data." + packet.toString());
        numCorrupted++;
        return;
      }
      int acknum = packet.getAcknum();
      System.out.println("A received ACK for packet #" + packet.getSeqnum());
      
      //If we receive an ACK for a packet, we can assume that we have received
      //ACKs all the duplicates for this packet, and we can computer the rtt
      //for all the duplicated packets.
      while (min_Seq < sentPackets.size()){
        
        if(sentPackets.get(min_Seq).getSeqnum() == acknum){
          break;//this means we calculated rtt for all the duplicates
        }
        
        Double x =rtt.get(min_Seq);
        x = time-x;
        rtt.add(min_Seq, x);
        
        min_Seq ++;
        max_Seq ++;
      }
      
      Double x =rtt.get(min_Seq);
      x = time-x;
      rtt.add(min_Seq, x);
      
      min_Seq ++;
      max_Seq ++;
    }
    
    
    
      
      
    // This routine will be called when A's timer expires (thus generating a 
    // timer interrupt). You'll probably want to use this routine to control 
    // the retransmission of packets. See startTimer() and stopTimer(), above,
    // for how the timer is started and stopped. 
    protected void aTimerInterrupt()
    {
      System.out.println("\nTimer interrupt! Resending window");
      startTimer(A, RxmtInterval);
      
      //retransmit packets
      for(int i = min_Seq; ((i <= max_Seq) &&  (i < sentPackets.size())); i++){
        
        Packet p = sentPackets.get(i);
        toLayer3(A,p);
        totalsent++;
        
        //if the packet is sent for the first time, we will store the time to rtt
        if(i == nexttoSend) {
          rtt.add(i,getTime());
          System.out.println("Sending packet to B:" + p.toString());
          nexttoSend++; 
        }else{
          System.out.println("Resending packet to B:" + p.toString());
        }
      }
    }
    
    // This routine will be called once, before any of your other A-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity A).
    protected void aInit()
    {
      seqNum = FirstSeqNo;
      sentPackets = new LinkedList<Packet>();
      min_Seq = FirstSeqNo;
      max_Seq = min_Seq + WindowSize - 1;
      nexttoSend = FirstSeqNo;
      totalsent = 0;
      numCorrupted = 0;
      numACK = 0;
      rtt = new LinkedList<Double>(); 
    }
    
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by an A-side procedure)
    // arrives at the B-side.  "packet" is the (possibly corrupted) packet
    // sent from the A-side.
    protected void bInput(Packet packet)
    {
      pktSuccess ++;
      //Cheack if the packet if corrupted.
      if(isCorrupted(packet)){
        System.out.println("B received corrupted packet - ignoring data");
        numCorrupted++;
        return; 
      }
      //Check if the packet if arrived in order
      if(packet.getSeqnum() == exptSeq){
        Packet ack = new Packet(packet.getSeqnum(), exptSeq, -1, "");
        int check = checkSum(ack);
        ack.setChecksum(check);
        System.out.println("Sending ACK for packet #"+ ack.getAcknum());
        toLayer3(B,ack);
        numACK ++;
        
        exptSeq = nextNum(exptSeq);
        
        toLayer5(packet.getPayload());
        numtoL5 ++;
      }else{
        System.out.println("B received a packet out of order - ignoring data. Expecting #" + exptSeq + " received #" + packet.getSeqnum());
      }
    }
    
    // This routine will be called once, before any of your other B-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity B).
    protected void bInit()
    {
      exptSeq = FirstSeqNo;
      pktSuccess = 0;
    }

   
    
    // Use to print final statistics
    protected void Simulation_done()
    {
     System.out.println("\n+------------------------------------------------+");
     System.out.println("+-----------------Report-----------------------+");
     System.out.println("Seed #" + numSeed );
     System.out.println("Window Size: " + WindowSize + "\n");
     System.out.println("Total packets sent = " + totalsent);
     System.out.println("Number of original data packets sent: " + nexttoSend);
     System.out.println("Number of ACK packets sent: " + numACK);
     System.out.println("Total number of packets delivered to Layer 5: " + numtoL5);
     System.out.println("Number of retransmissions: " + (totalsent - nexttoSend));
     
     int numlost = totalsent - nexttoSend - numCorrupted;
     double pLost = (double) (numlost)/ (totalsent);      
     double pCorrupted = (double) numCorrupted/(totalsent-numlost); 
     
     System.out.println("Number of packets corrupted: " + numCorrupted);
     System.out.println("The probability of geting a corrupted packet: " + pCorrupted);
     System.out.println("Number of packets lost: " + numlost);
     System.out.println("The probability of geting a lost packet: " + pLost);
     System.out.print("Average RTT for packets: ");
     double avgRTT = 0;
     for(int i = 0; i < min_Seq; i ++){
       avgRTT += rtt.get(i);
     }
     avgRTT = avgRTT / min_Seq;
     System.out.println("" + avgRTT + " ms");
    } 

}