package application.service;


import application.dto.RunDto;
import application.model.Run;
import application.model.User;
import application.repository.RunRepositoy;
import application.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RunService {
    @Autowired
    private RunRepositoy runRepositoy;

    @Autowired
    private UserRepository userRepository;

    public List<RunDto> findRunAll() {
        List<Run> runs = runRepositoy.fetchAll();
        List<RunDto> runDtos = new ArrayList<>();

        for (Run run: runs) {
            RunDto runDto = new RunDto();
            User user = userRepository.findByAthleteId(run.getAthleteId()).orElse(null);
            runDto.setAthleteId(run.getAthleteId());
            runDto.setDate(run.getDate());
            runDto.setDistance(run.getDistance());
            double a = run.getPace();
            double a1 = Math.round(a*100.0)/100.0;
            runDto.setPace(a1);
            runDto.setMovingTime(run.getMovingTime());
            runDto.setUser(user);
            runDtos.add(runDto);
        }
        return runDtos;
    }

}
