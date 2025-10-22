package TopicUDP.h2M8lLi9_UDP_String;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.TreeSet;

import Config.ConfigFile;

public class UDPClient {
    public static void main(String[] args) throws Exception{
        int svPort = 2208;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket dp = null;

        String mess = ";" + ConfigFile.STUDENT_CODE + ";" + "h2M8lLi9";
        dp = new DatagramPacket(mess.getBytes(), mess.length(), inetAddress, svPort);
        socket.send(dp);

        byte[] dpSend = new byte[1024];
        dp = new DatagramPacket(dpSend, dpSend.length);
        socket.receive(dp);

        System.out.println("==>Data: " + (new String(dp.getData(), 0, dp.getLength())));
        String[] data = (new String(dp.getData(), 0, dp.getLength())).trim().split("\\;");
        String requestId = data[0];
        String str1 = data[1];
        String str2 = data[2];

        TreeSet<Character> treeSet = new TreeSet<>();
        for(int i = 0; i < str2.length(); i++){
            treeSet.add(str2.charAt(i));
        }

        String result = requestId + ";";
        for(int i = 0; i < str1.length(); i++){
            if(!treeSet.contains(str1.charAt(i))){
                result += str1.charAt(i);
            }
        }

        System.out.println("==>Result: " + result);
        dp = new DatagramPacket(result.getBytes(), result.length(), inetAddress, svPort);
        socket.send(dp);

        socket.close();
    }
}
