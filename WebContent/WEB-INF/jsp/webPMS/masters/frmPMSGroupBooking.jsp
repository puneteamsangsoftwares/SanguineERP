<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=8">
<title></title>

		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap.min.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap-grid.min.css"/>" />
	 	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/Accordian/jquery-ui-1.8.9.custom.css "/>" />	
	 	
	 	<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.min.js"/>"></script>
	 	<script type="text/javascript" src="<spring:url value="/resources/js/Accordian/jquery.multi-accordion-1.5.3.js"/>"></script>	
	
	 
	 <script type="text/javascript">
	var fieldName;

	$(document).ready(function()
			{
	var message='';
	<%if (session.getAttribute("success") != null) {
		            if(session.getAttribute("successMessage") != null){%>
		            message='<%=session.getAttribute("successMessage").toString()%>';
		            <%
		            session.removeAttribute("successMessage");
		            }
					boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
					session.removeAttribute("success");
					if (test) {
					%>	
		alert("Data Save successfully\n\n"+message);
	<%
	}}%>
	
});
	
	
	
$(document).ready(function(){
		
		var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		
		$("#txtDob").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#txtDob").datepicker('setDate', pmsDate);

		
		
		$("#txtTravelDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#txtTravelDate").datepicker('setDate', pmsDate);
		
		
		$("#txtCheckInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#txtCheckInDate").datepicker('setDate', pmsDate);
		
		
		$("#txtCheckoutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#txtCheckoutDate").datepicker('setDate', pmsDate);
		
		$('#txtTravelTime').timepicker();
		
		$('#txtDepartureTime').timepicker({
	        'timeFormat':'H:i:s'
	});
		
		$('#txtDepartureTime').timepicker({
	        'timeFormat':'H:i:s'
});
		
	});
	
	
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
	//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	

	function funSetData(code){

		switch(fieldName){

			case 'WCCompanyCode' : 
				funSetCompanyCode(code);
				break;
			case 'WCComAreaMaster' : 
				funSetComAreaCode(code);
				break;
			case 'WCComCityMaster' : 
				funSetComCityCode(code);
				break;
			case 'WCComStateMaster' : 
				funSetComStateCode(code);
				break;
			case 'WCComRegionMaster' : 
				funSetComRegionCode(code);
				break;
			case 'WCComCountryMaster' : 
				funSetComCountryCode(code);
				break;
			case 'WCCategoryMaster' : 
				funSetComRegionCode(code);
				break;
				
			case 'WCmemProfileCustomer' :
				funloadMemberData(code);
				break;

			case 'WCCatMaster' :
				funSetCategoryData(code);
				
			case 'guestCode' : 
				funSetGuestCode(code);
				break;
				
		}
	}
	function funSetGuestCode(code){
		
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadGuestCode.html?guestCode=" + code,
			dataType : "json",
			success : function(response){ 
				$("#txtGroupLeaderCode").val(code);
				$("#txtGroupLeaderName").val(response.strFirstName+" "+response.strMiddleName+" "+response.strLastName);
				$("#txtAddress").val(response.strAddress);
				$("#txtCity").val(response.strCity);
				$("#txtCountry").val(response.strCountry);
				$("#txtPin").val(response.intPinCode);	
				$("#txtPhone").val('');	
				$("#txtMobile").val(response.lngMobileNo);
				$("#txtFax").val(response.lngFaxNo);
				$("#txtEmail").val(response.strEmailId);
				$("#txtDob").val(response.dteDOB);
				$("#txtNationality").val(response.strNationality);
			},
			error : function(e){
				if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }
			}
		});
	}


	function funSetCompanyCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadWebClubCompanyData.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 

				if(response.strCompanyCode=='Invalid Code')
	        	{
	        		alert("Invalid Company Code");
	        		$("#txtCompanyCode").val('');
	        	}
	        	else
	        	{      
		        	$("#txtCompanyCode").val(code);
		        	$("#txtCompanyName").val(response.strCompanyName);
		        	$("#txtAnnualTrunover").val(response.dblAnnualTrunover);
		        	$("#txtCapital").val(response.dblCapital);
		        	$("#txtMemberCode").val(response.strMemberCode);
		        	$("#txtCategoryCode").val(response.strCategoryCode);
		        	$("#txtActiveNominee").val(response.strActiveNominee);
		        	$("#txtAddress1").val(response.strAddress1);
		        	$("#txtAddress2").val(response.strAddress2);
		        	$("#txtAddress3").val(response.strAddress3);
		        	$("#txtLandMark").val(response.strLandmark);
		        	$("#txtAreaCode").val(response.strAreaCode);
		        	$("#txtCtCode").val(response.strCityCode);
		        	$("#txtStateCode").val(response.strStateCode);
		        	$("#txtRegionCode").val(response.strRegionCode);
		        	$("#txtCountryCode").val(response.strCountryCode);
		        	
		        	$("#txtPinCode").val(response.strPin);
		        	$("#txtTelePhone1").val(response.strTelephone1);
		        	$("#txtTelePhone2").val(response.strTelephone2);
		        	$("#txtFax1").val(response.strFax1);
		        	$("#txtFax2").val(response.strFax2);
// 		        	$("#").val(response.);	        	
					funSetComAreaCode(response.strAreaCode);				
					funSetComCityCode(response.strCityCode);					
					funSetComStateCode(response.strStateCode);					
					funSetComRegionCode(response.strRegionCode);			
					funSetComCountryCode(response.strCountryCode);								
					funloadMemberData(response.strMemberCode);					
					funSetCategoryData(response.strCategoryCode);		
	        	}
			},
			error: function(jqXHR, exception) {
	            if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }		            
	        }
      });
}

	function funSetComAreaCode(code){

		$("#txtAreaCode").val(code);
		var searchurl=getContextPath()+"/loadAreaCode.html?docCode="+code;
		//alert(searchurl);
		
			$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        success: function(response)
		        {
		        	if(response.strAreaCode=='Invalid Code')
		        	{
		        		alert("Invalid Group Code");
		        		$("#txtGroupCode").val('');
		        	}
		        	else
		        	{ 
		        		funSetComCityCode(response.strCityCode);
			        	$("#txtAreaName").val(response.strAreaName);
		        	}
				},
				error: function(jqXHR, exception) {
		            if (jqXHR.status === 0) {
		                alert('Not connect.n Verify Network.');
		            } else if (jqXHR.status == 404) {
		                alert('Requested page not found. [404]');
		            } else if (jqXHR.status == 500) {
		                alert('Internal Server Error [500].');
		            } else if (exception === 'parsererror') {
		                alert('Requested JSON parse failed.');
		            } else if (exception === 'timeout') {
		                alert('Time out error.');
		            } else if (exception === 'abort') {
		                alert('Ajax request aborted.');
		            } else {
		                alert('Uncaught Error.n' + jqXHR.responseText);
		            }		            
		        }
			});
	}
	
	
	function funSetComCityCode(code){
		//alert("Hii");
		$("#txtCtCode").val(code);
		var searchurl=getContextPath()+"/loadCityCode.html?docCode="+code;
		//alert(searchurl);
		
			$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        success: function(response)
		        {
		        	if(response.strCityCode=='Invalid Code')
		        	{
		        		alert("Invalid City Code In");
		        		$("#txtResidentCtCode").val('');
		        	}
		        	else
		        	{		        		
						funSetComCountryCode(response.strCountryCode);
		        		funSetComStateCode(response.strStateCode);								
						$("#txtPinCode").val(response.strSTDCode);
						$("#txtCityName").val(response.strCityName);
		        	}
		        	
		        },
		
			error: function(jqXHR, exception) {
	            if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }		            
	        }
		});
	}
	
	
	function funSetComCountryCode(code){
		  
		$("#txtCountryCode").val(code);
		var searchurl=getContextPath()+"/loadCountryCode.html?docCode="+code;
		//alert(searchurl);
		
			$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        success: function(response)
		        {
		        	if(response.strCountryCode=='Invalid Code')
		        	{
		        		alert("Invalid Country Code In");
		        		$("#txtCountryCode").val('');
		        	}
		        	else
		        	{
		        		$("#txtCountryName").val(response.strCountryName);
		        	}
		        	
		        },
			error: function(jqXHR, exception) {
	            if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }		            
	        }
		});
	}

	function funSetComStateCode(code){
		  
		$("#txtStateCode").val(code);
		var searchurl=getContextPath()+"/loadStateCode.html?docCode="+code;
		//alert(searchurl);
		
			$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        success: function(response)
		        {
		        	if(response.strStateCode=='Invalid Code')
		        	{
		        		alert("Invalid State Code In");
		        		$("#txtStateCode").val('');
		        	}
		        	else
		        	{

		        		funSetComRegionCode(response.strRegionCode);
		        		$("#txtStateName").val(response.strStateName);
		        		 
		        	}
		        	
		        },
			error: function(jqXHR, exception) {
	            if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }		            
	        }
		});
	
	}		
			function funSetComRegionCode(code){
				
				$("#txtRegionCode").val(code);
				var searchurl=getContextPath()+"/loadRegionCode.html?docCode="+code;
				//alert(searchurl);
				    
					$.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strRegionCode=='Invalid Code')
				        	{
				        		alert("Invalid Region Code In");
				        		$("#txtRegionCode").val('');
				        	}
				        	else
				        	{
				        		$("#txtRegionName").val(response.strRegionName);
				        	}
				        	
				        },

					error: function(jqXHR, exception) {
			            if (jqXHR.status === 0) {
			                alert('Not connect.n Verify Network.');
			            } else if (jqXHR.status == 404) {
			                alert('Requested page not found. [404]');
			            } else if (jqXHR.status == 500) {
			                alert('Internal Server Error [500].');
			            } else if (exception === 'parsererror') {
			                alert('Requested JSON parse failed.');
			            } else if (exception === 'timeout') {
			                alert('Time out error.');
			            } else if (exception === 'abort') {
			                alert('Ajax request aborted.');
			            } else {
			                alert('Uncaught Error.n' + jqXHR.responseText);
			            }		            
			        }
				});
			}
	
			
			function funSetCategoryData(code)
			{
				$("#txtCategoryCode").val(code);
				var searchurl=getContextPath()+"/loadWebClubMemberCategoryMaster.html?catCode="+code;
				//alert(searchurl);
				 $.ajax({
					        type: "GET",
					        url: searchurl,
					        dataType: "json",
					        success: function(response)
					        {
					        	if(response.strGCode=='Invalid Code')
					        	{
					        		alert("Invalid Category Code");
					        		$("#txtCatCode").val('');
					        	}
					        	else
					        	{
						        	//$("#txtCategoryCode").val(code);
						        	$("#strCategoryName").val(response.strCategoryName);
						        	
					        	}
							},
							error: function(jqXHR, exception) {
					            if (jqXHR.status === 0) {
					                alert('Not connect.n Verify Network.');
					            } else if (jqXHR.status == 404) {
					                alert('Requested page not found. [404]');
					            } else if (jqXHR.status == 500) {
					                alert('Internal Server Error [500].');
					            } else if (exception === 'parsererror') {
					                alert('Requested JSON parse failed.');
					            } else if (exception === 'timeout') {
					                alert('Time out error.');
					            } else if (exception === 'abort') {
					                alert('Ajax request aborted.');
					            } else {
					                alert('Uncaught Error.n' + jqXHR.responseText);
					            }		            
					        }
				      });
			}
			
			
			function funloadMemberData(code)
			{
				$("#txtMemberCode").val(code);
				var searchurl=getContextPath()+"/loadWebClubMemberProfileData.html?primaryCode="+code;
				//alert(searchurl);
				 $.ajax({
					        type: "GET",
					        url: searchurl,
					        success: function(response)
					        {
					        	if(response.strMemberCode=='Invalid Code')
					        	{
					        		alert("Invalid Member Code");
					        		$("#txtMemberCode").val('');
					           	}
					        	else
					        	{  
					        		$("#txtMemberName").val(response[0].strFullName);
						        	
					        	}
							},
							error: function(jqXHR, exception) {
					            if (jqXHR.status === 0) {
					                alert('Not connect.n Verify Network.');
					            } else if (jqXHR.status == 404) {
					                alert('Requested page not found. [404]');
					            } else if (jqXHR.status == 500) {
					                alert('Internal Server Error [500].');
					            } else if (exception === 'parsererror') {
					                alert('Requested JSON parse failed.');
					            } else if (exception === 'timeout') {
					                alert('Time out error.');
					            } else if (exception === 'abort') {
					                alert('Ajax request aborted.');
					            } else {
					                alert('Uncaught Error.n' + jqXHR.responseText);
					            }		            
					        }
				      });
			}
			
			
			function funSetCategoryData(code)
			{
				$("#txtCategoryCode").val(code);
				var searchurl=getContextPath()+"/loadWebClubMemberCategoryMaster.html?catCode="+code;
				//alert(searchurl);
				 $.ajax({
					        type: "GET",
					        url: searchurl,
					        dataType: "json",
					        success: function(response)
					        {
					        	if(response.strCatCode=='Invalid Code')
					        	{
					        		alert("Invalid Category Code");
					        		$("#txtCategoryCode").val('');
					        	}
					        	else
					        	{
					        		
						        	$("#txtCategoryName").val(response.strCatName);
						        	
					        	}
							},
							error: function(jqXHR, exception) {
					            if (jqXHR.status === 0) {
					                alert('Not connect.n Verify Network.');
					            } else if (jqXHR.status == 404) {
					                alert('Requested page not found. [404]');
					            } else if (jqXHR.status == 500) {
					                alert('Internal Server Error [500].');
					            } else if (exception === 'parsererror') {
					                alert('Requested JSON parse failed.');
					            } else if (exception === 'timeout') {
					                alert('Time out error.');
					            } else if (exception === 'abort') {
					                alert('Ajax request aborted.');
					            } else {
					                alert('Uncaught Error.n' + jqXHR.responseText);
					            }		            
					        }
				      });
			}
			function funValidateFields()
			{
				var flag=true;				
				if($("#txtCompanyName").val().trim().length==0)
				{
					alert("Please Enter Company Name.");
					flag=false;
				}
				else if($("#txtMemberCode").val().trim().length==0)
				{
					alert("Please Enter Member Code.");
					flag=false;
				}
				else if($("#txtCategoryCode").val().trim().length==0)
				{
					alert("Please Enter Category Code.");
					flag=false;
				}				
				else if($("#txtAreaCode").val().trim().length==0)
				{
					alert("Please Enter Area Code.");
					flag=false;
				}				
				else if($("#txtCtCode").val().trim().length==0)
				{
					alert("Please Enter City Code.");
					flag=false;
				}				
				else if($("#txtStateCode").val().trim().length==0)
				{
					alert("Please Enter State Code.");
					flag=false;
				}				
				else if($("#txtRegionCode").val().trim().length==0)
				{
					alert("Please Enter Region Code.");
					flag=false;
				}				
				else if($("#txtCountryCode").val().trim().length==0)
				{
					alert("Please Enter Country Code.");
					flag=false;
				}				
				return flag;
			}
			
	
			function funResetFields()
			{
				location.reload(true); 
			}
	
	
</script>

</head>
<body>	
	 <div class="container">
		<label id="formHeading">Group Booking</label>
		<s:form name="CompanyMaster" method="POST" action="savePMSGroupBooking.html">
	 <br>
	<div id="multiAccordion">	
		<h3><a href="#">Group Booking Details</a></h3>
			<div>
				<div class="container transtable" style="background-color:#f2f2f2;">
					<div class="row" style="padding-bottom:12px">
						<div class="col-md-2">
							<label>Reservation ID</label>
							<s:input id="txtReservationID" ondblclick="" cssClass="searchTextBox"
								readonly="true"  type="text" path="strReservationID"></s:input>
						</div>
						<div class="col-md-2"><s:input id="" required="true" path="" 
							placeholder="" type="text" style="margin-top:23px;"></s:input>
						</div>
						<div class="col-md-2">
							<label>Group Code</label>
							<s:input id="txtGroupCode" ondblclick="" cssClass="searchTextBox"
								readonly="true"  type="text" path="strGroupCode"></s:input>
						</div>
						<label>Group Name</label>
						<div class="col-md-2"><s:input id="strGroupName" required="true" path="" 
							placeholder="Group Name" type="text" style="margin-top:23px;"></s:input>
						</div>
						<div class="col-md-2">
							<label>Group Leader</label>
							<s:input id="txtGroupLeaderCode" ondblclick="funHelp('guestCode');"  cssClass="searchTextBox" required="true"
								 type="text" path="strGroupLeaderCode"></s:input>
						</div>
						<div class="col-md-2"><s:input id="txtGroupLeaderName" required="true" path="" 
							placeholder="" type="text" style="margin-top:23px;"></s:input>
						</div>
						<div class="col-md-4">
							<label>Address</label>
							<s:input id="txtAddress" type="text" path="strAddress"></s:input>
						</div>
						<div class="col-md-2">
							<label>City</label>
							<s:input id="txtCity"  path="strCity" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Country</label>
							<s:input id="txtCountry"  path="strCountry" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Pin</label>
							<s:input id="txtPin"  path="strPin" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Phone</label>
							<s:input id="txtPhone"  path="strPhone" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Mobile</label>
							<s:input id="txtMobile"  path="strMobile" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Fax</label>
							<s:input id="txtFax"  path="strFax" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Email</label>
							<s:input id="txtEmail"  path="strEmail" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>DOB</label>
							<s:input id="txtDob"  path="dteDob" type="text" cssClass="calenderTextBox" style="width:80%;"></s:input>
						</div>
						<div class="col-md-2">
							<label>Nationality</label>
							<s:input id="txtNationality"  path="strNationality" type="text" ></s:input>
						</div>
					</div>
				</div>	
			</div>
		
									
		<h3><a href="#">Group Instructions</a></h3>
			<div>							
				<div class="container transtable" style="background-color:#f2f2f2;">
					<div class="row" style="padding-bottom:12px">
						<div class="col-md-2">
							<label>Company</label>
							<s:input id="txtCompCode" ondblclick="" cssClass="searchTextBox"
								readonly="true"  type="text" path="strCompCode"></s:input>
						</div>
						<div class="col-md-2"><s:input id="txtCompName" required="true" path="strCompName" 
							placeholder="" type="text" style="margin-top:23px;"></s:input>
						</div>
						<div class="col-md-2">
							<label>Designation</label>
							<s:input id="txtDesignation" ondblclick="" cssClass="searchTextBox"
								readonly="true"  type="text" path="strDesignation"></s:input>
						</div>
						<div class="col-md-2">
							<label>City</label>
							<s:input id="txtGICity"  path="strGICity" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Phone</label>
							<s:input id="txtGIPhone"  path="strGIPhone" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Mobile</label>
							<s:input id="txtGIMobile"  path="strGIMobile" type="text"></s:input>
						</div>
						<div class="col-md-2">
							<label>Fax</label>
							<s:input id="txtGIFax"  path="strGIFax" type="text"></s:input>
						</div>
					</div>
				</div>
			</div>
			
					
       <h3><a href="#">Arrival Info</a></h3>
		<div>		
			<div class="container transtable" style="background-color:#f2f2f2;">
				<div class="row" style="padding-bottom:12px">
					<div class="col-md-12">
						<label>Travel Details</label>
					</div>
					<div class="col-md-2">
						<label>Date</label>
						<s:input id="txtTravelDate"  path="dteTravelDate" type="text" cssClass="calenderTextBox" style="width:80%;"></s:input>
					</div>
					<div class="col-md-2">
						<label>Time</label>
						<s:input id="txtTravelTime"  path="tmeTravelTime" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Pickup Required</label>
						<s:input id="txtPickupRequired"  path="strPickupRequired" type="text"></s:input>
					</div>
					
				</div>
			</div>
		</div>
    	
       <h3><a href="#"> Stay Details</a></h3>
		<div>	
			<div class="container transtable" style="background-color:#f2f2f2;">
				<div class="row" style="padding-bottom:12px">
					<div class="col-md-2">
						<label>Check in</label>
						<s:input id="txtCheckInDate"  path="dteCheckInDate" type="text" cssClass="calenderTextBox" style="width:80%;"></s:input>
					</div>
					<div class="col-md-2">
						<label>Checkout</label>
						<s:input id="txtCheckoutDate"  path="dteCheckoutDate" type="text" cssClass="calenderTextBox" style="width:80%;"></s:input>
					</div>
					<div class="col-md-2">
						<label>Pax</label>
						<s:input id="txtPax"  path="strPax" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Source</label>
						<s:input id="txtSource"  path="strSource" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Guest Type</label>
						<s:input id="txtGuestType"  path="strGuestType" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Extra Bed</label>
						<s:input id="txtExtraBed"  path="strExtraBed" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Child</label>
						<s:input id="txtChild"  path="strChild" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Infant</label>
						<s:input id="txtInfant"  path="strInfant" type="text"></s:input>
					</div>
					<div class="col-md-2">
						<label>Sales Channel</label>
						<s:input id="txtSalesChannel"  path="strSalesChannel" type="text"></s:input>
					</div>
				</div>
			</div>
		</div>
		
			<h3><a href="#">Rate Code</a></h3>
				<div>	
					<div class="container transtable" style="background-color:#f2f2f2;">
						<div class="row" style="padding-bottom:12px">
							<div class="col-md-2">
								<label>Room tariff</label>
								<s:input id="txtRoomTariff" ondblclick="" type="text" path="strRoomTariff"></s:input>
							</div>
							<div class="col-md-2">
								<label>Board</label>
								<s:input id="txtBoard" ondblclick=""   type="text" path="strBoard"></s:input>
							</div>
							<div class="col-md-2">
								<label>Room taxes</label>
								<s:input id="txtRoomTaxes" ondblclick=""  type="text" path="strRoomTaxes"></s:input>
							</div>
							<div class="col-md-2">
								<label>Other Taxes</label>
								<s:input id="txtOtherTaxes" ondblclick=""  type="text" path="strOtherTaxes"></s:input>
							</div>
							<div class="col-md-2">
								<label>Service Charges</label>
								<s:input id="txtServiceCharges" ondblclick=""   type="text" path="strServiceCharges"></s:input>
							</div>
							<div class="col-md-2">
								<label>Payments</label>
								<s:input id="txtPayments" ondblclick=""   type="text" path="strPayments"></s:input>
							</div>
							<div class="col-md-2">
								<label>Discounts</label>
								<s:input id="txtDiscounts" ondblclick=""   type="text" path="strDiscounts"></s:input>
							</div>
							<div class="col-md-12">
								<h6>Payment Details</h6>
							</div>
							<div class="col-md-2">
								<label>Card</label>
								<s:input id="txtCard" ondblclick=""   type="text" path="strCard"></s:input>
							</div>	
							<div class="col-md-2">
								<label>Cash</label>
								<s:input id="txtCash" ondblclick=""   type="text" path="strCash"></s:input>
							</div>
						</div>
					</div>
				</div>
			
	   <h3><a href="#">Billing Instructions</a></h3>
	    <div>
	    	<div class="container transtable" style="background-color:#f2f2f2;">
				<div class="row" style="padding-bottom:12px">
					<div class="col-md-2">
						<label>Payment by Group Leader</label>
						<input type="checkbox" name="" id="txtPaymentBtGroupLeader" path="strPaymentBtGroupLeader" value="Y"/>
					</div>
					<div class="col-md-2">
						<label>Guest</label>
						<input type="checkbox" name="" id="txtGuest" path="strGuest" value="Y"/>
					</div> 
					<div class="col-md-2">
						<label>Room </label>
						 <input type="checkbox" name="" id="txtRoom" path="strRoom" value="Y"/>
					</div>
					<div class="col-md-2">
						<label>F&B</label>
						<input type="checkbox" name="" id="txtFandB" path="strFandB" value="Y"/>
					</div>
					<div class="col-md-2">
						<label>Telphones</label>
						<input type="checkbox" name="" id="txtTelephones" path="strTelephones" value="Y"/>
					</div>
					<div class="col-md-2">
						<label>Extras</label>
						<input type="checkbox" name="" id="txtExtras" path="strExtras" value="Y"/>
					</div>
				</div>
			
		
		<%-- 
	  
	    <div class="dynamicTableContainer" style="">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#c0c0c0" style="height: 24px;">
					<td align="left" style="width:">Select</td>
					<td align="left" style="width:">Reservation </td>
					<td align="right" style="width:">Room Type</td>
					<td align="right">Room No</td>
					<td align="right" style="width:">Pax</td>
					<td align="right">child</td>
					<td align="right" style="width:">Infant</td>
					<td align="right">Arrival</td>
					<td align="right" style="width:">Departure</td>
					<td align="right">Rate Code</td>
					<td align="right" style="width:">Discount</td>
					<td align="right" style="width:">R.Status</td>
					<td align="right" style="width:">Tax Exempt</td>
												
				</tr>
			</table>
			<div style="background-color: #fafbfb; border: 1px solid #ccc; display: block; height: 200px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblIncomeHeadDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<!-- col1   -->
						<col width="100%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="100%" >
						<!-- col2   -->
						
						<!-- col3   -->
						<col width="100%" >
						<!-- col3   -->
						
						<!-- col4   -->
						<col  width="10%">
						<!-- col4   -->
					</tbody>
				</table>
			</div>	
	    </div> --%>
	
	
</div></div></div>
	<!-- <p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="" />
			<input type="reset" value="Reset" class="form_button" onclick=""/>
		</p> -->
	<div class="center" style="text-align:center">
		<button class="btn btn-primary center-block" id="" onclick="">WAITLIST</button> &nbsp;
		<button class="btn btn-primary center-block" id="" onclick="">CONFIRM</button> &nbsp;
		<button class="btn btn-primary center-block" id="" onclick="">PAYMENT</button> &nbsp;
		<button class="btn btn-primary center-block" id="" onclick="">SAVE</button> &nbsp;
		<button class="btn btn-primary center-block" id="" onclick="">CANCEL</button> &nbsp;
	</div>
</s:form>                                                                              
</div>
			 
			

	

			
	
				
					  
<script type="text/javascript">
		$(function(){
			$('#multiAccordion').multiAccordion({
// 				active: [1, 2],
				click: function(event, ui) {
				},
				init: function(event, ui) {
				},
				tabShown: function(event, ui) {
				},
				tabHidden: function(event, ui) {
				}
				
			});
			
			$('#multiAccordion').multiAccordion("option", "active", [0]);  // in this line [0,1,2] wirte then these index are open
		});
	</script>
					
</body>
</html>

