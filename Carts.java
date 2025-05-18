package caseStudy;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Carts {
    // Using HashMap to store products and their quantities
    private HashMap<Products, Integer> items;
    
    public Carts() {
        items = new HashMap<>();
    }
    
    public void addToCart(Products product, int quantity) {
        // No need to check stock here anymore since we've already updated it in Shop.java
        
        if (items.containsKey(product)) {
            // If product already exists in cart, increase quantity
            items.put(product, items.get(product) + quantity);
        } else {
            // Otherwise add new product
            items.put(product, quantity);
        }
        System.out.println(quantity + " " + product.getname() + " added to cart");
    }
    
    // View all items in cart with quantities
    public void viewCarts() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\n\t===== YOUR CART =====");
            double total = 0;
            for (Map.Entry<Products, Integer> entry : items.entrySet()) {
                Products product = entry.getKey();
                int quantity = entry.getValue();
                double itemTotal = product.getprice() * quantity;
                total += itemTotal;
                
                System.out.printf("%s | Price: ₱%.2f | Quantity: %d | Subtotal: ₱%.2f%n", 
                    product.getname(), product.getprice(), quantity, itemTotal);
            }
            System.out.printf("Total Amount: \t₱%.2f%n", total);
            System.out.println("\t=====================");
        }
    }
    
    // Get total price of all items in cart
    public double getTotal() {
        double total = 0;
        for(Map.Entry<Products, Integer> entry : items.entrySet()) {
            total += entry.getKey().getprice() * entry.getValue();
        }
        return total;
    }
    
    public void checkout(Scanner scanner) {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return;
        }
        
        double totalAmount = getTotal();
        System.out.printf("\nTotal amount: ₱%.2f%n", totalAmount);
        
        // payment
        double payment = 0;
        boolean validPayment = false;
        
        while (!validPayment) {
            System.out.print("Enter payment amount: ₱");
            try {
                payment = Double.parseDouble(scanner.nextLine().trim());
                if (payment < totalAmount) {
                    System.out.println("Insufficient payment. Please enter at least ₱" + totalAmount);
                } else {
                    validPayment = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
        
        // tip
        double tipAmount = 0;
        System.out.print("Would you like to leave a tip? (yes/no): ");
        String tipChoice = scanner.nextLine().toLowerCase().trim();
        
        if (tipChoice.equals("yes")) {
            System.out.print("Enter tip amount: ₱");
            try {
                tipAmount = Double.parseDouble(scanner.nextLine().trim());
                if (tipAmount > 0) {
                    System.out.printf("Thank you for your ₱%.2f tip!%n", tipAmount);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. No tip will be applied.");
            }
        }
        
        // Calculate and display change
        double change = payment - totalAmount;
        if (change > 0) {
            System.out.printf("Your change is: ₱%.2f%n", change);
            System.out.println("\nThank you for your purchase!");
        } else if(change == 0) {
            System.out.println("Thank you for your purchase!");
        }
        
        LocalDateTime showTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM/dd/yyyy\nhh:mm a");
        String formattedDateTime = showTime.format(format);
        
        // Generate receipt
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = product.getprice() * quantity;
        System.out.println("\n================ RECEIPT ================");
        System.out.println("MAPAGMAHAL PASTRY SHOP");
        System.out.println("---------------------------------------");
        System.out.println("Date & Time: " + formattedDateTime);
        System.out.println("Receipt #: " + generateReceiptNumber(showTime) +  " - "+product.getID());
        System.out.println("---------------------------------------");
        System.out.println("ITEMS:");
        
        
            System.out.printf("%s (ID: %s)\n", product.getname(), product.getID());
            System.out.printf("  %d × ₱%.2f = ₱%.2f%n", quantity, product.getprice(), itemTotal);
        }
        
        System.out.println("---------------------------------------");
        System.out.printf("Subtotal:         ₱%.2f%n", totalAmount);
        if (tipAmount > 0) {
            System.out.printf("Tip:              ₱%.2f%n", tipAmount);
            System.out.printf("Total:            ₱%.2f%n", (totalAmount + tipAmount));
        }
        System.out.printf("Payment:          ₱%.2f%n", payment);
        System.out.printf("Change:           ₱%.2f%n", change);
        System.out.println("---------------------------------------");
        System.out.println("Thank you for shopping with us!");
        System.out.println("Please come again!");
        System.out.println("========================================");
        
   
        items.clear(); // Empty the cart after checkout
    }
    
    // Generate unique receipt number based on date and time
    private String generateReceiptNumber(LocalDateTime dateTime) {
        // Format: YYYYMMDD-HHMMSS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return dateTime.format(formatter);
    }
   
    public void clearCart() {
        // Restore stock quantities when cart is cleared
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();
            product.updateStock(quantity); // Add back to stock
        }
        items.clear();
        System.out.println("Cart cleared.");
    }
   
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
	
	
	

