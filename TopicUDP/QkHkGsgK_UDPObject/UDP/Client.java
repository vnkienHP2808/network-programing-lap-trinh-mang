package TopicUDP.QkHkGsgK_UDPObject.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

public class Client {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        int svPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);
        DatagramPacket dp = null;

        String mess = ";" + ConfigFile.STUDENT_CODE + ";" + "Bt8TCUFl";
        dp = new DatagramPacket(mess.getBytes(), mess.length(), inetAddress, svPort);
        socket.send(dp);

        byte[] dpReceive = new byte[1024];
        dp = new DatagramPacket(dpReceive, dpReceive.length);
        socket.receive(dp);

        String requestId = new String(dp.getData(), 0, 8);
        ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData(), 8, dp.getLength()-8);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Product product = (Product)ois.readObject();

        product.setName(normalize(product.getName()));
        product.setQuantity(change(product.getQuantity()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(requestId.getBytes());
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(product);
        oos.flush();

        dp = new DatagramPacket(baos.toByteArray(), baos.size(), inetAddress, svPort);
        socket.send(dp);

        socket.close();
        ois.close();
        oos.close();
    }

    public static String normalize(String name){
        String[] data = name.split("\\s+");
        String result = "";
        
        result += data[data.length - 1] + " ";
        for(int i = 1; i < data.length - 1; i++){
            result += data[i] + " ";
        }
        return result + data[0];
    }

    public static int change(int quantity){
        int num = 0;
        while(quantity != 0){
            num = num * 10 + quantity%10;
            quantity /= 10;
        }
        return num;
    }
}
