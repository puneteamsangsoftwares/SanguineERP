package com.sanguine.webpms.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.model.clsPMSStaffMasterModel;
import com.sanguine.webpms.model.clsPMSStaffMasterModel_ID;

@Repository("clsPMSStaffMasterDao")
public class clsPMSStaffMasterDaoImpl implements clsPMSStaffMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdatePMSStaffMaster(clsPMSStaffMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsPMSStaffMasterModel funGetPMSStaffMaster(String docCode,String clientCode){
		return (clsPMSStaffMasterModel) webPMSSessionFactory.getCurrentSession().get(clsPMSStaffMasterModel.class,new clsPMSStaffMasterModel_ID(docCode,clientCode));
	}

	public clsPMSStaffMasterModel funGetObject(String code, String clientCode) {
		return (clsPMSStaffMasterModel) webPMSSessionFactory.getCurrentSession().get(clsPMSStaffMasterModel.class, new clsPMSStaffMasterModel_ID(code, clientCode));
	}
}
