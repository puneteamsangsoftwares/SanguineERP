package com.sanguine.webpms.controller;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsBillPrintingBean;
import com.sanguine.webpms.bean.clsCheckInDetailsBean;
import com.sanguine.webpms.bean.clsPMSMergeBillBean;
import com.sanguine.webpms.bean.clsReservationBean;
import com.sanguine.webpms.bean.clsUpdateHouseKeepingStatusBean;
import com.sanguine.webpms.model.clsBillDtlBackupModel;
import com.sanguine.webpms.model.clsBillDtlModel;
import com.sanguine.webpms.model.clsBillHdBackupModel;
import com.sanguine.webpms.model.clsBillHdModel;
import com.sanguine.webpms.model.clsBillTaxDtlBackupModel;
import com.sanguine.webpms.model.clsBillTaxDtlModel;
import com.sanguine.webpms.model.clsCheckInDtl;
import com.sanguine.webpms.model.clsPropertySetupHdModel;
import com.sanguine.webpms.service.clsBillService;
import com.sanguine.webpms.service.clsVoidBillService;
@Controller
public class clsMergeBillController {
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@Autowired
	private clsBillService objBillService;
	
	
	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@RequestMapping(value = "/frmMergeBill", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model,
			HttpServletRequest request) {
		String urlHits = "1";
		String strBillNo="";
		
		if(request.getParameter("docCode")==null)
		{
			strBillNo="";
			request.getSession().setAttribute("BillNo", strBillNo);
		}
		else
		{
			strBillNo = request.getParameter("docCode").toString();
			request.getSession().setAttribute("BillNo", strBillNo);
		}
		
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		/*if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMergeBill_1", "command",new clsBillPrintingBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMergeBill", "command",new clsBillPrintingBean());
		} else {
			return null;
		}*/
		return  new ModelAndView("frmMergeBill", "command",new clsPMSMergeBillBean());
	}
	@RequestMapping(value = "/loadBillDataForMergeBill", method = RequestMethod.GET)
	public @ResponseBody List loadRoomLimit(HttpServletRequest req) throws ParseException {
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String strBillNo = req.getParameter("strBillNo").toString();
		List retList = new ArrayList<>();
		
	
		String sqlData= "select a.strBillNo,a.strFolioNo,a.strCheckInNo,a.dblGrandTotal "
				+ "from tblbillhd a where a.strBillNo='"+strBillNo+"' and a.strClientCode='"+clientCode+"'";
		List listData = objGlobalFunctionsService.funGetListModuleWise(sqlData, "sql");

	
		
		return listData;
		
	}
	
	
	@RequestMapping(value = "/saveMergeBill", method = RequestMethod.POST)
	public ModelAndView funsaveMergeBill(@ModelAttribute("command") @Valid clsPMSMergeBillBean objBean, BindingResult result, HttpServletRequest req) {
		List listRooms = new ArrayList<>();
		List listHouseKeeping = new ArrayList<>();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String strPMSDate = req.getSession().getAttribute("PMSDate").toString();
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCurrTime = time.format(formatter);
		if (!result.hasErrors()) 
		{
			clsBillHdModel objMergeBillHdModel = new clsBillHdModel();
			
			List<clsBillDtlModel> listMergeBillDtl = new ArrayList<clsBillDtlModel>();
			
			List<clsBillTaxDtlModel> listMergeBillTaxDtlModels = new ArrayList<clsBillTaxDtlModel>();
			
			double dblSumGrandTotal = 0.0;
			String strCheckInNo = "";
			String strFolioNo =  "";
			String strRoomNo = "";
			String strRegistrationNo = "";
			String strExtraBedCode = "";
			String strBillNo ="";
			String transaDate = objGlobal.funGetCurrentDateTime("dd-MM-yyyy").split(" ")[0];
			strBillNo = objGlobal.funGeneratePMSDocumentCode("frmBillHd", transaDate, req);

			
			
			List listTemp = objBean.getListMergeBill();
			for(int i=0;i<listTemp.size();i++)
			{
				clsPMSMergeBillBean objTempBean = (clsPMSMergeBillBean) listTemp.get(i);
				
				clsBillHdModel objModel = objBillService.funLoadBill(objTempBean.getStrBillNo(), clientCode);
				
				dblSumGrandTotal = dblSumGrandTotal+objModel.getDblGrandTotal();
				
				clsBillHdBackupModel objBackupModel = funPrepareModel(objModel,clientCode);
				
				objBillService.funAddUpdateBillHdBackup(objBackupModel);
				
				//ForMerge Bill
				
				
				objMergeBillHdModel.setDteBillDate(objModel.getDteBillDate());
				objMergeBillHdModel.setDteDateCreated(objModel.getDteDateCreated());
				objMergeBillHdModel.setDteDateEdited(objModel.getDteDateEdited());
				objMergeBillHdModel.setStrBillSettled(objModel.getStrBillSettled());
				//objMergeBillHdModel.setStrCheckInNo(objModel.getStrCheckInNo());
				strCheckInNo = strCheckInNo+","+objModel.getStrCheckInNo();
				objMergeBillHdModel.setStrClientCode(objModel.getStrClientCode());
				objMergeBillHdModel.setStrCompanyName(objModel.getStrCompanyName());
				//objMergeBillHdModel.setStrExtraBedCode(strExtraBedCode);
				if(!objModel.getStrExtraBedCode().equals(""))
				{
					strExtraBedCode = strExtraBedCode+","+objModel.getStrExtraBedCode();	
				}
				
				//objMergeBillHdModel.setStrFolioNo(strFolioNo);
				strFolioNo = strFolioNo+","+objModel.getStrFolioNo();
				objMergeBillHdModel.setStrGSTNo(objModel.getStrGSTNo());
				//objMergeBillHdModel.setStrRegistrationNo(strRegistrationNo);;
				strRegistrationNo = strRegistrationNo+","+objModel.getStrRegistrationNo();
				objMergeBillHdModel.setStrReservationNo(objModel.getStrReservationNo());
				//objMergeBillHdModel.setStrRoomNo(strRoomNo);
				strRoomNo = strRoomNo+","+objModel.getStrRoomNo();
				objMergeBillHdModel.setStrSplitType(objModel.getStrSplitType());
				objMergeBillHdModel.setStrUserCreated(objModel.getStrUserCreated());
				objMergeBillHdModel.setStrUserEdited(objModel.getStrUserEdited());
				
				//For Merge Bill Dtl and tax dtl list
				
				for (clsBillDtlModel objBillDetails : objModel.getListBillDtlModels())
				{
					clsBillDtlModel objDtlModel = new clsBillDtlModel();
					objDtlModel.setDblBalanceAmt(objBillDetails.getDblBalanceAmt());
					objDtlModel.setDblCreditAmt(objBillDetails.getDblCreditAmt());
					objDtlModel.setDblDebitAmt(objBillDetails.getDblDebitAmt());
					objDtlModel.setDteDateEdited(objBillDetails.getDteDateEdited());
					objDtlModel.setDteDocDate(objBillDetails.getDteDocDate());
					objDtlModel.setStrDocNo(objBillDetails.getStrDocNo());
					objDtlModel.setStrFolioNo(objBillDetails.getStrFolioNo());
					objDtlModel.setStrPerticulars(objBillDetails.getStrPerticulars());
					objDtlModel.setStrRevenueCode(objBillDetails.getStrRevenueCode());
					objDtlModel.setStrRevenueType(objBillDetails.getStrRevenueType());
					objDtlModel.setStrTransactionType(objBillDetails.getStrTransactionType());
					objDtlModel.setStrUserEdited(objBillDetails.getStrUserEdited());
					listMergeBillDtl.add(objDtlModel);
				}
				
				for (clsBillTaxDtlModel objBillDetails : objModel.getListBillTaxDtlModels())
				{
					clsBillTaxDtlModel objTaxDtlModel = new clsBillTaxDtlModel();

					objTaxDtlModel.setDblTaxableAmt(objBillDetails.getDblTaxableAmt());
					objTaxDtlModel.setDblTaxAmt(objBillDetails.getDblTaxAmt());
					objTaxDtlModel.setStrDocNo(objBillDetails.getStrDocNo());
					objTaxDtlModel.setStrTaxCode(objBillDetails.getStrTaxCode());
					objTaxDtlModel.setStrTaxDesc(objBillDetails.getStrTaxDesc());
					
					listMergeBillTaxDtlModels.add(objTaxDtlModel);
				}
				
				
				objMergeBillHdModel.setListBillDtlModels(listMergeBillDtl);
				objMergeBillHdModel.setListBillTaxDtlModels(listMergeBillTaxDtlModels);
						
			}
			
			objMergeBillHdModel.setStrBillNo(strBillNo);
			objMergeBillHdModel.setDblGrandTotal(dblSumGrandTotal);
			objMergeBillHdModel.setStrCheckInNo(strCheckInNo.substring(1));
			objMergeBillHdModel.setStrExtraBedCode(strExtraBedCode);
			objMergeBillHdModel.setStrFolioNo(strFolioNo.substring(1));
			objMergeBillHdModel.setStrRegistrationNo(strRegistrationNo.substring(1));;
			objMergeBillHdModel.setStrRoomNo(strRoomNo.substring(1));
			
			objBillService.funAddUpdateBillHd(objMergeBillHdModel);
		} 
		else {
			return new ModelAndView("redirect:/frmMergeBill.html?saddr=" + 1);
		}
		return new ModelAndView("redirect:/frmMergeBill.html?saddr=" + 1);
	}
	private clsBillHdBackupModel funPrepareModel(clsBillHdModel objModel,String clientCode)
	{
		
		clsBillHdBackupModel objBackupModel = new clsBillHdBackupModel();
		
		objBackupModel.setDblGrandTotal(objModel.getDblGrandTotal());
		objBackupModel.setDteBillDate(objModel.getDteBillDate());
		objBackupModel.setDteDateCreated(objModel.getDteDateCreated());
		objBackupModel.setDteDateEdited(objModel.getDteDateEdited());
		objBackupModel.setStrBillNo(objModel.getStrBillNo());
		objBackupModel.setStrBillSettled(objModel.getStrBillSettled());
		objBackupModel.setStrCheckInNo(objModel.getStrCheckInNo());
		objBackupModel.setStrClientCode(objModel.getStrClientCode());
		objBackupModel.setStrCompanyName(objModel.getStrCompanyName());
		objBackupModel.setStrExtraBedCode(objModel.getStrExtraBedCode());
		objBackupModel.setStrFolioNo(objModel.getStrFolioNo());
		objBackupModel.setStrGSTNo(objModel.getStrGSTNo());
		objBackupModel.setStrRegistrationNo(objModel.getStrRegistrationNo());
		objBackupModel.setStrReservationNo(objModel.getStrReservationNo());
		objBackupModel.setStrRoomNo(objModel.getStrRoomNo());
		objBackupModel.setStrSplitType(objModel.getStrSplitType());
		objBackupModel.setStrUserCreated(objModel.getStrUserCreated());
		objBackupModel.setStrUserEdited(objModel.getStrUserEdited());
		
		
		List<clsBillDtlBackupModel> listBillDtlModel = new ArrayList<clsBillDtlBackupModel>();
		for (clsBillDtlModel objBillDetails : objModel.getListBillDtlModels())
		{
			clsBillDtlBackupModel objDtlModel = new clsBillDtlBackupModel();
			objDtlModel.setDblBalanceAmt(objBillDetails.getDblBalanceAmt());
			objDtlModel.setDblCreditAmt(objBillDetails.getDblCreditAmt());
			objDtlModel.setDblDebitAmt(objBillDetails.getDblDebitAmt());
			objDtlModel.setDteDateEdited(objBillDetails.getDteDateEdited());
			objDtlModel.setDteDocDate(objBillDetails.getDteDocDate());
			objDtlModel.setStrDocNo(objBillDetails.getStrDocNo());
			objDtlModel.setStrFolioNo(objBillDetails.getStrFolioNo());
			objDtlModel.setStrPerticulars(objBillDetails.getStrPerticulars());
			objDtlModel.setStrRevenueCode(objBillDetails.getStrRevenueCode());
			objDtlModel.setStrRevenueType(objBillDetails.getStrRevenueType());
			objDtlModel.setStrTransactionType(objBillDetails.getStrTransactionType());
			objDtlModel.setStrUserEdited(objBillDetails.getStrUserEdited());
			listBillDtlModel.add(objDtlModel);
		}
		
		List<clsBillTaxDtlBackupModel> listBillTaxDtlBackupModel = new ArrayList<clsBillTaxDtlBackupModel>();
		for (clsBillTaxDtlModel objBillDetails : objModel.getListBillTaxDtlModels())
		{
			clsBillTaxDtlBackupModel objTaxDtlModel = new clsBillTaxDtlBackupModel();

			objTaxDtlModel.setDblTaxableAmt(objBillDetails.getDblTaxableAmt());
			objTaxDtlModel.setDblTaxAmt(objBillDetails.getDblTaxAmt());
			objTaxDtlModel.setStrDocNo(objBillDetails.getStrDocNo());
			objTaxDtlModel.setStrTaxCode(objBillDetails.getStrTaxCode());
			objTaxDtlModel.setStrTaxDesc(objBillDetails.getStrTaxDesc());
			
			listBillTaxDtlBackupModel.add(objTaxDtlModel);
		}
		objBackupModel.setListBillDtlModels(listBillDtlModel);
		objBackupModel.setListBillTaxDtlModels(listBillTaxDtlBackupModel);
		
		
		return objBackupModel;
	}
	
	
	
}
