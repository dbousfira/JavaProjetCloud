package projetcloud.model;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

public abstract class ServicesCaller {
	
	/**
	 * call a web service
	 * @param url url of the service
	 * @param method http method
	 * @param expected response type expected
	 * @param args optionnals args
	 * @return a json object if returned
	 * @throws Exception if an error occured while calling the service
	 */
	public static Optional<Object> call(String url, RequestMethod method, ResponseEntity<?> expected, Object... args) throws Exception {
        RestTemplate rest = new RestTemplate();
        switch (method) {
        	case GET:
        		expected = rest.getForEntity(url, expected.getClass());
        		break;
        	case POST:
	    		expected = rest.postForEntity(url, build(args), expected.getClass());
	    		break;
        	case DELETE:
        		rest.delete(url);
        		break;
        	default:
        		throw new IllegalArgumentException("http method not implemented");
        }
        return expected == null ?
        		Optional.empty() :
        		Optional.of(expected.getBody());
	}
	
	/**
	 * put POST args into request
	 * @param args arguments
	 * @return http entity
	 */
	private static HttpEntity<?> build(Object[] args) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
            	if (i % 2 == 0) {
            		try {
            			parameters.add((String) args[i - 1], (String) args[i]);
            		} catch (ClassCastException e) {
            			parameters.add((String) args[i - 1], args[i].toString());
            		}
            	}
            }
        }
        return new HttpEntity<>(parameters);
	}
}