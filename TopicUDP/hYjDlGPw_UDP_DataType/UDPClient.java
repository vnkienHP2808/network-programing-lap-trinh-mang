package TopicUDP.hYjDlGPw_UDP_DataType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import Config.ConfigFile;

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        int serverPort = 2207;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);
        DatagramPacket dp = null;

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "hYjDlGPw";
        dp = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, serverPort);
        socket.send(dp);

        byte[] dpSend = new byte[1024];
        dp = new DatagramPacket(dpSend, dpSend.length);
        socket.receive(dp);

        String data = new String(dp.getData(), 0, dp.getLength());
        System.out.println("==>Data: " + data);

        String[] dataSplit = data.trim().split("\\;");
        String requestId = dataSplit[0];
        int n = Integer.parseInt(dataSplit[1]);
        int k = Integer.parseInt(dataSplit[2]);
        String[] arr = dataSplit[3].trim().split("\\,");
        List<Integer> list = new ArrayList<>();

        for(int i = 0; i < arr.length; i++){
            list.add(Integer.parseInt(arr[i]));
        }

        String result = "";
        for(int i = 0; i <= n-k; i++){
            TreeSet<Integer> set = new TreeSet<>();
            for(int j = 0; j < k; j++){
                set.add(list.get(i + j));
            }

            result += set.getLast() + ",";
        }

        
        String finalResult = requestId + ";" + result.substring(0, result.length()-1);
        System.out.println("Result: " + finalResult);
        dp = new DatagramPacket(finalResult.getBytes(), finalResult.length(), inetAddress, serverPort);
        socket.send(dp);

        socket.close();
    }   
}
