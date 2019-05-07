<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Wayfinder: Patron Requests</title>
  <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">
  
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
  
</head>





<body class="layout-default">



  <!-- NAVIGATION -->
    <jsp:include page="menu.jsp"/>
  <!-- END -->


  <section class="section">
    <div class="container">
    
        <div id="errormsgcol" class="columns is-hidden">
  			<div class="column is-three-fifths">
 			<div id="errormsg" class="notification is-danger is-hidden"></div>
			<span class="payment-errors is-hidden"></span>
 		 	</div>
        	<div class="column is-one-fifths">
      		</div>
  		</div>


      <nav class="level">
        <!-- Left side -->
        <div class="level-left">
          <div class="level-item">
            <h1 class="title is-two">Patron Requests</h1>
          </div>
        </div>
        <!-- Left side -->
        <div class="level-right">
          <div class="level-item">
            <a class="button is-info" onclick="openFormModal('newRequestModal')" >
                 <span class="icon is-small">
      				<i class="fa fa-plus"></i>
      			</span>  
            	<span>New Request</span>
            </a>
          </div>
           <div class="level-item">
            <a class="button is-info is-outlined" href="javascript:window.location.reload(false)"> 
            	<span class="icon is-small">
      				<i class="fa fa-refresh"></i>
      			</span>
    			<span>Refresh</span>
    		</a>
          </div>
        </div>
      </nav>

	<nav class="level"></nav>

	<div class="columns">

	  <div class="column">
       <table border="1" class="table is-fullwidth table is-striped is-hoverable">
        <thead>
          <tr>
            <th>Status</th>
            <th style="text-align:right">Count</th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="map" items="${summary}">
        
          <tr class="is-narrow">
            <td width="300px">
            	<a href="/task?accountId=${accountid}&processId=${processid}&taskId=${map.currentTaskId}">${map.title}</a>
            </td>
            <td style="text-align:right">${map.count}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
     </div>

</div>




    </div>
  </section>
  
  
  <!-- MODAL NEW REQUEST FORM -->
  
    <jsp:include page="new.jsp"/>
 	
    
 <!-- END MODAL -->

  	<script src="https://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
    <script src="js/custom.js"></script>

</body>


</html>
