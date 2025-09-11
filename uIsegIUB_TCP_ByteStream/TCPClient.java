package uIsegIUB_TCP_ByteStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Config.ConfigFile;

public class TCPClient {
    private static final int SERVER_PORT = 2206;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        String qCode = "uIsegIUB";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        System.out.println("Send init code to server: " + initMessage);
        os.write(initMessage.getBytes());
        os.flush();

        
        byte[] data = new byte[1024];
        int dataRead = is.read(data);

        if(dataRead != -1){
            String dataString = new String(data, 0, dataRead);
            System.out.println("Data from server: " + dataString);

            String result = process(dataString);
            System.out.println("Result: " + result);

            os.write(result.getBytes());
            os.flush();
        }
        socket.close();
    }

    public static String process(String data){
        String[] dataString = data.split("\\,");
        int[] sumArray = new int[dataString.length];

        for(int i = 0; i < dataString.length; i++){
            if(i == 0) sumArray[i] = Integer.parseInt(dataString[i]);
            else{
                sumArray[i] = sumArray[i-1] + Integer.parseInt(dataString[i]);
            }
        }

        int idx = 0;
        int dolech = Integer.MAX_VALUE;
        int left = 0;
        int right = 0;
        for(int i = 1; i < sumArray.length-1; i++){
            int sumLeft = sumArray[i-1];
            int sumRight = sumArray[sumArray.length-1] - sumArray[i];

            if(Math.abs(sumRight - sumLeft) < dolech){
                dolech = Math.abs(sumRight - sumLeft);
                left = sumLeft;
                right = sumRight;
                idx = i;
            }
        }

        return idx + "," + left + "," + right + "," + Math.abs(left-right);
    }
}
