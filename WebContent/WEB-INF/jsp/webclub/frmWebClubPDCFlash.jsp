<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
		var fieldName,gurl,listRow=0,mastercode;
	 $(document).ready(function()
					{
			
		 	$("#lbltotal").text(0);
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate").datepicker('setDate','today');
			$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtToDate").datepicker('setDate', 'today');
			
			
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
				alert("Data Save successfully\n");
			<%
			}}%>
		});
	
	 
	 function btnExecute() 
		{	
		 var fromDate = $("#txtFromDate").val();
		 var toDate = $("#txtToDate").val();
		 var cmbChequeType = $("#cmbChequeType").val();					    
		 var memCode = $("#txtMemCode").val();
		 funLoadDateWiseMemberData(fromDate,toDate,cmbChequeType,memCode);					 
		}	 
			
		function btnExport() 
		{		
			var fromDate = $("#txtFromDate").val();
		    var toDate = $("#txtToDate").val();
		    var cmbChequeType = $("#cmbChequeType").val();					    
		    var memCode = $("#txtMemCode").val();		
			window.location.href = getContextPath()+"/exportPDCSalesFlash.html?fromDate="+fromDate+"&toDate="+toDate+"&chequeType="+cmbChequeType+"&memCode="+memCode;

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
				        		$("#tab_container").css("height","380px");	
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
				        			funAddRowReceived(item[0],item[2],item[1],item[5],item[4],item[3]);
				        			totRec= parseInt(totRec)+parseInt(item[4]);
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
		function funAddRowReceived(memCode,drawnOn,chequeNo,chequeDate,chequeAmt,chequeType) 
		{   	    	    
		    var table = document.getElementById("tblDetails");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);   
		    
		    rowCount=listRow;
		    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"20%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strMemCode\" value='"+memCode+"' id=\"txtMemCode."+(rowCount)+"\" >";
			row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"25%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCode."+(rowCount)+"\" >";
		    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listPDCDtlRecieved["+(rowCount)+"].strChequeNo\" size=\"25%\"  id=\"txtChequeNo."+(rowCount)+"\" value='"+chequeNo+"'/>";	
		    row.insertCell(3).innerHTML= "<input class=\"Box\" size=\"20%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dteChequeDate\" id=\"txtChkDte."+(rowCount)+"\" value="+chequeDate+">";
		    row.insertCell(4).innerHTML= "<input class=\"Box\" size=\"25%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dblChequeAmt\" value='"+chequeAmt+"' style=\"text-align: right;\" id=\"txtAmt."+(rowCount)+"\" >";	
		    row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"20%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strChequeType\" value='"+chequeType+"' id=\"txtChequeType."+(rowCount)+"\" >";	
		    //row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strType\" value='"++""' id=\"txtRecieved."+(rowCount)+"\" >";	
			//row.insertCell(6).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"1%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRowRecieved(this)\"/>";
			   
		    listRow++;		    
		    funResetProductFieldsRecieved();		   		    
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
				        					funAddRowReceived(item[0],item[1],item[2],item[5],item[4],item[5])
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

	<div id="formHeading">
	<label>PDC Flash</label>
	</div>

<br/>
<br/>

	<s:form name="WebClubPDC" method="POST" action="saveWebClubPDC.html">

		<table class="masterTable">
			<table style="border: 0px solid black; width: 100%; height: 70%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
				<tr>
					<td>
						<div id="tab_container" style="height: 78px">
							<table class="transTable">
									<tr>					
										<td style="width: 220px;">
											<label>From Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<%-- <s:input id="txtfromDte" path="dtefromDate"  cssClass="calenderTextBox" /> --%>
											 <s:input id="txtFromDate" name="fromDate" path="dteFromDate" cssClass="calenderTextBox"/>
											 <s:errors path="dteFromDate"></s:errors>
										</td>
										</td>									
										<td ><label>To Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<%-- <s:input id="txttoDte" path="dtetoDate"  cssClass="calenderTextBox" /></td> --%>
											 <s:input id="txtToDate" name="toDate" path="dteToDate" cssClass="calenderTextBox"/>
			        						 <s:errors path="dteToDate"></s:errors>
										</td>														
									</tr>
									
									<tr>
									<td style="width: 320px;">
										<label>Cheque Type </label>&nbsp;
										<select id="cmbChequeType" path="strChequeType" class="BoxW124px">
											<option selected="selected">Received</option>
											<option>Issued</option>
										</select>				
									</td>
									<td>
										<label>Member Code </label>&nbsp;&nbsp;&nbsp;				
											<s:input type="text" id="txtMemCode" path="strMemCode"  class="searchTextBox" ondblclick="funHelp('WCmemProfileCustomer');" readonly="true"/>
											<!-- <td style="width: 120px;"><label id="lblMemCode"></label></td>	 -->	
										</td>									
									</tr>
									
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" id="btnExcecute" value="Execute"  class="form_button" onclick="btnExecute()" />
									</td>	
									<td>	
									<input type="button" id="btnExporte" value="Export"  class="form_button" onclick="btnExport()" />
									
									</td>					
								</tr>		
							</table>
							<div class="dynamicTableContainer" id="divTable" style="height: 300px; display: none; width: 99.80%;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">				
									<td style="width:6.20%;">Member Name</td>
									<td style="width:6.2%;">Drawn On</td>
									<td style="width:6.2%;">Cheque No</td>
									<td style="width:6.2%;">Cheque Date</td>
									<td style="width:6.15%;">Amount</td>
									<td style="width:6.2%;">Type</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
								<table id="tblDetails"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>			
										<col style="width:21.4%;">	
										<col style="width:21.5%;">
										<col style="width:21.5%;">
										<col style="width:21.7%;">
										<col style="width:21.5%;">
										<col style="width:17%;">
										<col style="width:2.4%;">
									</tbody>
								</table>
							</div>
						</div>						
					
					
						</div>
					</td>
				</tr>
			</table>					
							
			</table>
		<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<label id="lbltotal"></label>
		<!-- <p align="center">
				<input type="submit" value="Submit" onclick="return funValidate();" class="form_button" />
				&nbsp; &nbsp; &nbsp; 
				<input type="reset" value="Reset"
					class="form_button" onclick="funResetField()" />
			</p> -->
			<br>
			<br>
			
			
	</s:form>
</body>
</html>
