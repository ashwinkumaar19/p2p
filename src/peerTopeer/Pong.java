package peerTopeer;
//Pong is like server
import java.net.*;
import java.io.*;
public class Pong{
    Packet pongPacket;
    Pong(){
        pongPacket = new Packet(); 
    }
    public Packet createPacket(String dest_address,int id) throws UnknownHostException{
        InetAddress localhost = InetAddress.getLocalHost();
        pongPacket.packetID = id;
        pongPacket.payload_Desc = "pong";
        pongPacket.TTL = 10; //change value
        pongPacket.source_address = new String(localhost.getHostAddress()); 
        pongPacket.flag = 0;
        pongPacket.destination_address = dest_address;
        pongPacket.filename = null;
        return pongPacket;
    }
    public String createConnection(int serverPort) throws IOException,ClassNotFoundException{
        //server socket waits for client to connect
        ServerSocket socket = new ServerSocket(serverPort);
        Socket connect = socket.accept();
        System.out.println("Connected to "+pongPacket.destination_address);

        //on connecting, receives a packet
        InputStream inputstream = connect.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputstream);
        Packet receivedPacket = (Packet)objectInputStream.readObject();
        if (receivedPacket!=null){
            System.out.println("Received a ping packet");

            //creates a pong packet and sends it
            Packet pong = new Packet();
            pong = this.createPacket(receivedPacket.source_address,++receivedPacket.packetID);
            OutputStream outputstream = connect.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
            System.out.println("Sending a pong packet");
            objectOutputStream.writeObject(pong);

            //function returns its local ip to further continue pinging the other peers
            InetAddress localhost = InetAddress.getLocalHost();
            String localIP = new String(localhost.getHostAddress());
            socket.close();

            return localIP; 
        }
        System.out.println("Invalid ping packet");       
    }
}