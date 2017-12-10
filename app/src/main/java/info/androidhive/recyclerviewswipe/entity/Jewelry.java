package info.androidhive.recyclerviewswipe.entity;

/**
 * Created by Занятия on 07.12.2017.
 */

public class Jewelry {


    @Override
    public String toString() {
        return "Jewelry{" +
                "barCode='" + barCode + '\'' +
                ", article='" + article + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", cost=" + cost +
                '}';
    }

    private String article;

    private String barCode;
    private String category;
    private Integer cost;
    private String description;
    private Double discount;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Jewelry() {
        this.discount = 0.0;

    }
    public Jewelry(String barCode, String article, String category, String description, Integer cost) {
        this.barCode = barCode;
        this.article = article;
        this.category = category;
        this.description = description;
        this.cost = cost;
        this.discount = 0.0;
    }


    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
