package com.mlink.iwm.session.mrap;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.mrap.TechnicianEditPresentation.Status;
import com.mlink.iwm.util.DateUtils;

public class TechnicianStatusReconciler {

	private final AssetHome assetHome;

	public TechnicianStatusReconciler(AssetHome assetHome) {
		this.assetHome = assetHome;
	}

	public void reconcileStatus(TechnicianEditPresentation tech, List<AssetNotAvail> updatedAnas) {
		if (!tech.getStatusToday().equals(tech.getCalculatedStatusToday())) {
			if (tech.getStatusToday().equals(Status.ABSENT)) {
				processStatusAbsent(tech, updatedAnas, DateUtils.now());
				tech.setCalculatedStatusToday(Status.ABSENT);
			} else {
				processStatusIn(tech, updatedAnas, DateUtils.now());
				tech.setCalculatedStatusToday(Status.IN);
			}
		}
	}

	private void processStatusAbsent(TechnicianEditPresentation tech, List<AssetNotAvail> updatedAnas, Date today) {
		boolean leaveTimeExists = false;
		for (AssetNotAvail leaveTime : updatedAnas) {
			if (isLeaveTimeExists(leaveTime) == true) {
				leaveTimeExists = true;
				break;
			}
		}

		if (leaveTimeExists == false) {
			AssetNotAvail newLeaveTime = createNewLeaveTime(tech);
			newLeaveTime.setStartDate(today);
			newLeaveTime.setEndDate(today);
			updatedAnas.add(newLeaveTime);
		}
	}

	private void processStatusIn(TechnicianEditPresentation tech, List<AssetNotAvail> updatedAnas, Date today) {
		for (Iterator<AssetNotAvail> it = updatedAnas.iterator(); it.hasNext();) {
			AssetNotAvail leaveTime = it.next();
			if (isLeaveTimeExists(leaveTime)) {
				if (isLeaveTimeOneDay(leaveTime)) {
					it.remove();
					updatedAnas.remove(leaveTime);
					assetHome.deleteLeaveTime(tech.getTechnician(), leaveTime);
				} else {
					Calendar newEndDate = Calendar.getInstance();
					newEndDate.add(Calendar.DATE, -1);
					leaveTime.setEndDate(newEndDate.getTime());
				}
			}
		}
	}

	private boolean isLeaveTimeOneDay(AssetNotAvail leaveTime) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(leaveTime.getStartDate());
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(leaveTime.getEndDate());
		if (DateUtils.isSameDay(startCal, endCal)) {
			return true;
		}
		return false;
	}

	private AssetNotAvail createNewLeaveTime(TechnicianEditPresentation tech) {
		AssetNotAvail ana = new AssetNotAvail();
		ana.setAsset(tech.getTechnician());
		return ana;
	}

	private boolean isLeaveTimeExists(AssetNotAvail leaveTime) {
		return DateUtils.isTodayInRange(leaveTime.getStartDate(), leaveTime.getEndDate());
	}
}
