package com.maybank.sample.controller;

import javax.validation.Valid;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.sample.dto.EmployeeDTO;
import com.maybank.sample.dto.PageResponseDTO;
import com.maybank.sample.dto.RequestDTO;
import com.maybank.sample.dto.ResponseDTO;
import com.maybank.sample.json.JSONConverter;
import com.maybank.sample.service.EmployeeService;

@RestController
public class EmployeeController {

	protected final transient XLogger log = XLoggerFactory.getXLogger(getClass().getName());

	@Autowired
	private EmployeeService empService;

	@RequestMapping(value = "/getEmployeeList", method = RequestMethod.GET)
	public String getEmployee(@RequestParam(value = "page") final Integer page, @RequestParam(value = "perPage") final Integer perPage)
			throws Exception {
		log.entry();
		log.info("Page received : " + page);
		log.info("Per Page received : " + perPage);
		PageResponseDTO<EmployeeDTO> respDTO = empService.getEmployeePage(page, perPage);
		log.info("Response sent: " + JSONConverter.toJson(respDTO));
		log.exit();
		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = { "/getEmployee/{id}" }, method = RequestMethod.GET)
	public @ResponseBody String getEmployeeById(@PathVariable(value = "id") final String id) throws Exception {

		log.info("Request received : " + id);

		ResponseDTO respDTO = empService.retrieveEmployee(id);
		log.info("Response sent : " + JSONConverter.toJson(respDTO));

		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = "/addEmployee/**", method = RequestMethod.POST)
	public String addEmployee(@RequestBody(required = false) @Valid final EmployeeDTO reqDTO) throws Exception {
		log.entry();
		log.info("Request received : " + JSONConverter.toJson(reqDTO));
		ResponseDTO respDTO = empService.addNewEmployee(reqDTO);
		log.info("Response sent : " + JSONConverter.toJson(respDTO));

		log.exit();

		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = "/addEmployeeList/**", method = RequestMethod.POST)
	public String addEmployeeList(@Valid @RequestBody final RequestDTO reqDTO) throws Exception {
		log.entry();
		log.info("Request received : " + JSONConverter.toJson(reqDTO));
		ResponseDTO respDTO = empService.addNewEmployeeList(reqDTO.getEmployeeList());
		log.info("Response sent : " + JSONConverter.toJson(respDTO));
		log.exit();

		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = "/modifyEmployee", method = RequestMethod.PATCH)
	public String modifyEmployee(@Valid @RequestBody final EmployeeDTO reqDTO) throws Exception {
		log.entry();
		log.info("Request received : " + reqDTO);
		ResponseDTO respDTO = empService.modifyEmployee(reqDTO);
		log.info("Response sent : " + JSONConverter.toJson(respDTO));
		log.exit();

		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = "/deleteEmployee/{id}", method = RequestMethod.POST)
	public String deleteEmployee(@PathVariable(value = "id") final Long id) throws Exception {
		log.entry();
		log.info("Request received : " + id);
		ResponseDTO respDTO = empService.deleteEmployee(id);
		log.info("Response sent : " + JSONConverter.toJson(respDTO));
		log.exit();

		return JSONConverter.toJson(respDTO);
	}

	@RequestMapping(value = "/callApi/{id}", method = RequestMethod.GET)
	public String callOtherApi(@PathVariable(value = "id") final Long id) throws Exception {
		log.entry();
		ResponseDTO respDTO = empService.callAPI(id);
		log.info("Response sent : " + JSONConverter.toJson(respDTO));
		log.exit();

		return JSONConverter.toJson(respDTO);
	}

}
