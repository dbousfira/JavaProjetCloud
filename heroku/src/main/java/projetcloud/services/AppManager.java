package projetcloud.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projetcloud.database.AccessLayerException;
import projetcloud.database.DataAccessLayer;
import projetcloud.model.ExceptionWrapper;
import projetcloud.model.Approval;

@RestController
@RequestMapping("/appmanager")
public class AppManager {
	
	private DataAccessLayer dal;

	/**
	 * call add service
	 * @param name approval name to add
	 * @param approved is approved
	 * @return created entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> add(
			@RequestParam(name = "name", required = true) String name, 
			@RequestParam(name = "approved", required = true) boolean approved) {
		try {
			dal = DataAccessLayer.get();
			Approval inserted = dal.insert(name, approved);
			return new ResponseEntity<Approval>(inserted, HttpStatus.OK);
		} catch (AccessLayerException e) {
			return new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * call list service
	 * @return list of approvals
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<?> list() {
		try {
			dal = DataAccessLayer.get();
			List<Approval> approvals = dal.list();
			return new ResponseEntity<List<Approval>>(approvals, HttpStatus.OK);
		} catch (AccessLayerException e) {
			return new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * call delete service
	 * @param name approval name to delete
	 * @return true if the database has been affected
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> delete(@PathVariable String name) {
		try {
			dal = DataAccessLayer.get();
			boolean success = dal.delete(name);
			return new ResponseEntity<Boolean>(success, HttpStatus.OK);
		} catch (AccessLayerException e) {
			return new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
