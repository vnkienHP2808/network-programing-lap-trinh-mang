package TopicTCP.TCP_ByteStream_9uS4mZbv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.lang.Math;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): 9uS4mZbv].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). 
 Yêu cầu xây dựng chương trình client thực hiện kết nối tới server sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B16DCCN999;C89DAB45"
b. Nhận dữ liệu từ server là một chuỗi các số nguyên được phân tách bởi ký tự ",".
Ví dụ: "8,4,2,10,5,6,1,3"
c. Tính tổng của tất cả các số nguyên tố trong chuỗi và gửi kết quả lên server.
Ví dụ: Với dãy "8,4,2,10,5,6,1,3", các số nguyên tố là 2, 5, 3, tổng là 10. Gửi lên server chuỗi "10".
d. Đóng kết nối và kết thúc chương trình.
 */
public class TCPClient{
    private static final int SERVER_PORT = 2206;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        String qCode = "9uS4mZbv";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;

        os.write(initMessage.getBytes());
        os.flush();

        byte[] data = new byte[1024];
        int dataRead = is.read(data);

        if(dataRead != -1){
            String dataString = new String(data, 0, dataRead);
            System.out.println("Đã nhận dữ liệu từ SV");
            String result = String.valueOf(cal(dataString));

            os.write(result.getBytes());
            os.flush();
            System.out.println("Kết quả: " + result);
        }

        socket.close();
    }

    public static int cal(String data){
        int sum = 0;
        String[] dataSplit = data.split("\\,");
        for(String s : dataSplit){
            int x = Integer.parseInt(s);
            if(ngto(x)) sum += x;
        }
        return sum;
    }

    public static boolean ngto(int x){
        if(x == 2) return false;
        if(x > 2 && x % 2 == 0) return false;
        for(int i = 3; i <= Math.sqrt(x); i+=2){
            if(x % i == 0) return false;
        }

        return true;
    }
}
