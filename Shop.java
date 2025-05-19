package caseStudy;

import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        Carts cart = new Carts();
        ArrayList<Products> productList = new ArrayList<>();

        // Initialize products with names handled inside their constructors
        productList.add(new Donuts(30.0, 10, "1234"));
        productList.add(new Croissant(70.0, 10, "2341"));
        productList.add(new Cheesecake(90.0, 10, "3412"));
        productList.add(new Cupcake(30.0, 10, "4123"));
        productList.add(new Eggtart(40.0, 20, "12345"));

        boolean isOpen = true;
        while (isOpen) {
            Thread.sleep(2000);
            System.out.println("\n~~~ Mapagmahal Pastry Shop ~~~\n");
            Thread.sleep(2000);
            System.out.println("Please choose an option:");
            System.out.println("1. View Menu");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Clear Cart");
            System.out.println("5. Exit");
            Thread.sleep(2000);
            System.out.print("Enter your choice: ");
            String mainChoice = sc.nextLine().trim();
            System.out.println();

            switch (mainChoice) {
                case "1":
                    // Display menu
                    System.out.println("\n============ OUR MENU ============");
                    int index = 1;
                    for (Products product : productList) {
                        System.out.printf("%d. %s - ₱%.2f (Stock: %d)%n",
                                index++, capitalize(product.getname()), product.getprice(), product.getStock());
                    }
                    System.out.println("=================================\n");

                    boolean ordering = true;
                    while (ordering) {
                        System.out.print("Enter product name to order \n(or 'back' to return to main menu): ");
                        String choice = sc.nextLine().trim().toLowerCase();

                        if (choice.equals("back")) {
                            ordering = false;
                            continue;
                        }

                        // Find selected product by name (case-insensitive)
                        Products selectedProduct = null;
                        for (Products product : productList) {
                            if (product.getname().equalsIgnoreCase(choice)) {
                                selectedProduct = product;
                                break;
                            }
                        }

                        if (selectedProduct == null) {
                            System.out.println("Product not found. Please try again.");
                            continue;
                        }

                        if (selectedProduct.getStock() <= 0) {
                            System.out.println("Sorry, " + capitalize(selectedProduct.getname()) + " is out of stock.");
                            continue;
                        }

                        // Ask for quantity
                        System.out.print("Enter quantity (available: " + selectedProduct.getStock() + "): ");
                        int quantity;
                        try {
                            quantity = Integer.parseInt(sc.nextLine().trim());
                            if (quantity <= 0) {
                                System.out.println("Please enter a positive number.");
                                continue;
                            }
                            if (quantity > selectedProduct.getStock()) {
                                System.out.println("Sorry, only " + selectedProduct.getStock() + " available.");
                                continue;
                            }

                            // Update stock before adding to cart
                            selectedProduct.updateStock(-quantity);
                            cart.addToCart(selectedProduct, quantity);

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Please try again.");
                            continue;
                        }

                        System.out.print("Do you want to order more? (yes/no): ");
                        String orderMore = sc.nextLine().toLowerCase().trim();
                        if (!orderMore.equals("yes")) {
                            ordering = false;
                        }
                    }
                    break;

                case "2":
                    // View cart
                    cart.viewCarts();
                    break;

                case "3":
                    // Checkout
                    cart.viewCarts();
                    if (!cart.isEmpty()) {
                        System.out.print("Do you want to checkout? (yes/no): ");
                        String confirmCheckout = sc.nextLine().toLowerCase().trim();
                        if (confirmCheckout.equals("yes")) {
                            cart.checkout(sc);
                        }
                    }
                    break;

                case "4":
                    cart.clearCart();
                    break;

                case "5":
                    // Exit to welcome screen
                    System.out.println("\n======================================");
                    System.out.println("Thank you for visiting Mapagmahal Pastry Shop!");
                    System.out.println("======================================");
                    System.out.println("\nPress Enter to Shop again...");
                    sc.nextLine();
                    isOpen = false; // Close the shop loop
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }

    // Utility method to capitalize first letter of product names for display
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
