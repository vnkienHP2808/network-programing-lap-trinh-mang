package TCP_CharacterStream_Kk0twYSQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Config.ConfigFile;

public class TCPClient {
    private static final int SERVER_PORT = 2208;
    private static final String SERVER_HOST = ConfigFile.SERVER_HOST;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String qCode = "Kk0twYSQ";
        String initMessage = ConfigFile.STUDENT_CODE + ";" + qCode;
        System.out.println("Send StudentCode and qCode: " + initMessage);
        bw.write(initMessage);
        bw.newLine();
        bw.flush();

        System.out.println("Get data");
        String data = br.readLine();

        System.out.println("Process and sed to server");
        bw.write(splitAndSort(data));
        bw.newLine();
        bw.flush();

        socket.close();
        br.close();
        bw.close();
    }

    public static String splitAndSort(String data){
        ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split("\\s+")));
        StringBuilder sb = new StringBuilder("");
        Collections.sort(dataList, (s1, s2) ->{
            if(s1.length() == s2.length()) return 0;
            return s1.length() - s2.length();
        });

        for(String str : dataList){
            sb.append(str);
            if(!str.equals(dataList.getLast())) sb.append(", ");
        }
        return sb.toString();
    }
}
