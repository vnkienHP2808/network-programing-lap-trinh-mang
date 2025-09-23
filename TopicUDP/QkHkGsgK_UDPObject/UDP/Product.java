package TopicUDP.QkHkGsgK_UDPObject.UDP;

import java.io.Serializable;

public class Product implements Serializable{
    private String id;
    private String code;
    private String name;
    private int quantity;
    private static final long serialVersionUID = 20161107;
    
    public Product(String id, String code, String name, int quantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    
}
