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

import com.ssafy.vue.dto.Comment;
import com.ssafy.vue.service.CommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/comment")
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private CommentService commentService;

    @ApiOperation(value = "질문번호에 해당하는 모든 댓글의 정보를 반환한다.", response = List.class)
	@GetMapping("{articleno}")
	public ResponseEntity<List<Comment>> retrieveComment(@PathVariable int articleno) throws Exception {
		logger.debug("retrieveComment - 호출");
		return new ResponseEntity<List<Comment>>(commentService.retrieveComment(articleno), HttpStatus.OK);
	}

//    @ApiOperation(value = "댓글 번호에 해당하는 정보를 반환한다.", response = Comment.class)    
//	@GetMapping("comment/{cno}")
//	public ResponseEntity<Comment> detailComment(@PathVariable int cno) {
//		logger.debug("detailComment - 호출");
//		return new ResponseEntity<Comment>(qnaService.detailComment(cno), HttpStatus.OK);
//	}

    @ApiOperation(value = "새로운 댓글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeComment(@RequestBody Comment comment) {
		logger.debug("writeComment - 호출");
		if (commentService.writeComment(comment)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

    @ApiOperation(value = "댓글번호에 해당하는 정보를 수정한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("{no}")
	public ResponseEntity<String> updateComment(@RequestBody Comment comment) {
		logger.debug("updateComment - 호출");
		logger.debug("" + comment);
		
		if (commentService.updateComment(comment)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

    @ApiOperation(value = "댓글번호에 해당하는 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
    @DeleteMapping("{no}")
	public ResponseEntity<String> deleteBoard(@PathVariable int no) {
		logger.debug("deleteComment - 호출");
		if (commentService.deleteComment(no)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}