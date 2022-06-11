 package peerTopeer;
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
    static ArrayList<String> connectedpeers = new ArrayList<String>();
    ArrayList<Integer> neighbours = new ArrayList<Integer>();
    static ArrayList<Integer> alreadypinged = new ArrayList<Integer>();
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
            r = random.nextInt(10); //change bound
            if (r!=0){
                String dest_address = prop.getProperty("peer"+ r + ".ip");
                Ping ping_thread = new Ping(p,dest_address);
                ping_thread.start();
                Peer.alreadypinged.add(r);
            }

        }

        
        sc.close();   
    }

}

