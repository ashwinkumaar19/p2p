package peerTopeer;
//Pong is like server
//import peerTopeer.*;
import java.net.*;
import java.io.*;
public class Pong extends Thread{
    Peer p;
    Packet pongPacket;
    String dest_address;
    Pong(Peer p){
        this.p = p;
        pongPacket = new Packet(); 
    }
    public Packet createPacket(String dest_address) throws UnknownHostException{
        InetAddress localhost = InetAddress.getLocalHost();
        pongPacket.packetID = Peer.id;
        Peer.id++;
        pongPacket.peer_id = p.peer_id;
        pongPacket.payload_Desc = "pong";
        pongPacket.TTL = 10; //change value
        pongPacket.source_address = new String(localhost.getHostAddress()); 
        pongPacket.flag = 0;
        pongPacket.destination_address = dest_address;
        pongPacket.filename = null;
        return pongPacket;
    }
    public String createConnection() throws IOException,ClassNotFoundException{
        //server socket waits for client to connect
        ServerSocket socket = new ServerSocket(p.serverPort);
        Socket connect = socket.accept();
        System.out.println("Connected to "+pongPacket.destination_address);

        //on connecting, receives a packet
        InputStream inputstream = connect.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputstream);
        Packet receivedPacket = (Packet)objectInputStream.readObject();
        this.dest_address = receivedPacket.source_address;
        if (receivedPacket!=null){
            System.out.println("Received a ping packet");

            //creates a pong packet and sends it
            Packet pong = new Packet();
            pong = this.createPacket(this.dest_address);
            OutputStream outputstream = connect.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
            System.out.println("Sending a pong packet");
            objectOutputStream.writeObject(pong);

            //function returns its local ip to further continue pinging the other peers
            InetAddress localhost = InetAddress.getLocalHost();
            String localIP = new String(localhost.getHostAddress());
            socket.close();

            return localIP; 
        } else {
            System.out.println("Invalid ping packet");
            socket.close();
            return null; 
        }
    }

    public void run(){
        try{
            this.createConnection();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}