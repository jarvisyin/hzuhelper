package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.TakeoutMenuDAL;
import com.hzuhelper.Domain.TakeoutMenu;

public class TakeoutMenuBLL {

	public void delete(int id) throws SQLException {
		new TakeoutMenuDAL().delete(id);
	}

	public ArrayList<TakeoutMenu> getList(int restaurantId)
			throws SQLException {
		return new TakeoutMenuDAL().getList1(restaurantId);
	}

	public int count(int restaurantId) throws SQLException {
		TakeoutMenuDAL dal = new TakeoutMenuDAL();
		if (restaurantId > 0)
			return dal.count(restaurantId);
		else
			return dal.count();
	}

	public void add(TakeoutMenu model) throws SQLException {
		new TakeoutMenuDAL().add(model);
	}

	public void update(TakeoutMenu model) throws SQLException {
		new TakeoutMenuDAL().update(model);
	}
}
