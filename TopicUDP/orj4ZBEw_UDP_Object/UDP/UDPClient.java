package TopicUDP.orj4ZBEw_UDP_Object.UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): orj4ZBEw].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. 
 Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau:

Đối tượng trao đổi là thể hiện của lớp UDP.Book được mô tả:

    Tên đầy đủ lớp: UDP.Book
    Các thuộc tính: id (String), title (String), author (String), isbn (String), publishDate (String)
    Hàm khởi tạo:
        public Book(String id, String title, String author, String isbn, String publishDate)
    Trường dữ liệu: private static final long serialVersionUID = 20251107L

Thực hiện:

a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode". Ví dụ: ";B23DCCN005;eQkvAeId"

b. Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Book từ server.
 Trong đó, các thuộc tính id, title, author, isbn, và publishDate đã được thiết lập sẵn.

c. Thực hiện:
        Chuẩn hóa title: viết hoa chữ cái đầu của mỗi từ.
        Chuẩn hóa author theo định dạng "Họ, Tên". Họ viết hoa tất cả, tên thì chữ đầu tiên
        Chuẩn hóa mã ISBN theo định dạng "978-3-16-148410-0"
        Chuyển đổi publishDate từ yyyy-mm-dd sang mm/yyyy.
d. Gửi lại đối tượng đã được chuẩn hóa về server với cấu trúc: 08 byte đầu chứa chuỗi requestId 
và các byte còn lại chứa đối tượng Book đã được sửa đổi.

Đóng socket và kết thúc chương trình.
 */

public class UDPClient {
    public static void main(String[] args) throws Exception{
        DatagramSocket dSocket = new DatagramSocket();
        int serverPort = 2209;
        InetAddress inetAddress = InetAddress.getByName(ConfigFile.SERVER_HOST);

        String initMessage = ";" + ConfigFile.STUDENT_CODE + ";" + "orj4ZBEw";
        DatagramPacket dpSendMessage = new DatagramPacket(initMessage.getBytes(), initMessage.length(), inetAddress, serverPort);
        dSocket.send(dpSendMessage);

        byte[] receiveData = new byte[1024];
        DatagramPacket dpReceive = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(dpReceive);

        String requestId = new String(dpReceive.getData(), 0, 8);
        ByteArrayInputStream input = new ByteArrayInputStream(dpReceive.getData(), 8, dpReceive.getLength()-8);
        ObjectInputStream ois = new ObjectInputStream(input);

        Book book = (Book) ois.readObject();
        book.setAuthor(normalizeAuthor(book.getAuthor()));
        book.setIsbn(normalizeISBN(book.getIsbn()));
        book.setTitle(normalizeTitle(book.getTitle()));
        book.setPublishDate(changeDate(book.getPublishDate()));


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(book);
        oos.flush();

        byte[] sendResult = new byte[8 + outputStream.size()];
        System.arraycopy(requestId.getBytes(), 0, sendResult, 0, 8);
        System.arraycopy(outputStream.toByteArray(), 0, sendResult, 8, outputStream.size());

        DatagramPacket dpSendResult = new DatagramPacket(sendResult, sendResult.length, inetAddress,serverPort);
        dSocket.send(dpSendResult);

        dSocket.close();
    }

    public static String normalizeTitle(String title){
        String[] data = title.split("\\s+");
        StringBuilder sb = new StringBuilder("");
        for(String s : data){
            sb.append(s.substring(0,1).toUpperCase())
                .append(s.substring(1).toLowerCase())
                .append(" ");
        }
        return sb.toString().trim();
    }

    public static String changeDate(String date){
        String[] data = date.split("\\-");
        return data[1] + "/" + data[0];
    }

    public static String normalizeAuthor(String author){
        String[] data = author.split("\\s+");
        StringBuilder sb = new StringBuilder("");

        sb.append(data[0].toUpperCase() + ", ");
        for(int i = 1; i < data.length; i++){
            sb.append(data[i].substring(0, 1).toUpperCase())
                .append(data[i].substring(1).toLowerCase())
                .append(" ");
        }

        return sb.toString().trim();
    }
    public static String normalizeISBN(String isbn){
        return isbn.substring(0,3) + "-" + isbn.substring(3, 4) + "-" +
                isbn.substring(4, 6) + "-" + isbn.substring(6, 12) + "-" + 
                isbn.substring(12);
    }
}
