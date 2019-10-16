<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<script src="/resources/js/less.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<spring:url  value="/resources/css/styles/styles.css"/> "/>
<title><tiles:insertAttribute name="title" ignore="true"></tiles:insertAttribute>
</title>
<script>
      function showPopup() {
        document.getElementById('popover').style.cssText = 'display: block';
      }
</script>
</head>
<body>
	<div class="main-app">
       <div class="title-row">
         <div class="title">Room Status Diary</div>
         <div class="status-list">
           <ul>
             <li>
               <div class="thumb occupied"></div>
               <div class="thumb-title">Occupied</div>
             </li>
             <li>
               <div class="thumb booked"></div>
               <div class="thumb-title">Booked</div>
             </li>
             <li>
               <div class="thumb onhold"></div>
               <div class="thumb-title">On Hold</div>
             </li>
             <li>
               <div class="thumb cleaning"></div>
               <div class="thumb-title">Cleaning</div>
             </li>
             <li>
               <div class="thumb repaires"></div>
               <div class="thumb-title">Repaires</div>
             </li>
           </ul>
         </div>
       </div>
       <div class="app-calender">
         <div class="day-wrap"> 
           <div class="date">16 October 2019</div>
           <div class="date-actions"></div>
         </div>
         <div class="room-week-wrap">
           <div class="rooms">
             <div class="title">Rooms</div>
             <div class="room-accordian">
               <div class="room-category">
                 <div class="cell-text">Deluxe </div>
                 <ul class="accordian-data">
                   <li>Room 1</li>
                   <li>Room 2</li>
                   <li>Room 3</li>
                   <li>Room 4</li>
                   <li>Room 5</li>
                   <li>Room 6</li>
                   <li>Room 7</li>
                   <li>Room 8</li>
                 </ul>
               </div>
             </div>
           </div>
           <div class="week-view">
             <div class="day-column">
               <div class="column-info">
                 <div class="column-date"> Sunday, 11</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Monday, 12</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Tuesday, 13</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Wednesday, 14</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Thursday, 15</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Friday, 16</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>
               <div class="column-info">
                 <div class="column-date"> Saturday, 16</div>
                 <div class="slot-wrap">
                   <div class="slot-time">11 AM</div>
                   <div class="slot-time">2 PM</div>
                 </div>
               </div>                                                      
             </div>
             <div class="grid-cell">
               <div class="day-room-row">
                 <div class="bookings">
                   <div class="booking1 booking occupied">
                     <div class="booking-info" onclick="showPopup();">
                       <p class="bookedby">Tyler Rhodes</p>
                       <span class="notification">3</span>
                     </div>
                     <div class="tooltip-info">
                       <div class="title"> Room 1</div>
                       <div class="info-row">
                         <label for="">Check in Date : </label>
                         <span> 11/08/2019</span>
                       </div>
                       <div class="info-row">
                         <label for="">Check out Date : </label>
                         <span> 13/08/2019</span>
                       </div>
                       <div class="info-row">
                         <label for="">Room Status : </label>
                         <span> Occupied </span>
                       </div>
                     </div>
                   </div>
                 </div>
                 <div class="empty-cell-wrap">
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                 </div>
               </div>
               <div class="day-room-row">
                 <div class="bookings">
                   <div class="booking1 booking onhold">
                     <div class="booking-info" onclick="showPopup();">
                       <p class="bookedby">Peter Parker</p>
                       <span class="notification">3</span>
                     </div>
                     <!-- <div class="tooltip-info">
                       <div class="title"> Room 1</div>
                       <div class="info-row">
                         <label for="">Check in Date : </label>
                         <span> 11/08/2019</span>
                       </div>
                       <div class="info-row">
                         <label for="">Check out Date : </label>
                         <span> 13/08/2019</span>
                       </div>
                       <div class="info-row">
                         <label for="">Room Status : </label>
                         <span> Occupied </span>
                       </div>
                     </div> -->
                   </div>
                 </div>
                 <div class="empty-cell-wrap">
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                   <div class="empty-cell"></div>
                 </div>
               </div>
             </div>
           </div>
         </div>
       </div>
     </div>
     
     <!-- ----- Pop up ----------------- -->
      <div class="popup-details" id="popover">
        <div class="popup-backdrop">
          <div class="popup-data">
            <div class="cust-info-wrap">
              <div class="cust-name-img">
                <div class="profile-image">
                  <img src="/resources/images/user.jpg" alt="user">
                </div>
                <div class="display-name">
                  <div class="name">Tyler Rhodes</div>
                  <div class="room">Room 1</div>
                </div>
                <div style="clear: both"></div>
              </div>
              <div class="info-tabs">
                <ul>
                  <li class="active"><a href="#">Checkout Details</a></li>
                  <li><a href="#">Customer Details</a></li>
                  <li><a href="#">Requests</a></li>
                </ul>
                <div class="checkout-tab-data">
                  <div class="checkout-grid">
                    <div class="grid-row">
                        <div class="grid-data">
                            <div class="icon">@</div>
                            <div class="data-info">
                              <div class="attr">Check in Date </div>
                              <div class="value">11/08/2019</div>
                            </div>
                        </div>
                        <div class="grid-data">
                          <div class="icon">@</div>
                          <div class="data-info">
                            <div class="attr">Check out Date </div>
                            <div class="value">13/08/2019</div>
                          </div>
                      </div>
                    </div>
                    <div class="grid-row">
                      <div class="grid-data">
                        <div class="icon">@</div>
                        <div class="data-info">
                          <div class="attr">Registration Number </div>
                          <div class="value">01RH000RI45</div>
                        </div>
                      </div>
                      <div class="grid-data">
                        <div class="icon">@</div>
                        <div class="data-info">
                          <div class="attr">Folio Number </div>
                          <div class="value">F0000551</div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="billing-wrap">
                    <div class="heading">
                      <div class="title">Receipt</div>
                      <div class="action">Print Receipt</div>
                    </div>
                    <div class="billing-details">
                      <div class="distribution">
                        <div class="bill-row">
                          <div class="label">Basic Cost per night</div>
                          <div class="cost">Rs. 3240</div>
                        </div>
                        <div class="bill-row">
                          <div class="label">X2 Nights</div>
                          <div class="cost">Rs. 6840</div>
                        </div>
                        <div class="bill-row">
                          <div class="label">GST (5%)</div>
                          <div class="cost">Rs. 342</div>
                        </div>
                        <div class="bill-row">
                          <div class="label">Cleaning Fee</div>
                          <div class="cost">Rs. 870</div>
                        </div>
                        <div class="bill-row">
                          <div class="label">High season charges</div>
                          <div class="cost">Rs. 640</div>
                        </div>
                      </div>
                      <div class="bill-total">
                        <div class="label">Total</div>
                        <div class="cost">Rs. 8640</div>
                      </div>
                      <div class="cnfm-action">
                        <button>Confirm Checkout</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
	 
</body>
</html>
