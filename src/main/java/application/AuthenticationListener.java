package application;

import application.dto.RunDto;
import application.model.Run;
import application.model.Token;
import application.model.User;
import application.repository.RunRepositoy;
import application.repository.TokenRepository;
import application.repository.UserRepository;
import application.utility.ApiRequester;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;


@Component
public class AuthenticationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ApiRequester apiRequester;
    private final ObjectMapper objectMapper;
    private final RunRepositoy runRepositoy;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationListener(ApiRequester apiRequester,
                                  ObjectMapper objectMapper,
                                  RunRepositoy runRepositoy,
                                  TokenRepository tokenRepository
    ) {
        this.apiRequester = apiRequester;
        this.objectMapper = objectMapper;
        this.runRepositoy = runRepositoy;
        this.tokenRepository = tokenRepository;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        List<Token> tokens = tokenRepository.fetchAll();
        for (Token token : tokens) {
            List<JsonNode> jsons;
            String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("/api/v3/athlete/activities")
                    .toUriString();
            ResponseEntity<String> response = apiRequester.sendGetRequest(token.getAccess(), uri);

            String body = response.getBody();

            jsons = objectMapper.readValue(body, new TypeReference<List<JsonNode>>() {});

            for (JsonNode node : jsons) {
                Run run = new Run();
                double distance = node.get("distance").asDouble();
                long movingTime = node.get("moving_time").asLong();
                double avgPace = (double) distance / movingTime;
                run.setAthleteId(token.getAthleteId());
                run.setDistance(distance);
                run.setMovingTime(movingTime);
                run.setPace(avgPace);
                run.setDate(node.get("start_date").asText());
                runRepositoy.save(run);
            }
        }

    }
}

/**
@Component
public class AuthenticationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent>{

    private final OAuth2AuthorizedClientService clientService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ApiRequester apiRequester;
    private final ObjectMapper objectMapper;
    private final RunRepositoy runRepositoy;

    @Autowired
    public AuthenticationListener(OAuth2AuthorizedClientService clientService,
                                  TokenRepository tokenRepository,
                                  UserRepository userRepository,
                                  ApiRequester apiRequester,
                                  ObjectMapper objectMapper,
                                  RunRepositoy runRepositoy
                                  ) {
        this.clientService = clientService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.apiRequester = apiRequester;
        this.objectMapper = objectMapper;
        this.runRepositoy = runRepositoy;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) event.getAuthentication();
        String registrationId = token.getAuthorizedClientRegistrationId();
        OAuth2User user = token.getPrincipal();

        Map<String, Object> map = user.getAttributes();
        DefaultOAuth2User defaultUser = (DefaultOAuth2User) event.getAuthentication().getPrincipal();

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(registrationId, defaultUser.getName());
        String access_token =  client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();

        Token insertToken = new Token();
        insertToken.setAccess(access_token);
        insertToken.setRefresh(refreshToken);
        tokenRepository.save(insertToken);


        String fullName = map.get("lastname") + " " + map.get("firstname");
        String image = (String) map.get("profile");

        User insertUser = new User();
        insertUser.setName(fullName);
        insertUser.setImage(image);
        userRepository.save(insertUser);

        List<JsonNode> jsons;
        String uri = UriComponentsBuilder.newInstance().scheme("https").host("www.strava.com").path("/api/v3/athlete/activities")
                .toUriString();
        ResponseEntity<String> response = apiRequester.sendGetRequest(client, uri);

        String body = response.getBody();

        jsons = objectMapper.readValue(body, new TypeReference<List<JsonNode>>() {});

        for (JsonNode node : jsons) {
            Run run = new Run();
            run.setDistance(node.get("distance").asDouble());
            run.setMovingTime(node.get("moving_time").asInt());
            run.setDate(node.get("start_date").asText());
            runRepositoy.save(run);
        }
    }
}
*/
