package application.dto;

import application.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created By hiepnd
 * Date: 01/04/2021
 * Time: 9:32 AM
 * Contact me via mail hiepnd@vnpt-technology.vn
 */
public class Statistic {

    private long id;
    private double distance;
    private long athleteId;
    private double avgPace;
    private long runs;
    private Date date;
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

    public double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(double avgPace) {
        this.avgPace = avgPace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getRuns() {
        return runs;
    }

    public void setRuns(long runs) {
        this.runs = runs;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}