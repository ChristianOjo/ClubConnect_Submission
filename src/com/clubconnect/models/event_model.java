package com.clubconnect.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Represents an event in the ClubConnect system
 */
public class Event {
    private int eventId;
    private int clubId;
    private String title;
    private String description;
    private Date eventDate;
    private Time startTime;
    private Time endTime;
    private String venue;
    private int capacity;
    private EventType eventType;
    private boolean budgetRequested;
    private BigDecimal budgetAmount;
    private BudgetStatus budgetStatus;
    private Timestamp createdAt;
    
    public enum EventType {
        MEETING, SEATING, EVENT
    }
    
    public enum BudgetStatus {
        PENDING, APPROVED, REJECTED
    }
    
    /**
     * Default constructor
     */
    public Event() {
        this.eventType = EventType.EVENT;
        this.budgetRequested = false;
        this.budgetStatus = BudgetStatus.PENDING;
    }
    
    /**
     * Constructor with essential fields
     */
    public Event(int clubId, String title, Date eventDate, Time startTime, Time endTime, String venue) {
        this.clubId = clubId;
        this.title = title;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.eventType = EventType.EVENT;
        this.budgetRequested = false;
        this.budgetStatus = BudgetStatus.PENDING;
    }
    
    // Getters and Setters
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    
    public Time getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    
    public Time getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    
    public String getVenue() {
        return venue;
    }
    
    public void setVenue(String venue) {
        this.venue = venue;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    
    public boolean isBudgetRequested() {
        return budgetRequested;
    }
    
    public void setBudgetRequested(boolean budgetRequested) {
        this.budgetRequested = budgetRequested;
    }
    
    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }
    
    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    
    public BudgetStatus getBudgetStatus() {
        return budgetStatus;
    }
    
    public void setBudgetStatus(BudgetStatus budgetStatus) {
        this.budgetStatus = budgetStatus;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return title + " - " + eventDate;
    }
}