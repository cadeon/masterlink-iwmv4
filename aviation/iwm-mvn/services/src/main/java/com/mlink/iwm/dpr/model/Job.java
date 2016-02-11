package com.mlink.iwm.dpr.model;

import java.util.ArrayList;

public class Job {
	private String shopsection;
	private String subshop;
	private String ero;
	private String tam;
	private String tamid;
	private String serial;
	private String cat;
	private String rdd;
	private String pri;
	private String nsninmaint;
	private String nomen;
	private String dcd;
	private String dris;
	private String bdi;
	private String eot;
	private String awtgstat;
	private String owner;
	private String partscharge;
	private String ech;
	private String qty;
	private String xeros;
	private String maresdate;
	private String defect;
	private String ddl;
	private String dis;
	private String destholder;

	private ArrayList<JobStatus> jobstatus = new ArrayList<JobStatus>();
	private ArrayList<NonAssetResource> parts = new ArrayList<NonAssetResource>();

	public String getShopsection(){return shopsection;}
	public String getSubshop(){return subshop;}
	public String getEro(){return ero;}
	public String getTam(){return tam;}
	public String getTamId(){return tamid;}
	public String getSerial(){return serial;}
	public String getCat(){return cat;}
	public String getRdd(){return rdd;}
	public String getPri(){return pri;}
	public String getNsninmaint(){return nsninmaint;}
	public String getNomen(){return nomen;}
	public String getDcd(){return dcd;}
	public String getDris(){return dris;}
	public String getBdi(){return bdi;}
	public String getEot(){return eot;}
	public String getAwtgstat(){return awtgstat;}
	public String getOwner(){return owner;}
	public String getPartscharge(){return partscharge;}
	public String getEch(){return ech;}
	public String getQty(){return qty;}
	public String getXeros(){return xeros;}
	public String getMaresdate(){return maresdate;}
	public String getDefect(){return defect;}
	public String getDdl(){return ddl;}
	public String getDis(){return dis;}
	public String getDestholder(){return destholder;}
	public ArrayList<JobStatus> getJobstatus(){return jobstatus;}
	public ArrayList<NonAssetResource> getParts(){return parts;}

	public void setShopsection(String s){ shopsection=s;}
	public void setSubshop(String s){ subshop=s;}
	public void setEro(String s){ ero=s;}
	public void setTam(String s){ tam=s;}
	public void setTamId(String s){ tamid=s;}
	public void setSerial(String s){ serial=s;}
	public void setCat(String s){ cat=s;}
	public void setRdd(String s){ rdd=s;}
	public void setPri(String s){ pri=s;}
	public void setNsninmaint(String s){ nsninmaint=s;}
	public void setNomen(String s){ nomen=s;}
	public void setDcd(String s){ dcd=s;}
	public void setDris(String s){ dris=s;}
	public void setBdi(String s){ bdi=s;}
	public void setEot(String s){ eot=s;}
	public void setAwtgstat(String s){ awtgstat=s;}
	public void setOwner(String s){ owner=s;}
	public void setPartscharge(String s){ partscharge=s;}
	public void setEch(String s){ ech=s;}
	public void setQty(String s){ qty=s;}
	public void setXeros(String s){ xeros=s;}
	public void setMaresdate(String s){ maresdate=s;}
	public void setDefect(String s){ defect=s;}
	public void setDdl(String s){ ddl=s;}
	public void setDis(String s){ dis=s;}
	public void setDestholder(String s){ destholder=s;}
	
	public void setJobstatus(ArrayList<JobStatus> al){ jobstatus=al;}
	public void setParts(ArrayList<NonAssetResource> al){ parts=al;}
	
	public void addJobStatus(JobStatus js){jobstatus.add(js);}
	public void addPart(NonAssetResource nar){parts.add(nar);}
}
