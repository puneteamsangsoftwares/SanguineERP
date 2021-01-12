package com.sanguine.webpms.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSPaymentBean;
import com.sanguine.webpms.bean.clsRefundBean;
import com.sanguine.webpms.dao.clsPMSPaymentDao;
import com.sanguine.webpms.model.clsBillHdModel;
import com.sanguine.webpms.model.clsPMSPaymentHdModel;
import com.sanguine.webpms.model.clsPMSPaymentReceiptDtl;

@Service("clsRefundService")
public class clsRefundServiceImpl implements clsRefundService {

	@Autowired
	private clsPMSPaymentDao objPaymentDao;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsBillService objBillService;

	@Override
	public void funAddUpdatePaymentHd(clsPMSPaymentHdModel objHdModel) {
		// TODO Auto-generated method stub
	}

	@Override
	public clsPMSPaymentHdModel funPrepareRefundModel(clsRefundBean objRefundBean, String clientCode, HttpServletRequest request, String userCode) {
		String reservationNo = "";
		String registrationNo = "";
		String folioNo = "";
		String checkInNo = "";
		String billNo = "";
		//String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", request.getSession().getAttribute("PMSDate").toString());
		

		clsPMSPaymentHdModel objModel = new clsPMSPaymentHdModel();

		if (objRefundBean.getStrReceiptNo().isEmpty()) {
			String transaDate = objGlobal.funGetCurrentDateTime("dd-MM-yyyy").split(" ")[0];
			String documentNo = objGlobal.funGeneratePMSDocumentCode("frmRefund", transaDate, request);
			objModel.setStrReceiptNo(documentNo);
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		} else {
			objModel.setStrReceiptNo(objRefundBean.getStrReceiptNo());
		}
		objModel.setStrAgainst("Bill");
		objModel.setDblPaidAmt(0.00);
		objModel.setDblReceiptAmt(objRefundBean.getDblReceiptAmt());

		
			/*String sql = " select a.strCheckInNo,a.strRegistrationNo,a.strReservationNo,a.strFolioNo " + " from tblbillhd a " + " where a.strBillNo='" + objRefundBean.getStrDocNo() + "'";
			List list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (list.size() > 0) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					Object[] arrObj = (Object[]) list.get(cnt);
					checkInNo = arrObj[0].toString();
					registrationNo = arrObj[1].toString();
					reservationNo = arrObj[2].toString();
					folioNo = arrObj[3].toString();
					billNo = objRefundBean.getStrDocNo();
					clsBillHdModel objBillHdModel = objBillService.funLoadBill(billNo, clientCode);
					String sqlReceipt=" SELECT ifnull(SUM(a.dblReceiptAmt),0) FROM tblreceipthd a WHERE a.strFolioNo='"+folioNo+"' ";
					List listReceipt = objGlobalFunctionsService.funGetListModuleWise(sqlReceipt, "sql");
					if(listReceipt.size()>0)
					{
						double balanceAmt=Double.parseDouble(listReceipt.get(0).toString());
						if(objBillHdModel.getDblGrandTotal()-balanceAmt-objRefundBean.getDblReceiptAmt()==0.0)
							objBillHdModel.setStrBillSettled("Y");
							
						else
							objBillHdModel.setStrBillSettled("N");
					}
					objBillHdModel.setStrRemark("");
					objBillHdModel.setStrMergedBillNo("");
					objBillService.funAddUpdateBillHd(objBillHdModel);
				}
			}*/
		

	
		objModel.setDteReceiptDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrCheckInNo(" ");
		objModel.setStrBillNo(objRefundBean.getStrDocNo());
		objModel.setStrRegistrationNo(" ");
		objModel.setStrReservationNo(reservationNo);
		objModel.setStrFolioNo(" "); //Use For Receipt Type
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);
		objModel.setStrFlagOfAdvAmt(objRefundBean.getStrFlagOfAdvAmt());

		List<clsPMSPaymentReceiptDtl> listPaymentReceiptDtlModel = new ArrayList<clsPMSPaymentReceiptDtl>();
		clsPMSPaymentReceiptDtl objPaymentReceiptDtlModel = new clsPMSPaymentReceiptDtl();
		objPaymentReceiptDtlModel.setDblSettlementAmt(0.00);

		/*String[] newDate = objRefundBean.getDteExpiryDate().split("-");
		if (newDate[0].length() >= 3) {
			objPaymentReceiptDtlModel.setDteExpiryDate(objRefundBean.getDteExpiryDate());
		} else {
			objPaymentReceiptDtlModel.setDteExpiryDate(objGlobal.funGetDate("yyyy/MM/dd", objRefundBean.getDteExpiryDate()));
		}*/
		objPaymentReceiptDtlModel.setDteExpiryDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objPaymentReceiptDtlModel.setStrCardNo(objRefundBean.getStrCardNo());
		
		String remark=objRefundBean.getStrRemarks()+ "#Refund Amount";
		if(objRefundBean.getStrRemarks().equalsIgnoreCase(""))
		{
			remark=remark.split("#")[1];
		}
		objPaymentReceiptDtlModel.setStrRemarks(remark);
		objPaymentReceiptDtlModel.setStrSettlementCode(objRefundBean.getStrSettlementCode());
		objPaymentReceiptDtlModel.setStrCustomerCode(objRefundBean.getStrCustomerCode());
		listPaymentReceiptDtlModel.add(objPaymentReceiptDtlModel);

		objModel.setListPaymentRecieptDtlModel(listPaymentReceiptDtlModel);
		return objModel;
	}
}
