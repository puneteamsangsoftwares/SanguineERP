package com.sanguine.webbooks.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbooks.bean.clsLetterProcessingBean;
import com.sanguine.webbooks.bean.clsReceiptBean;
import com.sanguine.webbooks.bean.clsReceiptDetailBean;
import com.sanguine.webbooks.model.clsEmployeeMasterModel;
import com.sanguine.webbooks.model.clsReceiptDebtorDtlModel;
import com.sanguine.webbooks.model.clsReceiptDtlModel;
import com.sanguine.webbooks.model.clsReceiptHdModel;
import com.sanguine.webbooks.model.clsReceiptInvDtlModel;
import com.sanguine.webbooks.model.clsSundaryCreditorMasterModel;
import com.sanguine.webbooks.model.clsSundryDebtorMasterModel;
import com.sanguine.webbooks.service.clsReceiptService;
import com.sanguine.webbooks.service.clsSundryCreditorMasterService;
import com.sanguine.webbooks.service.clsSundryDebtorMasterService;
import com.sanguine.webclub.bean.clsWebClubPDCFlashBean;

@Controller
public class clsPDCToReceiptController {
	
	@Autowired
	clsGlobalFunctionsService objGlobalService;
	
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	
	@Autowired
	private clsReceiptService objReceiptService;

	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsSundryDebtorMasterService objSundryDebtorMasterService;
	@Autowired
	private clsSundryCreditorMasterService objSundryCreditorMasterService;
	@Autowired
	clsEmployeeMasterController objEmployeeMasterController;
	
	@RequestMapping(value = "/frmPDCToReceipt", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}

		
		

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmPDCToReceipt", "command", new clsReceiptBean());
		} else {
			return new ModelAndView("frmPDCToReceipt_1", "command", new clsReceiptBean());
		}
	}
	
	@RequestMapping(value = "/savePDCToReceipt", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsReceiptBean objBean, BindingResult result, HttpServletRequest req) {
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propCode = req.getSession().getAttribute("propertyCode").toString();
			String startDate = req.getSession().getAttribute("startDate").toString();
			String strVoucherNo = "";
			
			for(clsReceiptBean i:objBean.getListReceiptBean()){
				if(i.getStrTransMode()!=null)
				{
					clsReceiptHdModel objHdModel = funPrepareHdModel(i, userCode, clientCode, propCode, req);
					objReceiptService.funAddUpdateReceiptHd(objHdModel);
					strVoucherNo = strVoucherNo+objHdModel.getStrVouchNo()+" ";
					req.getSession().setAttribute("success", true);
					req.getSession().setAttribute("successMessage", "Receipt No : ".concat(objHdModel.getStrVouchNo()));
					req.getSession().setAttribute("rptVoucherNo",strVoucherNo );
				}
			}
			return new ModelAndView("redirect:/frmPDCToReceipt.html");
		} else {
			return new ModelAndView("frmPDCToReceipt");
		}
	}
	
	public clsReceiptHdModel funPrepareHdModel(clsReceiptBean objBean, String userCode, String clientCode, String propertyCode, HttpServletRequest request) {

		clsReceiptHdModel objModel = new clsReceiptHdModel();
//		String strCurr = request.getSession().getAttribute("currValue").toString();
		
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
//		double currConversion = 1.0;
//		if (objSetup != null) {
//			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(objBean.getStrCurrency(), clientCode);
//			if (objCurrModel != null) {
//				currConversion = objCurrModel.getDblConvToBaseCurr();
//
//			}
//
//		}
		double currConversion = 1.0;
		if(objBean.getDblConversion()>0)
		{
			currConversion=objBean.getDblConversion();
		}

		
		Map<String, clsReceiptDtlModel> hmReceiptDtl = new HashMap<String, clsReceiptDtlModel>();
		
			String documentNo = objGlobal.funGenerateDocumentCodeWebBook("frmReceipt", objBean.getDteChequeDate(), request);
			objModel.setStrVouchNo(documentNo);
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		

		objModel.setStrCFCode(objBean.getStrCFCode());
		objModel.setStrType(objGlobal.funIfNull(objBean.getStrType(), "None", objBean.getStrType()));
		//objModel.setStrDebtorCode(objGlobal.funIfNull(objBean.getStrDebtorCode(), "", objBean.getStrDebtorCode()));
		objModel.setStrReceivedFrom(objGlobal.funIfNull(objBean.getStrReceivedFrom(), "", objBean.getStrReceivedFrom()));
		objModel.setStrChequeNo(objBean.getStrChequeNo());
		objModel.setStrDrawnOn(objGlobal.funIfNull(objBean.getStrDrawnOn(), "", objBean.getStrDrawnOn()));
		objModel.setStrBranch(objGlobal.funIfNull(objBean.getStrBranch(), "", objBean.getStrBranch()));
		objModel.setStrNarration(objGlobal.funIfNull(objBean.getStrNarration(), "NA", objBean.getStrNarration()));
		objModel.setStrSancCode(objGlobal.funIfNull(objBean.getStrSancCode(), "NA", objBean.getStrSancCode()));
		objModel.setDblAmt(objBean.getDblAmt() * currConversion);
		objModel.setStrCrDr(objBean.getStrCrDr());
		objModel.setDteVouchDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteChequeDate(objGlobal.funGetDate("yyyy-MM-dd", objGlobal.funIfNull(objBean.getDteChequeDate(),objGlobal.funGetCurrentDateTime("yyyy-MM-dd"),objBean.getDteChequeDate())));
		objModel.setIntVouchMonth(objGlobal.funGetMonth());
		objModel.setIntVouchNum(0);
		objModel.setStrTransMode("R");
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrDebtorCode(objBean.getStrDebtorCode());
	
			objModel.setStrReceiptType("MR");
		
		
		if (request.getSession().getAttribute("selectedModuleName").toString().equalsIgnoreCase("8-WebBookAPGL")) {
			objModel.setStrModuleType("APGL");

		} else {
			objModel.setStrModuleType("AP");
		}

		objModel.setStrPropertyCode(propertyCode);
		objModel.setStrClientCode(clientCode);
		
		
		

		
		
			objModel.setDteClearence(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		
		objModel.setStrReceivedFrom(objGlobal.funIfNull(objBean.getStrReceivedFrom(), "", objBean.getStrReceivedFrom()));
		objModel.setIntOnHold(0);
	
		
				clsReceiptDtlModel objReceiptDtlModel = new clsReceiptDtlModel();
				objReceiptDtlModel.setStrAccountCode(objBean.getStrSancCode());
				objReceiptDtlModel.setStrAccountName(objBean.getStrDebtorName());
				objReceiptDtlModel.setStrCrDr("");
				objReceiptDtlModel.setStrNarration("");
				objReceiptDtlModel.setDblCrAmt(objBean.getDblAmt() * currConversion);
				objReceiptDtlModel.setDblDrAmt(objBean.getDblAmt() * currConversion);
				objReceiptDtlModel.setStrPropertyCode(propertyCode);
				hmReceiptDtl.put(objBean.getStrSancCode(), objReceiptDtlModel);
	
		List<clsReceiptDtlModel> listReceiptDtlModel = new ArrayList<clsReceiptDtlModel>();
		for (Map.Entry<String, clsReceiptDtlModel> entry : hmReceiptDtl.entrySet()) {
			listReceiptDtlModel.add(entry.getValue());
		}
		objModel.setListReceiptDtlModel(listReceiptDtlModel);
		String debtorCode = "";
		String debtorName = "";
		List<clsReceiptDebtorDtlModel> listReceiptDebtorDtlModel = new ArrayList<clsReceiptDebtorDtlModel>();
		clsReceiptDetailBean objReceiptDetails=new clsReceiptDetailBean();
			clsReceiptDebtorDtlModel objReceiptDebtorDtlModel = new clsReceiptDebtorDtlModel();
			
			
				debtorCode = "";
				clsSundryDebtorMasterModel objSunDrModel = objSundryDebtorMasterService.funGetSundryDebtorMaster(debtorCode, clientCode);
				if (objSunDrModel != null) {
					objReceiptDebtorDtlModel.setStrDebtorName(objBean.getStrDebtorName());
					debtorName = objBean.getStrDebtorName();
				} else {
					objReceiptDebtorDtlModel.setStrDebtorName("");
				}
				
				
				
					clsSundryDebtorMasterModel objSunDebtor = objSundryDebtorMasterService.funGetSundryDebtorMaster(objReceiptDetails.getStrDebtorCode(), clientCode);
				
						debtorCode = "";
						debtorName = "";
						objReceiptDebtorDtlModel.setStrDebtorName(objBean.getStrDebtorName());
					
				
					
				
				
				objReceiptDebtorDtlModel.setStrDebtorCode("");

				objReceiptDebtorDtlModel.setStrAccountCode("");
				objReceiptDebtorDtlModel.setStrNarration("");
				objReceiptDebtorDtlModel.setStrPropertyCode(propertyCode);
				objReceiptDebtorDtlModel.setStrCrDr("");
				objReceiptDebtorDtlModel.setDblAmt(objBean.getDblAmt() * currConversion);

				objReceiptDebtorDtlModel.setStrBillNo("");
				objReceiptDebtorDtlModel.setStrInvoiceNo("");
				objReceiptDebtorDtlModel.setStrGuest("");
				objReceiptDebtorDtlModel.setStrCreditNo("");
				objReceiptDebtorDtlModel.setDteBillDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				objReceiptDebtorDtlModel.setDteInvoiceDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				objReceiptDebtorDtlModel.setDteDueDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));

				listReceiptDebtorDtlModel.add(objReceiptDebtorDtlModel);
		
			
		
		//objModel.setStrDebtorCode(debtorCode);
		objModel.setStrDebtorName(debtorName);
		objModel.setListReceiptDebtorDtlModel(listReceiptDebtorDtlModel);

		List<clsReceiptInvDtlModel> listReceiptInvDtl = new ArrayList<clsReceiptInvDtlModel>();
				clsReceiptInvDtlModel objReceitInvdtl = new clsReceiptInvDtlModel();
				objReceitInvdtl.setDblInvAmt(1 * currConversion);
				objReceitInvdtl.setDblPayedAmt(1* currConversion);
				objReceitInvdtl.setStrPropertyCode(propertyCode);
				listReceiptInvDtl.add(objReceitInvdtl);
			

		
		objModel.setListReceiptInvDtlModel(listReceiptInvDtl);
		objModel.setStrCurrency("Rupee");
		objModel.setDblConversion(currConversion);
		
		
		return objModel;
	}

}
