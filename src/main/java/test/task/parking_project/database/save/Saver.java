package test.task.parking_project.database.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.task.parking_project.domain.car.Car;
import test.task.parking_project.domain.Parking;
import test.task.parking_project.domain.ticket.Ticket;
import test.task.parking_project.database.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Saver implements Save {
    private static final Logger logger = LoggerFactory.getLogger(Saver.class);

    @Override
    public void saveTicket(Ticket ticket, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.INSERT_TICKET)) {
            stmt.setString(1, ticket.getId());
            stmt.setString(2, ticket.getCar().getModel());
            stmt.setString(3, ticket.getCar().getNumber());
            stmt.setInt(4, Integer.parseInt(ticket.getParkingPlace().getNumber().substring(1)));
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(6, (Boolean.toString(ticket.getOnParking())));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
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
                stmtIns.setString(2, car.getModel());
                stmtIns.executeUpdate();
                stmtIns.close();
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void saveParkingPlace(Ticket ticket, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.UPDATE_PLACE)) {
            stmt.setString(1, ticket.getId());
            stmt.setString(2, ticket.getCar().getNumber());
            stmt.setString(3, (Boolean.toString(ticket.getParkingPlace().isFree())));
            stmt.setInt(4, Integer.parseInt(ticket.getParkingPlace().getNumber().substring(1)));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void initParking(Parking parking, Connection connection) {
        try (PreparedStatement stmtCheck = connection.prepareStatement(SQL.SELECT_INIT_PLACES)) {
            ResultSet rs = stmtCheck.executeQuery();
            if (!rs.next()) {
                addSizeParking(parking, connection);
                PreparedStatement stmtInit = connection.prepareStatement(SQL.INIT_PARKING);
                for (int place = parking.getNumberPlaces(); place != 0; place--) {
                    stmtInit.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void addSizeParking(Parking parking, Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL.ADD_SIZE_PARKING)) {
            stmt.setInt(1, parking.getNumberPlaces());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
