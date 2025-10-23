package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Config.ConfigFile;

public class CXe6TMCO_RMI_Data {
    public static void main(String[] args) throws Exception{
        Registry rg = LocateRegistry.getRegistry(ConfigFile.SERVER_HOST);
        DataService ds = (DataService)rg.lookup("RMIDataService");

        String studentCode = ConfigFile.STUDENT_CODE;
        String qCode = "CXe6TMCO";

        String data = (String)ds.requestData(studentCode, qCode);
        System.out.println(data);
        String result = nextPermutation(data);
        System.out.println(result);
        ds.submitData(studentCode, qCode, result);

    }
    public static String nextPermutation(String data) {
        String[] tmp = data.split("\\, ");
        int n = tmp.length;
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = Integer.parseInt(tmp[i]);
        }
        if (a == null || a.length <= 1) return data;
        int i = n - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i < 0) {
            reverse(a, 0, n - 1);
            String result = "";
            for(int k = 0; k < n; k++){
                result += a[k] + "";
                if(k != n-1) result += ",";
            }
            return result;
        }
        int j = n - 1;
        while (j > i && a[j] <= a[i]) j--;
        swap(a, i, j);
        reverse(a, i + 1, n - 1);

        String result = "";
            for(int k = 0; k < n; k++){
                result += a[k] + "";
                if(k != n-1) result += ",";
            }
        return result;
    }

    private static void swap(int[] a, int x, int y) {
        int t = a[x]; a[x] = a[y]; a[y] = t;
    }

    private static void reverse(int[] a, int l, int r) {
        while (l < r) swap(a, l++, r--);
    }
}
