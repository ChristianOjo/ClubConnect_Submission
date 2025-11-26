package com.clubconnect.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a club in the ClubConnect system
 */
public class Club {
    private int clubId;
    private String name;
    private String category;
    private String missionStatement;
    private String description;
    private int createdBy;
    private BigDecimal initialBudget;
    private BigDecimal currentBudget;
    private ClubStatus status;
    private Timestamp createdAt;
    
    public enum ClubStatus {
        PENDING, ACTIVE, INACTIVE, ARCHIVED
    }
    
    /**
     * Default constructor
     */
    public Club() {
        this.status = ClubStatus.PENDING;
        this.initialBudget = BigDecimal.ZERO;
        this.currentBudget = BigDecimal.ZERO;
    }
    
    /**
     * Constructor with essential fields
     */
    public Club(String name, String category, String missionStatement, int createdBy) {
        this.name = name;
        this.category = category;
        this.missionStatement = missionStatement;
        this.createdBy = createdBy;
        this.status = ClubStatus.PENDING;
        this.initialBudget = BigDecimal.ZERO;
        this.currentBudget = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getMissionStatement() {
        return missionStatement;
    }
    
    public void setMissionStatement(String missionStatement) {
        this.missionStatement = missionStatement;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public BigDecimal getInitialBudget() {
        return initialBudget;
    }
    
    public void setInitialBudget(BigDecimal initialBudget) {
        this.initialBudget = initialBudget;
    }
    
    public BigDecimal getCurrentBudget() {
        return currentBudget;
    }
    
    public void setCurrentBudget(BigDecimal currentBudget) {
        this.currentBudget = currentBudget;
    }
    
    public ClubStatus getStatus() {
        return status;
    }
    
    public void setStatus(ClubStatus status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return name;
    }
}