package TCP_CharacterStream_8KIKH8ro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Config.ConfigFile;

public class TCPClient {
    private static final int SERVER_PORT = 2208;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String qCode = "8KIKH8ro";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        bw.write(initMessage);
        bw.newLine();
        bw.flush();
        System.out.println("Send StudentCode and qCode");

        String data = br.readLine();
        System.out.println("Get data:\n" + data);

        String result = revertAndRLE(data);
        bw.write(result);
        bw.newLine();
        bw.flush();
        System.out.println("Send result to server:\n" + result);

        socket.close();
        br.close();
        bw.close();
    }

    public static String revertAndRLE(String data){
        String[] dataSplit = data.split("\\s+");
        StringBuilder sb = new StringBuilder("");

        for(String s : dataSplit){
            int total = 1;
            char c = s.charAt(s.length()-1);
            for(int i = s.length()-1; i >= 0; i--){
                if((s.charAt(i)==c)){
                    if(i != s.length()-1) total++;
                }
                else{
                    sb.append(c);
                    if(total > 1) sb.append(String.valueOf(total));
                    total = 1;
                    c = s.charAt(i);
                }
            }
            sb.append(c);
            if(total > 1) sb.append(String.valueOf(total));
            if(!s.equals(dataSplit[dataSplit.length-1])) sb.append(" ");
        }
        return sb.toString();
    }
}
