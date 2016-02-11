package com.mlink.iwm.session.mrap;

import com.mlink.iwm.entity.asset.Asset;

public class QualificationEditPresentation {
	private Asset qualification;

	public QualificationEditPresentation() {
		this.qualification = new Asset();
	}

	public QualificationEditPresentation(Asset qualification) {
		this.qualification = qualification;
	}

	public String getQualification() {
		return this.qualification.getTag();
	}
}
