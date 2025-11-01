package com.example.smartwastemanagement;

public class WasteRequest {
    private String requestId;
    private String userId;
    private String address;
    private String wasteType;
    private String description;
    private String status;
    private String timestamp;

    public WasteRequest() {
        // Empty constructor needed for Firebase
    }

    public WasteRequest(String requestId, String userId, String address, String wasteType,
                        String description, String status, String timestamp) {
        this.requestId = requestId;
        this.userId = userId;
        this.address = address;
        this.wasteType = wasteType;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getRequestId() { return requestId; }
    public String getUserId() { return userId; }
    public String getAddress() { return address; }
    public String getWasteType() { return wasteType; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }

    // Setters
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setAddress(String address) { this.address = address; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
