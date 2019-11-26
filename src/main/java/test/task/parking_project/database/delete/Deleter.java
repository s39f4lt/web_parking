package test.task.parking_project.database.delete;

import test.task.parking_project.database.sql.SQL;
import test.task.parking_project.parking.ticket.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deleter implements Delete {
    @Override
    public void deletePlace(Ticket ticket, Connection con) {
        try (PreparedStatement stmt = con.prepareStatement(SQL.UPDATE_EXIT_PLACE)) {
            stmt.setInt(1, Integer.parseInt(ticket.getParkingPlace().getNumber().substring(1)));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: deletePlace");
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteTicket(Ticket ticket, Connection con) {
        try (PreparedStatement stmt = con.prepareStatement(SQL.UPDATE_EXIT_TICKET)) {
            stmt.setString(1, ticket.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: deleteTicket");
            ex.printStackTrace();
        }
    }
}
