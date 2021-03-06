package application.dto;

import application.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime date;
    private double totalPoint;
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
//        BigDecimal decimalDistance = new BigDecimal(distance);
//        decimalDistance = decimalDistance.divide(new BigDecimal(1000f));
//        decimalDistance = decimalDistance.setScale(2, RoundingMode.CEILING);
////        double distancess = distance/1000.0;
        double distances =(Math.round((distance)*100.0)/100.0);

        this.distance =distances;
    }

    public double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(double avgPace) {
        double avg =Math.round(avgPace*100.0)/100.0;
        this.avgPace = avg;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setTotalPoint(double totalPoint){
        double tpoints =Math.round((totalPoint)*100.0)/100.0;
        this.totalPoint = tpoints;
    }
    public double getTotalPoint(){
        return totalPoint;
    }
}