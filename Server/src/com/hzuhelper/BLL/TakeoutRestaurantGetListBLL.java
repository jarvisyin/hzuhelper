package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.TakeoutRestaurantDAL;
import com.hzuhelper.Domain.TakeoutRestaurant1;

public class TakeoutRestaurantGetListBLL {
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ArrayList<TakeoutRestaurant1> getList( ) {
		
		try {
			ArrayList<TakeoutRestaurant1> clist = new TakeoutRestaurantDAL()
					.getList1( );
			return clist;
		} catch (SQLException e) {
			errorMsg = e.toString();
			return null;
		}
	}
}
