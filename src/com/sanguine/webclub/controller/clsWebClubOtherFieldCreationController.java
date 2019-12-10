package com.sanguine.webclub.controller;

import java.util.List;

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

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
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
						
			StringBuilder sbSql = new StringBuilder();
			int k=0;
			if(!objBean.getListTableCreation().isEmpty()&&objBean!=null)
			{
			sbSql.append("DROP TABLE IF EXISTS tblotherdtl; ");
			objWebClubOtherFieldCreationService.funExecuteQuery(sbSql.toString());
			sbSql.setLength(0);
			sbSql.append("CREATE TABLE `tblotherdtl` ( strMemberCode VARCHAR(10) NOT NULL DEFAULT '',strClientCode VARCHAR(10) NOT NULL DEFAULT ''");			
					for(int i=0;i<objBean.getListTableCreation().size();i++)					{	
						clsWebClubOtherFieldCreationBean obj = objBean.getListTableCreation().get(i);
						if(obj.getStrFieldName()!=null)
						{
							/*if(k==0)
							{
								if(obj.getStrDataType().equalsIgnoreCase("DATE")||obj.getStrDataType().equalsIgnoreCase("TIME")||obj.getStrDataType().equalsIgnoreCase("DATETIME")||obj.getStrDataType().equalsIgnoreCase("BLOB")||obj.getStrDataType().equalsIgnoreCase("TEXT"))
								{
									sbSql.append(" `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+" ");
								}	
								else if(obj.getStrDataType().equalsIgnoreCase("DECIMAL"))
								{
									sbSql.append(" `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+",2) ");
								}
								else if(obj.getStrDataType().equalsIgnoreCase("VARCHAR"))
								{
									sbSql.append(" `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") NOT NULL DEFAULT ''");
								}
								else
								{
									sbSql.append(" `"+obj.getStrFieldName()+"` "+obj.getStrDataType()+"("+obj.getDblLength()+") ");
								}
								
							}
							else*/ if(k>=1&&i==objBean.getListTableCreation().size()-1)
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
					return new ModelAndView("redirect:/frmOtherFieldCreation.html");
			}
				return new ModelAndView("frmOtherFieldCreation");				
		}
	
	// Assign filed function to set data onto form for edit transaction.
	/*	@RequestMapping(value = "/loadPDCMemberWiseData", method = RequestMethod.GET)
		public @ResponseBody List funAssignPDCMemberData(@RequestParam("memCode") String memCode, HttpServletRequest req) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			clsWebClubOtherFieldCreationBean objModel = new clsWebClubOtherFieldCreationBean();
			//List list = objWebClubPDCService.funGetWebClubPDC(memCode, clientCode);
			String sql="SELECT a.strMemCode,a.strChequeNo,a.strDrawnOn,a.strType,a.dblChequeAmt,Date(a.dteChequeDate) FROM tblpdcdtl a WHERE a.strMemCode='"+memCode+"' and a.strClientCode='"+clientCode+"' ";
			List list=objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (null == list) {				
				objModel.setStrMemCode("Invalid Code");
			}
			return list;
		}
		
		// Assign filed function to set data onto form for edit transaction.
				@RequestMapping(value = "/loadWebBookBankCode", method = RequestMethod.GET)
				public @ResponseBody List funAssignBankCode(@RequestParam("bankCode") String bankCode, HttpServletRequest req) {
					String clientCode = req.getSession().getAttribute("clientCode").toString();
					String strWebBooksDB=req.getSession().getAttribute("WebBooksDB").toString();
					clsWebClubOtherFieldCreationBean objModel = new clsWebClubOtherFieldCreationBean();
					//List list = objWebClubPDCService.funGetWebClubPDC(memCode, clientCode);
					String sql="SELECT a.strBankName FROM "+strWebBooksDB+".tblbankmaster a where a.strBankCode='"+bankCode+"' AND a.strClientCode='"+clientCode+"' ";
					List list=objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
										
					return list;
				}
		*/
	
/*//Convert bean to model function
	private clsWebClubPDCModel funPrepareModel(clsWebClubOtherFieldCreationBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsWebClubPDCModel objModel = null;
		
		for(int i=0;i<objBean.getListPDCDtl().size();i++)
		{
			clsWebClubOtherFieldCreationBean obj = objBean.getListPDCDtl().get(i);
			objModel.setStrMemCode(obj.getStrMemCode());
			objModel.setStrChequeNo(obj.getStrChequeNo());
			objModel.setStrDrawnOn(obj.getStrDrawnOn());
			objModel.setStrType(obj.getStrType());
			objModel.setDblChequeAmt(obj.getDblChequeAmt());
			objModel.setDteChequeDate(obj.getDteChequeDate());
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrClientCode(clientCode);			
		}		
		return objModel;

	}*/

}
