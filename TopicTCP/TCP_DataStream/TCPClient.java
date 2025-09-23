package TopicTCP.TCP_DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): ImkOzHOD].  Một chương trình máy chủ cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5s),
  yêu cầu xây dựng chương trình (tạm gọi là client) thực hiện kết nối tới server tại cổng 2207, sử dụng luồng byte dữ liệu (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự: 
a.	Gửi chuỗi là mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;1D25ED92"
b.	Nhận lần lượt hai số nguyên a và b từ server
c.	Thực hiện tính toán tổng, tích và gửi lần lượt từng giá trị theo đúng thứ tự trên lên server
d.	Đóng kết nối và kết thúc
 */

public class TCPClient {
    private static final int SERVER_PORT = 2207;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) {
        Socket socket = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String qCode = "ImkOzHOD";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            dos.writeUTF(initMessage);
            dos.flush();

            int a, b;
            try {
                a = dis.readInt();
                b = dis.readInt();
            } catch (EOFException eof) {
                return;
            }

            int sum = a + b;
            int mul = a * b;

            dos.writeInt(sum);
            dos.flush();

            dos.writeInt(mul);
            dos.flush();

            System.out.println("Đã gửi: Tổng = " + sum + ", Tích = " + mul);
        } catch (UnknownHostException e) {
            System.err.println("Không tìm thấy host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi IO: " + e.getMessage());
        } finally {
            try {
                if (dis != null) dis.close();
                if (dos != null) dos.close();
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Kết nối đã được đóng.");
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
