package TopicTCP.pllRqziB_TCP_ByteStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Config.ConfigFile;

/* 
 [Mã câu hỏi (qCode): pllRqziB].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). 
 Yêu cầu xây dựng chương trình client thực hiện kết nối tới server sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B16DCCN999;D45EFA12"
b. Nhận dữ liệu từ server là một chuỗi các số nguyên được phân tách bởi ký tự ",".
Ví dụ: "10,5,15,20,25,30,35"
c. Xác định hai số trong dãy có tổng gần nhất với gấp đôi giá trị trung bình của toàn bộ dãy. Gửi thông điệp lên server theo định dạng "num1,num2" (với num1 < num2)
Ví dụ: Với dãy "10,5,15,20,25,30,35", gấp đôi giá trị trung bình là 40, hai số có tổng gần nhất là 15 và 25. Gửi lên server chuỗi "15,25".
d. Đóng kết nối và kết thúc chương trình.
*/

public class TCPClient {
    private static final int SERVER_PORT = 2206;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        String qCode = "pllRqziB";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        System.out.println("Send init code to server: " + initMessage);
        os.write(initMessage.getBytes());
        os.flush();

        byte[] data = new byte[1024];
        int dataRead = is.read(data);

        if(dataRead != -1){
            String dataString = new String(data, 0, dataRead);
            System.out.println("Data from server:\n" + dataString);

            String result = process(dataString);
            os.write(result.getBytes());
            os.flush();
            System.out.println("Result: " + result);
        }

        socket.close();
    }

    public static String process(String data){
        String[] dataSplit = data.split("\\,");
        float avg = 0;
        for(String str : dataSplit){
            avg += Float.parseFloat(str);
        }

        avg = avg/dataSplit.length * 2;
        System.out.println(avg);
        int num1 = 0;
        int num2 = 0;
        float dis = Float.MAX_VALUE;
        for(int i = 0; i < dataSplit.length; i++){
            for(int j = i+1; j < dataSplit.length; j++){
                int n1 = Integer.parseInt(dataSplit[i]);
                int n2 = Integer.parseInt(dataSplit[j]);
                if(Math.abs((n1+n2 - avg)) < dis){
                    dis = Math.abs(n1+n2-avg);
                    num1 = n1;
                    num2 = n2;
                }
            }
        }
        if(num1 > num2){
            int tmp = num1;
            num1 = num2;
            num2 = tmp;
        }
        return num1+","+num2;
    }
}
