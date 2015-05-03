package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.ChatTagDAL;
import com.hzuhelper.Control.DAL.TagRecordDAL;
import com.hzuhelper.Control.DAL.TakeoutMenuDAL;
import com.hzuhelper.Control.DAL.TakeoutRestaurantDAL;
import com.hzuhelper.Domain.ChatTag;
import com.hzuhelper.Domain.TakeoutRestaurant1;
import com.hzuhelper.Domain.TakeoutRestaurant2;

public class TakeoutRestaurantBLL {
	public void delete(String str_id) throws Exception {
		int id;
		try {
			id = Integer.parseInt(str_id);
		} catch (Exception ex) {
			throw ex;
		}
		TakeoutMenuDAL tmidal = new TakeoutMenuDAL();
		tmidal.deleteByResId(id);
		TakeoutRestaurantDAL dal = new TakeoutRestaurantDAL();
		dal.delete(id);
	}

	public void add(TakeoutRestaurant1 model) throws Exception {
		checkupModel(model);
		ChatTag chat_taginfo = new ChatTag();
		chat_taginfo.setName(model.getName());
		model.setTagId(new ChatTagDAL().add(chat_taginfo));
		new TakeoutRestaurantDAL().add(model);
	}

	public void update(TakeoutRestaurant1 model) throws Exception {
		checkupModel(model);
		new TakeoutRestaurantDAL().update(model);
	}

	public ArrayList<TakeoutRestaurant2> getList() throws SQLException {
		return new TakeoutRestaurantDAL().getList2();
	}

	private void checkupModel(TakeoutRestaurant1 model) throws Exception {
		if (model.getIntro().length() > 32) {
			throw new Exception("简介字数不得超过32");
		}
		if (model.getName().length() > 12) {
			throw new Exception("简介字数不得超过12");
		}
		String[] phones = model.getPhone().split(",");
		for (String string : phones) {
			if (string.matches("[0-9]{3,11}") == false) {
				throw new Exception("电话不合法");
			}
		}
	}

	public TakeoutRestaurant1 getModel(int id) throws SQLException {
		return new TakeoutRestaurantDAL().getModel(id);
	}

	public void ding(int id, String userId) throws Exception {
		if (new TagRecordDAL().exist(id, userId)) {
			throw new Exception("已经评价过");
		}
		new TakeoutRestaurantDAL().ding(id);
		new TagRecordDAL().add(id, userId);
	}

	public void cai(int id, String userId) throws Exception {
		if (new TagRecordDAL().exist(id, userId)) {
			throw new Exception("已经评价过");
		}
		new TakeoutRestaurantDAL().cai(id);
		new TagRecordDAL().add(id, userId);
	}
}
