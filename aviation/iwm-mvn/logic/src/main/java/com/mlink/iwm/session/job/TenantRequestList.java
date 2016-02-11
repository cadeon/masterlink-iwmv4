package com.mlink.iwm.session.job;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.job.TenantRequest;

@SuppressWarnings("serial")  
 @Name("tenantRequestList")
public class TenantRequestList extends EntityQuery<TenantRequest> {

	private static final String EJBQL = "select tenantRequest from TenantRequest tenantRequest";

	private static final String[] RESTRICTIONS = {
			"lower(tenantRequest.jobDescription) like lower(concat(#{tenantRequestList.tenantRequest.jobDescription},'%'))",
			"lower(tenantRequest.locationComment) like lower(concat(#{tenantRequestList.tenantRequest.locationComment},'%'))",
			"lower(tenantRequest.note) like lower(concat(#{tenantRequestList.tenantRequest.note},'%'))",};

	private TenantRequest tenantRequest = new TenantRequest();

	public TenantRequestList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TenantRequest getTenantRequest() {
		return tenantRequest;
	}
}
