package TopicTCP.A7WoBeVC_TCP_ByteStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): A7WoBeVC].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s).
Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng các luồng byte (InputStream/OutputStream) để trao đổi thông tin theo thứ tự: 
a.	Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: "B16DCCN999;2B3A6510"
b.	Nhận dữ liệu từ server là một số nguyên n nhỏ hơn 400. Ví dụ: 7
c.	Thực hiện các bước sau đây để sinh ra chuỗi từ số nguyên n ban đầu và gửi lên server.
        Bắt đầu với số nguyên nn:
            Nếu n là số chẵn, chia n cho 2 để tạo ra số tiếp theo trong dãy.
            Nếu n là số lẻ và khác 1, thực hiện phép toán n=3*n+1 để tạo ra số tiếp theo.
        Lặp lại quá trình trên cho đến khi n=1, tại đó dừng thuật toán.
Kết quả là một dãy số liên tiếp, bắt đầu từ n ban đầu, kết thúc tại 1 và độ dài của chuỗi theo format "chuỗi kết quả; độ dài"  
Ví dụ: kết quả với n = 7 thì dãy: 7 22 11 34 17 52 26 13 40 20 10 5 16 8 4 2 1; 17;  
d.	Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(ConfigFile.SERVER_HOST, 2206);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        String initMessage = ConfigFile.STUDENT_CODE + ";" + "A7WoBeVC";
        System.out.println("==>Send student code and qCode: " + initMessage);
        os.write(initMessage.getBytes());
        os.flush();

        byte[] data = new byte[1024];
        int is_read = is.read(data);

        if(is_read != -1){
            String number = new String(data, 0, data.length);
            System.out.println("==>Data: " + number);

            String result = process(number);
            System.out.println("==> Result: " + result);
            os.write(result.getBytes());
            os.flush();
        }

        os.close();
        is.close();
        socket.close();
    }

    public static String process(String num){
        String result = "";
        int cnt = 0;
        int number = Integer.parseInt(num.trim());
        while(number != 1){
            result += number + " ";
            cnt++;
            if(number % 2 == 0){
                number /= 2;
            }
            else{
                number = number * 3 + 1;
            }
        }
        cnt++;
        result += "1" + "; " + cnt; 
        return result;
    }
}
