import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Name: Manuel Perez
 * Assessment: Car Data Analyzer
 * Project A: Sort/Search by Brand
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current folder: " + System.getProperty("user.dir"));

        ArrayList<Car> cars = loadCars("Car_Data.csv");
        System.out.println("Loaded " + cars.size() + " cars from CSV.");

        if (cars.isEmpty()) {
            System.out.println("No cars were loaded.");
            scanner.close();
            return;
        }

        int endIndex = Math.min(2000, cars.size());
        ArrayList<Car> working = new ArrayList<>(cars.subList(0, endIndex));
        System.out.println("Working list contains " + working.size() + " cars.");

        ArrayList<Car> foundCars = new ArrayList<>();
        boolean sorted = false;
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("\nEnter your choice: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                    insertionSortByBrand(working);
                    sorted = true;
                    System.out.println("Working list sorted by brand.");
                    break;

                case 2:
                    if (working.isEmpty()) {
                        System.out.println("Working list is empty.");
                        break;
                    }

                    if (!sorted) {
                        insertionSortByBrand(working);
                        sorted = true;
                    }

                    System.out.print("Enter brand to search for: ");
                    String brandKey = scanner.nextLine().trim();

                    foundCars = searchAllByBrand(working, brandKey);

                    if (foundCars.isEmpty()) {
                        System.out.println("No cars found for brand: " + brandKey);
                    } else {
                        System.out.println(foundCars.size() + " car(s) found for brand: " + brandKey);
                    }
                    break;

                case 3:
                    if (foundCars.isEmpty()) {
                        System.out.println("No found car objects to display. Perform a search first.");
                    } else {
                        for (Car car : foundCars) {
                            System.out.println(car);
                        }
                    }
                    break;

                case 4:
                    showStats(working);
                    break;

                case 5:
                    System.out.println("Exiting program.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    public static void printMenu() {
        System.out.println("\n===== Car Data Analyzer =====");
        System.out.println("1. Sort working list by brand");
        System.out.println("2. Search working list by brand");
        System.out.println("3. Show found car object(s)");
        System.out.println("4. Show statistics");
        System.out.println("5. Exit");
    }

    public static ArrayList<Car> loadCars(String filename) {
        ArrayList<Car> cars = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (parts.length < 7) {
                    continue;
                }

                try {
                    String carId = clean(parts[0]);
                    String brand = clean(parts[1]);
                    String model = clean(parts[2]);
                    int year = Integer.parseInt(clean(parts[3]));
                    String fuelType = clean(parts[4]);
                    String color = clean(parts[5]);
                    double mileageKmpl = Double.parseDouble(clean(parts[6]));

                    cars.add(new Car(carId, brand, model, year, fuelType, color, mileageKmpl));
                } catch (Exception e) {
                    // skip bad row
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return cars;
    }

    public static String clean(String text) {
        text = text.trim();
        if (text.startsWith("\"") && text.endsWith("\"") && text.length() >= 2) {
            text = text.substring(1, text.length() - 1);
        }
        return text.trim();
    }

    public static void insertionSortByBrand(ArrayList<Car> list) {
        for (int i = 1; i < list.size(); i++) {
            Car key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).getBrand().compareToIgnoreCase(key.getBrand()) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }

    public static int binarySearchByBrand(ArrayList<Car> list, String target) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = list.get(mid).getBrand().compareToIgnoreCase(target);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public static ArrayList<Car> searchAllByBrand(ArrayList<Car> list, String target) {
        ArrayList<Car> results = new ArrayList<>();
        int index = binarySearchByBrand(list, target);

        if (index == -1) {
            return results;
        }

        int left = index;
        while (left >= 0 && list.get(left).getBrand().equalsIgnoreCase(target)) {
            left--;
        }

        int right = index;
        while (right < list.size() && list.get(right).getBrand().equalsIgnoreCase(target)) {
            right++;
        }

        for (int i = left + 1; i < right; i++) {
            results.add(list.get(i));
        }

        return results;
    }

    public static void showStats(ArrayList<Car> working) {
        if (working.isEmpty()) {
            System.out.println("Working list is empty.");
            return;
        }

        double totalMileage = 0;
        Map<String, Integer> fuelCounts = new LinkedHashMap<>();

        for (Car car : working) {
            totalMileage += car.getMileageKmpl();
            String fuel = car.getFuelType();
            fuelCounts.put(fuel, fuelCounts.getOrDefault(fuel, 0) + 1);
        }

        double avgMileage = totalMileage / working.size();

        System.out.printf("Average mileage: %.2f kmpl%n", avgMileage);
        System.out.println("Fuel type counts:");
        for (Map.Entry<String, Integer> entry : fuelCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}