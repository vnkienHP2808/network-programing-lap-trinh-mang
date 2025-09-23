package TopicUDP.UDP_Object_88AVBUkq.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): 88AVBUkq].  Thông tin khách hàng được yêu cầu thay đổi định dạng lại cho phù hợp với khu vực, cụ thể:
a.	Tên khách hàng cần được chuẩn hóa theo định dạng mới. Ví dụ: nguyen van hai duong -> DUONG, Nguyen Van Hai
b.	Ngày sinh của khách hàng đang ở dạng mm-dd-yyyy, cần được chuyển thành định dạng dd/mm/yyyy. Ví dụ: 10-11-2012 -> 11/10/2012
c.	Tài khoản khách hàng được tạo từ các chữ cái in thường được sinh tự động từ họ tên khách hàng. Ví dụ: nguyen van hai duong -> nvhduong


Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client giao tiếp với server theo mô tả sau:
a.	Đối tượng trao đổi là thể hiện của lớp UDP.Customer được mô tả như sau
•	Tên đầy đủ của lớp: UDP.Customer
•	Các thuộc tính: id String, code String, name String, , dayOfBirth String, userName String
•	Một Hàm khởi tạo với đầy đủ các thuộc tính được liệt kê ở trên
•	Trường dữ liệu: private static final long serialVersionUID = 20151107; 

b.	Client giao tiếp với server theo các bước
•       Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;EE29C059”

•	Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Customer từ server.
 Trong đó, các thuộc tính id, code, name,dayOfBirth đã được thiết lập sẵn.
•	Yêu cầu thay đổi thông tin các thuộc tính như yêu cầu ở trên và gửi lại đối tượng khách hàng đã được sửa đổi lên server với cấu trúc:
08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Customer đã được sửa đổi.
•	Đóng socket và kết thúc chương trình.
 */

public class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket dSocket = new DatagramSocket();

        int serverPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "88AVBUkq";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress,
                serverPort);
        dSocket.send(dpSendMessage);

        byte[] receiveData = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(dpReceive);

        String requestId = new String(dpReceive.getData(), 0, 8);
        ByteArrayInputStream input = new ByteArrayInputStream(dpReceive.getData(), 8, dpReceive.getLength() - 8);
        ObjectInputStream ois = new ObjectInputStream(input);

        Customer customer = (Customer) ois.readObject();
        customer.setUserName(newUserName(customer.getName()));
        customer.setName(normalizeName(customer.getName()));
        customer.setDayOfBirth(normalizeDob(customer.getDayOfBirth()));

        ByteArrayOutputStream ouput = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(ouput);
        oos.writeObject(customer);
        oos.flush();

        byte[] sendResult = new byte[8 + ouput.size()];
        System.arraycopy(requestId.getBytes(), 0, sendResult, 0, 8);
        System.arraycopy(ouput.toByteArray(), 0, sendResult, 8, ouput.size());

        DatagramPacket dpSendResult = new DatagramPacket(sendResult, sendResult.length, inetAddress, serverPort);
        dSocket.send(dpSendResult);

        dSocket.close();
    }

    public static String normalizeName(String name) {
        String[] data = name.split("\\s+");
        StringBuilder sb = new StringBuilder(data[data.length - 1].toUpperCase() + ", ");
        for (int i = 0; i < data.length - 1; i++) {
            sb.append(data[i].substring(0, 1).toUpperCase())
                    .append(data[i].substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    public static String normalizeDob(String dob) {
        String[] data = dob.split("\\-");
        return data[1] + "/" + data[0] + "/" + data[2];
    }

    public static String newUserName(String name) {
        String[] data = name.split("\\s+");
        String result = "";
        for (int i = 0; i < data.length - 1; i++) {
            result += (data[i].charAt(0)+"").toLowerCase();
        }
        result += data[data.length - 1].toLowerCase();
        return result;
    }
}
