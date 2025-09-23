package TopicTCP.TCP_ByteStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): H1Bwwub6].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). 
Yêu cầu xây dựng chương trình client thực hiện kết nối tới server trên sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự: 
a.	Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: "B16DCCN999;C64967DD"
b.	Nhận dữ liệu từ server là một chuỗi gồm các giá trị nguyên được phân tách với nhau bằng  "|"
Ex: 2|5|9|11
c.	Thực hiện tìm giá trị tổng của các số nguyên trong chuỗi và gửi lên server
Ex: 27
d.	Đóng kết nối và kết thúc
 */

public class TCPClient {
    private static final int SERVER_PORT = 2206;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;
    
    public static void main(String[] args) throws IOException{
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connect to Server: " + SERVER_HOST + ":" + SERVER_PORT);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            String qCode = "H1Bwwub6";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            System.err.println("Send MSV + qCode: " + initMessage);

            outputStream.write(initMessage.getBytes());
            outputStream.flush();

            byte[] data = new byte[1024];
            int dataRead = inputStream.read(data);

            if(dataRead != -1){
                String dataString = new String(data, 0, dataRead);
                System.out.println("Get data from server!");
                String sum = String.valueOf(cal(dataString));

                outputStream.write(sum.getBytes());
                outputStream.flush();

                System.out.println("Calculate success: " + sum);
            }
            socket.close();
    }

    public static long cal(String data){
        String[] stringSplit = data.split("\\|");
        return Arrays.stream(stringSplit).mapToLong(Long::parseLong).sum();
    }
}
