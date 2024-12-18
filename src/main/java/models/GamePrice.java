package models;

public class GamePrice {

    private String platform;
    private double price;

    public GamePrice(String platform, double price) {
        this.platform = platform;
        this.price = price;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GamePrice [platform=" + platform + ", price=" + price + "]";
    }
}
