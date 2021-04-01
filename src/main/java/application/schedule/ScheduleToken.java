package application.schedule;

import application.dto.RunDto;
import application.model.Run;
import application.model.Token;
import application.model.User;
import application.repository.RunRepositoy;
import application.repository.TokenRepository;
import application.repository.UserRepository;
import application.utility.ApiRequester;
import application.utility.AppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Created By hiepnd
 * Date: 29/03/2021
 * Time: 11:18 AM
 * Contact me via mail hiepnd@vnpt-technology.vn
 */

@Configuration
@EnableScheduling
public class ScheduleToken {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    private final TokenRepository tokenRepository;
    private final ApiRequester apiRequester;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RunRepositoy runRepositoy;

    public ScheduleToken(TokenRepository tokenRepository,
                         ApiRequester apiRequester,
                         ObjectMapper objectMapper,
                         UserRepository userRepository,
                         RunRepositoy runRepositoy) {
        this.tokenRepository = tokenRepository;
        this.apiRequester = apiRequester;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.runRepositoy = runRepositoy;
    }

    @Scheduled(cron = "0 0 0 * * *")//chạy sau mỗi 0h 0p mỗi
    public void updateToken() throws JsonProcessingException {
        List<Token> tokens = tokenRepository.findAll();

        for (Token token : tokens) {
            JsonNode jsonNode;
            String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("/oauth/token")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("refresh_token", token.getRefresh())
                    .queryParam("grant_type", "refresh_token")
                    .toUriString();
            ResponseEntity<String> response = apiRequester.sendGetRequestForRefreshToken(uri);

            String body = response.getBody();

            jsonNode = objectMapper.readValue(body, new TypeReference<JsonNode>() {});

            Token updateToken = tokenRepository.findById(token.getId()).orElse(null);
            updateToken.setAccess(jsonNode.get("access_token").asText());
            updateToken.setRefresh(jsonNode.get("refresh_token").asText());
            tokenRepository.save(updateToken);
        }

    }

    @Scheduled(cron = "0 0 0 * * *")//chạy sau mỗi 0h 0p mỗi
    public void activitySync() throws JsonProcessingException {
        List<Token> tokens = tokenRepository.findAll();
        for (Token token : tokens) {
            List<JsonNode> jsons;
            String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("api/v3/activities")
                    .toUriString();
            ResponseEntity<String> response = apiRequester.sendGetRequestForRefreshToken(uri);

            String body = response.getBody();

            jsons = objectMapper.readValue(body, new TypeReference<List<JsonNode>>() {
            });

            JsonNode node = jsons.get(0);

            double distance = node.get("distance").asDouble();
            long movingTime = node.get("moving_time").asLong();
            double avgPace = (movingTime / 60) / (distance / 1000);
            String date = node.get("start_date").asText();
            String type = node.get("type").asText();

            String[] splitDate = date.split("T");
            LocalDate localDate = LocalDate.parse(splitDate[0]);

            Run run = new Run();
            if (type.equals("Run")) {
                run.setAthleteId(token.getAthleteId());
                run.setDistance(distance);
                run.setMovingTime(movingTime);
                run.setPace(avgPace);
                run.setDate(AppUtil.convertToDateViaInstant(localDate));
                runRepositoy.save(run);
            }
        }

    }
}
