package projetcloud.services;

import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import projetcloud.model.ApprovalResponseWrapper;
import projetcloud.model.ExceptionWrapper;
import projetcloud.model.ServicesCaller;

@RestController
@RequestMapping("/loanapproval")
public class LoanApprovalService {
	
	/** 
	 * approval service
	 * @param name person name
	 * @param amount amount to approve
	 * @return a {@link JSONObject} containing approval response and user account
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{name}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ask(@PathVariable String name, @PathVariable Long amount) {
		JSONObject account;
		boolean approved = false;
		try {
			if (amount < 10000 && !isRisky(name) || isApproved(name)) {
				approved = true;
			}
			account = approved ? updateAccount(name, amount) : fetchAccount(name);
			return new ResponseEntity<ApprovalResponseWrapper>(new ApprovalResponseWrapper(account, approved), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * call accmanager service (add amount to an account)
	 * @param name person name
	 * @param amount amount to add
	 * @return the account of the person
	 * @throws Exception if an error occurred while trying to call service
	 */
	private JSONObject updateAccount(String name, long amount) throws Exception {
		String service = ServicesCaller.UPDATE_ACCOUNT;
		Optional<Object> data = ServicesCaller.call(service,
				RequestMethod.POST, new ResponseEntity<JSONObject>(HttpStatus.OK), "name", name, "amount", amount);
		if (!data.isPresent()) {
			throw new Exception("An error occurred while updating account amount");
		}
		return fetchAccount(name);
	}
	
	/**
	 * call accmanager service (fetch one person account)
	 * @param name of the person
	 * @return the account of the person
	 * @throws Exception if an error occurred while trying to call service
	 */
	private JSONObject fetchAccount(String name) throws Exception {
		String service = ServicesCaller.LIST_ACCOUNTS;
		Optional<Object> data = ServicesCaller.call(service,
				RequestMethod.GET, new ResponseEntity<List<JSONObject>>(HttpStatus.OK));
		if (!data.isPresent()) {
			throw new Exception("No data found while fetching accounts list");
		}
		List<JSONObject> collection = (List<JSONObject>) data.get();
		return find(collection, name);
	}
	
	/**
	 * call accmanager service (fetch risk value associated to an account)
	 * @param name name of the person
	 * @return true if the account is risky
	 * @throws Exception if an error occurred while trying to call service
	 */
	private boolean isRisky(String name) throws Exception {
		String service = ServicesCaller.CHECK_ACCOUNT;
		Optional<Object> data = ServicesCaller.call(service, 
				RequestMethod.POST, new ResponseEntity<JSONObject>(HttpStatus.OK), "name", name);
		if (!data.isPresent()) {
			throw new Exception("No data found about the risk of this account");
		}
		JSONObject object = (JSONObject) data.get();
		return object.getString("risk").equalsIgnoreCase("high");
	}
	
	/**
	 * call appmanager service (fetch approval value associated to an account)
	 * @param name name of the person
	 * @return true if the approval is approved, false if not, or if it doesn't exists yet
	 * @throws Exception if an error occurred while trying to call service
	 */
	private boolean isApproved(String name) throws Exception {
		String service = ServicesCaller.LIST_APPROVALS;
		Optional<Object> data = ServicesCaller.call(service,
				RequestMethod.GET, new ResponseEntity<List<JSONObject>>(HttpStatus.OK));
		if (!data.isPresent()) {
			throw new Exception("No data found while fetching approvals list");
		}
		List<JSONObject> collection = (List<JSONObject>) data.get();
		return find(collection, name).getBoolean("approved");
	}
	
	/**
	 * find an account wrapped into a {@link JSONObject}
	 * @param objects list of json objects
	 * @param name account to find
	 * @return wrapper json found
	 */
	private JSONObject find(List<JSONObject> objects, String name) {
		Optional<JSONObject> account = objects.stream()
				.filter(obj -> {
					try {
						return obj.getString("name").equalsIgnoreCase(name);
					} catch (JSONException e) {
						return false;
					}
				}).findAny();
		if (!account.isPresent()) {
			throw new IllegalArgumentException("No data found while fetching account");
		}
		return account.get();
	}
}
