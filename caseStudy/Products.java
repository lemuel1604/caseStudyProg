package caseStudy;

public class Products {
    private String name;
    private double price;
    private int stock;
    private String ID;

    public Products(String name, double price, int stock, String ID) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.ID = ID;
    }  
    
    public String getname() {
        return name;
    }
    
    public double getprice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public String getID() {
        return ID;
    }
    
    public void updateStock(int quantityChange) {
        this.stock += quantityChange;
    }
    
    public void viewInventory() {
        System.out.printf("Product: %s\nPrice: ₱%.2f\nStock: %d\nID: %s\n\n", 
            name, price, stock, ID);
    }
    public boolean reduceStock(int amount) {
        if (amount <= stock) {
            stock -= amount;
            return true;
        }
        return false;
    }
    
    // Override equals and hashCode for proper HashMap functionality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Products other = (Products) obj;
        return ID.equals(other.ID);
    }
    
    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
