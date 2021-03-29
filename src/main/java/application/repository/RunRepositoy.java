package application.repository;

import application.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepositoy extends JpaRepository<Run,Long> {
}
