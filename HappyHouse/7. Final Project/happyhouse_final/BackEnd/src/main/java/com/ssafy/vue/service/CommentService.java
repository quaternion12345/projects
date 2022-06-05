package com.ssafy.vue.service;

import java.util.List;

import com.ssafy.vue.dto.Comment;

public interface CommentService {
	public List<Comment> retrieveComment(int articleno);
//	public Comment detailComment(int articleno);
	public boolean writeComment(Comment comment);
	public boolean updateComment(Comment comment);
	public boolean deleteComment(int no);
}
