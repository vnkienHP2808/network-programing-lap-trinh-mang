package uIsegIUB_TCP_ByteStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): uIsegIUB].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B16DCCN999;E56FAB67"
b. Nhận dữ liệu từ server là một chuỗi các số nguyên được phân tách bởi ký tự ",".
Ví dụ: " 3,7,2,5,8,1"
c. Tìm vị trí mà độ lệch của tổng bên trái và tổng bên phải là nhỏ nhất -> Gửi lên server vị trí đó, tổng trái, tổng phải và độ lệch. Ví dụ: với dãy " 3,7,2,5,8,1", vị trí 3 có độ lệch nhỏ nhất = 3 → Kết quả gửi server: "3,12,9,3"
d. Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient {
    private static final int SERVER_PORT = 2206;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        String qCode = "uIsegIUB";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        System.out.println("Send init code to server: " + initMessage);
        os.write(initMessage.getBytes());
        os.flush();

        
        byte[] data = new byte[1024];
        int dataRead = is.read(data);

        if(dataRead != -1){
            String dataString = new String(data, 0, dataRead);
            System.out.println("Data from server: " + dataString);

            String result = process(dataString);
            System.out.println("Result: " + result);

            os.write(result.getBytes());
            os.flush();
        }
        socket.close();
    }

    public static String process(String data){
        String[] dataString = data.split("\\,");
        int[] sumArray = new int[dataString.length];

        for(int i = 0; i < dataString.length; i++){
            if(i == 0) sumArray[i] = Integer.parseInt(dataString[i]);
            else{
                sumArray[i] = sumArray[i-1] + Integer.parseInt(dataString[i]);
            }
        }

        int idx = 0;
        int dolech = Integer.MAX_VALUE;
        int left = 0;
        int right = 0;
        for(int i = 1; i < sumArray.length-1; i++){
            int sumLeft = sumArray[i-1];
            int sumRight = sumArray[sumArray.length-1] - sumArray[i];

            if(Math.abs(sumRight - sumLeft) < dolech){
                dolech = Math.abs(sumRight - sumLeft);
                left = sumLeft;
                right = sumRight;
                idx = i;
            }
        }

        return idx + "," + left + "," + right + "," + Math.abs(left-right);
    }
}
