package net.bamss.bamssanalytics.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssanalytics.connections.AuthConnection;
import net.bamss.bamssanalytics.models.UserAnalytics;
import net.bamss.bamssanalytics.util.DatabaseUtils;

@RestController
public class AnalyticsController {
	@PostMapping("/analytics")
	public ResponseEntity<UserAnalytics> userLevelAnalytis(@RequestBody Map<String, Object> body) {
    String token = (String) body.get("token");
    String apiKey = (String) body.get("api_key");
    long startDateTs = Long.parseLong("" + body.get("start_date"));
    long endDateTs = Long.parseLong("" + body.get("end_date"));
    List<String> keys = (List<String>) body.get("keys");

    String username = null;
		if (token != null) {
			username = AuthConnection.validateToken(token);
		} else if (apiKey != null) {
			username = AuthConnection.validateApiKey(apiKey);
    }
    
    if (username != null) {
      UserAnalytics result = DatabaseUtils.getUserLevelAnalytics(keys, startDateTs, endDateTs);
      return new ResponseEntity<>(result, HttpStatus.OK);
    }

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}
