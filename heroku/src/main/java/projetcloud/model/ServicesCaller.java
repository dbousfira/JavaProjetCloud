package projetcloud.model;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

public abstract class ServicesCaller {
	
	public static final String LIST_APPROVALS = "https://stark-savannah-65713.herokuapp.com/appmanager/";
	public static final String LIST_ACCOUNTS = "https://inf63app12.appspot.com/accounts";
	public static final String UPDATE_ACCOUNT = "https://inf63app12.appspot.com/accounts";
	public static final String CHECK_ACCOUNT = "https://inf63app12.appspot.com/check";
	
	/**
	 * call a web service
	 * @param url url of the service
	 * @param method http method
	 * @param collection is the result a collection ?
	 * @param args optionnals args
	 * @return json object returned
	 * @throws Exception if an error occurred while calling the service
	 */
	public static Object call(String url, RequestMethod method, boolean collection, Object... args) throws Exception {
		ResponseEntity<?> res;
		Class<?> type = collection ? List.class : LinkedHashMap.class;
        RestTemplate rest = new RestTemplate();
        switch (method) {
        	case GET:
        		res = rest.exchange(url, HttpMethod.GET, build(args), type);
        		break;
        	case POST:
	    		res = rest.exchange(url, HttpMethod.POST, build(args), type);
	    		break;
        	case DELETE:
        		res = rest.exchange(url, HttpMethod.DELETE, build(args), type);
        		break;
        	default:
        		throw new IllegalArgumentException("http method not implemented");
        }
        return res.getBody();
	}
	
	/**
	 * put args into request
	 * @param args arguments
	 * @return http entity
	 */
	private static HttpEntity<?> build(Object[] args) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        for (int i = 0; i < args.length; i++) {
        	if (i % 2 == 0 && i != 0) {
        		try {
        			parameters.add((String) args[i - 1], (String) args[i]);
        		} catch (ClassCastException e) {
        			parameters.add((String) args[i - 1], args[i].toString());
        		}
        	}
        }
        return new HttpEntity<>(parameters);
	}
}
