package shopping;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
	Logger logger = Logger.getInstance();
	logger.info("WELCOME TO ONLINE SHOPPING CATALOGUE.");

	Scanner scanner = new Scanner(System.in);
	Authen authService = new Authen();
	Products catalog = new Products();
	Cart cart = Cart.getInstance();
	OrderProcessing orderProcessing = new OrderProcessing (new PaymentBilling());
	CartBuilder cartBuilder = new CartBuilder();
	PaymentBilling paymentBilling = new PaymentBilling();

	// Populate product catalog for demonstration
	ProductFactory factory = new ProductFactory();
	catalog.addProduct(factory.createProduct("Electronics", "P2001", "WATCH", 200.99, "WATER RESISTANT"));
	catalog.addProduct(factory.createProduct("Electronics", "P2002", "phone", 9999.95, "5G Version"));
	catalog.addProduct(factory.createProduct("Electronics", "P2003", " TABLET", 899.93, "NVIDIA "));
	catalog.addProduct(factory.createProduct("Electronics", "P2004", "TV", 150.50, "SMART TV"));
	catalog.addProduct(factory.createProduct("Clothing", "P1001", "PANT", 29.99, "TORN"));
	catalog.addProduct(factory.createProduct("Clothing", "P1002", "HOODIES", 30.99, "TURTLE NECK"));
	catalog.addProduct(factory.createProduct("Clothing", "P1003", "JACKETS", 60.99, "LEATHER"));
	catalog.addProduct(factory.createProduct("Clothing", "P1004", "SHIRTS", 29.99, "NYLON"));
	catalog.addProduct(factory.createProduct("Kitchen", "P3001", "OVEN", 49.99, "SteelBody"));
	catalog.addProduct(factory.createProduct("Kitchen", "P3002", "Hair Drier", 59.99, "Prestige"));
	catalog.addProduct(factory.createProduct("Kitchen", "P3003", "JUICEMAker", 39.99, "MASH"));
	catalog.addProduct(factory.createProduct("Kitchen", "P3004", "InductionStove", 89.99, "Prestige"));
	// logger.info("Product catalog generated.");

	boolean exit = false;
	boolean isLoggedIn = false; // login status

	while (!exit) {
	    System.out.println(
		    "\n1. Create An Account\n2. Log In\n3. Search Products\n4. View Cart\n5. Place Order\n6. Exit");
	    System.out.print("Please enter the option: ");
	    int choice = scanner.nextInt();
	    scanner.nextLine();

	    switch (choice) {
	    case 1:
		// Registration logic
		logger.info("User selected Create Account.");
		System.out.print("Username: ");
		String username = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		authService.registerUser(username, password);
		break;

	    case 2:
		// Login logic
		logger.info("User selected Log In.");
		System.out.print("Username: ");
		String loginUsername = scanner.nextLine();
		System.out.print("Password: ");
		String loginPassword = scanner.nextLine();
		isLoggedIn = authService.loginUser(loginUsername, loginPassword);
		if (isLoggedIn) {
		    System.out.println("Successfull Login.");
		} else {
		    System.out.println("Login failure. Exiting further steps.");
		}
		break;

	    case 3:
		// Check login status
		if (!isLoggedIn) {
		    System.out.println("Please Log In first.");
		    break;
		}
		// Browse products and add to cart
		catalog.listProducts();
		System.out.print("Enter Product ID to add to cart. ");
		String productChoice = scanner.nextLine();
		if (!productChoice.equals("0")) {
		    Product chosenProduct = catalog.findProductById(productChoice);
		    if (chosenProduct != null) {
			System.out.print("Enter Quantity: ");
			int quantity = scanner.nextInt();
			scanner.nextLine(); // Consume newline
			cart.addProduct(chosenProduct, quantity);
		    } else {
			System.out.println("Product not found.");
		    }
		}
		break;

	    case 4:
		// Check login status before proceeding
		if (!isLoggedIn) {
		    System.out.println("Please Log In.");
		    break;
		}
		// View cart

		double totalCost = cart.calculateTotalCost();
		System.out.println("Total amount to be paid: $" + totalCost);
		break;

	    case 5:
		// Check login status before proceeding
		if (!isLoggedIn) {
		    System.out.println("Log In first.");
		    break;
		}
		// Place order
		if (cart.isEmpty()) {
		    System.out.println(" Shopping Cart is empty.");
		    break;
		}
		// cart.viewCart();
		double cost = cart.calculateTotalCost();
		System.out.println("Total amount to be paid: $" + cost);
		System.out.println("Select payment method:");
		System.out.println("1. Credit Card\n2. Debit Card\n3. Cash\n4. UPI.");
		System.out.print("Please select an option: ");
		int paymentChoice = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		String payMethod = "";
		switch (paymentChoice) {
		case 1:
			payMethod = "Credit Card";
		    break;
		case 2:
			payMethod = "Debit Card";
		    break;
		case 3:
			payMethod = "Cash";
		    break;
		case 4:
			payMethod = "UPI";
		    break;
		default:
		    System.out.println(" Payment failed. Please try again.");
		    break;
		}

		if (!payMethod.isEmpty()) {
		    System.out.println("Selected payment method: " + payMethod);
		    if (paymentChoice >= 1 && paymentChoice <= 3) {
			boolean isPaymentSuccessful = paymentBilling.processPayment(cost);
			if (isPaymentSuccessful) {
			    System.out.println("Payment successfull.");
			    cart.clearCart();
			    System.out.println("Your cart is empty .");
			    exit = true; // Exit the application after successful payment
			} else {
			    System.out.println("Payment failed. Please try again.");
			}
		    }
		}
		break;

	    case 6:
		exit = true;
		logger.info("Exit  Application .");
		break;

	    default:
		System.out.println("Invalid choice.");
		logger.warn("Invalid choice.");
		break;
	    }
	}

	scanner.close();
	logger.info("Application closed.");
    }
}
