package com.sanguine.webclub.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webclub.bean.clsWebClubMemberProfileBean;
import com.sanguine.webclub.model.clsWebClubDependentMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberPhotoModel;
import com.sanguine.webclub.model.clsWebClubMemberPhotoModel_ID;
import com.sanguine.webclub.model.clsWebClubMemberProfileModel;
import com.sanguine.webclub.model.clsWebClubMemberProfileModel_ID;
import com.sanguine.webclub.model.clsWebClubPreMemberProfileModel;
import com.sanguine.webclub.service.clsWebClubAreaMasterService;
import com.sanguine.webclub.service.clsWebClubCityMasterService;
import com.sanguine.webclub.service.clsWebClubCountryMasterService;
import com.sanguine.webclub.service.clsWebClubDependentMasterService;
import com.sanguine.webclub.service.clsWebClubMemberPhotoService;
import com.sanguine.webclub.service.clsWebClubMemberProfileService;
import com.sanguine.webclub.service.clsWebClubRegionMasterService;
import com.sanguine.webclub.service.clsWebClubStateMasterService;

@Controller
public class clsWebClubMemberProfileController {
	
	@Autowired
	clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsWebClubMemberProfileService objMemberProfileService;

	@Autowired
	private clsWebClubAreaMasterService objAreaMasterService;

	@Autowired
	private clsWebClubRegionMasterService objRegionMasterService;

	@Autowired
	private clsWebClubStateMasterService objStateMasterService;

	@Autowired
	private clsWebClubCountryMasterService objCountryMasterService;

	@Autowired
	private clsWebClubCityMasterService objCityMasterService;

	@Autowired
	private clsWebClubDependentMasterService objDependentMasterService;

	@Autowired
	private clsWebClubMemberPhotoService objWebClubMemberPhotoService;
	
	// Open MemberProfile
	@RequestMapping(value = "/frmMemberProfile", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberProfile_1", "command", new clsWebClubMemberProfileBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberProfile", "command", new clsWebClubMemberProfileBean());
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/savefrmWebClubMemberProfile", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsWebClubMemberProfileBean memProfileBean, BindingResult result, HttpServletRequest req, @RequestParam("memberImage") MultipartFile file) throws IOException {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		
		if (!result.hasErrors()) {
			// for primary member
			clsWebClubMemberProfileModel objMemProfileModel = funPrepareModel(memProfileBean, req);
			objMemberProfileService.funAddUpdateMemberProfile(objMemProfileModel);

			if(!memProfileBean.getStrMaritalStatus().equalsIgnoreCase("Single"))
			{
				// for Spouse member
				clsWebClubMemberProfileModel objMemberProfileSpouseModel = funPrepardSpouseModel(memProfileBean, objMemProfileModel, req,file);
				objMemberProfileService.funAddUpdateMemberProfile(objMemberProfileSpouseModel);

			}
			if(!memProfileBean.getListDependentMember().isEmpty()){
				// for Dependent member
				funPrepardDependentModel(memProfileBean, objMemProfileModel, req);

			}
		
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Member Code : ".concat(objMemProfileModel.getStrMemberCode()));
			return new ModelAndView("redirect:/frmMemberProfile.html?saddr=" + urlHits);
		}
		return new ModelAndView("redirect:/frmMemberProfile.html?saddr=" + urlHits);

	}
	
    public Map funDataBaseShrink()
    {
    	  Map hmap = new LinkedHashMap();
    	  List<String> list=new ArrayList<String>();
    	  objGlobal=new clsGlobalFunctions();
    	  //System.out.println("Getting Column Names Example!");
    	  Connection con = null;
    	  String url = "jdbc:mysql://localhost:3306/";
    	  String db = "jdbctutorial";
    	  String driver = "com.mysql.jdbc.Driver";
    	  String user = "root";
    	  String pass = "root";
    	  String dbName="";
    	  try{
    	  Class.forName(driver);
    	  con = (Connection) DriverManager.getConnection(objGlobal.urlwebclub, objGlobal.urluser, objGlobal.urlPassword);
    	  try{
    	  Statement st = (Statement) con.createStatement();
    	  ResultSet rs = (ResultSet) st.executeQuery("SELECT * FROM tblotherdtl");
    	  ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
    	  int col = md.getColumnCount();
    	 /* System.out.println("Number of Column : "+ col);
    	  System.out.println("Columns Name: ");*/
    	  for (int i = 1; i <= col; i++){
    	  String col_name = md.getColumnName(i);
    	  String col_type = md.getColumnTypeName(i);
    	  hmap.put(col_name, col_type);
    	  //list.add(col_name.toString());
    	  //System.out.println(col_name);
    	  }
    	  }
    	  catch (SQLException s){
    	  //System.out.println("SQL statement is not executed!");
    	  }
    	  }
    	  catch (Exception e){
    	  e.printStackTrace();
    	  }
    	  return hmap;
    }

	
	
	
	private clsWebClubMemberProfileModel funPrepareModel(clsWebClubMemberProfileBean memProfileBean, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		objGlobal = new clsGlobalFunctions();
		long lastNo = 0;
		clsWebClubMemberProfileModel mpModel;
		if (memProfileBean.getStrCustomerCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
			String customerCode = "C" + String.format("%06d", lastNo);
			mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(customerCode, clientCode));
			mpModel.setIntGId(lastNo);
			mpModel.setStrCustomerID("01");
			mpModel.setStrUserCreated(userCode);
			mpModel.setStrUserModified(userCode);
			mpModel.setStrPropertyCode(propCode);
			mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			mpModel.setStrPrimaryCode(customerCode);
		} else {
			clsWebClubMemberProfileModel objMemberProfile = objMemberProfileService.funGetCustomer(memProfileBean.getStrCustomerCode(), clientCode);
			if (null == objMemberProfile) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
				String customerCode = "C" + String.format("%06d", lastNo);
				mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(customerCode, clientCode));
				mpModel.setIntGId(lastNo);
				mpModel.setStrCustomerID("01");
				mpModel.setStrUserCreated(userCode);
				mpModel.setStrUserModified(userCode);
				mpModel.setStrPropertyCode(propCode);
				mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setStrPrimaryCode(customerCode);
			} else {
				mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(memProfileBean.getStrCustomerCode(), clientCode));
				mpModel.setStrUserModified(userCode);
				mpModel.setStrPropertyCode(propCode);
				mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setStrPrimaryCode(memProfileBean.getStrCustomerCode());
			}
		}
		
		/* String memberCode ="";
		 String custID =
		 objMemberProfileService.funGetCustomerID(mpModel.getStrCustomerCode(),
		 clientCode);
		 if(mpModel.getStrCustomerID()=="01")
		 {
		
		 memberCode = memProfileBean.getStrMemberCode() +
		 " "+mpModel.getStrCustomerID();
		 mpModel.setStrMemberCode(memberCode);
		
		 }else
		 {
		
		 if(!(memProfileBean.getStrMemberCode().contains(" ")))
		 {
		 int intcustId = Integer.parseInt(custID);
		 intcustId = intcustId+1;
		 mpModel.setStrCustomerID("0"+intcustId);
		 memberCode = memProfileBean.getStrMemberCode() +
		 mpModel.getStrCustomerID();
		 mpModel.setStrMemberCode(memberCode);
		 }else
		 {
		 mpModel.setStrMemberCode(memProfileBean.getStrMemberCode());
		 }
		
		 }*/
	
		mpModel.setStrMemberCode(memProfileBean.getStrMemberCode() + " 01");
				
		mpModel.setStrUserCreated(userCode);
		mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		mpModel.setStrPrefixCode(memProfileBean.getStrPrefixCode());
		mpModel.setStrFirstName(memProfileBean.getStrFirstName());
		mpModel.setStrMiddleName(memProfileBean.getStrMiddleName());
		mpModel.setStrLastName(memProfileBean.getStrLastName());
		mpModel.setStrFullName(memProfileBean.getStrFirstName()+" "+memProfileBean.getStrMiddleName()+" "+memProfileBean.getStrLastName());
		//mpModel.setStrFullName(memProfileBean.getStrFullName());
		mpModel.setStrNameOnCard(memProfileBean.getStrNameOnCard());
		
		// Residence Address
		mpModel.setStrResidentAddressLine1(memProfileBean.getStrResidentAddressLine1());
		mpModel.setStrResidentAddressLine2(memProfileBean.getStrResidentAddressLine2());
		mpModel.setStrResidentAddressLine3(memProfileBean.getStrResidentAddressLine3());
		mpModel.setStrResidentAreaCode(memProfileBean.getStrResidentAreaCode());
		mpModel.setStrResidentCountryCode(memProfileBean.getStrResidentCountryCode());
		mpModel.setStrResidentCtCode(memProfileBean.getStrResidentCtCode());
		mpModel.setStrResidentEmailID(memProfileBean.getStrResidentEmailID());
		mpModel.setStrResidentFax1(memProfileBean.getStrResidentFax1());
		mpModel.setStrResidentFax2(memProfileBean.getStrResidentFax2());
		mpModel.setStrResidentLandMark(memProfileBean.getStrResidentLandMark());
		mpModel.setStrResidentMobileNo(memProfileBean.getStrResidentMobileNo());
		mpModel.setStrResidentPinCode(memProfileBean.getStrResidentPinCode());
		mpModel.setStrResidentRegionCode(memProfileBean.getStrResidentRegionCode());
		mpModel.setStrResidentStateCode(memProfileBean.getStrResidentStateCode());
		mpModel.setStrResidentTelephone1(memProfileBean.getStrResidentTelephone1());
		mpModel.setStrResidentTelephone2(memProfileBean.getStrResidentTelephone2());

		// Company Address
		mpModel.setStrCompanyAddressLine1(memProfileBean.getStrCompanyAddressLine1());
		mpModel.setStrCompanyAddressLine2(memProfileBean.getStrCompanyAddressLine2());
		mpModel.setStrCompanyAddressLine3(memProfileBean.getStrCompanyAddressLine3());
		mpModel.setStrCompanyAreaCode(memProfileBean.getStrCompanyAreaCode());
		mpModel.setStrCompanyCode(memProfileBean.getStrCompanyCode());
		mpModel.setStrCompanyCountryCode(memProfileBean.getStrCompanyCountryCode());
		mpModel.setStrCompanyCtCode(memProfileBean.getStrCompanyCtCode());
		mpModel.setStrCompanyEmailID(memProfileBean.getStrCompanyEmailID());
		mpModel.setStrCompanyFax1(memProfileBean.getStrCompanyFax1());
		mpModel.setStrCompanyFax2(memProfileBean.getStrCompanyFax2());
		mpModel.setStrCompanyLandMark(memProfileBean.getStrCompanyLandMark());
		mpModel.setStrCompanyMobileNo(memProfileBean.getStrCompanyMobileNo());
		mpModel.setStrCompanyName(memProfileBean.getStrCompanyName());
		mpModel.setStrCompanyPinCode(memProfileBean.getStrCompanyPinCode());
		mpModel.setStrCompanyRegionCode(memProfileBean.getStrCompanyRegionCode());
		mpModel.setStrCompanyStateCode(memProfileBean.getStrCompanyStateCode());
		mpModel.setStrCompanyTelePhone1(memProfileBean.getStrCompanyTelePhone1());
		mpModel.setStrCompanyTelePhone2(memProfileBean.getStrCompanyTelePhone2());
		mpModel.setStrHoldingCode(memProfileBean.getStrHoldingCode());
		mpModel.setStrJobProfileCode(memProfileBean.getStrJobProfileCode());

		// Bill Address
		mpModel.setStrBillingAddressLine1(memProfileBean.getStrBillingAddressLine1());
		mpModel.setStrBillingAddressLine2(memProfileBean.getStrBillingAddressLine2());
		mpModel.setStrBillingAddressLine3(memProfileBean.getStrBillingAddressLine3());
		mpModel.setStrBillingAreaCode(memProfileBean.getStrBillingAreaCode());
		mpModel.setStrBillingCountryCode(memProfileBean.getStrBillingCountryCode());
		mpModel.setStrBillingCtCode(memProfileBean.getStrBillingCtCode());
		mpModel.setStrBillingEmailID(memProfileBean.getStrBillingEmailID());
		mpModel.setStrBillingFax1(memProfileBean.getStrBillingFax1());
		mpModel.setStrBillingFax2(memProfileBean.getStrBillingFax2());
		mpModel.setStrBillingFlag(memProfileBean.getStrBillingFlag());
		mpModel.setStrBillingLandMark(memProfileBean.getStrBillingLandMark());
		mpModel.setStrBillingMobileNo(memProfileBean.getStrBillingMobileNo());
		mpModel.setStrBillingPinCode(memProfileBean.getStrBillingPinCode());
		mpModel.setStrBillingRegionCode(memProfileBean.getStrBillingRegionCode());
		mpModel.setStrBillingStateCode(memProfileBean.getStrBillingStateCode());
		mpModel.setStrBillingTelePhone1(memProfileBean.getStrBillingTelePhone1());
		mpModel.setStrBillingTelePhone2(memProfileBean.getStrBillingTelePhone2());

		// Personal Information
		mpModel.setStrGender(memProfileBean.getStrGender());
		mpModel.setDteDateofBirth(objGlobal.funGetDate("yyyy-MM-dd",memProfileBean.getDteDateofBirth()));
		
		//mpModel.setDteDateofBirth(memProfileBean.getDteDateofBirth());

		//Bank Information
		mpModel.setStrBankCode(memProfileBean.getStrBankCode());
		mpModel.setStrIfscCOde(memProfileBean.getStrIfscCOde());
		mpModel.setStrBranchName(memProfileBean.getStrBranchName());
		mpModel.setStrAccNo(memProfileBean.getStrAccNo());
		
		
		mpModel.setStrMaritalStatus(memProfileBean.getStrMaritalStatus());
		mpModel.setStrProfessionCode(memProfileBean.getStrProfessionCode());
		mpModel.setDteMembershipStartDate(objGlobal.funGetDate("yyyy-MM-dd", memProfileBean.getDteMembershipStartDate()));
		mpModel.setDteMembershipEndDate(objGlobal.funGetDate("yyyy-MM-dd", memProfileBean.getDteMembershipEndDate()));
		
		
		//mpModel.setDteMembershipStartDate(memProfileBean.getDteMembershipStartDate());
		//mpModel.setDteMembershipEndDate(memProfileBean.getDteMembershipEndDate());
//		/mpModel.setDteAnniversary(memProfileBean.getDteAnniversary());
		mpModel.setDteAnniversary(objGlobal.funGetDate("yyyy-MM-dd",memProfileBean.getDteAnniversary()));
		
		// mpModel.setStrpName

		// Member Information
		// mpModel.setStr txtMSCategoryCode
		// mpModel.setStr txtMSCategoryNamae
		mpModel.setStrCategoryCode(memProfileBean.getStrCategoryCode());
		mpModel.setStrProposerCode(memProfileBean.getStrProposerCode());
		// mpModel.setStrProposerName
		mpModel.setStrSeconderCode(memProfileBean.getStrSeconderCode());
		// mpModel.setStrseconderName
		mpModel.setStrFatherMemberCode(memProfileBean.getStrFatherMemberCode());
		mpModel.setStrInstation(memProfileBean.getStrInstation());

		// mpModel.set txtdtMembershipStartDate
		// mpModel.setStr txtdtMembershipEndDate
		// mpModel.setStrBlocked(memProfileBean.getStrBlocked());
		mpModel.setStrAlternateMemberCode("");
		mpModel.setStrAttachment("");

		// Card Authontication Check Box
		// mpModel.setStrReasonCode(memProfileBean.getStrBlockedreasonCode());
		mpModel.setStrQualification(memProfileBean.getStrQualification());
		mpModel.setStrDesignationCode(memProfileBean.getStrDesignationCode());
		mpModel.setDblEntranceFee(memProfileBean.getDblEntranceFee());
		mpModel.setDblSubscriptionFee(memProfileBean.getDblSubscriptionFee());
		mpModel.setStrPanNumber(memProfileBean.getStrPanNumber());
		mpModel.setStrDepMobileNo(memProfileBean.getStrDepMobileNo());		
		mpModel.setStrDepEmailID(memProfileBean.getStrDepEmailID());		
		mpModel.setStrDepAadharCardNo(memProfileBean.getStrDepAadharCardNo());		
		mpModel.setStrAadharCardNo(memProfileBean.getStrAadharCardNo());
		mpModel.setStrVoterIdNo(memProfileBean.getStrVoterIdNo());
		mpModel.setStrPassportNo(memProfileBean.getStrPassportNo());
		mpModel.setStrAccNo(memProfileBean.getStrAccNo());
		mpModel.setStrBranchName(memProfileBean.getStrBranchName());
		mpModel.setStrIfscCOde(memProfileBean.getStrIfscCOde());
		// mpModel.setStr Bill Detail
		mpModel.setStrLocker(memProfileBean.getStrLocker());
		mpModel.setStrLibrary(memProfileBean.getStrLibrary());
		mpModel.setStrSeniorCitizen(memProfileBean.getStrSeniorCitizen());
		mpModel.setStrStopCredit(memProfileBean.getStrStopCredit());
		// mpModel.setStr Rescident Yes/No
		mpModel.setStrInstation(memProfileBean.getStrInstation());
		mpModel.setStrGolfMemberShip(memProfileBean.getStrGolfMemberShip());
		mpModel.setStrBlocked(memProfileBean.getStrBlocked());
		mpModel.setStrBlockedreasonCode(memProfileBean.getStrBlockedreasonCode());
		mpModel.setStrDepedentRelation("");

		mpModel.setDtePermitExpDate("1990-01-01 00:00:00");
		mpModel.setStrLiquorPermitNo("");
		mpModel.setIntFormNo(0);
		mpModel.setStrGuestEntry("Y");
		mpModel.setStrVirtualAccountCode("");
		mpModel.setChkmail(0);
		mpModel.setStrSSuffixCode("0");
		mpModel.setStrNSuffixCode("0");

		mpModel.setChrCircularemail("0");
		mpModel.setStrAuthorisedMember("");
		mpModel.setStrMemberStatusCode("");
		mpModel.setStrLikes("");
		mpModel.setStrDisLikes("");
		mpModel.setStrSendInvThrough("");
		mpModel.setStrSendCircularNoticeThrough("");
		mpModel.setDteInterviewDate("1990-01-01 00:00:00");
		mpModel.setDblCMSBalance(0.00);
		mpModel.setStrPhoto("");
		mpModel.setStrRemark("");
		mpModel.setStrDependentYesNo("N");
		mpModel.setStrSalesStaffCode("");
		mpModel.setDteProfileCreationDate("1990-01-01 00:00:00");
		mpModel.setStrResNonRes("Y");
		mpModel.setDteDependentDateofBirth("1990-01-01 00:00:00");
		mpModel.setDteMemberBlockDate("1990-01-01 00:00:00");
		
		mpModel.setDteMembershipExpiryDate(objGlobal.funGetDate("yyyy-MM-dd", memProfileBean.getDteMembershipEndDate()));
		//mpModel.setDteMembershipExpiryDate(memProfileBean.getDteMembershipEndDate());
		mpModel.setStrDebtorCode("");
		mpModel.setStrDependentFullName("");
		mpModel.setStrDependentMemberCode("");
		mpModel.setStrDependentReasonCode("");
		
		mpModel.setStrAadharCardNo(memProfileBean.getStrAadharCardNo());
		mpModel.setStrVoterIdNo(memProfileBean.getStrVoterIdNo());
		mpModel.setStrPassportNo(memProfileBean.getStrPassportNo());
		
		mpModel.setStrCustomerID("");
 		mpModel.setStrBillingFlag("N");
		mpModel.setStrMemberYesNo("");

		// mpModel.set SEND INNVOICE Through

		// mpModel.setStr Circle Notice

		// Facility Information

		// mpModel.setstr Facility Information
		// mpModel.setStrPayment;

		// mpModel.set From Date
		// mpModel.set To Date

		// mpModel.setst Block Facilty

		
		mpModel.setStrSeconderCode("");
		mpModel.setStrFatherMemberCode("");
		mpModel.setStrProposerCode("");
		return mpModel;
	}

	private void funPrepardDependentModel(clsWebClubMemberProfileBean memProfileBean, clsWebClubMemberProfileModel objMemberProfile, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		objGlobal = new clsGlobalFunctions();
		String memberCode = objMemberProfile.getStrMemberCode();
		String WebPMSDB=req.getSession().getAttribute("WebPMSDB").toString();
		
		if (memberCode.length() > 0) {
			memberCode.split(" ");

		}
	
		String sql="SELECT * FROM "+WebPMSDB+".tblotherdtl a WHERE a.strMemberCode='"+memberCode+"' ";
		List list =objGlobalFunctionsService.funGetList("SELECT * FROM "+WebPMSDB+".tblotherdtl a WHERE a.strMemberCode='"+memberCode+"' ");
		Map mhasMap = funDataBaseShrink();
		if(list.isEmpty())
		{		
			if(memProfileBean.listField!=null)
			{	
				StringBuilder sbSql= new StringBuilder (); 
				sbSql.append("INSERT INTO tblotherdtl (strMemberCode,strClientCode");
				for(int i=0;i<memProfileBean.listField.size();i++)
				{
					
					clsWebClubMemberProfileBean obj = new clsWebClubMemberProfileBean();
					obj=memProfileBean.listField.get(i);
					if(obj.getStrFieldValue()!=null&&!obj.getStrFieldValue().equalsIgnoreCase(""))
					{
						/*if(i==0)
						{
							sbSql.append(obj.getStrFieldName());
						}
						else 
						{*/
							sbSql.append(","+obj.getStrFieldName());
						//}	
					}
				}
				sbSql.append(") VALUES ('"+memProfileBean.getStrMemberCode() + " 01','"+clientCode+ "',");
				
				for(int i=0;i<memProfileBean.listField.size();i++)
				{
					
					clsWebClubMemberProfileBean obj = new clsWebClubMemberProfileBean();
					obj=memProfileBean.listField.get(i);
					if(obj.getStrFieldValue()!=null&&!obj.getStrFieldValue().equalsIgnoreCase(""))
					{
						if(i==0)
						{	if(mhasMap.get(obj.getStrFieldName())=="VARCHAR"||mhasMap.get(obj.getStrFieldName())=="DATE"||mhasMap.get(obj.getStrFieldName())=="TIME"||mhasMap.get(obj.getStrFieldName())=="DATETIME")
							{
								sbSql.append("'"+obj.getStrFieldValue()+"'");
							}
							else
							{
								sbSql.append(obj.getStrFieldValue());
							}					
						}
						else 
						{
							if(mhasMap.get(obj.getStrFieldName())=="VARCHAR"||mhasMap.get(obj.getStrFieldName())=="DATE"||mhasMap.get(obj.getStrFieldName())=="TIME"||mhasMap.get(obj.getStrFieldName())=="DATETIME")
							{
								sbSql.append(",'"+obj.getStrFieldValue()+"'");
							}
							else
							{
								sbSql.append(","+obj.getStrFieldValue());
							}					
						}	
					}
				}
				sbSql.append(");");
				objMemberProfileService.funExecuteQuery(sbSql.toString());
				
				//objGlobal.funGetList(sbSql.toString());
				}
			}
			else
			{
				if(memProfileBean.listField!=null)
				{	StringBuilder sbsqll= new StringBuilder ();				
					sbsqll.append("UPDATE  "+WebPMSDB+".tblotherdtl a SET ");					
					for(int i=0;i<memProfileBean.listField.size();i++)
					{
						
						clsWebClubMemberProfileBean obj = new clsWebClubMemberProfileBean();
						obj=memProfileBean.listField.get(i);
						if(obj.getStrFieldValue()!=null&&!obj.getStrFieldValue().equalsIgnoreCase(""))
						{
							if(i==0)
							{	if(mhasMap.get(obj.getStrFieldName())=="VARCHAR"||mhasMap.get(obj.getStrFieldName())=="DATE"||mhasMap.get(obj.getStrFieldName())=="TIME"||mhasMap.get(obj.getStrFieldName())=="DATETIME")
								{
									sbsqll.append("a."+obj.getStrFieldName()+"='"+obj.getStrFieldValue()+"'");
								}
								else
								{
									sbsqll.append("a."+obj.getStrFieldName()+"="+obj.getStrFieldValue()+"");
								}					
							}
							else 
							{
								if(mhasMap.get(obj.getStrFieldName())=="VARCHAR"||mhasMap.get(obj.getStrFieldName())=="DATE"||mhasMap.get(obj.getStrFieldName())=="TIME"||mhasMap.get(obj.getStrFieldName())=="DATETIME")
								{
									sbsqll.append(",a."+obj.getStrFieldName()+"='"+obj.getStrFieldValue()+"'");
								}
								else
								{
									sbsqll.append(",a."+obj.getStrFieldName()+"="+obj.getStrFieldValue()+"");
								}									
							}	
						}					
					}
					Object [] objj = (Object[]) list.get(0);					
					sbsqll.append(" WHERE a.strMemberCode= '"+objj[0].toString()+"' AND a.strClientCode='"+objj[1].toString()+"'");
					objMemberProfileService.funExecuteQuery(sbsqll.toString());						
				}	
			}

		
		// clsWebClubDependentMasterModel objDependentMasterModel = new
		// clsWebClubDependentMasterModel();

		List<clsWebClubDependentMasterModel> listDependentMaster = memProfileBean.getListDependentMember();
		if (null != listDependentMaster && listDependentMaster.size() > 0) {
			for (clsWebClubDependentMasterModel obDM : listDependentMaster) {
				long lastNo = 1;
				clsWebClubMemberProfileModel mpModel = new clsWebClubMemberProfileModel();
				String dependentMember = obDM.getStrMemberCode();
				String[] arrCustID = dependentMember.split(" ");

				if (obDM.getStrCustomerCode() == null) {

					lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
					String customerCode = "C" + String.format("%06d", lastNo);
					obDM.setStrCustomerCode(customerCode);
					obDM.setIntGId(lastNo);

					obDM.setStrUserCreated(userCode);
					obDM.setStrUserModified(userCode);
					obDM.setStrPropertyCode(propCode);
					obDM.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
					obDM.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));

					mpModel.setStrCustomerID(arrCustID[0]);
					mpModel.setStrCustomerCode(obDM.getStrCustomerCode());
					mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());

				} else {
					clsWebClubMemberProfileModel objMemProfile = objMemberProfileService.funGetCustomer(obDM.getStrCustomerCode(), clientCode);
					if (null == objMemProfile) {
						lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
						String customerCode = "C" + String.format("%06d", lastNo);

						obDM.setStrCustomerCode(customerCode);
						obDM.setStrUserCreated(userCode);
						obDM.setStrUserModified(userCode);
						obDM.setStrPropertyCode(propCode);
						obDM.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
						obDM.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
						mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());

						mpModel.setStrCustomerID(arrCustID[1]);
						mpModel.setStrCustomerCode(obDM.getStrCustomerCode());
						mpModel.setIntGId(lastNo);
					} else {

						mpModel.setIntGId(objMemProfile.getIntGId());
						mpModel.setStrCustomerID(arrCustID[1]);
						mpModel.setStrCustomerCode(obDM.getStrCustomerCode());
						mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());
					}
				}

				mpModel.setStrMemberCode(obDM.getStrMemberCode());

				mpModel.setStrUserCreated(userCode);
				mpModel.setStrUserModified(userCode);
				mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setStrPropertyCode(propCode);
				obDM.setDteMemberBlockDate("1990-01-01 00:00:00");
				mpModel.setStrClientCode(clientCode);
				mpModel.setStrDepedentRelation(obDM.getStrDepedentRelation());
				mpModel.setStrPrefixCode("");
				mpModel.setStrFirstName("");
				mpModel.setStrMiddleName("");
				mpModel.setStrLastName("");
				mpModel.setStrFullName(obDM.getStrDependentFullName());
				mpModel.setStrNameOnCard(obDM.getStrDependentFullName());

				// Residence Address
				mpModel.setStrResidentAddressLine1(memProfileBean.getStrResidentAddressLine1());
				mpModel.setStrResidentAddressLine2(memProfileBean.getStrResidentAddressLine2());
				mpModel.setStrResidentAddressLine3(memProfileBean.getStrResidentAddressLine3());
				mpModel.setStrResidentAreaCode(memProfileBean.getStrResidentAreaCode());
				mpModel.setStrResidentCountryCode(memProfileBean.getStrResidentCountryCode());
				mpModel.setStrResidentCtCode(memProfileBean.getStrResidentCtCode());
				mpModel.setStrResidentEmailID(memProfileBean.getStrResidentEmailID());
				mpModel.setStrResidentFax1(memProfileBean.getStrResidentFax1());
				mpModel.setStrResidentFax2(memProfileBean.getStrResidentFax2());
				mpModel.setStrResidentLandMark(memProfileBean.getStrResidentLandMark());
				mpModel.setStrResidentMobileNo(memProfileBean.getStrResidentMobileNo());
				mpModel.setStrResidentPinCode(memProfileBean.getStrResidentPinCode());
				mpModel.setStrResidentRegionCode(memProfileBean.getStrResidentRegionCode());
				mpModel.setStrResidentStateCode(memProfileBean.getStrResidentStateCode());
				mpModel.setStrResidentTelephone1(memProfileBean.getStrResidentTelephone1());
				mpModel.setStrResidentTelephone2(memProfileBean.getStrResidentTelephone2());
				// Company Address
				mpModel.setStrCompanyAddressLine1("");
				mpModel.setStrCompanyAddressLine2("");
				mpModel.setStrCompanyAddressLine3("");
				mpModel.setStrCompanyAreaCode("");
				mpModel.setStrCompanyCode("");
				mpModel.setStrCompanyCountryCode("");
				mpModel.setStrCompanyCtCode("");
				mpModel.setStrCompanyEmailID("");
				mpModel.setStrCompanyFax1("");
				mpModel.setStrCompanyFax2("");
				mpModel.setStrCompanyLandMark("");
				mpModel.setStrCompanyMobileNo("");
				mpModel.setStrCompanyName("");
				mpModel.setStrCompanyPinCode("");
				mpModel.setStrCompanyRegionCode("");
				mpModel.setStrCompanyStateCode("");
				mpModel.setStrCompanyTelePhone1("");
				mpModel.setStrCompanyTelePhone2("");
				mpModel.setStrHoldingCode("");
				mpModel.setStrJobProfileCode("");
				// Bill Address
				mpModel.setStrBillingAddressLine1("");
				mpModel.setStrBillingAddressLine2("");
				mpModel.setStrBillingAddressLine3("");
				mpModel.setStrBillingAreaCode("");
				mpModel.setStrBillingCountryCode("");
				mpModel.setStrBillingCtCode("");
				mpModel.setStrBillingEmailID("");
				mpModel.setStrBillingFax1("");
				mpModel.setStrBillingFax2("");
				mpModel.setStrBillingFlag("");
				mpModel.setStrBillingLandMark("");
				mpModel.setStrBillingMobileNo("");
				mpModel.setStrBillingPinCode("");
				mpModel.setStrBillingRegionCode("");
				mpModel.setStrBillingStateCode("");
				mpModel.setStrBillingTelePhone1("");
				mpModel.setStrBillingTelePhone2("");
				// Personal Information
				mpModel.setStrGender(obDM.getStrGender());
				mpModel.setDteDateofBirth(objGlobal.funGetDate("yyyy-MM-dd", obDM.getDteDependentDateofBirth()));
				//mpModel.setDteDateofBirth(obDM.getDteDependentDateofBirth());
				mpModel.setStrMaritalStatus(obDM.getStrMaritalStatus());
				mpModel.setStrProfessionCode(obDM.getStrProfessionCode());
				mpModel.setDteAnniversary("1990-01-01 00:00:00");
				// mpModel.setStrpName

				// Member Information
				mpModel.setStrCategoryCode("");
				mpModel.setStrProposerCode("");
				mpModel.setStrSeconderCode("");
				mpModel.setStrFatherMemberCode("");
				mpModel.setDteMembershipEndDate(objGlobal.funGetDate("yyyy-MM-dd", obDM.getDteMembershipExpiryDate()));
				mpModel.setDteMembershipStartDate(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteMembershipStartDate()));
				
				//mpModel.setDteMembershipStartDate(memProfileBean.getDteMembershipStartDate());
				//mpModel.setDteMembershipEndDate(obDM.getDteMembershipExpiryDate());

				// Card Authontication Check Box
				mpModel.setStrQualification("");
				mpModel.setStrDesignationCode("");
				mpModel.setDblEntranceFee(new Double(0));
				mpModel.setDblSubscriptionFee(new Double(0));
				mpModel.setStrPanNumber("");
				mpModel.setStrDepMobileNo("");
				mpModel.setStrDepEmailID("");
				mpModel.setStrDepAadharCardNo("");				
				mpModel.setStrAadharCardNo("");
				mpModel.setStrPassportNo("");
				mpModel.setStrVoterIdNo("");
				mpModel.setStrAccNo("");
				mpModel.setStrBranchName("");
				mpModel.setStrIfscCOde("");
				mpModel.setStrLocker("No");
				mpModel.setStrLibrary("No");
				mpModel.setStrSeniorCitizen("No");
				mpModel.setStrStopCredit("No");
				mpModel.setStrInstation("No");
				mpModel.setStrGolfMemberShip("No");
				mpModel.setStrBlocked(obDM.getStrBlocked());
				mpModel.setStrBlockedreasonCode(obDM.getStrDependentReasonCode());
				mpModel.setStrAlternateMemberCode("");
				mpModel.setStrAttachment("");
				mpModel.setDtePermitExpDate("1990-01-01 00:00:00");
				mpModel.setStrLiquorPermitNo("");
				mpModel.setIntFormNo(0);
				mpModel.setStrGuestEntry("Y");
				mpModel.setStrVirtualAccountCode("");
				mpModel.setChkmail(0);
				mpModel.setStrSSuffixCode("0");
				mpModel.setStrNSuffixCode("0");

				mpModel.setChrCircularemail("0");
				mpModel.setStrAuthorisedMember("");
				mpModel.setStrMemberStatusCode("");
				mpModel.setStrLikes("");
				mpModel.setStrDisLikes("");
				mpModel.setStrSendInvThrough("");
				mpModel.setStrSendCircularNoticeThrough("");
				mpModel.setDteInterviewDate("1990-01-01 00:00:00");
				mpModel.setDblCMSBalance(0.00);
				mpModel.setStrPhoto("");
				mpModel.setStrRemark("");
				mpModel.setStrDependentYesNo("N");
				mpModel.setStrSalesStaffCode("");
				mpModel.setDteProfileCreationDate("1990-01-01 00:00:00");
				mpModel.setStrResNonRes("Y");
				mpModel.setDteDependentDateofBirth("1990-01-01 00:00:00");
				mpModel.setDteMemberBlockDate("1990-01-01 00:00:00");
				mpModel.setDteMembershipExpiryDate(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteMembershipEndDate()));
				
				//mpModel.setDteMembershipExpiryDate(memProfileBean.getDteMembershipEndDate());
				mpModel.setStrDebtorCode("");
				mpModel.setStrDependentFullName("");
				mpModel.setStrDependentMemberCode("");
				mpModel.setStrDependentReasonCode("");

				mpModel.setStrCustomerID("");
				mpModel.setStrBillingFlag("N");
				mpModel.setStrMemberYesNo("");
				mpModel.setStrBankCode("");
				objMemberProfileService.funAddUpdateMemberProfile(mpModel);
			}

		}

	}

	private clsWebClubMemberProfileModel funPrepardSpouseModel(clsWebClubMemberProfileBean memProfileBean, clsWebClubMemberProfileModel objMemberProfile, HttpServletRequest req,MultipartFile file) throws IOException {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		objGlobal = new clsGlobalFunctions();
		long lastNo = 0;
		clsWebClubMemberProfileModel mpModel=null;
		
		
		
		//image code
		
		
		objGlobal = new clsGlobalFunctions();
		clsWebClubMemberPhotoModel objModel;
		objModel = new clsWebClubMemberPhotoModel(new clsWebClubMemberPhotoModel_ID(objMemberProfile.getStrMemberCode(), clientCode));

		objModel.setStrPropertyCode(propCode);
		objModel.setStrUserCreated(userCode);
		objModel.setStrUserModified(userCode);
		objModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteLastModified(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrMemberName(objMemberProfile.getStrFirstName());

		if (file.getSize() != 0) {
			System.out.println(file.getOriginalFilename());
			File imgFolder = new File(System.getProperty("user.dir") + "\\ProductIcon");
			if (!imgFolder.exists()) {
				if (imgFolder.mkdir()) {
					System.out.println("Directory is created! " + imgFolder.getAbsolutePath());
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			File fileImageIcon = new File(System.getProperty("user.dir") + "\\ProductIcon\\" + file.getOriginalFilename());
			String formatName = "jpg";
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(funInputStreamToBytearrayInputStrean(file.getInputStream()));
			String path = fileImageIcon.getPath().toString();
			ImageIO.write(bufferedImage, "jpg", new File(path));
			BufferedImage bfImg = scaleImage(150, 155, path);
			ImageIO.write(bfImg, "jpg", byteArrayOutputStream);
			byte[] imageBytes = byteArrayOutputStream.toByteArray();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

			//Blob blobProdImage = Hibernate.createBlob(byteArrayInputStream);
			//objModel.setStrMemberImage(blobProdImage);

			if (fileImageIcon.exists()) {
				fileImageIcon.delete();
				objModel.setStrMemberImage(imageBytes);
			}
			else {
				//objModel.setStrMemberImage(funBlankBlob());
			}
			objWebClubMemberPhotoService.funAddUpdateWebClubMemberPhoto(objModel);
		//image code end 
		
		
		
		
		
		
		
		
		

		if (memProfileBean.getStrSpouseCustomerCode() == null) {

			lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
			String customerCode = "C" + String.format("%06d", lastNo);
			mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(customerCode, clientCode));
			mpModel.setIntGId(lastNo);
			mpModel.setStrCustomerID("02");
			mpModel.setStrUserCreated(userCode);
			mpModel.setStrUserModified(userCode);
			mpModel.setStrPropertyCode(propCode);
			mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());

		} else {

			clsWebClubMemberProfileModel objMemProfile = objMemberProfileService.funGetCustomer(memProfileBean.getStrSpouseCustomerCode(), clientCode);
			if (null == objMemProfile) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblmembermaster", "MemberProfile", "intGId", clientCode);
				String customerCode = "C" + String.format("%06d", lastNo);
				mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(customerCode, clientCode));
				mpModel.setIntGId(lastNo);
				mpModel.setStrCustomerID("02");
				mpModel.setStrUserCreated(userCode);
				mpModel.setStrUserModified(userCode);
				mpModel.setStrPropertyCode(propCode);
				mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());
			} else {
				mpModel = new clsWebClubMemberProfileModel(new clsWebClubMemberProfileModel_ID(objMemProfile.getStrCustomerCode(), clientCode));
				mpModel.setStrUserModified(userCode);
				mpModel.setStrPropertyCode(propCode);
				mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				mpModel.setStrCustomerID("02");
				mpModel.setStrPrimaryCode(objMemberProfile.getStrCustomerCode());
			}
		}
		
		mpModel.setStrMemberCode(memProfileBean.getStrSpouseCode());

		mpModel.setStrUserCreated(userCode);
		mpModel.setStrUserModified(userCode);
		mpModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		mpModel.setDteModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		mpModel.setStrPropertyCode(propCode);
		mpModel.setDteMemberBlockDate("1990-01-01 00:00:00");
		mpModel.setStrClientCode(clientCode);
		mpModel.setStrDepedentRelation("");
		mpModel.setStrPrefixCode("");
		mpModel.setStrFirstName(memProfileBean.getStrSpouseFirstName());
		mpModel.setStrMiddleName(memProfileBean.getStrSpouseMiddleName());
		mpModel.setStrLastName(memProfileBean.getStrSpouseLastName());
		mpModel.setStrFullName(memProfileBean.getStrSpouseFirstName() + " " + memProfileBean.getStrSpouseMiddleName() + " " + memProfileBean.getStrSpouseLastName());
		mpModel.setStrNameOnCard(memProfileBean.getStrSpouseFirstName() + " " + memProfileBean.getStrSpouseMiddleName() + " " + memProfileBean.getStrSpouseLastName());
		// Residence Address
		mpModel.setStrResidentAddressLine1(memProfileBean.getStrResidentAddressLine1());
		mpModel.setStrResidentAddressLine2(memProfileBean.getStrResidentAddressLine2());
		mpModel.setStrResidentAddressLine3(memProfileBean.getStrResidentAddressLine3());
		mpModel.setStrResidentAreaCode(memProfileBean.getStrResidentAreaCode());
		mpModel.setStrResidentCountryCode(memProfileBean.getStrResidentCountryCode());
		mpModel.setStrResidentCtCode(memProfileBean.getStrResidentCtCode());
		mpModel.setStrResidentEmailID(memProfileBean.getStrSpouseResidentMobileNo());
		mpModel.setStrResidentFax1(memProfileBean.getStrResidentFax1());
		mpModel.setStrResidentFax2(memProfileBean.getStrResidentFax2());
		mpModel.setStrResidentLandMark(memProfileBean.getStrResidentLandMark());
		mpModel.setStrResidentMobileNo(memProfileBean.getStrSpouseResidentMobileNo());
		mpModel.setStrResidentPinCode(memProfileBean.getStrResidentPinCode());
		mpModel.setStrResidentRegionCode(memProfileBean.getStrResidentRegionCode());
		mpModel.setStrResidentStateCode(memProfileBean.getStrResidentStateCode());
		mpModel.setStrResidentTelephone1(memProfileBean.getStrResidentTelephone1());
		mpModel.setStrResidentTelephone2(memProfileBean.getStrResidentTelephone2());
		// Company Address
		mpModel.setStrCompanyAddressLine1("");
		mpModel.setStrCompanyAddressLine2("");
		mpModel.setStrCompanyAddressLine3("");
		mpModel.setStrCompanyAreaCode("");
		mpModel.setStrCompanyCode(memProfileBean.getStrSpouseCompanyCode());
		mpModel.setStrCompanyCountryCode("");
		mpModel.setStrCompanyCtCode("");
		mpModel.setStrCompanyEmailID("");
		mpModel.setStrCompanyFax1("");
		mpModel.setStrCompanyFax2("");
		mpModel.setStrCompanyLandMark("");
		mpModel.setStrCompanyMobileNo("");
		mpModel.setStrCompanyName("");
		mpModel.setStrCompanyPinCode("");
		mpModel.setStrCompanyRegionCode("");
		mpModel.setStrCompanyStateCode("");
		mpModel.setStrCompanyTelePhone1("");
		mpModel.setStrCompanyTelePhone2("");
		mpModel.setStrHoldingCode("");
		mpModel.setStrJobProfileCode(memProfileBean.getStrSpouseJobProfileCode());
		// Bill Address
		mpModel.setStrBillingAddressLine1("");
		mpModel.setStrBillingAddressLine2("");
		mpModel.setStrBillingAddressLine3("");
		mpModel.setStrBillingAreaCode("");
		mpModel.setStrBillingCountryCode("");
		mpModel.setStrBillingCtCode("");
		mpModel.setStrBillingEmailID("");
		mpModel.setStrBillingFax1("");
		mpModel.setStrBillingFax2("");
		mpModel.setStrBillingFlag("");
		mpModel.setStrBillingLandMark("");
		mpModel.setStrBillingMobileNo("");
		mpModel.setStrBillingPinCode("");
		mpModel.setStrBillingRegionCode("");
		mpModel.setStrBillingStateCode("");
		mpModel.setStrBillingTelePhone1("");
		mpModel.setStrBillingTelePhone2("");
		// Personal Information
		mpModel.setStrGender("F");
		mpModel.setDteDateofBirth(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteSpouseDateofBirth()));
		
		//mpModel.setDteDateofBirth(memProfileBean.getDteSpouseDateofBirth());
		mpModel.setStrMaritalStatus("married");
		mpModel.setStrProfessionCode(memProfileBean.getStrSpouseProfessionCode());
		mpModel.setDteAnniversary(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteAnniversary()));
		
		//mpModel.setDteAnniversary(memProfileBean.getDteAnniversary());
		// mpModel.setStrpName

		// Member Information
		mpModel.setStrCategoryCode("");
		mpModel.setStrProposerCode("");
		mpModel.setStrSeconderCode("");
		mpModel.setStrFatherMemberCode("");
		mpModel.setDteMembershipStartDate(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteMembershipStartDate()));
		
		//mpModel.setDteMembershipStartDate(memProfileBean.getDteMembershipStartDate());
		// mpModel.setDteMembershipEndDate(memProfileBean.getDteMembershipExpiryDate());
		mpModel.setDteMembershipEndDate(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteMembershipEndDate()));
		
		//mpModel.setDteMembershipEndDate(memProfileBean.getDteMembershipEndDate());
		mpModel.setStrInstation("");

		// Card Authontication Check Box
		mpModel.setStrQualification("");
		mpModel.setStrDesignationCode("");
		mpModel.setDblEntranceFee(new Double(0));
		mpModel.setDblSubscriptionFee(new Double(0));
		mpModel.setStrPanNumber("");
		mpModel.setStrDepMobileNo("");
		mpModel.setStrDepEmailID("");
		mpModel.setStrDepAadharCardNo("");
		mpModel.setStrAadharCardNo("");
		mpModel.setStrPassportNo("");
		mpModel.setStrVoterIdNo("");
		mpModel.setStrAccNo("");
		mpModel.setStrBranchName("");
		mpModel.setStrIfscCOde("");

		mpModel.setStrStopCredit(memProfileBean.getStrSpouseStopCredit());
		mpModel.setStrLocker("No");
		mpModel.setStrLibrary("No");
		mpModel.setStrSeniorCitizen("No");

		mpModel.setStrInstation("No");
		mpModel.setStrGolfMemberShip("No");
		mpModel.setStrBlocked(memProfileBean.getStrSpouseBlocked());
		mpModel.setStrBlockedreasonCode("");

		mpModel.setDtePermitExpDate("1990-01-01 00:00:00");
		mpModel.setStrLiquorPermitNo("");
		mpModel.setIntFormNo(0);
		mpModel.setStrGuestEntry("Y");
		mpModel.setStrVirtualAccountCode("");
		mpModel.setChkmail(0);
		mpModel.setStrSSuffixCode("0");
		mpModel.setStrNSuffixCode("0");
		mpModel.setStrStopCredit(memProfileBean.getStrStopCredit());
		mpModel.setChrCircularemail("0");
		mpModel.setStrAuthorisedMember("");
		mpModel.setStrMemberStatusCode("");
		mpModel.setStrLikes("");
		mpModel.setStrDisLikes("");
		mpModel.setStrSendInvThrough("");
		mpModel.setStrSendCircularNoticeThrough("");
		mpModel.setDteInterviewDate("1990-01-01 00:00:00");
		mpModel.setDblCMSBalance(0.00);
		mpModel.setStrPhoto("");
		mpModel.setStrRemark("");
		mpModel.setStrDependentYesNo("N");
		mpModel.setStrSalesStaffCode("");
		mpModel.setDteProfileCreationDate("1990-01-01 00:00:00");
		mpModel.setStrResNonRes("Y");
		mpModel.setDteDependentDateofBirth("1990-01-01 00:00:00");
		mpModel.setDteMemberBlockDate("1990-01-01 00:00:00");
		mpModel.setDteMembershipExpiryDate(objGlobal.funGetDate("yyyy-mm-dd", memProfileBean.getDteMembershipEndDate()));
		
		//mpModel.setDteMembershipExpiryDate(memProfileBean.getDteMembershipEndDate());
		mpModel.setStrDebtorCode("");
		mpModel.setStrDependentFullName("");
		mpModel.setStrDependentMemberCode("");
		mpModel.setStrDependentReasonCode("");
		mpModel.setStrAlternateMemberCode("");
		mpModel.setStrAttachment("");
		mpModel.setStrCustomerID("");
		mpModel.setStrBillingFlag("N");
		mpModel.setStrMemberYesNo("");
		mpModel.setStrBankCode("");
		return mpModel;
	}
		return mpModel;
	}
	
	private Blob funBlankBlob() {
		Blob blob = new Blob() {

			@Override
			public void truncate(long len) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int setBytes(long pos, byte[] bytes) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public OutputStream setBinaryStream(long pos) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long position(Blob pattern, long start) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long position(byte[] pattern, long start) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long length() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public byte[] getBytes(long pos, int length) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getBinaryStream(long pos, long length) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getBinaryStream() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void free() throws SQLException {
				// TODO Auto-generated method stub

			}
		};
		return blob;
	}


	public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
		BufferedImage bi = null;
		try {
			ImageIcon ii = new ImageIcon(filename);// path to image
			bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D gra2d = (Graphics2D) bi.createGraphics();
			gra2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			gra2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bi;
	}

	@SuppressWarnings("finally")
	private ByteArrayInputStream funInputStreamToBytearrayInputStrean(InputStream ins) {
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			byte[] buff = new byte[8000];

			int bytesRead = 0;

			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			while ((bytesRead = ins.read(buff)) != -1) {
				bao.write(buff, 0, bytesRead);
			}

			byte[] data = bao.toByteArray();

			byteArrayInputStream = new ByteArrayInputStream(data);

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			return byteArrayInputStream;
		}
	}

	@RequestMapping(value = "/loadWebClubMemberProfileData", method = RequestMethod.GET)
	public @ResponseBody List funAssignFields(@RequestParam("primaryCode") String primaryCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsWebClubMemberProfileModel objbean=null;
		objGlobal=new clsGlobalFunctions();
		List<clsWebClubMemberProfileModel> objMemberModelList = objMemberProfileService.funGetAllMember(primaryCode, clientCode);
		List<clsWebClubMemberProfileModel> finalList = new ArrayList<>();
		if (null != objMemberModelList) {
			for(int i=0;i<objMemberModelList.size();i++)
			{
				if(objMemberModelList.get(i).getStrMemberCode()!=null && objMemberModelList.size()>0 && !objMemberModelList.get(i).getStrMemberCode().equals(""))
				{
					objbean=new clsWebClubMemberProfileModel();
					objbean=objMemberModelList.get(i);				
					objbean.setDteAnniversary(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteAnniversary().split(" ")[0]));
					objbean.setDteDateofBirth(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteDateofBirth().split(" ")[0]));					
					objbean.setDteDependentDateofBirth(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteDependentDateofBirth().split(" ")[0]));
					objbean.setDteInterviewDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteInterviewDate().split(" ")[0]));
					objbean.setDteMemberBlockDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteMemberBlockDate().split(" ")[0]));
					objbean.setDteMembershipEndDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteMembershipEndDate().split(" ")[0]));
					objbean.setDteMembershipExpiryDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteMembershipExpiryDate().split(" ")[0]));
					objbean.setDteMembershipStartDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteMembershipStartDate().split(" ")[0]));
					objbean.setDtePermitExpDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDtePermitExpDate().split(" ")[0]));
					objbean.setDteProfileCreationDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteProfileCreationDate().split(" ")[0]));
									
					finalList.add(objbean);
				}
			}
		}
		return finalList;
	}
	
	
	@RequestMapping(value = "/loadWebClubMemberPre-ProfileData", method = RequestMethod.GET)
	public @ResponseBody List funAssignPre_profileMemberData(@RequestParam("primaryCode") String primaryCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsWebClubPreMemberProfileModel objbean=null;
		objGlobal=new clsGlobalFunctions();
		List<clsWebClubPreMemberProfileModel> objMemberModelList = objMemberProfileService.funGetAllMemberPreProfile(primaryCode, clientCode);
		List<clsWebClubPreMemberProfileModel> finalList = new ArrayList<>();
		if (null != objMemberModelList) {
			for(int i=0;i<objMemberModelList.size();i++)
			{
				if(objMemberModelList.get(i).getStrMemberCode()!=null && objMemberModelList.size()>0 && !objMemberModelList.get(i).getStrMemberCode().equals(""))
				{
					objbean=new clsWebClubPreMemberProfileModel();
					objbean=objMemberModelList.get(i);	
					objbean.setDteDateofBirth(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteDateofBirth().split(" ")[0]));
					objbean.setDteDependentDateofBirth(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteDependentDateofBirth().split(" ")[0]));
					//objbean.setDteDependentMemExpDate(objGlobal.funGetDate("dd-MM-yyyy",objMemberModelList.get(i).getDteDependentDateofBirth().split(" ")[0]));)
					finalList.add(objbean);
				}
			}
		}
		return finalList;
	}
	
	@RequestMapping(value = "/loadWebClubMemberWiseFieldData", method = RequestMethod.GET)
	public @ResponseBody Map funMemberWiseFieldData(@RequestParam("primaryCode") String MemberCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		//List<clsWebClubMemberProfileModel> objMemberModelList = objMemberProfileService.funGetAllMember(primaryCode, clientCode);
		
		List list=objGlobalFunctionsService.funGetListModuleWise("SELECT a.strMemberCode FROM tblmembermaster a WHERE a.strCustomerCode='"+MemberCode+"' AND a.strClientCode='"+clientCode+"'","sql") ;
		Map hm = new LinkedHashMap<String,List>();		
		List listField=objGlobalFunctionsService.funGetListModuleWise("SELECT * FROM tblotherdtl a WHERE a.strMemberCode='"+list.get(0).toString()+"' ","sql") ;
		Map hmap=funDataBaseShrink();
		if(listField!=null||listField.size()>0)
		{
			for(int i=0;i<listField.size();i++)
			{
				int k=1;
				Iterator<Map.Entry<String, String>> itr = hmap.entrySet().iterator(); 
				while(itr.hasNext()) 
			    { 	
					Object obj [] = (Object[]) listField.get(i);
					Map.Entry<String, String> entry = itr.next(); 
					if(!entry.getKey().equalsIgnoreCase("strMemberCode")&&!entry.getKey().equalsIgnoreCase("strClientCode"))
					{	
						List list1 = new ArrayList<>();						
						list1.add(entry.getValue());
						list1.add(obj[k+1].toString());						
						hm.put(entry.getKey(),list1);
						k++;
					}
			    }	
			}
			
		}
		return hm;
		
		
	}
	
	
	@RequestMapping(value = "/loadWebClubMemberProfileCustomerData", method = RequestMethod.GET)
	public @ResponseBody clsWebClubMemberProfileModel funAssignFieldsUsingCustomer(@RequestParam("customerCode") String customerCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsWebClubMemberProfileModel objMemberModel = objMemberProfileService.funGetCustomer(customerCode, clientCode);
		if (null == objMemberModel) {
			objMemberModel = new clsWebClubMemberProfileModel();
			objMemberModel.setStrMemberCode("Invalid Code");
		}
		return objMemberModel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadDependentMasterData", method = RequestMethod.GET)
	public @ResponseBody List funLoadDependentMasterData(@RequestParam("docCode") String docCode, HttpServletRequest req) {
		List<clsWebClubDependentMasterModel> listDMData = new LinkedList<clsWebClubDependentMasterModel>();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		listDMData = objDependentMasterService.funGetWebClubDependentMasterList(docCode, clientCode);

		return listDMData;
	}

	@RequestMapping(value = "/loadWebClubMemberFieldListData", method = RequestMethod.GET)
	public @ResponseBody Map funAssignFieldData(HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		List<clsWebClubMemberProfileModel> finalList = new ArrayList<>();
		Map hmap=funDataBaseShrink();		
		return hmap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadFieldsMemberWise", method = RequestMethod.GET)
	public @ResponseBody Map funGetFieldsMemberWise(@RequestParam("memCode") String memCode, HttpServletRequest req) {
 		objGlobal=new clsGlobalFunctions();
		List<clsWebClubDependentMasterModel> listDMData = new LinkedList<clsWebClubDependentMasterModel>();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		Map hm = new HashMap<String,String>();
		String [] str=memCode.split(" ");
		List list=objGlobalFunctionsService.funGetListModuleWise("select * from tblotherdtl a where a.strMemberCode='"+memCode+"' ","sql") ;
		Map hmap=funDataBaseShrink();
		if(list!=null||list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				int k=0;
				Iterator<Map.Entry<String, String>> itr = hmap.entrySet().iterator(); 
				while(itr.hasNext()) 
			    { 	
					Object obj [] = (Object[]) list.get(i);
					Map.Entry<String, String> entry = itr.next(); 
					if(!entry.getKey().equalsIgnoreCase("strMemberCode"))
					{
						hm.put(entry.getKey(),obj[k+1].toString());
						k++;
					}
					
			        //System.out.println("Key = " + entry.getKey() +  ", Value = " + entry.getValue()); 
			    
			        
			    }	
			}
			
		}
		 
		
		return hm;
	}
	
	//loadMembProfileImage
	@RequestMapping(value = "/loadMembProfileImage", method = RequestMethod.GET)
	public void getImage(@RequestParam("prodCode") String prodCode, HttpServletRequest req, HttpServletResponse response) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsWebClubMemberPhotoModel objmemPhotoModel = null;
		if (prodCode.length() > 8) {
			objmemPhotoModel = objWebClubMemberPhotoService.funGetWebClubMemberPhoto(prodCode, clientCode);
		} else {
			objmemPhotoModel = objWebClubMemberPhotoService.funGetWebClubMemberPhoto(prodCode, clientCode);
		}

		try {
			//Blob image = null;
			byte[] imgData = null;
		//	image = objModel.getStrProductImage();
			//if (null != image && image.length() > 0) {
				imgData =objmemPhotoModel.getStrMemberImage();
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				OutputStream o = response.getOutputStream();
				o.write(imgData);
				o.flush();
				o.close();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// //Open MemberProfile
	// @RequestMapping(value = "/saveMemberPreProfile", method =
	// RequestMethod.GET)
	// public ModelAndView funOpenFormMenberProfile(Map<String,Object> model
	// ,HttpServletRequest request){
	// String urlHits="1";
	// try{
	// urlHits=request.getParameter("saddr").toString();
	// }catch(NullPointerException e){
	// urlHits="1";
	// }
	// model.put("urlHits",urlHits);
	//
	// if("2".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmMemberProfile_1","command", new
	// clsWebClubMemberProfileBean());
	// }else if("1".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmMemberProfile","command", new
	// clsWebClubMemberProfileBean());
	// }else {
	// return null;
	// }
	//
	// }
	//

}
