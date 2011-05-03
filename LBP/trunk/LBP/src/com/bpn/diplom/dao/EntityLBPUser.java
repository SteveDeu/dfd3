package com.bpn.diplom.dao;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EntityLBPUser {

	private int id;
	private String name;
	private Date dateAdd;
	private int[] vector8;
	private int[] vector12;
	private int[] vector16;
	private Image imageFace;
	
	
//	`name` VARCHAR( 50 ) NOT NULL ,
//	`date_add` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
//	`vector8` BLOB NULL ,
//	`vector12` BLOB NULL ,
//	`vector16` BLOB NULL ,
//	`face_image` MEDIUMBLOB NULL ,
	
	
	public EntityLBPUser(int id, String name, Date dateAdd, Image face) {
		super();
		this.id = id;
		this.name = name;
		this.dateAdd = dateAdd;
		this.imageFace = face;
	}
	
	public EntityLBPUser(String name, int[] vector8, int[] vector12, int[] vector16) {
		super();
		this.name = name;
		this.vector8 = vector8;
		this.vector12 = vector12;
		this.vector16 = vector16;
	}
	
	public EntityLBPUser(String name, int[] vector8, int[] vector12, int[] vector16, Image face) {
		super();
		this.name = name;
		this.vector8 = vector8;
		this.vector12 = vector12;
		this.vector16 = vector16;
		this.imageFace = face;
	}
	
	public EntityLBPUser(int id, String name, Date dateAdd, int[] vector8,
			int[] vector12, int[] vector16, Image imageFace) {
		super();
		this.id = id;
		this.name = name;
		this.dateAdd = dateAdd;
		this.vector8 = vector8;
		this.vector12 = vector12;
		this.vector16 = vector16;
		this.imageFace = imageFace;
	}
	
	
	public boolean isValidForSave(){
		if(name != null 
				&& vector8 != null
				&& vector12 != null
				&& vector16 != null)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "EntityLBPUser ["+" id=" + id + " name=" + name + ", dateAdd=" + dateAdd
				+ ",\nvector8=" + Arrays.toString(vector8) + ",\nvector12="
				+ Arrays.toString(vector12) + ",\nvector16="
				+ Arrays.toString(vector16) + ",\nimageFace=" + imageFace + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDateAdd() {
		return dateAdd;
	}
	public String getDateTimeAdd() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(dateAdd);
	}
	public void setDateAdd(Date dateAdd) {
		this.dateAdd = dateAdd;
	}
	public int[] getVector8() {
		return vector8;
	}
	public void setVector8(int[] vector8) {
		this.vector8 = vector8;
	}
	public int[] getVector12() {
		return vector12;
	}
	public void setVector12(int[] vector12) {
		this.vector12 = vector12;
	}
	public int[] getVector16() {
		return vector16;
	}
	public void setVector16(int[] vector16) {
		this.vector16 = vector16;
	}
	public Image getImageFace() {
		return imageFace;
	}
	public void setImageFace(Image imageFace) {
		this.imageFace = imageFace;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}

