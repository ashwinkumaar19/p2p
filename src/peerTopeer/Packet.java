package peerTopeer;
import java.io.Serializable;
public class Packet implements Serializable{

    int packetID;
    int peer_id;
    String payload_Desc;
    int TTL;
    int hops;
    int flag;
    String source_address;
    String destination_address;
    String filename;

    /*Packet(int PacketID,String payload_Desc,int TTL,int hops,String source_address,String filename)
    {
        this.PacketID = PacketID;
        this.payload_Desc = payload_Desc;
        this.TTL = TTL;
        this.hops = hops;
        this.source_address = source_address;
        this.filename = filename;
    }*/

    
}
