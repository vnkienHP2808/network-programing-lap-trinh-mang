package TopicTCP.ldmod5iI_TCP_CharacterStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): ldmod5iI].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau: 
a.	Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;EC4F899B"
b.	Nhận một chuỗi ngẫu nhiên là danh sách các một số tên miền từ server
Ví dụ: giHgWHwkLf0Rd0.io, I7jpjuRw13D.io, wXf6GP3KP.vn, MdpIzhxDVtTFTF.edu, TUHuMfn25chmw.vn, HHjE9.com, 4hJld2m2yiweto.vn, y2L4SQwH.vn, s2aUrZGdzS.com, 4hXfJe9giAA.edu
c.	Tìm kiếm các tên miền .edu và gửi lên server
Ví dụ: MdpIzhxDVtTFTF.edu, 4hXfJe9giAA.edu
d.	Đóng kết nối và kết thúc chương trình.
 */
public class TCPClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(ConfigFile.SERVER_HOST, 2208);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String initMessage = ConfigFile.STUDENT_CODE + ";" + "ldmod5iI";
        System.out.println("==> Student code + qCode: " + initMessage);
        bw.write(initMessage);
        bw.newLine();
        bw.flush();

        String data = br.readLine().trim();
        System.out.println("==> Data: " + data);

        String[] domains = data.split("\\,");
        String result = "";
        List<String> list = new ArrayList<>();
        for(String line : domains){
            String[] domainSplit = line.trim().split("\\.");
            if(domainSplit[1].equals("edu")){
                list.add(line);
            }
        }
        
        for(String str : list){
            result += str;
            if(!str.equals(list.getLast())) result += ", ";
        }
        System.out.println("==>Result: " + result);
        bw.write(result);
        bw.newLine();
        bw.flush();

        socket.close();
        br.close();
        bw.close();
    }
}
