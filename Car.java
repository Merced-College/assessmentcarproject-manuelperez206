/**
 * Name: Manuel Perez
 * Assessment: Car Data Analyzer
 */
public class Car {
    private String carId;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private String color;
    private double mileageKmpl;

    public Car(String carId, String brand, String model, int year,
               String fuelType, String color, double mileageKmpl) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.color = color;
        this.mileageKmpl = mileageKmpl;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getColor() {
        return color;
    }

    public double getMileageKmpl() {
        return mileageKmpl;
    }

    @Override
    public String toString() {
        return "Car ID: " + carId +
               ", Brand: " + brand +
               ", Model: " + model +
               ", Year: " + year +
               ", Fuel Type: " + fuelType +
               ", Color: " + color +
               ", Mileage (kmpl): " + mileageKmpl;
    }
}