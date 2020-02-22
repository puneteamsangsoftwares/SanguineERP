package com.sanguine.webpms.service;

import com.sanguine.webpms.model.clsPMSGroupBookingModel;

public interface clsPMSGroupBookingService{

	public void funAddUpdatePMSGroupBooking(clsPMSGroupBookingModel objMaster);

	public clsPMSGroupBookingModel funGetPMSGroupBooking(String docCode,String clientCode);

}
