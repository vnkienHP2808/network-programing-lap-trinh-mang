package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;

import Config.ConfigFile;

public class qCode_48gVCNfk_RMI_Character {
    public static void main(String[] args) throws Exception{
        Registry rg = LocateRegistry.getRegistry(ConfigFile.SERVER_HOST);
        CharacterService cs = (CharacterService)rg.lookup("RMICharacterService");

        String studentCode = ConfigFile.STUDENT_CODE;
        String qCode = "48gVCNfk";

        String data = cs.requestCharacter(studentCode, qCode);
        System.out.println("==>Data: " + data);
        
        String result = process(data);
        System.out.println("==>Result: " + result);
        cs.submitCharacter(studentCode, qCode, result); 
    }

    public static String process(String data){
        LinkedHashMap<Character, Integer> linkedHashMap = new LinkedHashMap<>();
        for(int i = 0; i < data.length(); i++){
            char c = data.charAt(i);
            linkedHashMap.putLast(c, linkedHashMap.getOrDefault(c, 0) + 1);
        }

        StringBuilder sb = new StringBuilder("{");
        for(Character c : data.toCharArray()){
            if(linkedHashMap.containsKey(c)){
                sb.append('"');
                sb.append(c).append('"').append(": ").append(linkedHashMap.get(c)).append(", ");
                linkedHashMap.remove(c);
            }
        }

        return sb.toString().substring(0, sb.toString().length()-2) + "}";
    }
}
