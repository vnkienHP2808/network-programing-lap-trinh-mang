package AQ39X6kw_TCP_DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Config.ConfigFile;
/*
 [Mã câu hỏi (qCode): AQ39X6kw].  Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây). 
 Yêu cầu xây dựng chương trình client thực hiện giao tiếp với server sử dụng luồng data (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B10DCCN003;C6D7E8F9"
b. Nhận lần lượt:
•	Một số nguyên k là độ dài đoạn.
•	Chuỗi chứa mảng số nguyên, các phần tử được phân tách bởi dấu phẩy ",".
Ví dụ: Nhận k = 3 và "1,2,3,4,5,6,7,8".
c. Thực hiện chia mảng thành các đoạn có độ dài k và đảo ngược mỗi đoạn, sau đó gửi mảng đã xử lý lên server. Ví dụ: Với k = 3 và mảng "1,2,3,4,5,6,7,8", kết quả là "3,2,1,6,5,4,8,7". Gửi chuỗi kết quả "3,2,1,6,5,4,8,7" lên server.
d. Đóng kết nối và kết thúc chương trình
 */

public class TCPClient {
    private static final int SERVER_PORT = 2207;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String qCode = "AQ39X6kw";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;

            System.out.println("Send StudentCode + qCode: " + initMessage);
            dos.writeUTF(initMessage);
            dos.flush();

            int k = dis.readInt();
            String data = dis.readUTF();


            dos.writeUTF(convert(data, k));
            dos.flush();

        } catch (UnknownHostException e) {
            System.err.println("Không thể tìm thấy host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi I/O: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Kết nối đã được đóng.");
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
    public static String convert(String data, int k){
        StringBuilder sb = new StringBuilder("");
        String[] dataSplit = data.split("\\,");

        for (int i = 0; i < dataSplit.length; i += k) {
        int end = Math.min(i + k, dataSplit.length);

        for (int j = end - 1; j >= i; j--) {
            sb.append(dataSplit[j]);
            if (j != i || end < dataSplit.length) sb.append(",");
        }
    }

        return sb.toString();
    }
}
