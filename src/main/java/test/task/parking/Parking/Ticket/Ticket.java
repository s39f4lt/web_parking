package test.task.parking.Parking.Ticket;

import test.task.parking.Parking.Car.Car;
import test.task.parking.Parking.Place;

public class Ticket {
    private final int NUM_FOR_GENERATOR = 50;

    private String id;
    private Car car;
    private Place parkingPlace;

    public Ticket(Car car, Place place) {
        this.id = generateTicketId(car.getNumber(), place.getNumber());
        this.car = car;
        this.parkingPlace = place;
        place.setCar(car);
    }

    private String generateTicketId(String numberCar, String numberPlace) {
        return (numberCar + numberPlace + (int)(Math.random() * NUM_FOR_GENERATOR));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Place getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(Place parkingPlace) {
        this.parkingPlace = parkingPlace;
    }
}
