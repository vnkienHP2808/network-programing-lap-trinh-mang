package TCP_CharacterStream_8KIKH8ro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): 8KIKH8ro].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng ký tự (BufferedReader/BufferedWriter) theo kịch bản sau:
a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode".
Ví dụ: "B15DCCN999;1D08FX21"
b. Nhận từ server một chuỗi chứa nhiều từ, các từ được phân tách bởi khoảng trắng.
Ví dụ: "hello world programming is fun"
c. Thực hiện đảo ngược từ và mã hóa RLE để nén chuỗi ("aabb" nén thành "a2b2"). Gửi chuỗi đã được xử lý lên server. Ví dụ: "ol2eh dlrow gnim2argorp si nuf".
d. Đóng kết nối và kết thúc chương trình
 */

public class TCPClient {
    private static final int SERVER_PORT = 2208;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String qCode = "8KIKH8ro";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        bw.write(initMessage);
        bw.newLine();
        bw.flush();
        System.out.println("Send StudentCode and qCode");

        String data = br.readLine();
        System.out.println("Get data:\n" + data);

        String result = revertAndRLE(data);
        bw.write(result);
        bw.newLine();
        bw.flush();
        System.out.println("Send result to server:\n" + result);

        socket.close();
        br.close();
        bw.close();
    }

    public static String revertAndRLE(String data){
        String[] dataSplit = data.split("\\s+");
        StringBuilder sb = new StringBuilder("");

        for(String s : dataSplit){
            int total = 1;
            char c = s.charAt(s.length()-1);
            for(int i = s.length()-1; i >= 0; i--){
                if((s.charAt(i)==c)){
                    if(i != s.length()-1) total++;
                }
                else{
                    sb.append(c);
                    if(total > 1) sb.append(String.valueOf(total));
                    total = 1;
                    c = s.charAt(i);
                }
            }
            sb.append(c);
            if(total > 1) sb.append(String.valueOf(total));
            if(!s.equals(dataSplit[dataSplit.length-1])) sb.append(" ");
        }
        return sb.toString();
    }
}
