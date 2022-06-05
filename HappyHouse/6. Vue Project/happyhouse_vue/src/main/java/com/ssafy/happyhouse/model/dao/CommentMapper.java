package com.ssafy.happyhouse.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.dto.Comment;
@Mapper
public interface CommentMapper {
	public List<Comment> selectComment(int articleno);
	public Comment selectCommentByNo(int no);
	public int insertComment(Comment comment);
	public int updateComment(Comment comment);
	public int deleteComment(int no);
}