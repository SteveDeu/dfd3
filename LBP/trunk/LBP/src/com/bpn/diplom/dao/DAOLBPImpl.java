package com.bpn.diplom.dao;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.sun.jimi.core.util.JimiImageSerializer;

public class DAOLBPImpl implements DAOLBP{
	public static final String TABLE_NAME = "users";

	@Override
	public List<EntityLBPUser> getAllUsers() {
		
		List<EntityLBPUser> result = new ArrayList<EntityLBPUser>();
		try {
			Connection con = Connect.newInstance();
			PreparedStatement ps = con.prepareCall("select * from " + TABLE_NAME + " order by id");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Image imageFace = null;
				if(rs.getBlob("face_image") != null)
					imageFace = ((JimiImageSerializer)getObjectFromBlob(rs.getBlob("face_image"))).getImage();
				result.add(
						new EntityLBPUser(
								rs.getInt("id"), 
								rs.getString("name"), 
								rs.getTimestamp("date_add"), 
								(int[])getObjectFromBlob(rs.getBlob("vector8")), 
								(int[])getObjectFromBlob(rs.getBlob("vector12")),
								(int[])getObjectFromBlob(rs.getBlob("vector16")),
								imageFace));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public EntityLBPUser getUser(int id) {
		EntityLBPUser result = null;
		try {
			Connection con = Connect.newInstance();
			PreparedStatement ps = con.prepareCall("select * from " + TABLE_NAME + " where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				Image imageFace = null;
				if(rs.getBlob("face_image") != null)
					imageFace = ((JimiImageSerializer)getObjectFromBlob(rs.getBlob("face_image"))).getImage();
				result = new EntityLBPUser(
								rs.getInt("id"), 
								rs.getString("name"), 
								rs.getTimestamp("date_add"), 
								(int[])getObjectFromBlob(rs.getBlob("vector8")), 
								(int[])getObjectFromBlob(rs.getBlob("vector12")),
								(int[])getObjectFromBlob(rs.getBlob("vector16")),
								imageFace);
			}
		} catch (SQLException e) {
			System.out.println(e.getSQLState());
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<EntityLBPUser> getUsers(String name) {
		
		List<EntityLBPUser> result = new ArrayList<EntityLBPUser>();
		try {
			Connection con = Connect.newInstance();
			PreparedStatement ps = con.prepareCall("select * from " + TABLE_NAME + " where name=? order by id");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.add(
						new EntityLBPUser(
								rs.getInt("id"),
								rs.getString("name"), 
								rs.getTimestamp("date_add"), 
								(int[])getObjectFromBlob(rs.getBlob("vector8")), 
								(int[])getObjectFromBlob(rs.getBlob("vector12")),
								(int[])getObjectFromBlob(rs.getBlob("vector16")),
								(Image)getObjectFromBlob(rs.getBlob("face_image"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int removeUser(int id) {
		int updateCount = 0;
		try {
			Connection con = Connect.newInstance();
			PreparedStatement ps = con.prepareCall("delete from " + TABLE_NAME + " " +
												   "where id=?");
			ps.setInt(1, id);
			ps.execute();
			updateCount = ps.getUpdateCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateCount;
	}

	@Override
	public void addUser(EntityLBPUser user) {
		if(!user.isValidForSave())
			return;

		try {
			Connection con = Connect.newInstance();
			PreparedStatement ps = con.prepareCall("insert into " + TABLE_NAME + "(name, vector8, vector12, vector16, face_image) " +
												   "values(?,?,?,?,?)");
			ps.setString(1, user.getName());
			
			if(user.getVector8() != null)	ps.setBlob(2, getByteArrayInputStream(user.getVector8()));
			else							ps.setNull(2, Types.BLOB);
			
			if(user.getVector12() != null)	ps.setBlob(3, getByteArrayInputStream(user.getVector12()));
			else							ps.setNull(3, Types.BLOB);
			
			if(user.getVector16() != null)	ps.setBlob(4, getByteArrayInputStream(user.getVector16()));
			else							ps.setNull(4, Types.BLOB);
			
			if(user.getImageFace() != null)	ps.setBlob(5, getByteArrayInputStream(new JimiImageSerializer(user.getImageFace())));
			else							ps.setNull(5, Types.BLOB);
			
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	private ByteArrayInputStream getByteArrayInputStream(Object data){
		if(data == null)
			return null;
		ByteArrayInputStream is = null;
		try {
			ByteArrayOutputStream boss = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(boss);
			oos.writeObject(data);
			oos.close();
			is = new ByteArrayInputStream(boss.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	private Object getObjectFromBlob(Blob blob){
		if(blob == null)
			return null;
		Object result = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
			result = ois.readObject(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
}
