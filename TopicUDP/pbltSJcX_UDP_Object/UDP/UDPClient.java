package TopicUDP.pbltSJcX_UDP_Object.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): pbltSJcX].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209.
  Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau:
Đối tượng trao đổi là thể hiện của lớp UDP.Student được mô tả:
•	Tên đầy đủ lớp: UDP.Student
•	Các thuộc tính: id String,code String, name String, email String
•	02 Hàm khởi tạo: 
o	public Student(String id, String code, String name, String email)
o	public Student(String code)
•	Trường dữ liệu: private static final long serialVersionUID = 20171107
Thực hiện:
•       Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;EE29C059”
b.	Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Student từ server. 
Trong đó, các thông tin được thiết lập gồm id và name.
c.	Yêu cầu:
-	Chuẩn hóa tên theo quy tắc: Chữ cái đầu tiên in hoa, các chữ cái còn lại in thường và gán lại thuộc tính name của đối tượng
-	Tạo email ptit.edu.vn từ tên người dùng bằng cách lấy tên và các chữ cái bắt đầu của họ và tên đệm.
 Ví dụ: nguyen van tuan nam -> namnvt@ptit.edu.vn. Gán giá trị này cho thuộc tính email của đối tượng nhận được
-	Gửi thông điệp chứa đối tượng xử lý ở bước c lên Server với cấu trúc: 08 byte đầu chứa chuỗi requestId 
và các byte còn lại chứa đối tượng Student đã được sửa đổi.
d.	Đóng socket và kết thúc chương trình.
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int serverPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "pbltSJcX";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, serverPort);
        dSocket.send(dpSendMessage);

        byte[] receiveData = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(dpReceive);

        String requestId = new String(dpReceive.getData(), 0, 8);
        ByteArrayInputStream input = new ByteArrayInputStream(dpReceive.getData(), 8, dpReceive.getLength()-8);
        ObjectInputStream ois = new ObjectInputStream(input);

        Student student = (Student) ois.readObject();
        student.setEmail(autoEmail(student.getName()));
        student.setName(normalizeName(student.getName()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(student);
        oos.flush();

        byte[] sendResult = new byte[8 + output.size()];
        System.arraycopy(requestId.getBytes(), 0, sendResult, 0, 8);
        System.arraycopy(output.toByteArray(), 0, sendResult, 8, output.size());

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

    public static String autoEmail(String name){
        String[] data = name.split("\\s+");
        StringBuilder sb = new StringBuilder(data[data.length-1].toLowerCase());
        for(int i = 0; i < data.length-1; i++){
            sb.append((data[i].charAt(0)+"").toLowerCase());
        }
        return sb.toString() + "@ptit.edu.vn";
    }
}
