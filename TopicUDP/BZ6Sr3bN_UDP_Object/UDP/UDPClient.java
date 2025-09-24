package TopicUDP.BZ6Sr3bN_UDP_Object.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import Config.ConfigFile;

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int serverPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "BZ6Sr3bN";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, serverPort);
        dSocket.send(dpSendMessage);

        byte[] receiveData = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(dpReceive);

        String requestId = new String(dpReceive.getData(), 0, 8);
        ByteArrayInputStream input = new ByteArrayInputStream(dpReceive.getData(), 8, dpReceive.getLength()-8);
        ObjectInputStream ois = new ObjectInputStream(input);

        Employee employee = (Employee) ois.readObject();
        employee.setSalary(increaseSalary(employee.getSalary(), employee.getHireDate()));
        employee.setHireDate(normalizeHireDate(employee.getHireDate()));
        employee.setName(normalizeName(employee.getName()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(employee);
        oos.flush();

        byte[] sendResult = new byte[8 + outputStream.size()];
        System.arraycopy(requestId.getBytes(), 0, sendResult, 0, 8);
        System.arraycopy(outputStream.toByteArray(), 0, sendResult, 8, outputStream.size());

        DatagramPacket dpSendResult = new DatagramPacket(sendResult, sendResult.length, inetAddress, serverPort);
        dSocket.send(dpSendResult);

        dSocket.close();
    }

    public static String normalizeName(String name){
        String[] data = name.split("\\s+");
        StringBuilder sb = new StringBuilder("");
        for(String s : data){
            sb.append(s.substring(0,1).toUpperCase())
                .append(s.substring(1).toLowerCase())
                .append(" ");
        }
        return sb.toString().trim();
    }

    public static String normalizeHireDate(String hireDate) {
        String[] data = hireDate.split("\\-");
        return data[2] + "/" + data[1] + "/" + data[0];
    }

    public static double increaseSalary(double salary, String hireDate){
        int sum = 0;
        for(int i = 0; i < 4; i++){
            sum += hireDate.charAt(i) - '0'; 
        }
        double result = salary * (1.0 + 1.0 * sum / 100);

        return Math.round(result * 100.0) / 100.0;
    }
}
