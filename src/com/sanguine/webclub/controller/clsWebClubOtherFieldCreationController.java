package com.sanguine.webclub.controller;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webclub.bean.clsWebClubMemberProfileSetupBean;
import com.sanguine.webclub.bean.clsWebClubOtherFieldCreationBean;
import com.sanguine.webclub.service.clsWebClubOtherFieldCreationService;


@Controller
public class clsWebClubOtherFieldCreationController{

	@Autowired
	private clsWebClubOtherFieldCreationService objWebClubOtherFieldCreationService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

	//Open WebClubPDC
	@RequestMapping(value = "/frmOtherFieldCreation", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
				
		return new ModelAndView("frmOtherFieldCreation","command", new clsWebClubOtherFieldCreationBean());
	}
	//Load Master Data On Form
	@RequestMapping(value = "/frmOtherFieldCreation1", method = RequestMethod.POST)
	public @ResponseBody clsWebClubOtherFieldCreationBean funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("usercode").toString();
		clsWebClubOtherFieldCreationBean objBean=new clsWebClubOtherFieldCreationBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsWebClubOtherFieldCreationBean objWebClubPDC = new clsWebClubOtherFieldCreationBean();
		return objWebClubPDC;
	}

	//Save or Update WebClubPDC
		@RequestMapping(value = "/saveOtherFieldCreation", method = RequestMethod.POST)
		public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsWebClubOtherFieldCreationBean objBean ,BindingResult result,HttpServletRequest req){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			List list=null;		
			StringBuilder sbSql = new StringBuilder();
			int k=0;
			if(!objBean.getListTableCreation().isEmpty()&&objBean!=null)
			{
				//check table is empty or not
				String WebCLUBDB=req.getSession().getAttribute("WebCLUBDB").toString();					
				try{
					sbSql.append("SELECT * FROM "+WebCLUBDB+".tblotherdtl ; ");
					 list =objWebClubOtherFieldCreationService.funExecuteList(sbSql.toString());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				boolean tableStatus=false;
				Map<String,List> map=funDataBaseShrink(sbSql.toString());	
				if(list==null)
				{
					tableStatus=true;				
				}
				else if(list.isEmpty())
				{
					tableStatus=false;
				}					
				if(tableStatus)
				{					
					//drop if empty
					sbSql.setLength(0);
					sbSql.append("DROP TABLE IF EXISTS tblotherdtl; ");
					objWebClubOtherFieldCreationService.funExecuteQuery(sbSql.toString());
					
					//create table
					sbSql.setLength(0);
					sbSql.append("CREATE TABLE `tblotherdtl` ( strMemberCode VARCHAR(10) NOT NULL DEFAULT '',strClientCode VARCHAR(10) NOT NULL DEFAULT ''");			
							for(int i=0;i<objBean.getListTableCreation().size();i++)					{	
								clsWebClubOtherFieldCreationBean obj = objBean.getListTableCreation().get(i);
								if(obj.getStrFieldName()!=null)
								{
									if(k>=1&&i==objBean.getListTableCreation().size()-1)
									{
										if(obj.getStrDataType().equalsIgnoreCase("DATE")||obj.getStrDataType().equalsIgnoreCase("TIME")||obj.getStrDataType().equalsIgnoreCase("DATETIME")||obj.getStrDataType().equalsIgnoreCase("BLOB")||obj.getStrDataType().equalsIgnoreCase("TEXT"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+" ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("DECIMAL"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+",2) ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("VARCHAR"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") NOT NULL DEFAULT ''");
										}
										else
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") ");
										}
									}
									else
									{
										if(obj.getStrDataType().equalsIgnoreCase("DATE")||obj.getStrDataType().equalsIgnoreCase("TIME")||obj.getStrDataType().equalsIgnoreCase("DATETIME")||obj.getStrDataType().equalsIgnoreCase("BLOB")||obj.getStrDataType().equalsIgnoreCase("TEXT"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+" ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("DECIMAL"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+",2) ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("VARCHAR"))
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") NOT NULL DEFAULT ''");
										}
										else
										{
											sbSql.append(",`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") ");
										}								
									}
									k++;
								}				
							}
							sbSql.append(" ,PRIMARY KEY (`strMemberCode`,`strClientCode`)) ");
							sbSql.append(" COLLATE='latin1_swedish_ci' ");
							sbSql.append(" ENGINE=InnoDB ;");	
							objWebClubOtherFieldCreationService.funExecuteQuery(sbSql.toString());				
							req.getSession().setAttribute("success", true);
							req.getSession().setAttribute("successMessage", "Table tblotherdtl Created Successfully ");
				}			
				else
				{
					sbSql.setLength(0);
					if(!objBean.getListTableCreation().isEmpty())
					{
						sbSql.append(" ALTER TABLE `tblotherdtl` ");
						 int count=0;
						 String AfterValue="";
						 for (Map.Entry<String,List> entry : map.entrySet()){ 
							 AfterValue=entry.getKey(); 
						 }
						 for(int i=0;i<objBean.getListTableCreation().size();i++)					{	
								clsWebClubOtherFieldCreationBean obj = objBean.getListTableCreation().get(i);
								if(obj.getStrFieldName()!=null)
								{
									if(count==0)
									 {
										if(obj.getStrDataType().equalsIgnoreCase("DATE")||obj.getStrDataType().equalsIgnoreCase("TIME")||obj.getStrDataType().equalsIgnoreCase("DATETIME")||obj.getStrDataType().equalsIgnoreCase("BLOB")||obj.getStrDataType().equalsIgnoreCase("TEXT"))
										{
											sbSql.append("ADD COLUMN `"+obj.getStrFieldName().toString()+"` "+obj.getStrDataType()+" AFTER `"+AfterValue+"` ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("DECIMAL"))
										{
											sbSql.append("ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+",2) AFTER `"+AfterValue+"` ");
										}
										else if(obj.getStrDataType().equalsIgnoreCase("VARCHAR"))
										{
											sbSql.append("ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") NOT NULL DEFAULT '' AFTER `"+AfterValue+"` ");
										}
										else
										{
											sbSql.append("ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") AFTER `"+AfterValue+"` ");
										}
									    AfterValue=obj.getStrFieldName();
									    count++;
									 }
									 else
									 {
										 if(obj.getStrDataType().equalsIgnoreCase("DATE")||obj.getStrDataType().equalsIgnoreCase("TIME")||obj.getStrDataType().toString().equalsIgnoreCase("DATETIME")||obj.getStrDataType().equalsIgnoreCase("BLOB")||obj.getStrDataType().equalsIgnoreCase("TEXT"))
											{
												sbSql.append(",ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+" AFTER `"+AfterValue+"` ");
											}
											else if(obj.getStrDataType().equalsIgnoreCase("DECIMAL"))
											{
												sbSql.append(",ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+",2) AFTER `"+AfterValue+"` ");
											}
											else if(obj.getStrDataType().equalsIgnoreCase("VARCHAR"))
											{
												sbSql.append(",ADD COLUMN `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") NOT NULL DEFAULT '' AFTER `"+AfterValue+"` ");
											}
											else
											{
												sbSql.append("ADD COLUMN ,`"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") AFTER `"+AfterValue+"` ");
											}
									 }
								}
						 }
						objWebClubOtherFieldCreationService.funExecuteQuery(sbSql.toString());
						req.getSession().setAttribute("success", true);
						req.getSession().setAttribute("successMessage", "Table tblotherdtl Updated Successfully ");		
					}									
				}
					return new ModelAndView("redirect:/frmOtherFieldCreation.html");
			}
			return new ModelAndView("frmOtherFieldCreation");				
		}
	
		public Map funDataBaseShrink(String Sql)
	    {
	    	  Map<String,List> hmap = new LinkedHashMap();	    	  
	    	  List<List> list=new ArrayList<List>();
	    	  objGlobal=new clsGlobalFunctions();
	    	  Connection con = null;
	    	  String driver = "com.mysql.jdbc.Driver";
	    	 // String url = "jdbc:mysql://localhost:3306/";
	    	 // String db = "jdbctutorial";	    	 
	    	 // String user = "root";
	    	 // String pass = "root";
	    	 // String dbName="";
	    	  try{
		    	  Class.forName(driver);
		    	  con = (Connection) DriverManager.getConnection(objGlobal.urlwebclub, objGlobal.urluser, objGlobal.urlPassword);
		    	  try{
			    	  Statement st = (Statement) con.createStatement();
			    	  ResultSet rs = (ResultSet) st.executeQuery(Sql);
			    	  ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
			    	  int col = md.getColumnCount();
			    	  for (int i = 1; i <= col; i++)
			    	  {
				    	  List listt=new ArrayList<>();
				    	  String col_name = md.getColumnName(i);
				    	  String col_type = md.getColumnTypeName(i);
				    	  int col_size = md.getColumnDisplaySize(i);
				    	  if(!(col_name.equalsIgnoreCase("strMemberCode")||col_name.equalsIgnoreCase("strClientCode")))
				    	  {
				    		  listt.add(col_name);
					    	  listt.add(col_type);
					    	  listt.add(col_size);
					    	  hmap.put(col_name, listt);
				    	  }	    	  
			    	  }				
		    	  }
		    	  catch (SQLException s){
		    	  }
	    	  }
	    	  catch (Exception e)
	    	  {
	    		  e.printStackTrace();
	    	  }
	    	  return hmap;
	    }
}
