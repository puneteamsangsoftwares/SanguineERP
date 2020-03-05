<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="default.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Locationwise Categorywise SO</title>
        <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap.min.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap-grid.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/newdesigncss/bootstrap-grid.min.css"/>" />
	    <script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/newdesignjs/bootstrap.min.js"/>"></script>
    
    <style>
  #tblGroup tr:hover , #tblSubGroup tr:hover, #tblloc tr:hover{
	background-color: #c0c0c0;
	
}
</style>
    <script type="text/javascript">
    
 		 //Serching on Table when user type in text field
          $(document).ready(function()
    		{
    			var tablename='';
    			
    			$('#txtSGCode').keyup(function()
    	    			{
    						tablename='#tblSG';
    	    				searchTable($(this).val(),tablename);
    	    			});
    			$('#txtLocCode').keyup(function()
    	    			{
    						tablename='#tblloc';
    	    				searchTable($(this).val(),tablename);
    	    			});	
    			
    		});

           //Searching on table on the basis of input value and table name
    		function searchTable(inputVal,tablename)
    		{
    			var table = $(tablename);
    			table.find('tr').each(function(index, row)
    			{
    				var allCells = $(row).find('td');
    				if(allCells.length > 0)
    				{
    					var found = false;
    					allCells.each(function(index, td)
    					{
    						var regExp = new RegExp(inputVal, 'i');
    						if(regExp.test($(td).find('input').val()))
    						{
    							found = true;
    							return false;
    						}
    					});
    					if(found == true)$(row).show();else $(row).hide();
    				}
    			});
    		}
    		
	    var fieldName="";
	    //Ajax Wait Image display
	    $(document).ready(function() 
    		{
    			$(document).ajaxStart(function()
    		 	{
    			    $("#wait").css("display","block");
    		  	});
    			$(document).ajaxComplete(function(){
    			    $("#wait").css("display","none");
    			  });
    		});
    
	    //Set Start Date in date picker
        $(function() 
    		{
        	var startDate="${startDate}";
			var arr = startDate.split("/");
			Dat=arr[0]+"-"+arr[1]+"-"+arr[2];      	  
        	$("#dteFromDate").datepicker({ dateFormat : 'dd-mm-yy' });
			$("#dteFromDate").datepicker('setDate', Dat);	
			
			
			$("#dteToDate").datepicker({ dateFormat : 'dd-mm-yy' });
			$("#dteToDate").datepicker('setDate', 'today');	
			
			
// 			$("#dteFromFulfillment").datepicker({ dateFormat : 'dd-mm-yy' });
// 			$("#dteFromFulfillment").datepicker('setDate', 'today');	
			
			
// 			$("#dteToFulfillment").datepicker({ dateFormat : 'dd-mm-yy' });
// 			$("#dteToFulfillment").datepicker('setDate', 'today');	
    			
    			 var strPropCode='<%=session.getAttribute("propertyCode").toString()%>';
    			 
    			 var locationCode ='<%=session.getAttribute("locationCode").toString()%>';

     			 funSetAllLocationAllPrpoerty();
    			 funSetAllSubGroup();
    			 
    			 
    		});	
      

        function update_FromFulFillmentDate(selecteDate){
    		var date = $('#dteFromDate').val();
    		$('#dteToDate').val(selecteDate);
    	}
        	
    
	  //Open Help
      function funHelp(transactionName)
		{
    	  	fieldName=transactionName;
		//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
		}
	  
	  //Set data After Seletion of Help
      function funSetData(code)
		{
			switch (fieldName) 
			{

			    case 'custcode':
			    	funSetCustomer(code);
			        break;  
			}
		}
      
   
    //Get and Set All Location on the basis of all Property
      function funSetAllLocationAllPrpoerty() {
			var searchUrl = "";
			searchUrl = getContextPath()+ "/loadAllLocationForAllProperty.html";
			$.ajax({
				type : "GET",
				url : searchUrl,
				dataType : "json",
				beforeSend : function(){
					 $("#wait").css("display","block");
			    },
			    complete: function(){
			    	 $("#wait").css("display","none");
			    },
				success : function(response) {
					if (response.strLocCode == 'Invalid Code') {
						alert("Invalid Location Code");
						$("#txtFromLocCode").val('');
						$("#lblFromLocName").text("");
						$("#txtFromLocCode").focus();
					} else
					{
						$.each(response, function(i,item)
						 		{
							funfillLocationGrid(response[i].strLocCode,response[i].strLocName);
								});
						
					}
				},
				error : function(jqXHR, exception) {
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
    
      //Fill  Location Data
	    function funfillLocationGrid(strLocCode,strLocationName)
		{
			
			 	var table = document.getElementById("tblloc");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbToLocSel."+(rowCount)+"\" name=\"Locthemes\" type=\"checkbox\" class=\"LocCheckBoxClass\"  checked=\"checked\" value='"+strLocCode+"' />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strToLocCode."+(rowCount)+"\" value='"+strLocCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strToLocName."+(rowCount)+"\" value='"+strLocationName+"' >";
		}
	  
	  
		     
	    /**
		 * Getting  SubGroup Based on Group Code Passing Value(Group Codes)
		**/
		function funSetAllSubGroup()
		{
				var searchUrl = getContextPath() + "/AllloadSubGroup.html";
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					beforeSend : function(){
						 $("#wait").css("display","block");
				    },
				    complete: function(){
				    	 $("#wait").css("display","none");
				    },
					success : function(response)
					{
						$.each(response, function(i, item) {
							
							funfillSubGroup(response[i].strSGCode,response[i].strSGName);
						});
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
				
		    //Fill Subgroup Data
		    function funfillSubGroup(strSGCode,strSGName)
			{
				
				 	var table = document.getElementById("tblSG");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbSGSel."+(rowCount)+"\" name=\"SGthemes\" type=\"checkbox\" class=\"SGCheckBoxClass\"  checked=\"checked\" value='"+strSGCode+"' />";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strSGCode."+(rowCount)+"\" value='"+strSGCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strSGName."+(rowCount)+"\" value='"+strSGName+"' >";
			}
		    //Remove All Row from Grid Passing Table Id as a parameter
		    function funRemRows(tablename) 
			{
				var table = document.getElementById(tablename);
				var rowCount = table.rows.length;
				while(rowCount>0)
				{
					table.deleteRow(0);
					rowCount--;
				}
			}
		 
			//Select All Group,SubGroup,From Location, To Location When Clicking Select All Check Box
			 $(document).ready(function () 
						{
							
							$("#chkSGALL").click(function () {
							    $(".SGCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							
							$("#chkLocALL").click(function () {
							    $(".LocCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
						
							
						});
					 
			 
	   //Submit Data after clicking Submit Button with validation 
	   function btnSubmit_Onclick()
	    {
			 
				 var strSGCode="";
					 
					 $('input[name="SGthemes"]:checked').each(function() {
						 if(strSGCode.length>0)
							 {
							 strSGCode=strSGCode+","+this.value;
							 }
							 else
							 {
								 strSGCode=this.value;
							 }
						 
						});

					 $("#hidSGCodes").val(strSGCode);
					 
					 
					 var strLocCode="";
					 
					 $('input[name="Locthemes"]:checked').each(function() {
						 if(strLocCode.length>0)
							 {
							 strLocCode=strLocCode+","+this.value;
							 }
							 else
							 {
								 strLocCode=this.value;
							 }
						 
						});
					 if(strLocCode=="")
					 {
					 	alert("Please Select Location");
					 	return false;
					 }
					 $("#hidLocCodes").val(strLocCode);
					 
					 
					 
					 
					 
					 
		    	document.forms["frmLocationWiseCategorySOReport"].submit();
		    }
	  
	    
	   //Reset All Filed After Clicking Reset Button
	    function funResetFields()
		{
			location.reload(true); 
		}
	</script>    
  </head>
  	
	<body id="LocationWiseCategorySO" onload="funOnload();">
	<div class="container transTable">
		<label id="formHeading">Location Wise Category wise SO Report</label>
 		<s:form name="frmLocationWiseCategorySOReport" method="POST" action="rptLocationWiseCategoryWiseSOReport.html" target="_blank">
	   	<br />
	   		
	   	<div class="row" >
			   <div class="col-md-2"><label>From Date</label>
				     <s:input type="text" id="dteFromDate" path="dtFromDate" required="true" class="calenderTextBox" onchange="update_FromFulFillmentDate(this.value);" style="width:70%"/>
			   </div>
			   
			   <div class="col-md-2"><label>To Date</label>
				     <s:input type="text" id="dteToDate" path="dtToDate" required="true" class="calenderTextBox" style="width:70%"/>
			   </div>				
			   <div class="col-md-8"></div>
<!-- 			<tr> -->
<!-- 				<td><label>From Fulfillment Date</label></td> -->
<%-- 				<td><s:input type="text" id="dteFromFulfillment" path="dteFromFulfillment" required="true" class="calenderTextBox" /></td> --%>
<!-- 				<td><label>To Fulfillment Date</label></td> -->
<%-- 				<td><s:input type="text" id="dteToFulfillment" path="dteToFulfillment" required="true" class="calenderTextBox" /></td>				 --%>
<!-- 			</tr> -->
				
			<div class="col-md-2"><label>Location</label>
			        <input type="text" id="txtLocCode" Class="searchTextBox" placeholder="Type to search"></input>
			</div>
			
			<div class="col-md-4"><label id="lblLocName" style="background-color:#dcdada94; width: 50%; height: 50%; margin-top:8%"></label></div>

			<div class="col-md-2"><label>SubGroupmer</label>
			      <input type="text" id="txtSGCode" 
			         Class="searchTextBox" placeholder="Type to search"></input>
			</div>
			
			<div class="col-md-4"><label id="lblSGName" style="background-color:#dcdada94; width: 50%; height: 50%; margin-top:8%"></label>
		    </div>
			
			<div class="col-md-12"></div>	
			<br>	
		    <div class="col-md-6" style="background-color: #fafbfb; border: 1px solid #ccc; display: block; height: 150px;width: 490px; overflow-x: hidden; overflow-y: scroll;">
                   <table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
						<tbody>
							<tr bgcolor="#c0c0c0">
										<td width="15%"><input type="checkbox" checked="checked" 
										id="chkLocALL"/>Select</td>
										<td width="25%"> Location Code</td>
										<td width="65%"> Location Name</td>

							  </tr>
						</tbody>
						</table>
				   <table id="tblloc" class="masterTable"
								style="width: 100%; border-collapse: separate;">

							<!-- <tr bgcolor="#72BEFC">
									

							</tr> -->
					</table>
				</div>
				
				<div class="col-md-6" style="background-color: #fafbfb; border: 1px solid #ccc; display: block; height: 150px;width: 480px; overflow-x: hidden; overflow-y: scroll;">

					<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
							<tbody>
								<tr bgcolor="#c0c0c0">
										<td width="15%"><input type="checkbox" checked="checked" 
										id="chkSGALL"/>Select</td>
										<td width="25%">To SubGroup Code</td>
										<td width="65%">To SubGroup Name</td>

								</tr>
							</tbody>
					</table>
					<table id="tblSG" class="masterTable"
								style="width: 100%; border-collapse: separate;">

							<!-- 	<tr bgcolor="#72BEFC">
									

								</tr> -->
					</table>
			 </div>
				
		     <div class="col-md-2"><label>Report Type :</label>
				     <s:select id="cmbDocType" path="strDocType" style="width:auto;">
 						 <s:option value="PDF">PDF</s:option> 
						 <s:option value="XLS">EXCEL</s:option>
<%-- 						   <s:option value="HTML">HTML</s:option> --%>
<%-- 						   <s:option value="CSV">CSV</s:option> --%>
					</s:select>
			</div>
		</div>
           <br>
			<p align="center">
				 <input type="button" value="Submit" onclick="return btnSubmit_Onclick();" class="btn btn-primary center-block" class="form_button" />
				 &nbsp;
				 <input type="button" value="Reset" class="btn btn-primary center-block" class="form_button" onclick="funResetFields()"/>			     
			</p>  
			
			<s:input type="hidden" id="hidSGCodes" path="strSGCode"></s:input>
			<s:input type="hidden" id="hidLocCodes" path="strLocationCode"></s:input>
			
			<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>
		</s:form>
		</div>
	</body>
</html>