package peerTopeer;
//Ping is like client

//import peerTopeer.*;
import java.net.*;
import java.io.*;
public class Ping extends Thread{
    Peer p;
    Packet pingPacket;
    String dest_address;
    Ping(Peer p, String dest_address){
        //super();
        pingPacket = new Packet();
        this.p = p; 
        this.dest_address = dest_address;
    }
    public void createPacket() throws UnknownHostException{
        InetAddress localhost = InetAddress.getLocalHost();
        pingPacket.packetID = Peer.id;
        pingPacket.peer_id = p.peer_id;
        Peer.id++;
        pingPacket.payload_Desc = "ping";
        pingPacket.TTL = 10; //change value
        pingPacket.source_address = new String(localhost.getHostAddress()); 
        pingPacket.flag = 0;
        pingPacket.destination_address = this.dest_address;
        pingPacket.filename = null; 
    }
    public void createConnection() throws IOException,ClassNotFoundException{

        //sending ping packet

        Socket socket = new Socket(pingPacket.destination_address,p.clientPort);
        System.out.println("Connected to "+pingPacket.destination_address);
        OutputStream outputstream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
        System.out.println("Sending a ping packet");
        objectOutputStream.writeObject(pingPacket);


        //receiving pong packet
        InputStream inputstream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputstream);       
        Packet pongPacket = (Packet) objectInputStream.readObject();

        //add to connected peers list
        if (pongPacket!= null) {
                (Peer.connectedpeers).add(pingPacket.source_address);
                (p.neighbours).add(pongPacket.peer_id);
        }
        socket.close();    
    }
    public void run() {
        try {
            this.createPacket();
        } catch(UnknownHostException u) {
            u.printStackTrace();
        }

        try {
            this.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}