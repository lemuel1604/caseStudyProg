package caseStudy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Carts {
    private HashMap<Products, Integer> items;

    public Carts() {
        items = new HashMap<>();
    }

    public void addToCart(Products product, int quantity) {
        if (items.containsKey(product)) {
            items.put(product, items.get(product) + quantity);
        } else {
            items.put(product, quantity);
        }
        System.out.println(quantity + " " + product.getname() + " added to cart");
    }

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

        int cheesecakeQty = 5;
        int croissantQty = 5;

        // Find quantities and freebies reference
        for (Products p : items.keySet()) {
            String name = p.getname().toLowerCase();
            int qty = items.get(p);
            if (name.equals("cheesecake")) cheesecakeQty = qty;
            if (name.equals("croissant")) croissantQty = qty;
            if (name.equals("cupcake")) cupcake = p;
            if (name.equals("donut")) donut = p;
        }

        // Mark promo eligibility
        if (cheesecakeQty >= 5) cheesecakeFreebie = true;
        if (croissantQty >= 5) croissantFreebie = true;

        // Add freebies once before total calculation
        if (cheesecakeFreebie && cupcake != null) {
            addToCart(cupcake, 1);
            System.out.println("Promo applied: Free Cupcake added for buying 5 Cheesecakes!");
        }
        if (croissantFreebie && donut != null) {
            addToCart(donut, 1);
            System.out.println("Promo applied: Free Donut added for buying 5 Croissants!");
        }

        // Now calculate total with freebies included
        double total = 0;
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products p = entry.getKey();
            int qty = entry.getValue();
            total += p.getprice() * qty;
        }

        // Apply 5% discount if total > 500
        double discount = (total > 500) ? total * 0.05 : 0;
        if (discount > 0) {
            System.out.printf("You got a 5%% discount! (-₱%.2f)%n", discount);
            total -= discount;
        }

        System.out.printf("\nTotal amount: ₱%.2f%n", total);

        // Payment process
        double payment = 0;
        while (payment < total) {
            System.out.print("Enter payment amount: ₱");
            try {
                payment = Double.parseDouble(scanner.nextLine().trim());
                if (payment < total) {
                    System.out.println("Insufficient payment. Please enter at least ₱" + total);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }

        // Tip option
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

        double change = payment - (total + tipAmount);
        LocalDateTime showTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM/dd/yyyy\nhh:mm a");
        String formattedDateTime = showTime.format(format);

        System.out.println("\n================ RECEIPT ================");
        System.out.println("MAPAGMAHAL PASTRY SHOP");
        System.out.println("---------------------------------------");
        System.out.println("Date & Time: " + formattedDateTime);
        System.out.println("Receipt #: " + generateReceiptNumber(showTime));
        System.out.println("---------------------------------------");
        System.out.println("ITEMS:");

        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = product.getprice() * quantity;
            System.out.printf("%s (ID: %s)\n", product.getname(), product.getID());
            System.out.printf("  %d × ₱%.2f = ₱%.2f%n", quantity, product.getprice(), itemTotal);
        }

        System.out.println("---------------------------------------");
        System.out.printf("Subtotal:         ₱%.2f%n", total + discount);
        if (discount > 0) System.out.printf("Discount:         -₱%.2f%n", discount);
        if (tipAmount > 0) {
            System.out.printf("Tip:              ₱%.2f%n", tipAmount);
            System.out.printf("Total:            ₱%.2f%n", (total + tipAmount));
        }
        System.out.printf("Payment:          ₱%.2f%n", payment);
        System.out.printf("Change:           ₱%.2f%n", change);
        System.out.println("---------------------------------------");
        System.out.println("Thank you for shopping with us!");
        System.out.println("Please come again!");
        System.out.println("========================================");

        // Clear cart and restore stock is handled elsewhere if needed
        items.clear();
    }

    private String generateReceiptNumber(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return dateTime.format(formatter);
    }

    public void clearCart() {
        for (Map.Entry<Products, Integer> entry : items.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();
            product.updateStock(quantity); // Restore stock
        }
        items.clear();
        System.out.println("Cart cleared.");
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

	
	
	

