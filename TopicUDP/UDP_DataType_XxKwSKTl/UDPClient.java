package TopicUDP.UDP_DataType_XxKwSKTl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 [Mã câu hỏi (qCode): XxKwSKTl].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a.	Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;DC73CA2E”
b.	Nhận thông điệp là một chuỗi từ server theo định dạng “requestId;a1,a2,...,a50” 
-	requestId là chuỗi ngẫu nhiên duy nhất
-	a1 -> a50 là 50 số nguyên ngẫu nhiên
c.	Thực hiện tìm giá trị lớn nhất và giá trị nhỏ nhất thông điệp trong a1 -> a50 và gửi thông điệp lên lên server theo định dạng “requestId;max,min”
d.	Đóng socket và kết thúc chương trình
 */

import Config.ConfigFile;

public class UDPClient {
    private static final int SERVER_PORT = 2207;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        InetAddress is = InetAddress.getByName(SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE+";" + "XxKwSKTl";
        DatagramPacket dpSend = new DatagramPacket(initMessage.getBytes(), initMessage.length(), is,SERVER_PORT);

        dSocket.send(dpSend);

        byte[] data = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(data, data.length);
        dSocket.receive(dpReceive);

        String dataString = new String(dpReceive.getData());
        System.out.println("Data: " + dataString);
        
        String[] dataSplit = dataString.split("\\;");
        // dataSplit[0] = requestId;
        int numMin = Integer.MAX_VALUE;
        int numMax = Integer.MIN_VALUE;
        String[] dataString2 = dataSplit[1].split("\\,");
        for(String str : dataString2){
            int x = Integer.parseInt(str.trim());
            if(x < numMin) numMin = x;
            if(x > numMax) numMax = x;
        }

        String result = dataSplit[0] + ";" + numMax + "," + numMin;
        DatagramPacket dpResult = new DatagramPacket(result.getBytes(), result.length(), is, SERVER_PORT);
        dSocket.send(dpResult);
        dSocket.close();
    }
}
