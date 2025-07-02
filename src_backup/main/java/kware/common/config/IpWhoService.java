package kware.common.config;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class IpWhoService {

    public Map<String, Object> getGeoInfo(String ip) {
        Map<String, Object> result = new HashMap<>();
        String url = "https://ipwho.is/" + ip;
        RestTemplate restTemplate = new RestTemplate();
        result = restTemplate.getForObject(url, Map.class);
        return result;
    }
}
