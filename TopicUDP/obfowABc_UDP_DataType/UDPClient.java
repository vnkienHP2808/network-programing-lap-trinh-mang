package TopicUDP.obfowABc_UDP_DataType;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): obfowABc].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. 
 Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode".
Ví dụ: ";B15DCCN010;D3F9A7B8"
b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;a;b", với:
•	requestId là chuỗi ngẫu nhiên duy nhất.
•	a và b là chuỗi thể hiện hai số nguyên lớn (hơn hoặc bằng 10 chữ số).
Ví dụ: "X1Y2Z3;9876543210;123456789"
c. Tính tổng và hiệu của hai số a và b, gửi thông điệp lên server theo định dạng "requestId;sum,difference".Ví dụ: 
Nếu nhận được "X1Y2Z3;9876543210;123456789", tổng là 9999999999 và hiệu là 9753086421. Kết quả gửi lại sẽ là "X1Y2Z3;9999999999,9753086421".
d. Đóng socket và kết thúc chương trình
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int SERVER_PORT = 2207;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "obfowABc";
        DatagramPacket dpMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpMessage);

        byte[] data = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(data, data.length);
        dSocket.receive(dpReceive);

        String[] dataReceive = new String(dpReceive.getData(), 0, dpReceive.getLength()).split("\\;");
        String requestId = dataReceive[0].trim();

        BigInteger num1 = new BigInteger(dataReceive[1].trim());
        BigInteger num2 = new BigInteger(dataReceive[2].trim());
        System.out.println(num1.toString() + " ; " + num2.toString());

        BigInteger sum = num1.add(num2);
        BigInteger difference = num1.subtract(num2);

        String result = requestId + ";" + sum.toString() + "," + difference.abs().toString();
        System.out.println(result);
        DatagramPacket dpSendResult = new DatagramPacket(result.getBytes(), result.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpSendResult);

        dSocket.close();
    }
}
