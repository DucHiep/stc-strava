package application.dto;

import application.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

/**
 * Created By hiepnd
 * Date: 30/03/2021
 * Time: 7:57 AM
 * Contact me via mail hiepnd@vnpt-technology.vn
 */
public class RunDto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long athleteId;
    private double distance;
    private long pace;
    private String date;
    private long movingTime;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(long athleteId) {
        this.athleteId = athleteId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getPace() {
        return pace;
    }

    public void setPace(long pace) {
        this.pace = pace;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(long movingTime) {
        this.movingTime = movingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}