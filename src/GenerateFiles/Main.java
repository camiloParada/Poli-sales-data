package GenerateFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the main entry point for generating reports based on sales data.
 * It reads information about salesmen, products, and sales transactions from files,
 * generates sales reports, and writes them to CSV files.
 */
public class Main {
	
    /**
     * The main method reads information about salesmen, products, and sales transactions,
     * generates sales reports, and writes them to CSV files.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
        	// Read information about salesmen, products, and sales transactions
            List<String> salesmen = readLinesFromFile("salesmen_info.txt");
            Map<String, String> products = readProductsFromFile("products.txt");
            List<String> salesFiles = getSalesFiles("sales");

            // Generate sales report
            Map<String, Double> salesReport = generateSalesReport(salesFiles, products);
            
            // Sort salesmen by sales amount
            List<String> sortedSalesmen = sortSalesmenBySales(salesmen, salesReport);

            // Write salesmen report to CSV file
            writeSalesmenReport("salesmen_report.csv", sortedSalesmen, salesReport);
            
            // Write products report to CSV file
            writeProductsReport("products_report.csv", products);

            System.out.println("Reports generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating reports: " + e.getMessage());
        }
    }

    /**
     * Reads lines from a file and returns them as a list of strings.
     *
     * @param fileName The name of the file to read from
     * @return A list of strings containing the lines read from the file
     * @throws IOException If an I/O error occurs while reading the file
     */
    private static List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Reads product information from a file and returns it as a map.
     *
     * @param fileName The name of the file to read from
     * @return A map containing product IDs as keys and product information as values
     * @throws IOException If an I/O error occurs while reading the file
     */
    private static Map<String, String> readProductsFromFile(String fileName) throws IOException {
        Map<String, String> products = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                products.put(parts[0], parts[1] + ";" + parts[2]);
            }
        }
        return products;
    }

    /**
     * Retrieves the paths of sales files from the specified folder.
     *
     * @param folderPath The path to the folder containing sales files
     * @return A list of strings containing the absolute paths of sales files
     */
    private static List<String> getSalesFiles(String folderPath) {
        List<String> salesFiles = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    salesFiles.add(file.getAbsolutePath());
                }
            }
        }
        return salesFiles;
    }

    /**
     * Generates a sales report based on the information from sales files and products.
     *
     * @param salesFiles A list of file paths containing sales transactions
     * @param products   A map containing product IDs and their corresponding information
     * @return A map containing salesman IDs and their total sales amounts
     * @throws IOException If an I/O error occurs while reading sales files
     */
    private static Map<String, Double> generateSalesReport(List<String> salesFiles, Map<String, String> products) throws IOException {
        Map<String, Double> salesReport = new HashMap<>();
        for (String salesFile : salesFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    for (int i = 1; i < parts.length; i++) {
                        String[] sale = parts[i].split(",");
                        String productName = products.get(sale[0]);
                        double price = Double.parseDouble(productName.split(";")[1]);
                        double quantity = Double.parseDouble(sale[1]);
                        double total = price * quantity;
                        salesReport.put(parts[0], salesReport.getOrDefault(parts[0], 0.0) + total);
                    }
                }
            }
        }
        return salesReport;
    }

    /**
     * Sorts salesmen by their total sales amounts in descending order.
     *
     * @param salesmen    A list of salesman information
     * @param salesReport A map containing salesman IDs and their total sales amounts
     * @return A list of salesman information sorted by total sales amounts
     */
    private static List<String> sortSalesmenBySales(List<String> salesmen, Map<String, Double> salesReport) {
        salesmen.sort((a, b) -> {
            double salesA = salesReport.getOrDefault(a.split(";")[1], 0.0);
            double salesB = salesReport.getOrDefault(b.split(";")[1], 0.0);
            return Double.compare(salesB, salesA);
        });
        return salesmen;
    }

    /**
     * Writes the salesmen report to a CSV file.
     *
     * @param fileName       The name of the CSV file to write to
     * @param sortedSalesmen A list of salesman information sorted by total sales amounts
     * @param salesReport    A map containing salesman IDs and their total sales amounts
     * @throws IOException If an I/O error occurs while writing to the file
     */
    private static void writeSalesmenReport(String fileName, List<String> sortedSalesmen, Map<String, Double> salesReport) throws IOException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (String salesman : sortedSalesmen) {
                String[] parts = salesman.split(";");
                double sales = salesReport.getOrDefault(parts[1], 0.0);
                writer.println(parts[1] + ";" + sales);
            }
        }
    }

    /**
     * Writes the products report to a CSV file.
     *
     * @param fileName The name of the CSV file to write to
     * @param products A map containing product IDs and their corresponding information
     * @throws IOException If an I/O error occurs while writing to the file
     */
    private static void writeProductsReport(String fileName, Map<String, String> products) throws IOException {
        List<Map.Entry<String, String>> productList = new ArrayList<>(products.entrySet());
        productList.sort((a, b) -> a.getValue().compareToIgnoreCase(b.getValue()));
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (Map.Entry<String, String> entry : productList) {
                writer.println(entry.getValue());
            }
        }
    }
}