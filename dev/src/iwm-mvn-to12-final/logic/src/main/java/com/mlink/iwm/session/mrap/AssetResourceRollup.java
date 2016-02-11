package com.mlink.iwm.session.mrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.res.AssetResource;

public class AssetResourceRollup {

	private List<Long> assetResourceIds = new ArrayList<Long>();
	private Long assetTemplateId;
	private String assetTemplateName;
	private int quantity;

	public AssetResourceRollup(String templateName, Long templateId, int quantity ) {	
		this.assetTemplateName = templateName;
		this.assetTemplateId = templateId;
		this.quantity = quantity;
	}
	
	public List<Long> getAssetResourceIds() { return assetResourceIds; }
	public Long getAssetTemplateId() { return assetTemplateId; }
	public String getAssetTemplateName() { return assetTemplateName; }
	public int getQuantity() { return quantity; }
	
	public void setAssetResourceIds(List<Long> ls) { this.assetResourceIds = ls; }
	public void setAssetTemplateId(Long l) { this.assetTemplateId = l; }
	public void setAssetTemplateName(String s) { }
	public void setQuantity(int i) { quantity = i; }
	
	public void addAssetResourceId(Long l) { this.assetResourceIds.add(l); }
	
	public static AssetResourceRollup copy(AssetResource ar) {
		return new AssetResourceRollup( 
				ar.getAssetTemplate().getName(), 
				ar.getAssetTemplate().getId(), 
				ar.getQuantity());
	}
	public static List<AssetResourceRollup> join(List<AssetResource> ars) {
		Map<Long,AssetResourceRollup> rollupMap = new HashMap<Long,AssetResourceRollup>();
		for (AssetResource ar : ars) {
			AssetResourceRollup rollup = rollupMap.get(ar.getAssetTemplate().getId());
			if (rollup == null) {
				rollup = AssetResourceRollup.copy(ar);
				rollupMap.put(ar.getAssetTemplate().getId(), rollup);
			} else {
				rollup.quantity++;
			}
			rollup.addAssetResourceId(ar.getId());
		}
		return new ArrayList<AssetResourceRollup>(rollupMap.values());
	}
	/**
	 * Splits the list of AssetResourceRollup objects into list of 
	 * AssetTemplate ids. Quantity determines the number of AssetTemplate
	 * id entries for the same AssetTemplate
	 * 
	 * @param rollups List of AssetResourceRollup objects to split
	 * @return
	 */
	public static List<Long> split(List<AssetResourceRollup> rollups) {
		List<Long> ats = new ArrayList<Long>();
		for (AssetResourceRollup rollup : rollups) {
			for (int i = 0; i<rollup.quantity; i++) {
				ats.add(new Long(rollup.getAssetTemplateId()));
			}
		}
		return ats;
	}
}
