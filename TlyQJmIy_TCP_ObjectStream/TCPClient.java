package TlyQJmIy_TCP_ObjectStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): TlyQJmIy].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectOutputStream/ObjectInputStream) theo kịch bản dưới đây:

Biết lớp TCP.Product gồm các thuộc tính (id int, name String, price double, int discount) và private static final long serialVersionUID = 20231107;

a. Gửi đối tượng là một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;1E08CA31"

b. Nhận một đối tượng là thể hiện của lớp TCP.Product từ server.

c. Tính toán giá trị giảm giá theo price theo nguyên tắc: Giá trị giảm giá (discount) bằng tổng các chữ số trong phần nguyên của giá sản phẩm (price). Thực hiện gán giá trị cho thuộc tính discount và gửi lên đối tượng nhận được lên server.

d. Đóng kết nối và kết thúc chương trình.
 */

public class TCPClient {
    private static final int SERVER_PORT = 2209;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;
    public static void main(String[] args) throws IOException{
        Socket socket = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            String qCode = "TlyQJmIy";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            System.out.println("Send student code and qcode: " + initMessage);
            oos.writeObject(initMessage);
            oos.flush();

            Object obj;
            try {
                obj = ois.readObject();
            } catch (EOFException eof) {
                return;
            }

            Product product = (Product) obj;
            System.out.println("Data object from server: " + product);

            product.setDiscount(process(product.getPrice()));

            System.out.println("Send object to server: " + product);
            oos.writeObject(product);
            oos.flush();
        }  catch (UnknownHostException e) {
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
    public static int process(double price){
        int discount = 0;
        int convertPrice = (int)(price);
        while(convertPrice > 0){
        discount+= convertPrice%10;
        convertPrice/=10;
        }
        return discount;
    }
}
