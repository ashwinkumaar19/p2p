package peerTopeer;

//import peerTopeer.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;


public class Peer{
    Packet ping;
    int peer_id;
    String localDir;
    static ArrayList<String> connectedpeers = new ArrayList<String>(); //nodes that have send a ping and received a pong
    ArrayList<Integer> neighbours = new ArrayList<Integer>(); //list of neighbours for each node
    static ArrayList<Integer> alreadypinged = new ArrayList<Integer>(); //nodes that have sent atleast one ping request
    int serverPort;
    int clientPort;
    
    static int id = 1000;

    Peer(int peer_id,String localDir,int serverPort,int clientPort){
        this.peer_id = peer_id;
        this.localDir = localDir;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }
    public static void main(String[] args) throws IOException{
        
        //start a peer
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the config filename: ");
        String fileName = sc.nextLine();

        Properties prop = new Properties();
        InputStream is = new FileInputStream(fileName);
		prop.load(is);

        System.out.println("Enter the peer id: ");
        int peerID = sc.nextInt();

        System.out.println("Enter the local directory: ");
        String localDir = sc.nextLine();

        
		int serverport = Integer.parseInt(prop.getProperty("peer"+peerID+".serverport"));
		int clientport = Integer.parseInt(prop.getProperty("peer"+peerID+".clientport"));
        
        Peer p = new Peer(peerID,localDir,serverport,clientport);
        //start pinging
        
        int r=0;
        while(r!=peerID) {
            Random random = new Random();
            r = random.nextInt(2); //change bound
            if (r==0) {
                continue;
            }
            if (r!=0) {
                if (!Peer.alreadypinged.contains(r)) {
                    String dest_address = prop.getProperty("peer"+ r + ".ip");
                    System.out.println("Ip to ping to: " + dest_address);
                    Pong pong_thread = new Pong(p);
                    pong_thread.start();
                    Ping ping_thread = new Ping(p,dest_address);
                    ping_thread.start();
                    Peer.alreadypinged.add(r);
                    break;
                }
                else {
                    continue;
                }
            }
        }

        
        sc.close();   
    }

}

