package TopicUDP.GkvIxHMb_UDP_DataType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.TreeSet;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): GkvIxHMb].  
Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207.
Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN004;99D9F604”
b. Nhận thông điệp là một chuỗi từ server theo định dạng “requestId;z1,z2,...,z50” requestId là chuỗi ngẫu nhiên duy nhất
    z1 -> z50 là 50 số nguyên ngẫu nhiên
    c. Thực hiện tính số lớn thứ hai và số nhỏ thứ hai của thông điệp trong z1 -> z50 và gửi thông điệp lên server theo định dạng “requestId;secondMax,secondMin”
    d. Đóng socket và kết thúc chương trình
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();

        int SERVER_PORT =  2207;
        InetAddress iAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "GkvIxHMb";
        DatagramPacket dpSennMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), iAddress, SERVER_PORT);
        dSocket.send(dpSennMessage);

        byte[] data = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(data, data.length);
        dSocket.receive(dpReceive);

        String dataString = new String(dpReceive.getData());
        System.out.println("Data: " + dataString);

        String[] dataSplit = dataString.split("\\;");
        String requestId = dataSplit[0];
        String[] receiveData = dataSplit[1].split("\\,");

        TreeSet<Integer> treeSet = new TreeSet<>();
        for(String str : receiveData){
            int num = Integer.parseInt(str.trim());
            treeSet.add(num);
        }

        treeSet.removeFirst();
        treeSet.removeLast();

        String result = requestId + ";" + treeSet.getLast() + "," + treeSet.getFirst();
        System.out.println(result);

        DatagramPacket dpSendResult = new DatagramPacket(result.getBytes(), result.length(), iAddress, SERVER_PORT);
        dSocket.send(dpSendResult);

        dSocket.close();
    }
}
