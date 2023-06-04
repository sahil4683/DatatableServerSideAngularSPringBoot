package com.sp.aop_demo.jquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class PersonController {
	@Autowired
	private JqueryDatatablePluginDemo jqueryDatatablePluginDemo;

	@GetMapping
	public JsonObjectDto getFilteredData(

			@RequestParam(value = "iDisplayStart", defaultValue = "0") String iDisplayStart,
			@RequestParam(value = "iDisplayLength", defaultValue = "10") String iDisplayLength,

			@RequestParam(value = "iSortCol", defaultValue = "2") String iSortCol,
			@RequestParam(value = "sSortDir", defaultValue = "ASC") String sSortDir,

//			@RequestParam(value = "sSearch", defaultValue = "") String sSearch,
			@RequestParam(value = "sSearch_0", defaultValue = "") String sSearch_0,
			@RequestParam(value = "sSearch_1", defaultValue = "") String sSearch_1,
			@RequestParam(value = "sSearch_2", defaultValue = "") String sSearch_2,
			@RequestParam(value = "sSearch_3", defaultValue = "") String sSearch_3,
			@RequestParam(value = "sSearch_4", defaultValue = "") String sSearch_4,
			@RequestParam(value = "sSearch_5", defaultValue = "") String sSearch_5) {

		DataTableRequest dataTableRequest = new DataTableRequest();
		dataTableRequest.setIDisplayStart(iDisplayStart);
		dataTableRequest.setIDisplayLength(iDisplayLength);
		dataTableRequest.setISortCol(iSortCol);
		dataTableRequest.setSSortDir(sSortDir);
//		dataTableRequest.setSSearch(sSearch);
		dataTableRequest.setSSearch_0(sSearch_0);
		dataTableRequest.setSSearch_1(sSearch_1);
		dataTableRequest.setSSearch_2(sSearch_2);
		dataTableRequest.setSSearch_3(sSearch_3);
		dataTableRequest.setSSearch_4(sSearch_4);
		dataTableRequest.setSSearch_5(sSearch_5);

		return jqueryDatatablePluginDemo.doGet(dataTableRequest);
	}
}
