package TCP_CharacterStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Config.ConfigFile;

/* TCP - Character Stream
[Mã câu hỏi (qCode): gJmWoERy].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau: 
a.	Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;EC4F899B"
b.	Nhận một chuỗi ngẫu nhiên là danh sách các một số tên miền từ server
Ví dụ: giHgWHwkLf0Rd0.io, I7jpjuRw13D.io, wXf6GP3KP.vn, MdpIzhxDVtTFTF.edu, TUHuMfn25chmw.vn, HHjE9.com, 4hJld2m2yiweto.vn, y2L4SQwH.vn, s2aUrZGdzS.com, 4hXfJe9giAA.edu
c.	Tìm kiếm các tên miền .edu và gửi lên server
Ví dụ: MdpIzhxDVtTFTF.edu, 4hXfJe9giAA.edu
d.	Đóng kết nối và kết thúc chương trình.
*/

public class TCPClient {
    private static final int SERVER_PORT = 2208;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server: " + SERVER_HOST + ":" + SERVER_PORT);

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String qCode = "gJmWoERy";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;

            writer.write(initMessage);
            writer.newLine();
            writer.flush();
            System.out.println("Chuỗi gồm MSV: " + initMessage);

            String data = reader.readLine();
            System.out.println("Domains từ server: " + data);

            String result = process(data);
            writer.write(result);
            writer.newLine();
            writer.flush();

        } catch (UnknownHostException e) {
            System.err.println("Không thể tìm thấy host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi I/O: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        } finally {
            writer.close();
            reader.close();
            socket.close();
        }
    }

    public static String process(String data) {
        String[] listDomain = data.split(",");
        ArrayList<String> array = new ArrayList<>();
        StringBuilder sb = new StringBuilder("");

        for (String s : listDomain) {
            String[] splitDomain = s.split("\\.");
            if (splitDomain[1].equals("edu")) {
                array.add(s);
            }
        }

        for (String s : array) {
            sb.append(s);
            if (!s.equals(array.get(array.size() - 1))) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}