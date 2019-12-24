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
	 	
	 	<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/pagination.js"/>"></script>

<script type="text/javascript">
$(function() {

	
		$("#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtFromDate" ).datepicker('setDate', 'today');
		$("#txtToDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtToDate" ).datepicker('setDate', 'today');
});


//Execute button 

$(document).ready(function() {
	
	$("#btnExecute").click(function(event)
			{

if($("#cmbType").val()=="Detail")
	{

		funCalculatePettyCashFlashDetail();
	}
else
	{
	funCalculatePettyCashFlasSummary();
	}
	
	});
			

});

				
function funCalculatePettyCashFlashDetail()
{
	var fromDat=$("#txtFromDate").val();
	var toDat=$("#txtToDate").val();
	var searchUrl=getContextPath()+"/rptPettyCashFlashDetail.html?fromDat="+fromDat+"&toDat="+toDat;

}
	
	
function funCalculatePettyCashFlasSummary()
{
	var fromDat=$("#txtFromDate").val();
	var toDat=$("#txtToDate").val();
	var searchUrl=getContextPath()+"/rptPettyCashFlashummary.html?fromDat="+fromDat+"&toDat="+toDat;

	
	
	
	
	}	

		
</script>
<body>
	<div class="container">
		<label id="formHeading">Petty Cash Flash</label>
			<s:form name="Petty Cash Flash" method="GET" action="" >				
				<div class="row transTable">
					<div class="col-md-6">
						<div class="row">
							<div class="col-md-6">
								<label>From Date:</label><br>
								<s:input  type="text" id="txtFromDate" cssClass="calenderTextBox" readonly="true"
									 path="dteFromDate" required="true" />
							</div>
							<div class="col-md-6">
								<label>To Date </label>
								<s:input id="txtToDate" type="text" path="dteToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
							</div>
						</div> 
					</div>
					<div class="col-md-6">
						<div class="row">
							<div class="col-md-6">
								<label>Type:</label><br>
									<s:select id="cmbType" cssClass="combo1" cssStyle="width:125px;height:25px;overflow:scroll" path="">
										<option value="Detail">Detail</option>
										<option value="Summary">Summary</option>
									</s:select>
							</div>
							<div class="col-md-6"></div>
						</div> 
					</div>
				</div>
				<p align="center">
					<input type="button" id="btnExecute" value="Excecute" class="btn btn-primary center-block" class="form_button" />
				</p>
				<dl id="Searchresult" style="width: 95%; margin-left: 26px; overflow:auto;"></dl>
					<div id="Pagination" class="pagination" style="width: 80%;margin-left: 26px;">
						<s:input type="hidden" id="hidSubCodes" path=""></s:input>	<%-- strCatCode --%>
					</div>
					
					<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
						<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
					</div>
				
			</s:form>
		</div>


<%-- <s:form name="Petty Cash Flash" method="GET" action="" >
		<div>
			<table class="transTable">
			    <tr>
					<td width="10%"><label>From Date </label></td>
					<td width="10%" colspan="1"><s:input id="txtFromDate" path="dteFromDate" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="10%"><label>To Date </label></td>
					<td width="10%"><s:input id="txtToDate" path="dteToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					</td>
					
					<td><label>Type</label> </td>
				<td><s:select id="cmbType" cssClass="combo1" cssStyle="width:125px;height:20px;overflow:scroll" path="">
					<option value="Detail">Detail</option>
					<option value="Summary">Summary</option>
				
					</s:select></td>
				</tr>
				
				

			</table>
			
			<br />
			<br />
			<p align="center">
				<input type="button" id="btnExecute" value="Excecute"  class="form_button" />
				
				
			</p>
			
			
			
			<br/>
			<br/>
			
				
			<dl id="Searchresult" style="width: 95%; margin-left: 26px; overflow:auto;"></dl>
		<div id="Pagination" class="pagination" style="width: 80%;margin-left: 26px;">
		<s:input type="hidden" id="hidSubCodes" path="strCatCode"></s:input>	
		</div>
		
			<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
				<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
			</div></div>
	</s:form>

 --%>


</body>
</html>