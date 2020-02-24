package com.sanguine.webpms.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSGroupBookingBean;
import com.sanguine.webpms.bean.clsPMSGroupBookingDetailBean;
import com.sanguine.webpms.bean.clsReservationDetailsBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsPMSGroupBookingDtlModel;
import com.sanguine.webpms.model.clsPMSGroupBookingHDModel;
import com.sanguine.webpms.model.clsReservationDtlModel;
import com.sanguine.webpms.service.clsPMSGroupBookingService;

@Controller
public class clsPMSGroupBookingController{

	@Autowired
	private clsPMSGroupBookingService objPMSGroupBookingService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
//Open PMSGroupBooking
	@RequestMapping(value = "/frmPMSGroupBooking", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
		return new ModelAndView("frmPMSGroupBooking","command", new clsPMSGroupBookingBean());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmPMSGroupBooking1", method = RequestMethod.POST)
	public @ResponseBody clsPMSGroupBookingHDModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("userCode").toString();
		clsPMSGroupBookingBean objBean=new clsPMSGroupBookingBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsPMSGroupBookingHDModel objPMSGroupBooking = new clsPMSGroupBookingHDModel();
		return objPMSGroupBooking;
	}
	
	@RequestMapping(value = "/frmPMSGroupBookingForReservation", method = RequestMethod.GET)
	public ModelAndView funOpenFormForReservation(Map<String, Object> model,
			HttpServletRequest request,
			@RequestParam("lblCorporateDesc") String lblCorporateDesc,
			@RequestParam("strPaxCnt") String strPaxCnt,
			@RequestParam("strDepartureTime") String strDepartureTime,
			@RequestParam("strArrivalTime") String strArrivalTime,
			@RequestParam("strDepartureDate") String strDepartureDate,
			@RequestParam("strCorporateCode") String strCorporateCode,
			@RequestParam("strArrDate") String strArrDate,
			@RequestParam("gRoomTypeCode") String gRoomTypeCode,
			@RequestParam("gRoomTypeDesc") String gRoomTypeDesc){
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		clsPMSGroupBookingBean objBean =new clsPMSGroupBookingBean();
		objBean.setStrCompName(lblCorporateDesc);
		objBean.setStrPax(strPaxCnt);
		objBean.setDteCheckoutDate(strDepartureDate);
		objBean.setStrCompCode(strCorporateCode);
		objBean.setDteCheckInDate(strArrDate);        
		objBean.setStrRoomType(gRoomTypeCode);
		objBean.setStrRoomTypeDesc(gRoomTypeDesc);
		
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSGroupBooking", "command", objBean);
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSGroupBooking", "command", objBean);
		} else {
			return null;
		}

	}
	

//Save or Update PMSGroupBooking
	@RequestMapping(value = "/savePMSGroupBooking", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPMSGroupBookingBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsPMSGroupBookingHDModel objModel = funPrepareModel(objBean,userCode,clientCode,req);
			objPMSGroupBookingService.funAddUpdatePMSGroupBooking(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", objModel.getStrGroupCode());
			return new ModelAndView("redirect:/frmPMSGroupBooking.html");
		}
		else{
			return new ModelAndView("frmPMSGroupBooking");
		}
	}

	
	// Load data from database to form
		@RequestMapping(value = "/loadGroupCode", method = RequestMethod.GET)
		public @ResponseBody clsPMSGroupBookingHDModel funFetchGuestMasterData(@RequestParam("groupCode") String groupCode, HttpServletRequest req) {
			clsGlobalFunctions objGlobal = new clsGlobalFunctions();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			clsPMSGroupBookingHDModel objPMSGroupBookingModel = objPMSGroupBookingService.funGetPMSGroupBooking(groupCode, clientCode);
			return objPMSGroupBookingModel;
		}
	
	
//Convert bean to model function
	private clsPMSGroupBookingHDModel funPrepareModel(clsPMSGroupBookingBean objBean,String userCode,String clientCode,HttpServletRequest req){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;	
		objGlobal = new clsGlobalFunctions();
		clsPMSGroupBookingHDModel objModel = new clsPMSGroupBookingHDModel();

		if (objBean.getStrGroupCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblgroupbookinghd", "GroupMaster", "strGroupCode", clientCode);
			String groupCode = "GR" + String.format("%06d", lastNo);
			objModel.setStrGroupCode(groupCode);
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		} else {
			objModel.setStrGroupCode(objBean.getStrGroupCode());
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		}
		
		objModel.setStrReservationID(objBean.getStrReservationID());
		objModel.setStrGroupName(objBean.getStrGroupName());
		objModel.setStrGroupLeaderCode(objBean.getStrGroupLeaderCode());
		objModel.setStrAddress(objBean.getStrAddress());
		objModel.setStrCity(objBean.getStrCity());
		objModel.setStrCountry(objBean.getStrCountry());
		objModel.setStrPin(objBean.getStrPin());
		objModel.setStrPhone(objBean.getStrPhone());
		objModel.setStrMobile(objBean.getStrMobile());
		objModel.setStrFax(objBean.getStrFax());
		objModel.setStrEmail(objBean.getStrEmail());
		objModel.setDteDob(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDteDob()));
		objModel.setStrNationality(objBean.getStrNationality());
		
		//14
		
		objModel.setStrCompCode(objBean.getStrCompCode());
		objModel.setStrCompName(objBean.getStrCompName());
		objModel.setStrDesignation(objBean.getStrDesignation());
		objModel.setStrGICity(objBean.getStrGICity());
		objModel.setStrGIPhone(objBean.getStrGIPhone());
		objModel.setStrGIMobile(objBean.getStrGIMobile());
		objModel.setStrGIFax(objBean.getStrGIFax());
		
		//21
		
		objModel.setDteTravelDate(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDteTravelDate()));
		objModel.setTmeTravelTime(objBean.getTmeTravelTime());
		objModel.setStrPickupRequired(objBean.getStrPickupRequired());
		
		//25		
		objModel.setDteCheckInDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteCheckInDate()));
		objModel.setDteCheckoutDate(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDteCheckoutDate()));
		objModel.setStrPax(objBean.getStrPax());
		objModel.setStrSource(objBean.getStrSource());
		objModel.setStrGuestType(objBean.getStrGuestType());
		objModel.setStrExtraBed(objBean.getStrExtraBed());
		objModel.setStrChild(objBean.getStrChild());
		objModel.setStrInfant(objBean.getStrInfant());
		objModel.setStrSalesChannel(objBean.getStrSalesChannel());
		
		//34
		
		objModel.setStrRoomType(objBean.getStrRoomType());
		objModel.setStrRoomTypeDesc(objBean.getStrRoomTypeDesc());
		objModel.setStrRoomTaxes(objBean.getStrRoomTaxes());
		objModel.setStrOtherTaxes(objBean.getStrOtherTaxes());
		objModel.setStrServiceCharges(objBean.getStrServiceCharges());
		objModel.setStrPayments(objBean.getStrPayments());
		objModel.setStrDiscounts(objBean.getStrDiscounts());
		
		//41
		
		objModel.setStrCard(objBean.getStrCard());
		objModel.setStrCash(objBean.getStrCash());
		objModel.setStrPaymentBtGroupLeader(objBean.getStrPaymentBtGroupLeader());
		objModel.setStrGuest(objBean.getStrGuest());
		objModel.setStrRoom(objBean.getStrRoom());
		objModel.setStrFandB(objBean.getStrFandB());
		objModel.setStrTelephones(objBean.getStrTelephones());
		objModel.setStrExtras(objBean.getStrExtras());
		
		
		//50			
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);

		//add list data in to tblgroupbookingdtl table		
		List<clsPMSGroupBookingDtlModel> listPMSGroupBookingDtlModel = new ArrayList<clsPMSGroupBookingDtlModel>();
		HashMap<String,clsPMSGroupBookingDtlModel> hmap = new LinkedHashMap<String,clsPMSGroupBookingDtlModel>();
		for (clsPMSGroupBookingDetailBean objPMSGroupBookingDetailBean: objBean.getListPMSGroupBookingDetailBean()) {
			//clsPMSGroupBookingDtlModel obj = new clsPMSGroupBookingDtlModel();
			clsPMSGroupBookingDtlModel objDtlBean = new clsPMSGroupBookingDtlModel();
			if(hmap.containsKey(objPMSGroupBookingDetailBean.getStrPayee()))
			{
				objDtlBean=hmap.get(objPMSGroupBookingDetailBean.getStrPayee().toString());
				//objDtlBean.setStrGroupCode(objModel.getStrGroupCode());
				objDtlBean.setStrRoom(funEquals(objPMSGroupBookingDetailBean.getStrRoom(),objDtlBean.getStrRoom()));
				objDtlBean.setStrPayee(objPMSGroupBookingDetailBean.getStrPayee());
				objDtlBean.setStrFandB(funEquals(objPMSGroupBookingDetailBean.getStrFandB(),objDtlBean.getStrFandB()));
				objDtlBean.setStrTelephone(funEquals(objPMSGroupBookingDetailBean.getStrTelephone(),objDtlBean.getStrTelephone()));
				objDtlBean.setStrExtra(funEquals(objPMSGroupBookingDetailBean.getStrExtra(),objDtlBean.getStrExtra()));
				hmap.put(objPMSGroupBookingDetailBean.getStrPayee(), objDtlBean);
			}
			else
			{
				//objDtlBean.setStrGroupCode(objModel.getStrGroupCode());
				objDtlBean.setStrRoom(objGlobal.funIfNull(objPMSGroupBookingDetailBean.getStrRoom(), null, "Y"));
				objDtlBean.setStrPayee(objPMSGroupBookingDetailBean.getStrPayee());
				objDtlBean.setStrFandB(objGlobal.funIfNull(objPMSGroupBookingDetailBean.getStrFandB(), null, "Y"));
				objDtlBean.setStrTelephone(objGlobal.funIfNull(objPMSGroupBookingDetailBean.getStrTelephone(), null, "Y"));
				objDtlBean.setStrExtra(objGlobal.funIfNull(objPMSGroupBookingDetailBean.getStrExtra(), null, "Y"));
				hmap.put(objPMSGroupBookingDetailBean.getStrPayee(), objDtlBean);
			}
		}
		 for (Map.Entry<String,clsPMSGroupBookingDtlModel> entry : hmap.entrySet())
		 {/*
			 System.out.println("Key = " + entry.getKey() + 
                     ", Value = " + entry.getValue()); */
			 listPMSGroupBookingDtlModel.add(entry.getValue());
		 }
		objModel.setListPMSGroupBookingDtlModel(listPMSGroupBookingDtlModel);		
		return objModel;

	}
	
	public String funEquals(String input, String defaultValue) {
		String op = "N";
		if(null!=defaultValue)
		{			
			op=defaultValue;
		}
		else
		{
			if(null==input&&null==defaultValue)
			{
				op="N";
			}
			else
			{
				op="Y";				
			}
		}
		return op;
	}

}
