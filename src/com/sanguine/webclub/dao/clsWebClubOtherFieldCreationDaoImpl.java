package com.sanguine.webclub.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("clsWebClubOtherFieldCreationDao")
public class clsWebClubOtherFieldCreationDaoImpl implements clsWebClubOtherFieldCreationDao{

	@Autowired
	private SessionFactory WebClubSessionFactory;
	
	//
	/*@Override
	private void funExecuteQuery(String sql) {
		try {
			Query query = WebClubSessionFactory.getCurrentSession().createSQLQuery(sql);
			query.executeUpdate();
		} catch (Exception e) {
		} finally {
			
		}
	}*/
	
	@Override
	public void funExecuteQuery(String sql) {
		try {
			Query query = WebClubSessionFactory.getCurrentSession().createSQLQuery(sql);
			query.executeUpdate();
		} catch (Exception e) {
		} finally {
			
		}
		
	}

}
