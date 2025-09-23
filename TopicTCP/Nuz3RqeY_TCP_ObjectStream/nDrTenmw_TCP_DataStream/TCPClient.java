package TopicTCP.Nuz3RqeY_TCP_ObjectStream.nDrTenmw_TCP_DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Config.ConfigFile;

/* 
 [Mã câu hỏi (qCode): nDrTenmw].  Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây).
  Yêu cầu xây dựng chương trình client thực hiện giao tiếp với server sử dụng luồng data (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B10DCCN002;B4C5D6E7"
b. Nhận chuỗi chứa mảng số nguyên từ server, các phần tử được phân tách bởi dấu phẩy ",". Ví dụ: "1,3,2,5,4,7,6"
c. Tính số lần đổi chiều và tổng độ biến thiên trong dãy số.
- Đổi chiều: Khi dãy chuyển từ tăng sang giảm hoặc từ giảm sang tăng 
-   Độ biến thiên: Tổng giá trị tuyệt đối của các hiệu số liên tiếp
Gửi lần lượt lên server: số nguyên đại diện cho số lần đổi chiều, sau đó là số nguyên đại diện cho tổng độ biến thiên. Ví dụ: Với mảng "1,3,2,5,4,7,6", số lần đổi chiều: 5 lần, Tổng độ biến thiên 11 -> Gửi lần lượt số nguyên 5 và 11 lên server.
d. Đóng kết nối và kết thúc chương trình.
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

            String qCode = "nDrTenmw";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            System.out.println("Student Code and qCode: " + initMessage);
            dos.writeUTF(initMessage);
            dos.flush();

            String data;
            try {
                data = dis.readUTF();
            } catch (EOFException eof) {
                return;
            }
            System.out.println(data);
            String[] result = process(data).split("\\,");
            System.out.println("count change and sum: " + result[0] + "," + result[1]);
            dos.writeInt(Integer.parseInt(result[0]));
            dos.flush();

            dos.writeInt(Integer.parseInt(result[1]));
            dos.flush();
        } catch (UnknownHostException e) {
            System.err.println("Không tìm thấy host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi IO: " + e.getMessage());
        } finally {
            try {
                if (dis != null)
                    dis.close();
                if (dos != null)
                    dos.close();
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Kết nối đã được đóng.");
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public static String process(String data) {
        String[] dataSplit = data.split("\\,");
        int isIncrease = 0; // 1: tăng; -1: giảm
        int change = 0;
        int sum = 0;
        for (int i = 0; i < dataSplit.length - 1; i++) {
            int x = Integer.parseInt(dataSplit[i]);
            int y = Integer.parseInt(dataSplit[i + 1]);
            
            sum += Math.abs(x - y);

            if(i != 0){
                if ((x < y && isIncrease == -1) || (x > y && isIncrease == 1)){
                        change++;
                }
            }
            if(x < y) isIncrease = 1;
            else if(x > y) isIncrease = -1;
        }
        return change + "," + sum;
    }
}
