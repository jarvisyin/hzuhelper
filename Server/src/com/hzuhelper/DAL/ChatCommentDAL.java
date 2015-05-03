package com.hzuhelper.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.hzuhelper.Model.ChatComment1Info;
import com.hzuhelper.Model.ChatComment2Info;
import com.hzuhelper.Utility.MySqlHelper;

public class ChatCommentDAL {
	static String TABLE_NAME = "ChatComment";
	static String id = "id";
	static String content = "content";
	static String tweetId = "tweetId";
	static String authorId = "authorId";
	static String statu = "statu";
	static String receiverId = "receiverId";
	static String publishDatetime = "publishDatetime";
	static String responseCommentId = "responseCommentId";

	ResultSet rs = null;
	PreparedStatement pst = null;
	Connection connection = null;
	MySqlHelper msh = MySqlHelper.getInstance();

	public ArrayList<ChatComment1Info> getList(int _tweetId)
			throws SQLException {
		ArrayList<ChatComment1Info> clist = null;
		try {
			String sqlText = "select * from " + TABLE_NAME + " where "
					+ tweetId + "=? " + "order by " + id + " desc limit 0,20";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _tweetId);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatComment1Info>();
			while (rs.next()) {
				clist.add(ResultSetToModel(rs));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public void add(ChatComment1Info _chatCommentInfo) throws SQLException {
		try {
			connection = msh.getConnection();
			String sql = "insert into " + TABLE_NAME + " (" + content + ","
					+ tweetId + "," + authorId + "," + statu + ","
					+ publishDatetime + "," + responseCommentId + ","
					+ receiverId + ") values (?,?,?,?,?,?,?)";
			pst = connection.prepareStatement(sql);
			pst.setString(1, _chatCommentInfo.getContent());
			pst.setInt(2, _chatCommentInfo.getTweetId());
			pst.setString(3, _chatCommentInfo.getAuthorId());
			pst.setInt(4, _chatCommentInfo.getStatu());
			pst.setTimestamp(5, new java.sql.Timestamp(_chatCommentInfo
					.getPublishDatetime().getTime()));
			pst.setInt(6, _chatCommentInfo.getResponseCommentId());
			pst.setString(7, _chatCommentInfo.getReceiverId());
			int i = pst.executeUpdate();
			if (i != 1) {
				throw new SQLException("sql insert failure,changed row != 1");
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public int count(int _tweetId) throws SQLException {
		try {
			connection = msh.getConnection();
			String sql = "select count(*) from " + TABLE_NAME + " where "
					+ tweetId + "=?";
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _tweetId);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
		return 0;
	}

	public ArrayList<ChatComment2Info> getList(String _receiverId, int _id,
			int type) throws SQLException {
		ArrayList<ChatComment2Info> clist = null;
		try {
			String sqlText = "select * from ("
					+ "select a.id,a.content,a.publishDatetime,a.tweetId,a.statu,a.responseCommentId,left(b.content,20) yourContent "
					+ "from ChatTweet b, ChatComment a "
					+ "where a.responseCommentId=0 "
					+ "and a.tweetId=b.id "
					+ "and a.{0}=? "
					+ "UNION "
					+ "select a.id,a.content,a.publishDatetime,a.tweetId,a.statu,a.responseCommentId,left(b.content,20) yourContent "
					+ "from ChatComment a,ChatComment b "
					+ "where a.responseCommentId=b.id "
					+ "and a.responseCommentId!=0 " + "and a.{0}=?) a ";
			if (type < 0)
				sqlText = sqlText.replace("{0}", "authorId");
			else
				sqlText = sqlText.replace("{0}", "receiverId");
			if (_id < 1)
				sqlText += "order by id desc limit 20;";
			else
				sqlText += "where id < ? order by id desc limit 20";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, _receiverId);
			pst.setString(2, _receiverId);
			if (_id > 0)
				pst.setInt(3, _id);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatComment2Info>(20);
			while (rs.next()) {
				ChatComment2Info model = new ChatComment2Info();
				model.setId(rs.getInt("id"));
				model.setContent(rs.getString("content"));
				model.setPublishDatetime(new Date(rs.getTimestamp(
						"publishDatetime").getTime()));
				model.setTweetId(rs.getInt("tweetId"));
				model.setYourContent(rs.getString("yourContent"));
				model.setStatu(rs.getInt("statu"));
				model.setReceiverId(rs.getInt("responseCommentId"));
				clist.add(model);
			}
			return clist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public ArrayList<ChatComment1Info> getList(int _tweetId, int _commentId)
			throws SQLException {
		ArrayList<ChatComment1Info> clist = null;
		try {
			String sqlText = "select * from " + TABLE_NAME + " where "
					+ tweetId + "=? {0} order by " + id + " desc limit 0,20";
			if (_commentId > 0)
				sqlText = sqlText.replace("{0}", "and " + id + "<?");
			else
				sqlText = sqlText.replace("{0}", "");
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _tweetId);
			if (_commentId > 1)
				pst.setInt(2, _commentId);
			rs = pst.executeQuery();
			clist = new ArrayList<ChatComment1Info>(20);
			while (rs.next()) {
				clist.add(ResultSetToModel(rs));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return clist;
	}

	public ChatComment1Info getModel(int _id) throws SQLException {
		try {
			String sqlText = "select * from " + TABLE_NAME + " where " + id
					+ "=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			rs = pst.executeQuery();
			if (!rs.next()) {
				throw new SQLException("不存在该行");
			}
			return ResultSetToModel(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	private ChatComment1Info ResultSetToModel(ResultSet rs) throws SQLException {
		ChatComment1Info chatCmmentInfo = new ChatComment1Info();
		chatCmmentInfo.setId(rs.getInt(id));
		chatCmmentInfo.setAuthorId(rs.getString(authorId));
		chatCmmentInfo.setContent(rs.getString(content));
		chatCmmentInfo.setPublishDatetime(new Date(rs.getTimestamp(
				publishDatetime).getTime()));
		chatCmmentInfo.setTweetId(rs.getInt(tweetId));
		chatCmmentInfo.setReceiverId(rs.getString(receiverId));
		chatCmmentInfo.setStatu(rs.getInt(statu));
		return chatCmmentInfo;
	}
}
