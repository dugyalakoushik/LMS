package com.tamu.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.tamu.dto.MyBookDto;
import com.tamu.entity.Book;
import com.tamu.entity.User;

public class TestAdapter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		User user = new User();
	    Gson gson = new Gson();

		user.setUsername("atharv_a");
		user.setPassword("password123");
		user.setAge(3);
		user.setEmail("abcd23@gmail.com");
		RemoteDataAdapter adapter = new RemoteDataAdapter();
		String body = gson.toJson(user);
		User loggedinUser=null ;
		List<Book> sampleBooks= new ArrayList<Book>();
		String res ="";
		List<MyBookDto> mbtres;
		try {
			res  = adapter.unsubscribe(Integer.toString(56));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(res);
	}
	
}
