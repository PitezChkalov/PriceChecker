package info.androidhive.recyclerviewswipe.entity;

import java.io.Serializable;
import java.util.List;


public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Jewelry> orderContent;

    private Integer totalCost;
    private String username;

    @Override
    public String toString() {
        return "Order{" +
                "orderContent=" + orderContent +
                ", totalCost=" + totalCost +
                ", username='" + username + '\'' +
                '}';
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost() {
        this.totalCost = 0;
        for(Jewelry i: this.getOrderContent()){
            this.totalCost+=i.getCost();
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Jewelry> getOrderContent() {
        return orderContent;
    }

    public Order() {
    }

    public void setOrderContent(List<Jewelry> orderContent) {
        this.orderContent = orderContent;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Order(List<Jewelry> orderContent, String user) {

        this.orderContent = orderContent;
        this.username = user;
        setTotalCost();
    }
}