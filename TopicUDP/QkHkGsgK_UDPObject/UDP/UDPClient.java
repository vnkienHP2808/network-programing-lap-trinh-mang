package TopicUDP.QkHkGsgK_UDPObject.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/* 
 [Mã câu hỏi (qCode): QkHkGsgK].  Thông tin sản phẩm vì một lý do nào đó đã bị sửa đổi thành không đúng, cụ thể:
a.	Tên sản phẩm bị đổi ngược từ đầu tiên và từ cuối cùng, ví dụ: “lenovo thinkpad T520” bị chuyển thành “T520 thinkpad lenovo”
b.	Số lượng sản phẩm cũng bị đảo ngược giá trị, ví dụ từ 9981 thành 1899

Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client giao tiếp với server để gửi/nhận các sản phẩm theo mô tả dưới đây:
a.	Đối tượng trao đổi là thể hiện của lớp Product được mô tả như sau
•	Tên đầy đủ của lớp: UDP.Product
•	Các thuộc tính: id String, code String, name String, quantity int
•	Một hàm khởi tạo có đầy đủ các thuộc tính được liệt kê ở trên
•	Trường dữ liệu: private static final long serialVersionUID = 20161107; 
b.	Giao tiếp với server theo kịch bản
•       Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;EE29C059”

•	Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Product từ server. 
Trong đối tượng này, các thuộc tính id, name và quantity đã được thiết lập giá trị.
•	Sửa các thông tin sai của đối tượng về tên và số lượng như mô tả ở trên và gửi đối tượng vừa được sửa đổi lên server theo cấu trúc:
08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Product đã được sửa đổi.
•	Đóng socket và kết thúc chương trình.
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();

        int serverPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);
        
        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "QkHkGsgK";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, serverPort);
        dSocket.send(dpSendMessage);

        byte[] receiveData = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(dpReceive);

        String requestId = new String(dpReceive.getData(), 0, 8);
        ByteArrayInputStream input = new ByteArrayInputStream(dpReceive.getData(), 8, dpReceive.getLength()-8);
        ObjectInputStream ois = new ObjectInputStream(input);
        Product product = (Product) ois.readObject();

        product.setName(normalize(product.getName()));
        product.setQuantity(change(product.getQuantity()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(product);
        oos.flush();

        //tạo mảng data mới để gửi
        byte[] sendResult = new byte[8 + output.size()];
        System.arraycopy(requestId.getBytes(), 0, sendResult, 0, 8);
        System.arraycopy(output.toByteArray(), 0, sendResult, 8, output.size());

        DatagramPacket dpSendResult = new DatagramPacket(sendResult, sendResult.length, inetAddress, serverPort);
        dSocket.send(dpSendResult);

        dSocket.close();
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
