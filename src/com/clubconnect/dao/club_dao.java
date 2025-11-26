package com.clubconnect.dao;

import com.clubconnect.database.DatabaseManager;
import com.clubconnect.models.Club;
import com.clubconnect.models.Club.ClubStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Club operations
 */
public class ClubDAO {
    
    /**
     * Creates a new club in the database
     */
    public boolean createClub(Club club) {
        String sql = "INSERT INTO clubs (name, category, mission_statement, description, created_by, " +
                    "initial_budget, current_budget, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getCategory());
            pstmt.setString(3, club.getMissionStatement());
            pstmt.setString(4, club.getDescription());
            pstmt.setInt(5, club.getCreatedBy());
            pstmt.setBigDecimal(6, club.getInitialBudget());
            pstmt.setBigDecimal(7, club.getCurrentBudget());
            pstmt.setString(8, club.getStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        club.setClubId(rs.getInt(1));
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
     * Retrieves a club by ID
     */
    public Club getClubById(int clubId) {
        String sql = "SELECT * FROM clubs WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClubFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves all clubs
     */
    public List<Club> getAllClubs() {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT * FROM clubs ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clubs.add(extractClubFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
    
    /**
     * Retrieves clubs by status
     */
    public List<Club> getClubsByStatus(ClubStatus status) {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT * FROM clubs WHERE status = ? ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clubs.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
    
    /**
     * Searches clubs by keyword and category
     */
    public List<Club> searchClubs(String keyword, String category) {
        List<Club> clubs = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM clubs WHERE status = 'ACTIVE'"
        );
        
        List<String> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ? OR mission_statement LIKE ?)");
            String pattern = "%" + keyword + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }
        
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        
        sql.append(" ORDER BY name");
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clubs.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
    
    /**
     * Retrieves clubs by category
     */
    public List<Club> getClubsByCategory(String category) {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT * FROM clubs WHERE category = ? AND status = 'ACTIVE' ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clubs.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
    
    /**
     * Updates club information
     */
    public boolean updateClub(Club club) {
        String sql = "UPDATE clubs SET name = ?, category = ?, mission_statement = ?, " +
                    "description = ?, current_budget = ?, status = ? WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getCategory());
            pstmt.setString(3, club.getMissionStatement());
            pstmt.setString(4, club.getDescription());
            pstmt.setBigDecimal(5, club.getCurrentBudget());
            pstmt.setString(6, club.getStatus().name());
            pstmt.setInt(7, club.getClubId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Approves a pending club
     */
    public boolean approveClub(int clubId) {
        String sql = "UPDATE clubs SET status = 'ACTIVE' WHERE club_id = ? AND status = 'PENDING'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Updates club budget
     */
    public boolean updateBudget(int clubId, BigDecimal newBudget) {
        String sql = "UPDATE clubs SET current_budget = ? WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, newBudget);
            pstmt.setInt(2, clubId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Deactivates a club
     */
    public boolean deactivateClub(int clubId) {
        String sql = "UPDATE clubs SET status = 'INACTIVE' WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Archives a club
     */
    public boolean archiveClub(int clubId) {
        String sql = "UPDATE clubs SET status = 'ARCHIVED' WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Deletes a club
     */
    public boolean deleteClub(int clubId) {
        String sql = "DELETE FROM clubs WHERE club_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Gets member count for a club
     */
    public int getMemberCount(int clubId) {
        String sql = "SELECT COUNT(*) FROM club_members WHERE club_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clubId);
            
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
     * Gets clubs created by a specific user
     */
    public List<Club> getClubsByCreator(int userId) {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT * FROM clubs WHERE created_by = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clubs.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
    
    /**
     * Gets all categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM clubs WHERE category IS NOT NULL ORDER BY category";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Extracts Club object from ResultSet
     */
    private Club extractClubFromResultSet(ResultSet rs) throws SQLException {
        Club club = new Club();
        club.setClubId(rs.getInt("club_id"));
        club.setName(rs.getString("name"));
        club.setCategory(rs.getString("category"));
        club.setMissionStatement(rs.getString("mission_statement"));
        club.setDescription(rs.getString("description"));
        club.setCreatedBy(rs.getInt("created_by"));
        club.setInitialBudget(rs.getBigDecimal("initial_budget"));
        club.setCurrentBudget(rs.getBigDecimal("current_budget"));
        club.setStatus(ClubStatus.valueOf(rs.getString("status")));
        club.setCreatedAt(rs.getTimestamp("created_at"));
        return club;
    }
}