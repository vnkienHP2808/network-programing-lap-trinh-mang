package TopicTCP.LKggOoCo_TCP_ObjectStream.TCP;

import java.io.Serializable;

public class Laptop implements Serializable{
    private int id, quantity;
    private String code, name;
    private static final long serialVersionUID = 20150711L;
    public Laptop(int id, String code, String name, int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.code = code;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    
}
