package TCP_CharacterStream_Kk0twYSQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): Kk0twYSQ].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng ký tự (BufferedReader/BufferedWriter) theo kịch bản sau:

a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;C1234567"

b. Nhận từ server một chuỗi chứa nhiều từ, các từ được phân tách bởi khoảng trắng. Ví dụ: "hello world this is a test example"

c. Sắp xếp các từ trong chuỗi theo độ dài, thứ tự xuất hiện. Gửi danh sách các từ theo từng nhóm về server theo định dạng: "a, is, this, test, hello, world, example".

d. Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient {
    private static final int SERVER_PORT = 2208;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String qCode = "Kk0twYSQ";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        System.out.println("Send StudentCode and qCode: " + initMessage);
        bw.write(initMessage);
        bw.newLine();
        bw.flush();

        System.out.println("Get data");
        String data = br.readLine();

        System.out.println("Process and sed to server");
        bw.write(splitAndSort(data));
        bw.newLine();
        bw.flush();

        socket.close();
        br.close();
        bw.close();
    }

    public static String splitAndSort(String data){
        ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split("\\s+")));
        StringBuilder sb = new StringBuilder("");
        Collections.sort(dataList, (s1, s2) ->{
            if(s1.length() == s2.length()) return 0;
            return s1.length() - s2.length();
        });

        for(String str : dataList){
            sb.append(str);
            if(!str.equals(dataList.getLast())) sb.append(", ");
        }
        return sb.toString();
    }
}
