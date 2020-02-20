<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
	var fieldName;

	$(function() 
	{
	});

	function funSetData(code){

		switch(fieldName){

		}
	}





	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Weekend Master</label>
	</div>

<br/>
<br/>

	<s:form name="BanquetWeekendMaster" method="POST" action="saveBanquetWeekendMaster.html">

		<table class="masterTable">
		<tr>
		<td>
				<label>Days</label>
				</td>
		</tr>
			<tr>
				<td>
					<label>Sunday</label>
					<!-- <input type="checkbox" name="chkSunday" value="sunday">  -->
					<td><s:checkbox id="chkSunday" name="chkSunday" path="strSunday" value="Sunday" /></td> 
				
				</td>
				
				<td>
					<label>Monday</label>
					<!-- <input type="checkbox" name="chkMonday" value="monday"> -->
					<td><s:checkbox id="chkMonday" name="chkMonday" path="strMonday" value="Monday" /></td> 
				 
				</td>
				
			</tr>
			<tr>
				<td>
					<label>Tuesday</label>
					<!-- <input type="checkbox" name="chkTuesday" value="tuesday">  -->
					<td><s:checkbox id="chkTuesday" name="chkTuesday" path="strTuesday" value="Tuesday" /></td> 
				
				</td>
				<td>
					<label>Wednesday</label>
					<!-- <input type="checkbox" name="chkWednesday" value="wednesday">  -->
					<td><s:checkbox id="chkWednesday" name="chkWednesday" path="strWednesday" value="Wednesday" /></td> 
				
				</td>
			</tr>
			<tr>
				<td>
					<label>Thursday</label>
					<!-- <input type="checkbox" name="chkThursday" value="thursday">  -->
					<td><s:checkbox id="chkThursday" name="chkSunday" path="strThursday" value="Thursday" /></td> 
				
				</td>
				<td>
					<label>Friday</label>
					<!-- <input type="checkbox" name="chkFriday" value="friday">  -->
					<td><s:checkbox id="chkFriday" name="chkFriday" path="strFriday" value="Friday" /></td> 
				
				</td>
			</tr>
			<tr>
				<td>
					<label>Saturday</label>
					<!-- <input type="checkbox" name="chkSaturday" value="saturday">  -->
					<td><s:checkbox id="chkSaturday" name="chkSaturday" path="strSaturday" value="Saturday" /></td> 
				
				</td>
				
				
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
