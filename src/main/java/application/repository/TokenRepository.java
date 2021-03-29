package application.repository;


import application.model.Token;
import application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query(value = "SELECT * FROM Token t INNER JOIN User u ON u.athlete_id = t.athlete_id", nativeQuery = true)
    List<Token> fetchAll();
}
