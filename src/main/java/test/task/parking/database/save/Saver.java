package test.task.parking.database.save;

import test.task.parking.Parking.Car.Car;
import test.task.parking.Parking.Parking;
import test.task.parking.Parking.Ticket.Ticket;
import test.task.parking.database.SQL.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Saver implements Save {
    @Override
    public void saveTicket(Ticket ticket, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.INSERT_TICKET)) {
            stmt.setString(1, ticket.getId());
            stmt.setString(2, ticket.getCar().getModel());
            stmt.setString(3, ticket.getCar().getNumber());
            stmt.setInt(4, Integer.parseInt(ticket.getParkingPlace().getNumber().substring(1)));
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: saveTicket");
            ex.printStackTrace();
        }
    }

    @Override
    public void saveCar(Car car, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.GET_CAR)) {
            stmt.setString(1, car.getNumber());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                PreparedStatement stmtIns = connection.prepareStatement(SQL.INSERT_CAR);
                stmtIns.setString(1, car.getNumber());
                stmtIns.executeUpdate();
                stmtIns.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: saveCar");
            ex.printStackTrace();
        }
    }

    @Override
    public void saveParkingPlace(Ticket ticket, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.UPDATE_PLACE)) {
            stmt.setString(1, ticket.getId());
            stmt.setString(2, (Boolean.toString(ticket.getParkingPlace().isFree())));
            stmt.setInt(3, Integer.parseInt(ticket.getParkingPlace().getNumber().substring(1)));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: savePlace");
            ex.printStackTrace();
        }
    }

    public void initParking(Parking parking, Connection connection) {
        try (PreparedStatement stmtCheck = connection.prepareStatement(SQL.SELECT_INIT_PLACES)) {
            ResultSet rs = stmtCheck.executeQuery();
            if (!rs.next()) {
                PreparedStatement stmtInit = connection.prepareStatement(SQL.INIT_PARKING);
                for (int place = parking.getNumberPlaces(); place != 0; place--) {
                    stmtInit.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: initParking");
            ex.printStackTrace();
        }
    }
}