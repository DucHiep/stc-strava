package application.schedule;

import application.dto.RunDto;
import application.model.Run;
import application.model.Token;
import application.model.User;
import application.repository.RunRepositoy;
import application.repository.TokenRepository;
import application.repository.UserRepository;
import application.utility.ApiRequester;
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

    @Value("${spring.security.oauth2.client.registration.strava.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.strava.client-secret}")
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

    @Scheduled(cron = "* * */6 * * *")//Chạy sau mỗi 5h
    public void updateToken() throws JsonProcessingException {
        List<Token> tokens = tokenRepository.findAll();

        for (Token token : tokens) {
            List<JsonNode> jsons;
            String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("/oauth/token")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("refresh_token", token.getRefresh())
                    .queryParam("grant_type", "refresh_token")
                    .toUriString();
            ResponseEntity<String> response = apiRequester.sendGetRequestForRefreshToken(uri);

            String body = response.getBody();

            jsons = objectMapper.readValue(body, new TypeReference<List<JsonNode>>() {});

            JsonNode jsonNode = jsons.get(0);

            Token updateToken = tokenRepository.findById(token.getId()).orElse(null);
            updateToken.setAccess(jsonNode.get("access_token").asText());
            updateToken.setRefresh(jsonNode.get("refresh_token").asText());
            tokenRepository.save(updateToken);
        }

    }

    @Scheduled(cron = "0 0 * * * *")//chạy sau mỗi 0h 0p mỗi
    public void activitySync() throws JsonProcessingException {
        List<User> users = userRepository.findAll();

        List<JsonNode> jsons;
        String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("api/v3/activities")
                .toUriString();
        ResponseEntity<String> response = apiRequester.sendGetRequestForRefreshToken(uri);

        String body = response.getBody();

        jsons = objectMapper.readValue(body, new TypeReference<List<JsonNode>>() {});

        JsonNode node = jsons.get(0);

        Run run = new Run();
        run.setDistance(node.get("distance").asDouble());
        run.setMovingTime(node.get("moving_time").asInt());
        run.setDate(node.get("start_date").asText());

        runRepositoy.save(run);

    }
}
