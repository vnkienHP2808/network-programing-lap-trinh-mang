package TopicTCP.uVllKNnF_TCP_DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Config.ConfigFile;

/*
[Mã câu hỏi (qCode): uVllKNnF].  Mật mã caesar, còn gọi là mật mã dịch chuyển, để giải mã thì mỗi ký tự nhận được sẽ được thay thế bằng một ký tự cách nó một đoạn s. Ví dụ: với s = 3 thì ký tự “A” sẽ được thay thế bằng ký tự “D”.
Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng chương trình client tương tác với server trên, sử dụng các luồng byte (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a.	Gửi một chuỗi gồm mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;D68C93F7"
b.	Nhận lần lượt chuỗi đã bị mã hóa caesar và giá trị dịch chuyển s nguyên
c.	Thực hiện giải mã ra thông điệp ban đầu và gửi lên Server
d.	Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(ConfigFile.SERVER_HOST, 2207);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        String initMess = ConfigFile.STUDENT_CODE + ";" + "uVllKNnF";
        System.out.println("==>MSV + qCode: " + initMess);
        dos.writeUTF(initMess);
        dos.flush();

        String caesar = dis.readUTF();
        int s = dis.readInt();
        System.out.println("==>Caesar + s: " + caesar + " : " + s);

        String result = process(caesar, s);
        System.out.println("==>Result: " + result);

        dos.writeUTF(result);
        dos.flush();

        socket.close();
        dos.close();
        dis.close();
    }

    public static String process(String caesar, int s){
        String tmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = "";

        for(int i = 0; i < caesar.length(); i++){
            int k = 0;
            int idx = 0;

            if(!Character.isDigit(caesar.charAt(i))){
                while(caesar.charAt(i) != tmp.charAt(idx)){
                    idx++;
                }

                while(26*k + idx - s < 0){
                    k++;
                }

                result += tmp.charAt(26*k + idx - s) + "";
            } 
            else result += caesar.charAt(i);
        }
        return result;
    }
}
