package com.sanguine.webpms.controller;

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

import java.util.List;
import java.util.Map;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSRateContractBean;
import com.sanguine.webpms.model.clsPMSRateContractModel;
import com.sanguine.webpms.model.clsRoomTypeMasterModel;
import com.sanguine.webpms.service.clsPMSRateContractService;

@Controller
public class clsPMSRateContractController{

	@Autowired
	private clsPMSRateContractService objPMSRateContractService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open PMSRateContract
	@RequestMapping(value = "/frmPMSRateContract", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model,HttpServletRequest request){
		String clientCode=request.getSession().getAttribute("clientCode").toString();

		String sqlRoomTypes = "select a.strRoomTypeCode from tblroomtypemaster a where a.strClientCode='"+clientCode+"'";
		
		List list = objGlobalFunctionsService.funGetListModuleWise(sqlRoomTypes, "sql");
		
		if(list!=null && list.size()>0)
		{
			
			model.put("RoomType", list);

		}

		String sqlSeason = "select a.strSeasonCode  from tblseasonmaster a  where a.strClientCode='"+clientCode+"'";
		
		List listSeason = objGlobalFunctionsService.funGetListModuleWise(sqlSeason, "sql");
		
		if(listSeason!=null && listSeason.size()>0)
		{
			
			model.put("Season", listSeason);

		}
		
		return new ModelAndView("frmPMSRateContract","command", new clsPMSRateContractModel());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmPMSRateContract1", method = RequestMethod.POST)
	public @ResponseBody clsPMSRateContractModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("userCode").toString();
		clsPMSRateContractBean objBean=new clsPMSRateContractBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsPMSRateContractModel objPMSRateContract = new clsPMSRateContractModel();
		return objPMSRateContract;
	}

//Save or Update PMSRateContract
	@RequestMapping(value = "/savePMSRateContract", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPMSRateContractBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsPMSRateContractModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objPMSRateContractService.funAddUpdatePMSRateContract(objModel);
			
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Rate management code : ".concat(objModel.getStrRateContractID()));

			
			return new ModelAndView("redirect:/frmPMSRateContract.html");
		}
		else{
			return new ModelAndView("frmPMSRateContract");
		}
	}

//Convert bean to model function
	private clsPMSRateContractModel funPrepareModel(clsPMSRateContractBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsPMSRateContractModel objModel = new clsPMSRateContractModel();
		if (objBean.getStrRateContractID().trim().length() == 0) {
			
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblpmsratecontractdtl", "pmsRateContractdtl", "strRateContractID", clientCode);
			String rateCode = "RQ" + String.format("%06d", lastNo);
			objModel.setStrRateContractID(rateCode);
			objModel.setDblChildTariWeekDays(objBean.getDblChildTariWeekDays());
			objModel.setDblChildTariWeekend(objBean.getDblChildTariWeekend());
			objModel.setDblDoubleTariWeekDays(objBean.getDblDoubleTariWeekDays());
			objModel.setDblDoubleTariWeekend(objBean.getDblDoubleTariWeekend());
			objModel.setDblExtraBedTariWeekDays(objBean.getDblExtraBedTariWeekDays());
			objModel.setDblExtraBedTariWeekend(objBean.getDblExtraBedTariWeekend());
			objModel.setDblSingleTariWeekDays(objBean.getDblSingleTariWeekDays());
			objModel.setDblSingleTariWeekend(objBean.getDblSingleTariWeekDays());
			objModel.setDblTrippleTariWeekDays(objBean.getDblTrippleTariWeekDays());
			objModel.setDblTrippleTariWeekend(objBean.getDblTrippleTariWeekend());
			objModel.setDblYouthTariWeekDays(objBean.getDblYouthTariWeekDays());
			objModel.setDblYouthTariWeekend(objBean.getDblYouthTariWeekend());
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteFromDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteFromDate()));
			objModel.setDteToDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteToDate()));
			objModel.setIntNoOfNights(objBean.getIntNoOfNights());
			objModel.setStrClientCode(clientCode);
			objModel.setStrFriday(objBean.getStrFriday());
			objModel.setStrIncludeTax(objBean.getStrIncludeTax());
			objModel.setStrMonday(objBean.getStrMonday());
			objModel.setStrRoomTypeCode(objBean.getStrRoomTypeCode());
			objModel.setStrSaturday(objBean.getStrSaturday());
			objModel.setStrSeasonCode(objBean.getStrSeasonCode());
			objModel.setStrSunday(objBean.getStrSunday());
			objModel.setStrThursday(objBean.getStrThursday());
			objModel.setStrTuesday(objBean.getStrTuesday());
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrWednesday(objBean.getStrWednesday());
			
		}
		else
		{
			objModel.setStrRateContractID(objBean.getStrRateContractID());
			objModel.setDblChildTariWeekDays(objBean.getDblChildTariWeekDays());
			objModel.setDblChildTariWeekend(objBean.getDblChildTariWeekend());
			objModel.setDblDoubleTariWeekDays(objBean.getDblDoubleTariWeekDays());
			objModel.setDblDoubleTariWeekend(objBean.getDblDoubleTariWeekend());
			objModel.setDblExtraBedTariWeekDays(objBean.getDblExtraBedTariWeekDays());
			objModel.setDblExtraBedTariWeekend(objBean.getDblExtraBedTariWeekend());
			objModel.setDblSingleTariWeekDays(objBean.getDblSingleTariWeekDays());
			objModel.setDblSingleTariWeekend(objBean.getDblSingleTariWeekDays());
			objModel.setDblTrippleTariWeekDays(objBean.getDblTrippleTariWeekDays());
			objModel.setDblTrippleTariWeekend(objBean.getDblTrippleTariWeekend());
			objModel.setDblYouthTariWeekDays(objBean.getDblYouthTariWeekDays());
			objModel.setDblYouthTariWeekend(objBean.getDblYouthTariWeekend());
			objModel.setDteDateCreated(objBean.getDteDateCreated());
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteFromDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteFromDate()));
			objModel.setDteToDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteToDate()));
			objModel.setIntNoOfNights(objBean.getIntNoOfNights());
			objModel.setStrClientCode(clientCode);
			objModel.setStrFriday(objBean.getStrFriday());
			objModel.setStrIncludeTax(objBean.getStrIncludeTax());
			objModel.setStrMonday(objBean.getStrMonday());
			objModel.setStrRoomTypeCode(objBean.getStrRoomTypeCode());
			objModel.setStrSaturday(objBean.getStrSaturday());
			objModel.setStrSeasonCode(objBean.getStrSeasonCode());
			objModel.setStrSunday(objBean.getStrSunday());
			objModel.setStrThursday(objBean.getStrThursday());
			objModel.setStrTuesday(objBean.getStrTuesday());
			objModel.setStrUserCreated(objBean.getStrUserCreated());
			objModel.setStrUserEdited(userCode);
			objModel.setStrWednesday(objBean.getStrWednesday());
		}
		
		return objModel;

	}

}
