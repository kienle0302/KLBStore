// package com.klbstore.security.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import lombok.AllArgsConstructor;

// @Service
// @AllArgsConstructor
// public class ApiService {
//     private final RestTemplate restTemplate;

//     public void performApiRequest(String jwtToken) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Bearer " + jwtToken); // Set JWT token in Authorization header

//         HttpEntity<String> entity = new HttpEntity<>(headers);

//         String apiUrl = "URL_TO_OTHER_API_ENDPOINT"; // Replace with your API endpoint URL
//         ResponseEntity<String> responseEntity = restTemplate.exchange(
//             apiUrl,
//             HttpMethod.GET, // Change the HTTP method based on your API requirement
//             entity,
//             String.class
//         );

//         if (responseEntity.getStatusCode().is2xxSuccessful()) {
//             String responseBody = responseEntity.getBody();
//             // Process the response body as needed
//         } else {
//             // Handle API error response
//         }
//     }
// }
