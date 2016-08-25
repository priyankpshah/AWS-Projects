
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author priyankpshah
 */
public class udpserver extends Thread {

    static int iter = 100;
    static int packetsize;
    static int port = 7678;
    static int threadno;
    int portNumber = 0;
    String Hostname;

    public udpserver(int i, String hn) {
        this.portNumber = i;
        this.Hostname = hn;
        
    }
    //run method for thread class
    public void run() {
        try {
            
            DatagramSocket ds = new DatagramSocket(this.portNumber);
            byte[] buf = new byte[packetsize];

            DatagramPacket dp = new DatagramPacket(buf, packetsize);
            ds.receive(dp);
            //receive data from client
            for (int i = 0; i < iter; i++) {
                buf = dp.getData();
            }
           
            //Passing hostname from user.
            InetAddress ip = InetAddress.getByName(Hostname);
            DatagramPacket send = new DatagramPacket(buf, buf.length, ip, dp.getPort());
            //send data to client
            for (int i = 0; i < iter; i++) {
                ds.send(dp);
            }
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        packetsize = Integer.parseInt(args[0]);    //packet size from user
        threadno = Integer.parseInt(args[1]);   //no of thread to run   
        String hn = args[2];                // hostname from user
        udpserver us[] = new udpserver[threadno];

        for (int i = 0; i < threadno; i++) {
            us[i] = new udpserver(port + i,hn);
           
            us[i].start();
            
        }
        for (int i = 0; i < threadno; i++) {
            us[i].join();
        }
    }
}
