package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigFile;

/*
 [Mã câu hỏi (qCode): iMXCIwut].  Một chương trình (tạm gọi là RMI Server) cung cấp giao diện cho phép triệu gọi từ xa để xử lý dữ liệu nhị phân.
Giao diện từ xa:
public interface ByteService extends Remote {
public byte[] requestData(String studentCode, String qCode) throws RemoteException;
public void submitData(String studentCode, String qCode, byte[] data) throws RemoteException;
}
Trong đó:
•	Interface ByteService được viết trong package RMI.
Đối tượng cài đặt giao diện từ xa ByteService được đăng ký với RegistryServer với tên là: RMIByteService.
Yêu cầu: Viết chương trình tại máy trạm (RMI client) để thực hiện các công việc sau với dữ liệu nhị phân nhận được từ RMI Server:
a. Triệu gọi phương thức requestData để nhận một mảng dữ liệu nhị phân (byte[]) từ server.
b. Thực hiện phân chia mảng byte[] nhận được thành hai phần: phần đầu chứa các byte có giá trị chẵn và phần sau chứa các byte có giá trị lẻ, duy trì thứ tự xuất hiện của các phần tử trong từng nhóm.
Ví dụ: Nếu mảng dữ liệu nhận được là [1, 2, 3, 4, 5], sau khi phân chia chẵn-lẻ, mảng kết quả sẽ là [2, 4, 1, 3, 5] (tất cả phần tử chẵn được đặt trước, theo sau là tất cả phần tử lẻ).
c. Triệu gọi phương thức submitData để gửi mảng byte[] đã được phân chia chẵn-lẻ trở lại server.
d. Kết thúc chương trình client.
 */

public class iMXCIwut_RMI_Byte {
    public static void main(String[] args) throws Exception{
        Registry rgt = LocateRegistry.getRegistry(ConfigFile.SERVER_HOST, 1099);
        ByteService bs = (ByteService)rgt.lookup("RMIByteService");

        String studentCode = ConfigFile.STUDENT_CODE;
        String qCode = "iMXCIwut";

        byte[] data = bs.requestData(studentCode, qCode);
        byte[] result = process(data);

        bs.submitData(studentCode, qCode, result);
    }

    public static byte[] process(byte[] data){
        byte[] arr = new byte[data.length];
        List<Byte> even = new ArrayList<>();
        List<Byte> odd = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            if(data[i] % 2 == 0) even.add(data[i]);
            else odd.add(data[i]);
        }
        
        int idx = 0;
        for(int i = 0; i < data.length; i++){
            if(i < even.size()) arr[i] = even.get(i);
            else{
                arr[i] = odd.get(idx);
                idx++;
            }
        }

        return arr;
    }
}
