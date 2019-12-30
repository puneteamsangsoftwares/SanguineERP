<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap.min.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap-grid.min.css"/>" />
	 	 
		<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.min.js"/>"></script>
<style>
.transTable td{
	border-style:none;}
	
	input[type=text] {
	width:90%;
}

</style>	

<script type="text/javascript">
		var fieldName,gurl,listRow=0,mastercode;
	 $(document).ready(function()
					{
			
		 	$("#lbltotal").text(0);
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate").datepicker('setDate','today');
			$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtToDate").datepicker('setDate', 'today');
			
			$("#btnSubmit").click(function(event) {
				
				if($("#cmbChequeType").val().includes("Issued"))
					{
					window.open(getContextPath()+"/frmPayment.html?",'_blank');
					}
				
			});
			
		 	$("#txtMemCode").val('');
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
				alert("Data Save successfully\n"+message);
			<%
			}}%>
			
			var code='';
			<%
			if(null!=session.getAttribute("rptVoucherNo"))
			{%>
				code='<%=session.getAttribute("rptVoucherNo").toString()%>';
				code;
				<%session.removeAttribute("rptVoucherNo");%>
				var codeArr = code.split(" ");
				for(var i=0;i<codeArr.length;i++)
				{
					var codeNew = codeArr[i];
					if(!codeNew==""){
					var isOk=confirm("Do You Want to Generate Slip?");
					if(isOk)
					{
						window.open(getContextPath()+"/openRptReciptReport.html?docCode="+codeNew,'_blank');
					}
					}
				}
				
						
					
			<%}%>
			code;
			$("#chkBill").click(function ()
					{
					    $(".suppCheckBoxClass").prop('checked', $(this).prop('checked'));
					});
		});
	
	 
	 function btnExecute() 
		{	
		 var flag=false;
		 var fromDate = $("#txtFromDate").val();
		 var toDate = $("#txtToDate").val();
		 var cmbChequeType = $("#cmbChequeType").val();					    
		 var memCode = $("#txtMemCode").val();
		 funLoadDateWiseMemberData(fromDate,toDate,cmbChequeType,memCode);	
		 return flag;
		}	 
			
		function btnExport() 
		{		
			
			var fromDate = $("#txtFromDate").val();
		    var toDate = $("#txtToDate").val();
		    var cmbChequeType = $("#cmbChequeType").val();					    
		    var memCode = $("#txtMemCode").val();		
			window.location.href = getContextPath()+"/exportPDCSalesFlash.html?fromDate="+fromDate+"&toDate="+toDate+"&chequeType="+cmbChequeType+"&memCode="+memCode;
			return false;
		}
		
		function funLoadDateWiseMemberData(fromDate,toDate,cmbChequeType,memCode){		 
			var searchurl=getContextPath()+"/loadDateWiseMemberData.html?fromDate="+fromDate+"&toDate="+toDate+"&chequeType="+cmbChequeType+"&memCode="+memCode;
			$.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response[0]=='Invalid Code')
				        	{
				        		alert("Data Not Present");
				        		$("#txtDrawnOn").val('');
				        	}
				        	else
				        	{	
				        		$("#divTable").css("display","block");
				        		$("#tab_container").css("height","500px");	
				        		var totRec=0;
				        		var table = document.getElementById("tblDetails");
				    			var rowCount = table.rows.length;
				    			while(rowCount>0)
				    			{
				    				table.deleteRow(0);
				    				rowCount--;
				    			}
				        		$.each(response, function(cnt,item)
					 			{ 
				        			funAddRowReceived(item.strMemName,item.strDrawnOn,item.strChequeNo,item.dteChequeDate,item.dblChequeAmt,item.strType,item.strAccCode);
				        			totRec= parseInt(totRec)+parseInt(item.dblChequeAmt);
								    $("#lbltotal").text(totRec);

					 			});	
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
	  	 
		 /*
		 * Check duplicate record in grid
		 */
		function funDuplicateProduct(strFacilityCode)
		{
		    var table = document.getElementById("tblDetails");
		    var rowCount = table.rows.length;		   
		    var flag=true;
		    if(rowCount > 0)
		    	{
				    $('#tblDetails tr').each(function()
				    {
					    if(strFacilityCode==$(this).find('input').val())// `this` is TR DOM element
	    				{
					    	alert("Already added "+ strFacilityCode);
					    	 funResetProductFields();
		    				flag=false;
	    				}
					});
				    
		    	}
		    return flag;
		  
		}
		
		/**
		 * Adding Product Data in grid 
		 */
		function funAddRowReceived(memCode,drawnOn,chequeNo,chequeDate,chequeAmt,chequeType,checkboxValue,accountCode) 
		{   	    	    
		    var table = document.getElementById("tblDetails");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);   
		    
		    rowCount=listRow;
		    row.insertCell(0).innerHTML= "<input class=\"Box\" readonly=\"true\" size=\"20%\" name=\"listReceiptBean["+(rowCount)+"].strDebtorName\" \ value='"+memCode+"'  id=\"txtMemCode."+(rowCount)+"\" >";
			row.insertCell(1).innerHTML= "<input class=\"Box\" readonly=\"true\" size=\"25%\" name=\"listReceiptBean["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCode."+(rowCount)+"\" >";
		    row.insertCell(2).innerHTML= "<input class=\"Box\" readonly=\"true\" type=\"text\" name=\"listReceiptBean["+(rowCount)+"].strChequeNo\" size=\"25%\" style=\"text-align: right;\" id=\"txtChequeNo."+(rowCount)+"\" value='"+chequeNo+"'/>";	
		    row.insertCell(3).innerHTML= "<input class=\"Box\" readonly=\"true\" size=\"20%\" name=\"listReceiptBean["+(rowCount)+"].dteChequeDate\"  id=\"txtChkDte."+(rowCount)+"\" value="+chequeDate+">";
		    row.insertCell(4).innerHTML= "<input class=\"Box\" readonly=\"true\" size=\"25%\" name=\"listReceiptBean["+(rowCount)+"].dblAmt\" value='"+chequeAmt+"' style=\"text-align: right;\" id=\"txtAmt."+(rowCount)+"\" >";	
		    row.insertCell(5).innerHTML= "<input class=\"Box\" readonly=\"true\" size=\"20%\" name=\"listReceiptBean["+(rowCount)+"].strReceiptType\" value='"+chequeType+"' id=\"txtChequeType."+(rowCount)+"\" >";	
		    row.insertCell(6).innerHTML= "<input id=\"checkBox."+(rowCount)+"\" type=\"checkbox\" checked=\"checked\" class=\"suppCheckBoxClass\" name=\"listReceiptBean["+(rowCount)+"].strTransMode\"  value='"+checkboxValue+"' />";
		    row.insertCell(7).innerHTML= "<input name=\"listReceiptBean["+(rowCount)+"].strSancCode\" type=\"hidden\" value = '"+checkboxValue+"' >";

		    
		    listRow++;		    
		    funResetProductFieldsRecieved();		   		    
		}
		function funCheckboxCheck(data)
		{
			var id=data.id;
			var value=data.checked;
			data.defaultValue=value;
			
			var i=id.split(".")[1];
			var id1="listReceiptBean."+i+".strTransMode";
			id1.defaultValue=value;
			//$("#"+id+"").val(value);
			alert();
		}
		
		/**
		 * Delete a particular record from a grid
		 */
		function funDeleteRowRecieved(obj)
		{
		    var index = obj.parentNode.parentNode.rowIndex;
		    var table = document.getElementById("tblDetails");
		    table.deleteRow(index);
		}
		
		/**
		 * Remove all product from grid
		 */
		 function funRemProdRows()
	    {
			var table = document.getElementById("tblDetails");
			var rowCount = table.rows.length;
			for(var i=rowCount;i>=0;i--)
			{
				table.deleteRow(i);
			}
	    } 
		

		/**
		 * Clear textfiled after adding data in textfield
		 */
		function funResetProductFieldsRecieved()
		{
			$("#txtChequeNo").val('');
			$("#txtAmt").val('');
			$("#txtDrawnOn").val('');
		}
		
		
		/* function funRemoveProductRows()
		{
			var table = document.getElementById("tblDetails");
			var rowCount = table.rows.length;
			while(rowCount>0)
			{
				table.deleteRow(0);
				rowCount--;
			}
		}
		 */
		
		
	
		
	 
	 function funHelp(transactionName)
		{	       
			fieldName=transactionName;
	    //    window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        
	    }
	 
	 function funResetFields()
		{
			location.reload(true); 
		}	 
	 function funValidate()
		{
		 	flag=true;
		 	var table = document.getElementById("tblDetails");
		    var rowRecieved = table.rows.length;		    	
		    	/* if($('#txtMemCode').val()=='')
		    		{
		    			flag=false;
		    			alert("Please Enter Data");
		    		} */
		 	return flag;
			
		}	
	 
	 
	 function funSetData(code)
		{
		 switch(fieldName)
		 	{

			case 'WCmemProfileCustomer' :
				funSetMemberDataReceived(code);				
				break;				
						
			}
		}
	 
		
	 function funSetMemberDataReceived(code){
		 
		 $("#txtFacilityCode").val(code);
			var searchurl=getContextPath()+"/loadWebClubMemberProfileData.html?primaryCode="+code;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Category Code");
				        		$("#txtMemCode").val('');
				        	}
				        	else
				        	{
				        		$("#txtMemCode").val(response[0].strMemberCode);
					        	$("#lblMemCode").text(response[0].strFirstName);
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
	 
	 function funSetMemberTableReceived(code)
	 {
		 var searchurl=getContextPath()+"/loadPDCMemberWiseData.html?memCode="+code;
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Member Code");
				        		$("#txtMemCode").val('');
				        	}
				        	else
				        	{				        		
				        		var table=document.getElementById("tblDetails");
				    			var rowCount=table.rows.length;
				    			while(rowCount>0)
				    			{table.deleteRow(0);
				    			   rowCount--;
				    			}
				        		$.each(response, function(cnt,item)
					 					{				        					
				        					funAddRowReceived(item[0],item[1],item[2],item[5],item[4],item[5],"Y",item[6])
				        				});						        						        	
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
	 
	 
</script>

</head>
<body>
	<div class="container-fuild">
		<label id="formHeading">PDC To Receipt</label>
			<s:form name="PDCToReceipt" method="POST" action="savePDCToReceipt.html">
					<table class="masterTable">
						<table style="border:0px solid black; width: 100%; height: 70%; margin:0 auto; overflow-y: hidden;">
							<tr>
								<td>
									<div id="tab_container" style="height: auto;">
										<div class="transTable" style="padding:10px; overflow-x: hidden; overflow-y: hidden;">
												<div class="row">
													<div class="col-md-3">
													<label>From Date:</label><s:input id="txtFromDate"
														 type="text" cssClass="calenderTextBox" path="dteFromDate"></s:input> <s:errors path="dteFromDate"></s:errors>
													</div>
													<div class="col-md-3">
													
													<label id="lblCityName">Cheque Type:</label><select id="cmbChequeType" type="text" path="strChequeType" >
														<option selected="selected">Received</option>
														<option>Issued</option>
													</select>
													</div>
													
													<div class="col-md-3">
													<label>To Date:</label><s:input id="txtToDate"  cssClass="calenderTextBox"
														type="text" path="dteToDate" /><s:errors path="dteToDate"></s:errors>
													</div>
													
												</div>
												<div class="center">
													<a href="#"><button class="btn btn-primary center-block" type="text" 
									  					class="form_button" id="btnExcecute" onclick="return btnExecute()">Execute</button></a>
													<!-- <a href="#"><button class="btn btn-primary center-block" type="text"  
						  								class="form_button" id="btnExporte" onclick="return btnExport()">Export</button></a> -->
												</div>
										</div>
													
										<div class="dynamicTableContainer" id="divTable" style="height: 300px; display: none; width: 100%;">
										<table
											style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold; background:#b5b1b1;">
											<tr>				
												<td style="width:6.20%;">Member Name</td>
												<td style="width:6.2%;">Drawn On</td>
												<td style="width:6.2%;">Cheque No</td>
												<td style="width:6.2%;">Cheque Date</td>
												<td style="width:6.15%;">Amount</td>
												<td style="width:6.2%;">Type</td>
												<td style="width:3%;">Select <input type="checkbox" id="chkBill" /></td>
												<!-- <td style="width:6.2%;"></td> -->
											</tr>
										</table>
										
										<div style="border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: auto; width: 99.80%;">
											<table id="tblDetails"
												style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
												class="transTablex col8-center">
												<tbody>			
													<col style="width:18.4%;">	
													<col style="width:21.5%;">
													<col style="width:21.5%;">
													<col style="width:21.7%;">
													<col style="width:21.5%;">
													<col style="width:21%;">
													<col style="width:2.4%;">
													<col style="width:21%;">
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
						</td>
					</tr>
				</table>	
			</table>
		<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<label id="lbltotal"></label>
		 <p align="center">
				<input type="submit" value="Submit" id="btnSubmit" onclick="return funValidate();" class="form_button" />
				&nbsp; &nbsp; &nbsp; 
				<input type="reset" value="Reset"
					class="form_button" onclick="funResetField()" />
			</p> 
			<br>
			<br>
			
			
	</s:form>
	</div>
</body>
</html>
