package com.example.consumer;

import com.example.consumer.service.ProductService1;
import com.example.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Component
public class ConsoleInterface implements CommandLineRunner {
	private final ProductService1 productService;
	private List<Product> products;

	public ConsoleInterface(ProductService1 productService) {
		this.productService = productService;
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			printAllProducts();
			Integer choice = getUserChoice(scanner);
			if (choice == null || choice <= 0 || choice > products.size()) {
				System.out.println("Exiting...");
				break;
			}
			printProduct(choice);
		}
	}

	private void printAllProducts() {
		products = productService.getAllProducts();
		System.out.println("\n\nProducts\n--------");
		IntStream.range(0, products.size())
				.forEach(index -> System.out.println(String.format("%d) %s", index + 1, products.get(index).getName())));
	}

	private Integer getUserChoice(Scanner scanner) {
		System.out.print("Select item to view details: ");
		String choice = scanner.nextLine();
		return parseChoice(choice);
	}

	private void printProduct(int index) {
		String id = products.get(index - 1).getId();
		try {
			Product product = productService.getProduct(id);

			System.out.println("Product Details\n---------------");
			System.out.println(product);
		} catch (Exception e) {
			System.out.println("Failed to load product " + id);
			System.out.println(e.getMessage());
		}
	}

	private Integer parseChoice(String choice) {
		try {
			return Integer.parseInt(choice);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
