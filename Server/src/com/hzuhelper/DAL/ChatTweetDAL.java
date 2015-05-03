package com.hzuhelper.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.hzuhelper.Model.ChatTweetInfo;
import com.hzuhelper.Utility.MySqlHelper;

public class ChatTweetDAL {
	static String TABLE_NAME = "ChatTweet";
	static String id = "id";
	static String content = "content";
	static String ding = "ding";
	static String cai = "cai";
	static String publishDatetime = "publishDatetime";
	static String statu = "statu";
	static String tagId = "tagId";
	static String commentCount = "commentCount";
	static String authorId = "authorId";

	ResultSet rs = null;
	PreparedStatement pst = null;
	Connection connection = null;
	MySqlHelper msh = MySqlHelper.getInstance();

	public ArrayList<ChatTweetInfo> getList(String author_id)
			throws SQLException {
		ArrayList<ChatTweetInfo> clist = null;
		try {
			connection = msh.getConnection();
			String sqlText = "select * from " + TABLE_NAME + " where "
					+ authorId + "=? " + "order by " + id + " desc limit 0,20";
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, author_id);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatTweetInfo>();
			while (rs.next()) {
				ChatTweetInfo model = new ChatTweetInfo();
				clist.add(rsToModel(rs, model));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ArrayList<ChatTweetInfo> getList(int _id, String _authorId)
			throws SQLException {
		ArrayList<ChatTweetInfo> clist = null;
		try {
			connection = msh.getConnection();
			String sqlText = "select * from " + TABLE_NAME + " where " + id
					+ "<? and " + authorId + "=? order by " + id
					+ " desc limit 0,20";
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			pst.setString(2, _authorId);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatTweetInfo>();
			while (rs.next()) {
				ChatTweetInfo model = new ChatTweetInfo();
				clist.add(rsToModel(rs, model));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ArrayList<ChatTweetInfo> getList(int _id) throws SQLException {
		ArrayList<ChatTweetInfo> clist = null;
		try {
			connection = msh.getConnection();
			String sqlText = "select * from " + TABLE_NAME + " where " + id
					+ "<? order by " + id + " desc limit 0,20";
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatTweetInfo>();
			while (rs.next()) {
				ChatTweetInfo model = new ChatTweetInfo();
				clist.add(rsToModel(rs, model));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ArrayList<ChatTweetInfo> getList() throws SQLException {
		ArrayList<ChatTweetInfo> clist = null;
		try {
			connection = msh.getConnection();
			String sqlText = "select * from " + TABLE_NAME + " order by " + id
					+ " desc limit 0,20";
			pst = connection.prepareStatement(sqlText);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatTweetInfo>();
			while (rs.next()) {
				ChatTweetInfo model = new ChatTweetInfo();
				clist.add(rsToModel(rs, model));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ArrayList<ChatTweetInfo> getList(int _id, int _tagId)
			throws SQLException {
		ArrayList<ChatTweetInfo> clist = null;
		try {
			connection = msh.getConnection();
			String sqlText = "select * from " + TABLE_NAME + " where " + tagId
					+ "=? {0} order by " + id + " desc limit 0,20";
			if (_id != -1)
				sqlText = sqlText.replace("{0}", "and " + id + "<?");
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _tagId);
			if (_id != -1)
				pst.setInt(2, _id);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatTweetInfo>();
			while (rs.next()) {
				ChatTweetInfo model = new ChatTweetInfo();
				clist.add(rsToModel(rs, model));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ChatTweetInfo getModel(int _id) throws SQLException {
		try {
			connection = msh.getConnection();
			String sql = "select * from " + TABLE_NAME + " where " + id
					+ "=? limit 1";
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _id);
			rs = pst.executeQuery();
			if (!rs.next()) {
				throw new SQLException("不存在改行");
			}
			ChatTweetInfo model = new ChatTweetInfo();
			return rsToModel(rs, model);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public void add(ChatTweetInfo _chatTweetInfo) throws SQLException {
		try {
			connection = msh.getConnection();
			String sql = "insert into " + TABLE_NAME + " (" + content + ","
					+ ding + "," + cai + "," + publishDatetime + "," + statu
					+ "," + tagId + "," + commentCount + "," + authorId
					+ ") values (?,?,?,?,?,?,?,?)";
			pst = connection.prepareStatement(sql);
			pst.setString(1, _chatTweetInfo.getContent());
			pst.setInt(2, _chatTweetInfo.getDing());
			pst.setInt(3, _chatTweetInfo.getCai());
			pst.setTimestamp(4, new java.sql.Timestamp(_chatTweetInfo
					.getPublishDatetime().getTime()));
			pst.setInt(5, _chatTweetInfo.getStatu());
			pst.setInt(6, _chatTweetInfo.getTagId());
			pst.setInt(7, _chatTweetInfo.getCommentCount());
			pst.setString(8, _chatTweetInfo.getAuthorId());
			if (pst.executeUpdate() != 1) {
				throw new SQLException("insert failure ,update row != 1 ");
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public boolean addCommentCount(int _id) throws SQLException {
		try {
			connection = msh.getConnection();
			String sql = "UPDATE " + TABLE_NAME + " SET " + commentCount
					+ " = " + commentCount + " +1 WHERE " + id + " =?";
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _id);
			int i = pst.executeUpdate();
			if (i != 1)
				throw new SQLException("update " + commentCount
						+ " row != 1 . id=" + _id);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
		return true;
	}

	private ChatTweetInfo rsToModel(ResultSet rs, ChatTweetInfo model)
			throws SQLException {
		model.setId(rs.getInt(id));
		model.setAuthorId(rs.getString(authorId));
		model.setCai(rs.getInt(cai));
		model.setCommentCount(rs.getInt(commentCount));
		model.setContent(rs.getString(content));
		model.setDing(rs.getInt(ding));
		model.setPublishDatetime(new Date(rs.getTimestamp(publishDatetime)
				.getTime()));
		model.setStatu(rs.getInt(statu));
		model.setTagId(rs.getInt(tagId));
		return model;
	}

}
