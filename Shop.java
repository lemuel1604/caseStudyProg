package caseStudy_SweetStudy;

import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        Carts cart = new Carts();
        ArrayList<Products> productList = new ArrayList<>();
        
        
        productList.add(new Products("Donuts ",100.0, 25, "240054"));
        productList.add(new Products("Croissant", 85.0, 50, "547801"));
        productList.add(new Products("Cheesecake", 90.0, 30, "1122333"));
        productList.add(new Products("Cupcake", 5.0, 10, "4123323"));
        productList.add(new Products ("Eggtart", 15.0, 20, "2382382"));
        
        boolean isOpen = true;
        while(isOpen) {
        	Thread.sleep(2000);
            System.out.println("\n~~~SWEET SPOT BAKERY~~~\n");
            Thread.sleep(1000);
            System.out.println("Please choose an option:");
            System.out.println("1. View Menu");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Clear Cart");
            System.out.println("5. Exit");
            Thread.sleep(1000);
            System.out.print("Enter your choice: ");
            String mainChoice = sc.nextLine().trim();
            System.out.println();
            switch(mainChoice) {
            case "1":
                // Display menu
                System.out.println("\n============ OUR MENU ============");
                int index = 1;
                for (Products product : productList) {
                    System.out.printf("%d. %s - ₱%.2f (Stock: %d)%n", 
                        index++, product.getname(), product.getprice(), product.getStock());
                }
                System.out.println("=================================");

                boolean ordering = true;
                while (ordering) {
                    System.out.print("What would you like to order? \n(Enter name or number of product. \n(or 'back' to return to main menu): ");
                    String choice = sc.nextLine().trim().toLowerCase();

                    if (choice.contains("back")) {
                        ordering = false;
                        continue;
                    }
                    
                    Products selectedProduct = null;

                    // First try to parse as number (index)
                    try {
                        int productIndex = Integer.parseInt(choice);
                        if (productIndex >= 1 && productIndex <= productList.size()) {
                            selectedProduct = productList.get(productIndex - 1); // Convert to 0-based index
                        }
                    } catch (NumberFormatException e) {
                        // If not a number, try natural language processing
                        selectedProduct = findProductFromText(choice, productList);
                    }

                    if (selectedProduct == null) {
                        System.out.println("________________________\n\nProduct not found. Please try again.\n________________________");
                        continue;
                    }

                    if (selectedProduct.getStock() <= 0) {
                        System.out.println("________________________\n\nSorry, " + selectedProduct.getname() + " is out of stock.\n________________________");
                        continue;
                    }

                    // Ask for quantity
                    System.out.print("\nEnter quantity (available: " + selectedProduct.getStock() + "): ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(sc.nextLine().trim());
                        if (quantity <= 0) {
                            System.out.println("________________________\nPlease enter a positive number.\n________________________");
                            continue;
                        }
                        if (quantity > selectedProduct.getStock()) {
                            System.out.println("________________________\n\nSorry, only " + selectedProduct.getStock() + " available.\n________________________");
                            continue;
                        }

                        // Temporarily update stock display
                        selectedProduct.updateStock(-quantity);
                        // Add to cart
                        cart.addToCart(selectedProduct, quantity);

                    } catch (NumberFormatException e) {
                    	System.out.println("________________________");
                        System.out.println("\nInvalid number. Please try again.");
                        System.out.println("________________________");
                        continue;
                    }
                    
                    System.out.println("------------------------------");
                    System.out.print("Do you want to order more? (yes/no): ");
                    String orderMore = sc.nextLine().toLowerCase().trim();
                    if (!orderMore.equals("yes")) {
                        ordering = false;
                        System.out.println("------------------------------");
                        break;
                    }
                    System.out.println("------------------------------");
                    
                }
                break;
                    
                case "2":
                    // View the cart
                    cart.viewCarts();
                    break;
                    
                case "3":
                    // Checkout
                    cart.viewCarts();
                    if(!cart.isEmpty()) {
                        System.out.print("Do you want to checkout? (yes/no): ");
                        String confirmCheckout = sc.nextLine().toLowerCase().trim();
                        if(confirmCheckout.equals("yes")) {
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
                    System.out.println("Thank you for visiting Sweet Spot Bakery!");
                    System.out.println("======================================");
                    System.out.println("\nPress Enter to Shop again...");
                    sc.nextLine();
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
    

    private static Products findProductFromText(String input, ArrayList<Products> productList) {
 
        String text = input.toLowerCase();
        
        // Define product keywords (including variations and common misspellings)
        String[][] productKeywords = {
            {"donut", "donuts", "doughnut", "donat", "donats"},
            {"croissant", "croissants", "croissan", "croisant"},
            {"cheesecake", "cheese cake", "cheese", "chesecake", "chescake"},
            {"cupcake", "cupcakes", "cup cake", "cupkake"},
            {"eggtart", "egg tart", "tart", "eggtarts"}
        };
        
        // Check each product against its keywords
        for (int i = 0; i < productKeywords.length && i < productList.size(); i++) {
            for (String keyword : productKeywords[i]) {
                if (text.contains(keyword)) {
                    return productList.get(i);
                }
            }
        }
        
       // If no keyword match found, try direct product name matching (fallback)
        for (Products product : productList) {
            String productName = product.getname().toLowerCase().trim();
            if (text.contains(productName)) {
                return product;
            }
        }
        
        return null; // No match found
    }
}
