package GenerateFiles;
import java.io.*;
import java.util.*;

/**
 * The GenerateInfoFiles class is responsible for generating test files as specified.
 * It provides methods to create files containing sales information for salesmen,
 * product information, and salesman information.
 */
public class GenerateInfoFiles {
    
    /**
     * Main method to generate test files.
     * It invokes methods to create files for salesmen, products, and salesman information.
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            createSalesMenFile(10, "salesmen", 1000);
            createProductsFile(50);
            createSalesManInfoFile(10);
            System.out.println("Test files generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating test files: " + e.getMessage());
        }
    }

    /**
     * Creates a file with sales information for salesmen.
     * Each salesman has a unique identifier and multiple sales records.
     * @param randomSalesCount The number of random sales records for each salesman.
     * @param name The base name for the file to be generated.
     * @param id The starting ID for salesmen.
     * @throws IOException If an I/O error occurs while creating the file.
     */
    private static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        Random random = new Random();
        try (PrintWriter writer = new PrintWriter(name + ".txt")) {
            for (int i = 0; i < randomSalesCount; i++) {
                writer.println("ID" + id + ";John_" + (i + 1) + " Doe_" + (i + 1));
                for (int j = 0; j < 3; j++) {
                    writer.println("Product_" + (j + 1) + ";" + (random.nextInt(10) + 1));
                }
                id++;
            }
        }
    }

    /**
     * Creates a file with product information.
     * Each product has a unique identifier, name, and price.
     * @param productsCount The number of products to be generated.
     * @throws IOException If an I/O error occurs while creating the file.
     */
    private static void createProductsFile(int productsCount) throws IOException {
        try (PrintWriter writer = new PrintWriter("products.txt")) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                writer.println("ProductID_" + (i + 1) + ";ProductName_" + (i + 1) + ";$" + (Math.round(random.nextDouble() * 100 * 100.0) / 100.0));
            }
        }
    }

    /**
     * Creates a file with information for salesmen.
     * Each salesman has a unique identifier, name, and additional information.
     * @param salesmanCount The number of salesmen to be generated.
     * @throws IOException If an I/O error occurs while creating the file.
     */ 
    private static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (PrintWriter writer = new PrintWriter("salesmen_info.txt")) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                writer.println("Cedula;" + (random.nextInt(100000)) +";John_" + (i + 1) + ";Doe_" + (i + 1));
            }
        }
    }
}
