package TopicUDP.Pdv3vTwv_UDP_String;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): Pdv3vTwv].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: ";B15DCCN009;EF56GH78"
b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;data", với:
•	requestId là chuỗi ngẫu nhiên duy nhất.
•	data là một chuỗi ký tự chứa nhiều từ, được phân cách bởi dấu cách.
Ví dụ: "EF56GH78;The quick brown fox"
c. Sắp xếp các từ trong chuỗi theo thứ tự từ điển ngược (z đến a) và gửi thông điệp lên server theo định dạng "requestId;word1,word2,...,wordN".
Ví dụ: Với data = "The quick brown fox", kết quả là: "EF56GH78;quick,fox,brown,The"
d. Đóng socket và kết thúc chương trình
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int SERVER_PORT = 2208;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "Pdv3vTwv";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpSendMessage);

        byte[] data = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(data, data.length);
        dSocket.receive(dpReceive);

        String[] dataString = new String(dpReceive.getData(), 0, dpReceive.getLength()).trim().split("\\;");
        String requestId = dataString[0];
        List<String> list = Arrays.asList(dataString[1].split("\\s+"));

        Collections.sort(list, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        // String.CASE_INSENSITIVE_ORDER: bỏ qua so sánh hoa thường
        // reverseOrder: đảo ngược lại
        
        String result = requestId + ";" + String.join(",", list);

        System.out.println(result);
        DatagramPacket dpSendResult = new DatagramPacket(result.getBytes(), result.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpSendResult);

        dSocket.close();
    }
}
