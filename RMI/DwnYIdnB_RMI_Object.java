package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Config.ConfigFile;

public class DwnYIdnB_RMI_Object {
    public static void main(String[] args) throws Exception{
        Registry rg = LocateRegistry.getRegistry(ConfigFile.SERVER_HOST);
        ObjectService os = (ObjectService) rg.lookup("RMIObjectService");

        String msv = ConfigFile.STUDENT_CODE;
        String qCode = "DwnYIdnB";

        ProductX productX = (ProductX)os.requestObject(msv, qCode);
        productX.setDiscount(process(productX.getDiscountCode()));

        os.submitObject(qCode, qCode, productX);
        
    }

    public static int process(String code){
        int n = 0;
        for(char c : code.toCharArray()){
            if(Character.isDigit(c)){
                n += c - '0';
            }
        }
        return n;
    }
}
