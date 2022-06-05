package com.ssafy.vue.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.vue.dto.Interest;
import com.ssafy.vue.service.InterestService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/interest")
public class InterestController {

	private static final Logger logger = LoggerFactory.getLogger(InterestController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private InterestService interestService;

	@ApiOperation(value = "모든 관심지역의 정보를 반환한다.", response = List.class)
	@GetMapping("{member_id}")
	public ResponseEntity<List<Interest>> retrieveInterest(@PathVariable String member_id) throws Exception {
		logger.debug("retrieveBoard - 호출");
		return new ResponseEntity<List<Interest>>(interestService.retrieveInterest(member_id), HttpStatus.OK);
	}

	// @ApiOperation(value = "글번호에 해당하는 게시글의 정보를 반환한다.", response = Interest.class)
	// @GetMapping("{articleno}")
	// public ResponseEntity<Interest> detailBoard(@PathVariable int articleno) {
	// logger.debug("detailBoard - 호출");
	// return new ResponseEntity<Interest>(interestService.detailBoard(articleno),
	// HttpStatus.OK);
	// }

	@ApiOperation(value = "새로운 관심지역 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeInterest(@RequestBody Interest interest) {
		logger.debug("writeBoard - 호출");
		if (interestService.writeInterest(interest)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "번호에 해당하는 관심지역의 정보를 수정한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("{no}")
	public ResponseEntity<String> updateInterest(@RequestBody Interest interest) {
		logger.debug("updateBoard - 호출");
		logger.debug("" + interest);

		if (interestService.updateInterest(interest)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "번호에 해당하는 관심지역의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("{no}")
	public ResponseEntity<String> deleteInterest(@PathVariable int no) {
		logger.debug("deleteBoard - 호출");
		if (interestService.deleteInterest(no)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}