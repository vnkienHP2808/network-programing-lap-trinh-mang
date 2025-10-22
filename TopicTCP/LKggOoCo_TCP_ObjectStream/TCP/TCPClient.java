package TopicTCP.LKggOoCo_TCP_ObjectStream.TCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Config.ConfigFile;

public class TCPClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(ConfigFile.SERVER_HOST, 2209);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        String initMessage = ConfigFile.STUDENT_CODE + ";LKggOoCo";
        System.out.println("==>MSV + qCode: " + initMessage);
        oos.writeObject(initMessage);
        oos.flush();

        Laptop laptop = (Laptop)ois.readObject();
        laptop.setName(refactorName(laptop.getName()));
        laptop.setQuantity(refactorQuantity(laptop.getQuantity()));

        oos.writeObject(laptop);
        oos.flush();

        socket.close();
        oos.close();
        ois.close();
    }

    public static String refactorName(String name){
        String[] data = name.trim().split("\\s+");
        String tmp = data[0];
        data[0] = data[data.length-1];
        data[data.length-1] = tmp;

        return String.join(" ", data).trim();
    }

    public static int refactorQuantity(int quantity){
        return Integer.parseInt(new StringBuilder(quantity+"").reverse().toString());
    }
}
