package com.ssafy.happyhouse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.dto.AptDeal;
import com.ssafy.happyhouse.service.AptDealService;

@RestController
@CrossOrigin("*")
@RequestMapping("/confirmation")
public class SearchPriceController {
	
	private final Logger logger = LoggerFactory.getLogger(SearchPriceController.class);
	
	@Autowired
	private AptDealService aptDealService;
	
	@GetMapping("/")
	public ResponseEntity<List<AptDeal>> initialize() throws Exception {
		return new ResponseEntity<List<AptDeal>>(aptDealService.init(), HttpStatus.OK);
	}
	
	@GetMapping("/dong")
	public ResponseEntity<List<AptDeal>> dong(@RequestParam("dong") String dong, @RequestParam("pageNo") int pageNo) throws Exception {
		return new ResponseEntity<List<AptDeal>>(aptDealService.getAptInDong(dong, pageNo), HttpStatus.OK);
	}
	
	@GetMapping("/aptname")
	public ResponseEntity<List<AptDeal>> aptname(@RequestParam("apt") String apt, @RequestParam("pageNo") int pageNo) throws Exception {
		return new ResponseEntity<List<AptDeal>>(aptDealService.getAptByName(apt, pageNo), HttpStatus.OK);
	}
}
