package nDrTenmw_TCP_DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Config.ConfigFile;

public class TCPClient {
    private static final int SERVER_PORT = 2207;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) {
        Socket socket = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String qCode = "nDrTenmw";
            String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
            System.out.println("Student Code and qCode: " + initMessage);
            dos.writeUTF(initMessage);
            dos.flush();

            String data = dis.readUTF();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String process(String data){
        return "";
    }
}
