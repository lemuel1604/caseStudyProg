package caseStudy_SweetStudy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Carts {
    // Using HashMap to store products and their quantities
    private HashMap<Products, Integer> items;
    
    public Carts() {
        items = new HashMap<>();
    }
    
    public void addToCart(Products product, int quantity) {
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
        
        // Check promos before calculating total
        boolean cheesecakeFreebie = false;
        boolean croissantFreebie = false;
        Products cupcake = null, donut = null;
        int cheesecakeQty = 0;
        int croissantQty = 0;

        // Find quantities and freebie references
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int qty = entry.getValue();
            String name = product.getname().toLowerCase();
            
            if (name.contains("cheese")) cheesecakeQty = qty;
            if (name.contains("croissant")) croissantQty = qty;
            if (name.contains("cupcake")) cupcake = product;
            if (name.contains("donut")) donut = product;
        }

       // PROMOS NOTIFY
       System.out.println();
        if (cheesecakeQty >= 5) {
            cheesecakeFreebie = true;
            System.out.println("🎉 PROMO ACQUIRED: You get 1 FREE Cupcake for buying 5+ Cheesecakes!");
        }
        if (croissantQty >= 5) {
            croissantFreebie = true;
            System.out.println("🎉 PROMO ACQUIRED: You get 1 FREE Donut for buying 5+ Croissants!");
        }
        
        double totalAmount = getTotal();
        
        // Apply 5% discount if total > 500
        double discount = (totalAmount > 500) ? totalAmount * 0.05 : 0;
        if (discount > 0) {
            System.out.printf("🎉 You got a 5%% discount! (-₱%.2f)%n", discount);
            totalAmount -= discount;
        }
        
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
        double change = payment - (totalAmount + tipAmount);
        if (change > 0) {
            System.out.printf("Your change is: ₱%.2f%n", change);
            System.out.println("\nThank you for your purchase!");
        } else if(change == 0) {
            System.out.println("Thank you for your purchase!");
        }
        
        // Generate receipt
        LocalDateTime showTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM/dd/yyyy hh:mm a");
        String formattedDateTime = showTime.format(format);
        String receiptNumber = generateReceiptNumber(showTime);
        
        System.out.println("\n================ RECEIPT ================");
        System.out.println("         SWEET SPOT BAKERY");
        System.out.println("=========================================");
        System.out.println("Date & Time: " + formattedDateTime);
        System.out.println("Receipt #: " + receiptNumber);
        System.out.println("=========================================");
        System.out.println("ITEMS PURCHASED:");
        System.out.println("-----------------------------------------");
        
        // Display all purchased items
        double originalTotal = totalAmount + discount;
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = product.getprice() * quantity;
            
            System.out.printf("%-20s (ID: %s)%n", product.getname(), product.getID());
            System.out.printf("  %d × ₱%.2f = ₱%.2f%n", quantity, product.getprice(), itemTotal);
            System.out.println();
        }
        
        // Show freebies in receipt only if acquired
        if (cheesecakeFreebie && cupcake != null) {
            System.out.println("-----------------------------------------");
            System.out.println("FREEBIE ITEMS:");
            System.out.printf("%-20s (ID: %s)%n", cupcake.getname() + " - FREE!", cupcake.getID());
            System.out.printf("  1 × ₱%.2f = ₱0.00 (FREE PROMO!)%n", cupcake.getprice());
            System.out.println();
        }
        if (croissantFreebie && donut != null) {
            if (!cheesecakeFreebie) {
                System.out.println("-----------------------------------------");
                System.out.println("FREEBIE ITEMS:");
            }
            System.out.printf("%-20s (ID: %s)%n", donut.getname() + " - FREE!", donut.getID());
            System.out.printf("  1 × ₱%.2f = ₱0.00 (FREE PROMO!)%n", donut.getprice());
            System.out.println();
        }
        
        System.out.println("=========================================");
        System.out.println("PAYMENT SUMMARY:");
        System.out.println("-----------------------------------------");
        System.out.printf("Subtotal:         ₱%.2f%n", originalTotal);
        if (discount > 0) {
            System.out.printf("Discount (5%%):    -₱%.2f%n", discount);
        }
        System.out.printf("Final Total:      ₱%.2f%n", totalAmount);
        if (tipAmount > 0) {
            System.out.printf("Tip:              ₱%.2f%n", tipAmount);
            System.out.printf("Total Paid:       ₱%.2f%n", (totalAmount + tipAmount));
        }
        System.out.printf("Payment:          ₱%.2f%n", payment);
        System.out.printf("Change:           ₱%.2f%n", change);
        
        // Show promo summary
        if (cheesecakeFreebie || croissantFreebie) {
            System.out.println("=========================================");
            System.out.println("PROmos:");
            System.out.println("-----------------------------------------");
            if (cheesecakeFreebie) {
                System.out.println("✓ Free Cupcake (Buy 5+ Cheesecakes)");
            }
            if (croissantFreebie) {
                System.out.println("✓ Free Donut (Buy 5+ Croissants)");
            }
        }
        
        System.out.println("=========================================");
        System.out.println("    Thank you for shopping with us! ^_^");
        System.out.println("          Please come again!");
        System.out.println("=========================================");
        
        items.clear(); //reset the carts
    }
    
 
    private String generateReceiptNumber(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "RCP-" + dateTime.format(formatter);
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
	
	
	
