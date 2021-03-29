package application.service;


import application.model.Run;
import application.repository.RunRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RunService {
    @Autowired
    private RunRepositoy runRepositoy;

    public List<Run> findRunAll() {
        return runRepositoy.fetchAll();
    }

}
