package com.ssafy.happyhouse.service;

import java.util.List;

import com.ssafy.happyhouse.model.dto.Comment;

public interface CommentService {
	public List<Comment> retrieveComment(int articleno);
	public Comment detailComment(int no);
	public boolean writeComment(Comment comment);
	public boolean updateComment(Comment comment);
	public boolean deleteComment(int no);
}
