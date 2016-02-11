package com.mlink.iwm.session.mrap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.session.asset.AssetList;

public class AssetHelper {

	@In(create = true, required = false)
	AssetList assetList;

	public List<AssetPresentation> autocomplete(Object suggest) {
		if (suggest == null)
			return null;
		String pref = (String) suggest;
		ArrayList<AssetPresentation> result = new ArrayList<AssetPresentation>();
		if (assetList == null)
			assetList = (AssetList) Component.getInstance(AssetList.class);
		List<Asset> al = assetList.getAll();
		for (Asset a : al) {
			if (AssetKind.PA.equals(a.getAssetKind()) && a.getTag() != null
					&& (a.getTag().toLowerCase().startsWith(pref.toLowerCase()) || "".equals(pref)) )
				result.add(AssetHelper.copyAsset(a));
		}
		return result;
	}

	public static String getAttributeValue(Asset asset, String attrName) {
		if (attrName == null || asset == null)
			return "";
		List<AssetAttribute> saa = asset.getAssetAttributes();
		for (AssetAttribute aa : saa) {
			if (attrName.equalsIgnoreCase(aa.getName()))
				return aa.getValue();
		}
		return "";
	}

	public static String getEroid(Asset a) {
		return AssetHelper.getAttributeValue(a, "id");
	}

	public static String getModel(Asset a) {
		return AssetHelper.getAttributeValue(a, "model");
	}

	public static String getTam(Asset a) {
		return AssetHelper.getAttributeValue(a, "tam");
	}

	public static AssetPresentation copyAsset(Asset asset) {
		AssetPresentation ap = new AssetPresentation();
		ap.setEroid(AssetHelper.getEroid(asset));
		ap.setModel(AssetHelper.getModel(asset));
		ap.setSerial(asset.getTag());
		ap.setTam(AssetHelper.getTam(asset));
		ap.setOwner(asset.getOrganization().getName());
		ap.setOwnerActivityCode(asset.getOrganization().getActivityCode());
		if (!asset.getOrganization().getContacts().isEmpty()) {
			Contact contact = asset.getOrganization().getContacts().get(0);
			if (contact != null) {
				ap.setOwnerContact(contact.getLname() != null ? contact.getLname() : "");
				ap.setOwnerPhone(contact.getPhone() != null ? contact.getPhone() : "");
				ap.setOwnerEmail(contact.getEmail() != null ? contact.getEmail() : "");
			}
		}
		return ap;
	}
}
