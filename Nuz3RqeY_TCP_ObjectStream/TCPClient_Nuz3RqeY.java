package Nuz3RqeY_TCP_ObjectStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): Nuz3RqeY].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây).
  Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectOutputStream/ObjectInputStream) để gửi/nhận và chuẩn hóa thông tin địa chỉ của khách hàng.
Biết rằng lớp TCP.Address có các thuộc tính (id int, code String, addressLine String, city String, postalCode String) và trường dữ liệu private static final long serialVersionUID = 20180801L.
a. Gửi đối tượng là một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;A1B2C3D4"
b. Nhận một đối tượng là thể hiện của lớp TCP.Address từ server. Thực hiện chuẩn hóa thông tin addressLine bằng cách:
•	Chuẩn hóa addressLine: Viết hoa chữ cái đầu mỗi từ, in thường các chữ còn lại, loại bỏ ký tự đặc biệt và khoảng trắng thừa (ví dụ: "123 nguyen!!! van cu" → "123 Nguyen Van Cu") 
•	Chuẩn hóa postalCode: Chỉ giữ lại số và ký tự "-" ví dụ: "123-456"
c. Gửi đối tượng đã được chuẩn hóa thông tin địa chỉ lên server.
d. Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient_Nuz3RqeY {
    private static final int SERVER_PORT = 2209;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;
    public static void main(String[] args)throws IOException {
        Socket socket = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            String qCode = "Nuz3RqeY";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            System.out.println("Send student code and qcode: " + initMessage);
            oos.writeObject(initMessage);
            oos.flush();

            Address address = (Address) ois.readObject();
            System.out.println("Data from server: " + address);

            address.setAddressLine(processAddressLine(address.getAddressLine()));
            address.setPostalCode(processPostalCode(address.getPostalCode()));
            System.out.println("Data after process: " + address);

            oos.writeObject(address);
            oos.flush();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (EOFException eof) {
            System.err.println("EOFException (server closed connection unexpectedly): " + eof.getMessage());
        } catch (SocketTimeoutException ste) {
            System.err.println("Socket timeout: " + ste.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found when reading object: " + e.getMessage());
        } finally{
            oos.close();
            ois.close();
            socket.close();
        }
    }
    public static String processAddressLine(String addressLine){
        String[] data = addressLine.split("\\s+");
        StringBuilder sb = new StringBuilder("");
        for(String s : data){
            if (!s.isEmpty()) {
                    s = s.replaceAll("[^a-zA-Z0-9\\s]", "");
                    if (!s.isEmpty()) {
                        sb.append(s.substring(0, 1).toUpperCase())
                                .append(s.substring(1).toLowerCase())
                                .append(" ");
                    }
                }
            }
        return sb.toString().trim();
    }

    public static String processPostalCode(String postalCode){
        return postalCode.replaceAll("[^0-9-]","");
    }
}
