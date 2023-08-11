package com.klbstore.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klbstore.delivery.DistrictResponse;
import com.klbstore.delivery.FeeResponse;
import com.klbstore.delivery.LeadtimeResponse;
import com.klbstore.delivery.ProvinceResponse;
import com.klbstore.delivery.WardResponse;

@Service
public class SecurityRestTemplate {
    private final String BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api";
    private final String TOKEN = "4525b920-35a7-11ee-a59f-a260851ba65c";
    RestTemplate restTemplate = new RestTemplate();

    static ObjectMapper mapper = new ObjectMapper();

    public JsonNode get(String url) {
        return this.request(url, HttpMethod.GET, null);
    }

    public JsonNode post(String url) {
        return this.request(url, HttpMethod.POST, null);
    }
    public JsonNode post(String url, Object data) {
        return this.request(url, HttpMethod.POST, data);
    }

    public JsonNode put(String url, Object data) {
        return this.request(url, HttpMethod.PUT, data);
    }

    public ProvinceResponse getProvinces() {
        String url = BASE_URL + "/master-data/province";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ProvinceResponse provinceResponse = objectMapper.readValue(response.getBody(), ProvinceResponse.class);
                return provinceResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public DistrictResponse getDistricts(int provinceID) {
        String url = BASE_URL + "/master-data/district";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);

        Map<String, Integer> requestData = new HashMap<>();
        requestData.put("province_id", provinceID);

        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestData, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                DistrictResponse districtResponse = objectMapper.readValue(response.getBody(), DistrictResponse.class);
                return districtResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public WardResponse getWards(int districtID) {
        String url = BASE_URL + "/master-data/ward";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);

        Map<String, Integer> requestData = new HashMap<>();
        requestData.put("district_id", districtID);

        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestData, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                WardResponse wardResponse = objectMapper.readValue(response.getBody(), WardResponse.class);
                return wardResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public FeeResponse calculateShippingFee(int toDistrictId, String toWardCode, int height, int length, int weight,
            int width) {
        String shippingFeeApiUrl = BASE_URL + "/v2/shipping-order/fee";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON); // Đảm bảo đúng kiểu dữ liệu là application/json
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("service_id", 53321);
        requestData.put("insurance_value", 500000);
        requestData.put("coupon", null);
        requestData.put("from_district_id", 1574);
        requestData.put("from_ward_code", "550307");
        requestData.put("to_district_id", toDistrictId);
        requestData.put("to_ward_code", toWardCode);
        requestData.put("height", height);
        requestData.put("length", length);
        requestData.put("weight", weight);
        requestData.put("width", width);
        System.out.println(requestData);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                shippingFeeApiUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                FeeResponse feeResponse = objectMapper.readValue(response.getBody(), FeeResponse.class);
                return feeResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    public LeadtimeResponse getDateDelivery(int toDistrictId, String toWardCode) {
        String dateDeliveryUrl = BASE_URL + "/v2/shipping-order/leadtime";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("service_id", 53320);
        requestData.put("from_district_id", 1574);
        requestData.put("from_ward_code", "550307");
        requestData.put("to_district_id", toDistrictId);
        requestData.put("to_ward_code", toWardCode);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                dateDeliveryUrl, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                LeadtimeResponse leadtimeResponse = objectMapper.readValue(response.getBody(), LeadtimeResponse.class);
                return leadtimeResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public JsonNode delete(String url) {
        return this.request(url, HttpMethod.DELETE, null);
    }

    public JsonNode patch(String url, Object data) {
        return this.request(url, HttpMethod.PATCH, data);
    }

    private JsonNode request(String url, HttpMethod method, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic dXNIcjM6MTIz");
        HttpEntity<Object> entity = new HttpEntity<>(data, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, method, entity, JsonNode.class);
        return response.getBody();
    }
}
