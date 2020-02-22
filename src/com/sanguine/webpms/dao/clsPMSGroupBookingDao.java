package com.sanguine.webpms.dao;

import com.sanguine.webpms.model.clsPMSGroupBookingModel;

public interface clsPMSGroupBookingDao{

	public void funAddUpdatePMSGroupBooking(clsPMSGroupBookingModel objMaster);

	public clsPMSGroupBookingModel funGetPMSGroupBooking(String docCode,String clientCode);

}
