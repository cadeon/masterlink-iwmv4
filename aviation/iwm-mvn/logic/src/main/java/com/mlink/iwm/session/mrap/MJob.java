package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.util.DateUtils;

public class MJob implements Serializable {

	private static final long serialVersionUID = 1L;
	Job job;
	String ero,tam,eroId,serial,model,dris,category,priority,jobstatus,duedate;
	String workitems, technicians;
	Integer estTime,dis;
	Date completeby;
	Long jobId;

	public String getCategory() {return category;}
	public Date getCompleteBy() {return completeby;}
	public Integer getDis() {return dis;}
	public String getDris() {return dris;}
	public String getDueDate() {return duedate;}
	public String getEro() {return ero;}
	public String getEroId() {return eroId;}
	public Integer getEstTime() {return estTime;}
	public Job getJob() { return job; }
	public Long getJobId() { return jobId; }
	public String getJobStatus() {return jobstatus;}
	public String getModel() {return model;}
	public String getPriority() {return priority;}
	public String getSerial() {return serial;}
	public String getTam() {return tam;}
	public String getTechnicians() {return technicians;}
	public String getWorkItems() {return workitems;}
	
	public void setCategory(String s) { category=s; }
	public void setCompleteBy(Date d) { completeby=d;}
	public void setDis(Integer i) { dis=i; }
	public void setDris(String s) { dris=s; }
	public void setDueDate(String s) { duedate=s; }
	public void setEro(String s) { ero=s; }
	public void setEroId(String s) { eroId=s; }
	public void setEstTime(Integer i) { estTime=i; }
	public void setJob(Job j) { job=j; }
	public void setJobId(Long l) { jobId=l; }
	public void setJobStatus(String s) { jobstatus=s; }
	public void setModel(String s) { model=s; }
	public void setPriority(String s) { priority=s; }
	public void setSerial(String s) { serial=s; }
	public void setTam(String s) { tam=s; }
	public void setTechnicians(String s) { technicians=s;}
	public void setWorkItems(String s) { workitems=s; }
	public void setTechnicians(Set<AssetTimeShare> lats) {
		StringBuffer sb = new StringBuffer();
		for (AssetTimeShare ats : lats) {
			sb.append(ats.getAsset().getTag()).append("<br/>");
		}
		technicians = sb.toString();		
	}
	public void setWorkItems(Set<JobTask> ljt) {
		StringBuffer sb = new StringBuffer();
		for (JobTask jt : ljt) {
			sb.append(jt.getDescription()).append("<br/>");
		}
		workitems=sb.toString();
	}
	
	public static String getAttributeValue(Job job, String attrName) {
		if (attrName == null ||
			job == null ||
			job.getTaskInstance() == null ||
		    job.getTaskInstance().getMaintainedAsset() == null) return "";
		Set<AssetAttribute> saa = job.getTaskInstance().getMaintainedAsset().getAssetAttributes();
		for (AssetAttribute aa : saa ) {
			if (attrName.equalsIgnoreCase(aa.getName())) return aa.getValue();
		}
		return "";
	}
	public static Asset getMaintainedAsset(Job job) {
		if (job == null ||
			job.getTaskInstance() == null ) return null;
		return job.getTaskInstance().getMaintainedAsset();
	}
	public static String getAssetSerial(Job job) {
		if (job == null ||
			job.getTaskInstance() == null ||
			job.getTaskInstance().getMaintainedAsset() == null) return "";
		return job.getTaskInstance().getMaintainedAsset().getTag();
	}
	
	
	public static MJob copyJob(Job j) {
		MJob mj = new MJob();
		mj.setCategory(j.getCat()!=null?j.getCat().getLabel():"");
		mj.setCompleteBy(j.getCompleteBy());
		mj.setDis(j.getDaysInShop());
		mj.setDris(DateUtils.get4CharDate(j.getReceivedInShopDate()));
		mj.setDueDate(DateUtils.get4CharDate(j.getCompleteBy()));
		mj.setEro(j.getEro());
		mj.setEroId(getAttributeValue(j,"ID"));
		mj.setEstTime(j.getEstTime());
		mj.setJob(j);
		mj.setJobId(j.getId());
		mj.setJobStatus(j.getStatus()!=null?j.getStatus().getLabel():"");
		mj.setModel(getAttributeValue(j,"MODEL"));
		mj.setPriority(j.getPriority()!=null?j.getPriority().getLabel():"");
		mj.setSerial(getAssetSerial(j));
		mj.setTam(getAttributeValue(j,"TAM"));
		mj.setTechnicians(j.getAssetTimeShares());
		mj.setWorkItems(j.getJobTasks());
		return mj;
	}
}

