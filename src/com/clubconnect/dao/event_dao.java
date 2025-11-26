package com.clubconnect.dao;

import com.clubconnect.database.DatabaseManager;
import com.clubconnect.models.Event;
import com.clubconnect.models.Event.EventType;
import com.clubconnect.models.Event.BudgetStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Event operations
 */
public class EventDAO {
    
    /**
     * Creates a new event
     */
    public boolean createEvent(Event event) {
        String sql = "INSERT INTO events (club_id, title, description, event_date, start_time, " +
                    "end_time, venue, capacity, event_type, budget_requested, budget_amount, budget_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, event.getClubId());
            pstmt.setString(2, event.getTitle());
            pstmt.setString(3, event.getDescription());
            pstmt.setDate(4, event.getEventDate());
            pstmt.setTime(5, event.getStartTime());
            pstmt.setTime(6, event.getEndTime());
            pstmt.setString(7, event.getVenue());
            pstmt.setInt(8, event.getCapacity());
            pstmt.setString(9, event.getEventType().name());
            pstmt.setBoolean(10, event.isBudgetRequested());
            pstmt.setBigDecimal(11, event.getBudgetAmount());
            pstmt.setString(12, event.getBudgetStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        event.setEventId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves an event by ID
     */
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEventFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date DESC, start_time DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Retrieves events by club
     */
    public List<Event> getEventsByClub(int clubId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE club_id = ? ORDER BY event_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Retrieves upcoming events
     */
    public List<Event> getUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE event_date >= CURDATE() " +
                    "ORDER BY event_date ASC, start_time ASC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Retrieves upcoming events for a specific club
     */
    public List<Event> getUpcomingEventsByClub(int clubId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE club_id = ? AND event_date >= CURDATE() " +
                    "ORDER BY event_date ASC, start_time ASC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Retrieves past events
     */
    public List<Event> getPastEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE event_date < CURDATE() " +
                    "ORDER BY event_date DESC, start_time DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Retrieves events by type
     */
    public List<Event> getEventsByType(EventType eventType) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE event_type = ? ORDER BY event_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, eventType.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Checks for resource conflicts
     */
    public boolean hasResourceConflict(int resourceId, Date eventDate, Time startTime, Time endTime, Integer excludeEventId) {
        String sql = "SELECT COUNT(*) FROM resource_bookings rb " +
                    "JOIN events e ON rb.event_id = e.event_id " +
                    "WHERE rb.resource_id = ? AND rb.booking_date = ? " +
                    "AND rb.status = 'CONFIRMED' " +
                    "AND ((rb.start_time < ? AND rb.end_time > ?) OR " +
                    "(rb.start_time < ? AND rb.end_time > ?) OR " +
                    "(rb.start_time >= ? AND rb.end_time <= ?))";
        
        if (excludeEventId != null) {
            sql += " AND rb.event_id != ?";
        }
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, resourceId);
            pstmt.setDate(2, eventDate);
            pstmt.setTime(3, endTime);
            pstmt.setTime(4, startTime);
            pstmt.setTime(5, startTime);
            pstmt.setTime(6, startTime);
            pstmt.setTime(7, startTime);
            pstmt.setTime(8, endTime);
            
            if (excludeEventId != null) {
                pstmt.setInt(9, excludeEventId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Updates event information
     */
    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET title = ?, description = ?, event_date = ?, " +
                    "start_time = ?, end_time = ?, venue = ?, capacity = ?, event_type = ?, " +
                    "budget_requested = ?, budget_amount = ?, budget_status = ? " +
                    "WHERE event_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setDate(3, event.getEventDate());
            pstmt.setTime(4, event.getStartTime());
            pstmt.setTime(5, event.getEndTime());
            pstmt.setString(6, event.getVenue());
            pstmt.setInt(7, event.getCapacity());
            pstmt.setString(8, event.getEventType().name());
            pstmt.setBoolean(9, event.isBudgetRequested());
            pstmt.setBigDecimal(10, event.getBudgetAmount());
            pstmt.setString(11, event.getBudgetStatus().name());
            pstmt.setInt(12, event.getEventId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Approves budget for an event
     */
    public boolean approveBudget(int eventId) {
        String sql = "UPDATE events SET budget_status = 'APPROVED' WHERE event_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Rejects budget for an event
     */
    public boolean rejectBudget(int eventId) {
        String sql = "UPDATE events SET budget_status = 'REJECTED' WHERE event_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Gets events with pending budget requests
     */
    public List<Event> getEventsWithPendingBudget() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE budget_requested = TRUE " +
                    "AND budget_status = 'PENDING' ORDER BY created_at ASC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Deletes an event
     */
    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Gets RSVP count for an event
     */
    public int getRSVPCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM rsvps WHERE event_id = ? AND status = 'ATTENDING'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Searches events by keyword
     */
    public List<Event> searchEvents(String keyword) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE title LIKE ? OR description LIKE ? " +
                    "ORDER BY event_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * Extracts Event object from ResultSet
     */
    private Event extractEventFromResultSet(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setClubId(rs.getInt("club_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getDate("event_date"));
        event.setStartTime(rs.getTime("start_time"));
        event.setEndTime(rs.getTime("end_time"));
        event.setVenue(rs.getString("venue"));
        event.setCapacity(rs.getInt("capacity"));
        event.setEventType(EventType.valueOf(rs.getString("event_type")));
        event.setBudgetRequested(rs.getBoolean("budget_requested"));
        event.setBudgetAmount(rs.getBigDecimal("budget_amount"));
        event.setBudgetStatus(BudgetStatus.valueOf(rs.getString("budget_status")));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        return event;
    }
}