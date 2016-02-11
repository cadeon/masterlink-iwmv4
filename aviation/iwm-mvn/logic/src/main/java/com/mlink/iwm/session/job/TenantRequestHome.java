package com.mlink.iwm.session.job;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.TenantRequest;
import com.mlink.iwm.session.contact.ContactHome;

@SuppressWarnings("serial")  
 @Name("tenantRequestHome")
public class TenantRequestHome extends EntityHome<TenantRequest> {

	@In(create = true)
	ContactHome contactHome;
	@In(create = true)
	JobHome jobHome;

	public void setTenantRequestId(Long id) {
		setId(id);
	}

	public Long getTenantRequestId() {
		return (Long) getId();
	}

	@Override
	protected TenantRequest createInstance() {
		TenantRequest tenantRequest = new TenantRequest();
		return tenantRequest;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Contact contact = contactHome.getDefinedInstance();
		if (contact != null) {
			getInstance().setContact(contact);
		}
		Job job = jobHome.getDefinedInstance();
		if (job != null) {
			getInstance().setJob(job);
		}
	}

	public boolean isWired() {
		return true;
	}

	public TenantRequest getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
