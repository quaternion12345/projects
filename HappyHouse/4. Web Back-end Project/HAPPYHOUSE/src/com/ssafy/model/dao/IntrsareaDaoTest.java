package com.ssafy.model.dao;

import java.util.List;

import com.ssafy.model.dto.Intrsarea;

public class IntrsareaDaoTest {
	public static void main(String[] args) {
		IntrsareaDAO dao = new IntrsareaDaoImp();
		
		try {
			dao.add(new Intrsarea(1, "thisid", "654654"));
			dao.update(new Intrsarea(1, "thisid", "654654"));
			dao.remove(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<Intrsarea> list = dao.searchAll();
			for(Intrsarea intrsarea : list) {
				System.out.println(intrsarea);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(dao.search("thisid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
