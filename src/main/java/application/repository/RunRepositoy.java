package application.repository;

import application.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RunRepositoy extends JpaRepository<Run,Long> {

    @Query(value = "SELECT * FROM Run r INNER JOIN User u ON r.athlete_id = u.athlete_id", nativeQuery = true)
    List<Run> fetchAll();

    @Query(value = "SELECT * FROM Run r WHERE r.date between ?1 and ?2 ", nativeQuery = true)
    List<Run> statistic(Date fromDate, Date toDate);
}
