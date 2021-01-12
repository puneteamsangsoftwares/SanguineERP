package com.sanguine.webpms.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsPOSGlobalFunctionsController;
import com.sanguine.model.clsCurrencyMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.util.clsReportBean;
import com.sanguine.webpms.bean.clsPMSPaymentBean;
import com.sanguine.webpms.bean.clsPaymentReciptBean;
import com.sanguine.webpms.bean.clsRefundBean;
import com.sanguine.webpms.dao.clsPMSPaymentDao;
import com.sanguine.webpms.dao.clsPMSSettlementMasterDao;
import com.sanguine.webpms.dao.clsRefundDao;
import com.sanguine.webpms.model.clsPMSPaymentHdModel;
import com.sanguine.webpms.model.clsPMSPaymentReceiptDtl;
import com.sanguine.webpms.service.clsCheckInService;
import com.sanguine.webpms.service.clsFolioService;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsPMSPaymentService;
import com.sanguine.webpms.service.clsPropertySetupService;
import com.sanguine.webpms.service.clsRefundService;
import com.sanguine.webpms.service.clsReservationService;
import com.sanguine.webpms.service.clsRoomMasterService;


@Controller
public class clsRefundController {

	@Autowired
	private clsRefundService objRefundService;

	@Autowired
	private clsRefundDao objRefundDao;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsPropertySetupService objPropertySetupService;

	@Autowired
	private clsGuestMasterService objGuestService;

	@Autowired
	private clsPropertyMasterService objPropertyMasterService;

	@Autowired
	clsPMSPaymentDao objDao;

	@Autowired
	private clsReservationService objReservationService;
	@Autowired
	private clsFolioService objFolioService;

	@Autowired
	clsCheckInService objCheckInService;

	@Autowired
	clsPMSSettlementMasterDao objtPMSSettlement;

	@Autowired
	clsRoomMasterService objRoomMaster;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunService;
	
	
	// Open Refund
	@RequestMapping(value = "/frmRefund", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		String strModule = request.getSession().getAttribute("selectedModuleName").toString();

		if(strModule.equalsIgnoreCase("3-WebPMS"))
		{
		List<String> listAgainst = new ArrayList<>();
		listAgainst.add("Reservation");
		//listAgainst.add("Check-In");
		listAgainst.add("Folio-No");
		listAgainst.add("Bill");
		model.put("listAgainst", listAgainst);
		
		List<String> listSettlement = new ArrayList<>();
		listSettlement.add("Part Settlement");
		listSettlement.add("Full Settlement");

		model.put("listSettlement", listSettlement);
		
		}
		else
		{
			List<String> listAgainst = new ArrayList<>();
			listAgainst.add("Banquet");
			
			model.put("listAgainst", listAgainst);
			
			List<String> listSettlement = new ArrayList<>();
			listSettlement.add("Part Settlement");
			listSettlement.add("Full Settlement");

			model.put("listSettlement", listSettlement);
		}
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if (urlHits.equalsIgnoreCase("1")) {

			return new ModelAndView("frmRefund", "command", new clsRefundBean());
		} else {
			return new ModelAndView("frmRefund_1", "command", new clsRefundBean());
		}
	}

	// Save or Update Payment
		@RequestMapping(value = "/saveRefund", method = RequestMethod.GET)
		public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsRefundBean objBean, BindingResult result, HttpServletRequest req) {
			if (!result.hasErrors()) {
				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				clsPMSPaymentHdModel objHdModel = objRefundService.funPrepareRefundModel(objBean, clientCode, req, userCode);
				objRefundDao.funAddUpdatePaymentHd(objHdModel);
				String propCode = req.getSession().getAttribute("propertyCode").toString();
//				funSendSMSPayment(objHdModel.getStrReceiptNo(), clientCode, propCode);
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Payment Code : ".concat(objHdModel.getStrReceiptNo()));
				req.getSession().setAttribute("GenerateSlip", objHdModel.getStrReceiptNo());
				req.getSession().setAttribute("Against", objHdModel.getStrAgainst());
				return new ModelAndView("redirect:/frmRefund.html");
			} else {
				return new ModelAndView("frmRefund.jsp");
			}
		}  

		// Load Payemt Data
		@RequestMapping(value = "/loadRefundData", method = RequestMethod.GET)
		public @ResponseBody clsRefundBean funLoadReceiptData(@RequestParam("receiptNo") String receiptNo, HttpServletRequest request) {

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			clsRefundBean objRefundBean = new clsRefundBean();
			clsPMSPaymentHdModel objPaymentModel = objRefundDao.funGetPaymentModel(receiptNo, clientCode);

			objRefundBean.setStrReceiptNo(objPaymentModel.getStrReceiptNo());
			objRefundBean.setStrAgainst(objPaymentModel.getStrAgainst());

			objRefundBean.setStrDocNo(objPaymentModel.getStrBillNo());
			objRefundBean.setStrFolioNo(" ");
			objRefundBean.setStrRegistrationNo(" ");
			objRefundBean.setStrFlagOfAdvAmt(objPaymentModel.getStrFlagOfAdvAmt());
			clsPMSPaymentReceiptDtl objPaymentDtlModel = objPaymentModel.getListPaymentRecieptDtlModel().get(0);
			objRefundBean.setStrRemarks(objPaymentDtlModel.getStrRemarks());
			objRefundBean.setStrCardNo(objPaymentDtlModel.getStrCardNo());
			objRefundBean.setStrSettlementCode(objPaymentDtlModel.getStrSettlementCode());
			objRefundBean.setDblPaidAmt(0.00);
			objRefundBean.setDblReceiptAmt(objPaymentModel.getDblReceiptAmt());
			objRefundBean.setDblSettlementAmt(objPaymentDtlModel.getDblSettlementAmt());
			objRefundBean.setDteExpiryDate(objGlobal.funGetDate("yyyy/MM/dd", objPaymentDtlModel.getDteExpiryDate()));

			return objRefundBean;
		}
		
		
		//Refund Slip
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/rptRefundRecipt", method = RequestMethod.GET)
		public void funGeneratePaymentRecipt(@RequestParam("reciptNo") String reciptNo, @RequestParam("checkAgainst") String checkAgainst, HttpServletRequest req, HttpServletResponse resp) {
			try {
				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				String propertyCode = req.getSession().getAttribute("propertyCode").toString();
				String companyName = req.getSession().getAttribute("companyName").toString();
				String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
				clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
				if (objSetup == null) {
					objSetup = new clsPropertySetupModel();
				}

				String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
				String userName = "";
				String sqlUserName = "select strUserName from "+webStockDB+".tbluserhd where strUserCode='" + userCode + "' ";

				List listOfUser = objGlobalFunctionsService.funGetDataList(sqlUserName, "sql");
				if (listOfUser.size() > 0) {
					// Object[] userData = (Object[]) listOfUser.get(0);
					userName = listOfUser.get(0).toString();
				}

				HashMap reportParams = new HashMap();
				reportParams.put("pCompanyName", companyName);
				reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
				reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
				reportParams.put("pContactDetails", "");
				reportParams.put("strImagePath", imagePath);
				reportParams.put("userName", userName);
				if(clientCode.equalsIgnoreCase("378.001"))
				{
					reportParams.put("lblChild", null);
				}
				else
				{
				    reportParams.put("lblChild", "Child                    :")	;
				}
				ArrayList datalist = new ArrayList();
				String reportName = "";
		 
			
					reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptRefundRecipt.jrxml");
					
					
					String sqlRefund="select a.strReceiptNo, DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y'),a.strBillNo, "
									 +	" IFNULL(d.strFirstName,''), IFNULL(d.strMiddleName,''),  "
									 +	" IFNULL(d.strLastName,''),a.dblReceiptAmt AS RefundAmt,c.dblGrandTotal,b.strRemarks,e.strSettlementDesc "
									 +	" from tblreceipthd a,tblreceiptdtl b,tblbillhd c,tblguestmaster d,tblsettlementmaster e "
									 +	" where a.strReceiptNo='"+reciptNo+"' and a.strReceiptNo=b.strReceiptNo and a.strBillNo=c.strBillNo "
									 +	" and b.strCustomerCode=d.strGuestCode and b.strSettlementCode=e.strSettlementCode";

					List listOfPayment = objGlobalFunctionsService.funGetDataList(sqlRefund, "sql");

					for (int i = 0; i < listOfPayment.size(); i++) {
						Object PaymentData[] = (Object[]) listOfPayment.get(i);

						String strReceiptNo = PaymentData[0].toString();
						String intNoOfAdults = "";
						String intNoOfChild ; 
						if(clientCode.equalsIgnoreCase("378.001"))
						{
							intNoOfChild=null;
						}
						else
						{
							intNoOfChild = "";
						}
						String dteReciptDate = PaymentData[1].toString();
						String strReservationNo = "";
						String strBillNo = PaymentData[2].toString();
						String strRoomType = "";
						String dteArrivalDate ="";
						String dteDepartureDate ="";
						String strFirstName = PaymentData[3].toString();
						String strMiddleName = PaymentData[4].toString();
						String strLastName = PaymentData[5].toString();
						String dblRefundAmt = PaymentData[6].toString();
						double dblBillAmt=Double.parseDouble(PaymentData[7].toString());
						String strRemarks = PaymentData[8].toString();
						
						if(PaymentData[8].toString().contains("#"))
						{
							strRemarks=strRemarks.split("#")[0];
						}
						String strSettlementDesc = PaymentData[9].toString();
						
						String dteModifiedDate ="";

						clsPaymentReciptBean objPaymentReciptBean = new clsPaymentReciptBean();
						objPaymentReciptBean.setStrReceiptNo(strReceiptNo);
						objPaymentReciptBean.setIntNoOfAdults(intNoOfAdults);
						objPaymentReciptBean.setIntNoOfChild(intNoOfChild);
						objPaymentReciptBean.setStrReservationNo(strReservationNo);
						objPaymentReciptBean.setStrRoomType(strRoomType);
						objPaymentReciptBean.setDteArrivalDate(dteArrivalDate);
						objPaymentReciptBean.setDteDepartureDate(dteDepartureDate);
						objPaymentReciptBean.setStrFirstName(strFirstName+" ");
						objPaymentReciptBean.setStrMiddleName(strMiddleName+" ");
						objPaymentReciptBean.setStrLastName(strLastName);
						objPaymentReciptBean.setStrSettlementDesc(strSettlementDesc);
						objPaymentReciptBean.setDblPaidAmt(dblRefundAmt);
						objPaymentReciptBean.setDblBalanceAmount(dblBillAmt);
						objPaymentReciptBean.setStrRemarks(strRemarks);
						objPaymentReciptBean.setDteReciptDate(dteReciptDate);
						objPaymentReciptBean.setDteModifiedDate(dteModifiedDate);
						objPaymentReciptBean.setStrBillNo(strBillNo);
						datalist.add(objPaymentReciptBean);
					}
			

				JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(datalist);
				JasperDesign jd = JRXmlLoader.load(reportName);
				JasperReport jr = JasperCompileManager.compileReport(jd);
				JasperPrint jp = JasperFillManager.fillReport(jr, reportParams, beanCollectionDataSource);
				List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
				if (jp != null) {
					jprintlist.add(jp);
					ServletOutputStream servletOutputStream = resp.getOutputStream();
					JRExporter exporter = new JRPdfExporter();
					resp.setContentType("application/pdf");
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition", "inline;filename=PaymentRecipt.pdf");
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
