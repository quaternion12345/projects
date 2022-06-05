package com.ssafy.vue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.vue.dto.Comment;
import com.ssafy.vue.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {
	
    @Autowired
	private CommentMapper commentMapper;

    @Override
	public List<Comment> retrieveComment(int articleno) {
		return commentMapper.selectComment(articleno);
	}
    
  	@Override
	public boolean writeComment(Comment comment) {
		return commentMapper.insertComment(comment) == 1;
	}

//	@Override
//	public Comment detailComment(int articleno) {
//		return commentMapper.selectCommentByNo(articleno);
//	}

	@Override
	@Transactional
	public boolean updateComment(Comment comment) {
		return commentMapper.updateComment(comment) == 1;
	}

	@Override
	@Transactional
	public boolean deleteComment(int no) {
		return commentMapper.deleteComment(no) == 1;
	}
}