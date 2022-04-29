package com.ssafy.model.dao;

import java.util.List;

import com.ssafy.model.dto.Housedeal;

public class HousedealDaoTest {

	public static void main(String[] args) {
		HousedealDAO dao = new HousedealDaoImp();
		
		try {
			dao.add(new Housedeal(68865, 1, "1","1","1","1", "1", "1", "1", "1"));
			System.out.println("ADD");
			dao.update(new Housedeal(68865, 2, "1","1","1","1", "1", "1", "1", "1"));
			System.out.println("UPDATE");
			dao.remove(68865);
			System.out.println("REMOVE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<Housedeal>  list = dao.searchAll();
			for (Housedeal housedeal : list) {
				System.out.println(housedeal);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("======search");
		try {
			System.out.println(dao.search(1));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
