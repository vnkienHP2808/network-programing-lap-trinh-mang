package TopicUDP.zYXFXVIP_UDP_String;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): zYXFXVIP].  Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208.
Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản dưới đây:
a.	Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;5B35BCC1”
b.	Nhận thông điệp từ server theo định dạng “requestId;data” 
-	requestId là một chuỗi ngẫu nhiên duy nhất
-	data là chuỗi dữ liệu cần xử lý
c.	Xử lý chuẩn hóa chuỗi đã nhận thành theo nguyên tắc 
i.	Ký tự đầu tiên của từng từ trong chuỗi là in hoa
ii.	Các ký tự còn lại của chuỗi là in thường
Gửi thông điệp chứa chuỗi đã được chuẩn hóa lên server theo định dạng “requestId;data”
d.	Đóng socket và kết thúc chương trình
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int SERVER_PORT = 2208;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "zYXFXVIP";
        DatagramPacket dpMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpMessage);

        byte[] data = new byte[8192];
        DatagramPacket dpReceive = new DatagramPacket(data, data.length);
        dSocket.receive(dpReceive);

        String[] dataString = new String(dpReceive.getData(), 0, dpReceive.getLength()).split("\\;");
        String requestId = dataString[0].trim();
        String line = dataString[1].trim();

        String result = requestId + ";" + process(line);
        DatagramPacket dpSendResult = new DatagramPacket(result.getBytes(), result.length(), inetAddress, SERVER_PORT);
        dSocket.send(dpSendResult);

        dSocket.close();
    }

    public static String process(String line){
        String[] lineSplit = line.split("\\s+");
        StringBuilder sb = new StringBuilder("");

        for(String str : lineSplit){
            sb.append(str.substring(0, 1).toUpperCase())
                .append(str.substring(1).toLowerCase())
                .append(" ");
        }
        return sb.toString().trim();
    }

}
