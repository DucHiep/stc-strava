package application.service;


import application.dto.RunDto;
import application.dto.Statistic;
import application.model.Run;
import application.model.User;
import application.repository.RunRepositoy;
import application.repository.UserRepository;
import application.utility.AppUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
            User user = userRepository.findById(run.getAthleteId()).orElse(null);
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

    public List<Statistic> statistic(String fromDate, String toDate) {
        Date from = null;
        Date to = null;

        try {
             from = AppUtil.convertStringToDate(fromDate);
             to = AppUtil.convertStringToDate(toDate);
        } catch (ParseException e) {
            e.getMessage();
        }

        List<Run> runs = runRepositoy.statistic(from, to);
        List<Statistic> statistics = new ArrayList<>();


        HashMap<Long, Statistic> map = new HashMap<>();
        for (int i = 0; i < runs.size(); i++) {
            Long athleteId = runs.get(i).getAthleteId();
            int count  = 0;
            if (map.containsKey(athleteId)) {
                count++;
                Statistic statistic = map.get(athleteId);
                statistic.setAthleteId(athleteId);
                statistic.setDistance(runs.get(i).getDistance() + map.get(athleteId).getDistance());
                LocalDate runLocalDate = AppUtil.convertToLocalDateViaInstant(runs.get(i).getDate());
                LocalDate mapLocalDate = AppUtil.convertToLocalDateViaInstant(map.get(athleteId).getDate());
                if ((runLocalDate.getDayOfMonth() != mapLocalDate.getDayOfMonth()) || (runLocalDate.getMonthValue() != mapLocalDate.getMonthValue())) {
                    statistic.setRuns(map.get(athleteId).getRuns() + 1);
                }
                statistic.setAvgPace((runs.get(i).getPace() + map.get(athleteId).getAvgPace())/count);
                map.put(athleteId, statistic);
            } else {
                count ++;
                Statistic statistic = new Statistic();
                statistic.setAthleteId(athleteId);
                statistic.setDistance(runs.get(i).getDistance());
                statistic.setDate(runs.get(i).getDate());
                statistic.setRuns(1);
                statistic.setAvgPace(runs.get(i).getPace());
                map.put(athleteId, statistic);
            }
        }

        for (Map.Entry<Long, Statistic> entry : map.entrySet())
        {
            Statistic statistic = entry.getValue();
            long count = statistic.getRuns();
            double avgPage = statistic.getAvgPace()/count;
            double avg = Math.round(avgPage*100.0)/100.0;
            User user = userRepository.findByAthleteId(statistic.getAthleteId()).orElse(null);
            statistic.setUser(user);
            statistic.setAvgPace(avgPage);
            statistics.add(statistic);
        }

        return statistics;
    }

}
