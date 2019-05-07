<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Wayfinder</title>
   <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">
  
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
  		
  		
      <nav class="breadcrumb" aria-label="breadcrumbs">
        <ul>
          <li><a href="#">Home</a></li>
          <li><a href="/process?id=${task.processId}">Patron Requests</a></li>
          <li class="is-active">
            <div class="field" style="padding:10px">
              <div class="control">
                <div class="select">
                  <select onchange="choseStatus(event)">
                   <c:forEach var="t" items="${tasklist}">
                    <option  ${t.id == task.id ? "SELECTED" : ""} value=${t.id} >${t.title}</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </div>
          </li>
        </ul>
      </nav>

      <nav class="level">
        <!-- Left side -->
        <div class="level-left">
          <div class="level-item">
            <h1 class="title is-two">${task.title}</h1>
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
        </div>
      </nav>

    </div>
    <nav class="level">
    </nav>
    <div class="container">
      <nav class="level">
        <!-- Left side -->
        <div class="level-left">

          <div class="level-item">
            <div class="field has-addons">
              <p class="control">
                <input class="input" type="text" placeholder="search...coming soon">
              </p>
              <p class="control">
                <button class="button">
                      Search
                    </button>
              </p>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </section>
  
    <div class="container">
    <table id="sortabletable" border="1" style="font-size:75%" class="table is-fullwidth table is-striped is-hoverable">

      <thead>
        <tr>
          <th>Title</th>
          <th>Requester</th>
          <th>Author</th>
          <th>Barcode</th>
          <th>ISSN</th>
          <th>OCLC</th>
          <th>Request Date</td>
        </tr>
      </thead>

      <tbody>
       <c:forEach var="wip" items="${wiplist}">
	       <tr class="is-narrow" onclick="window.location='details?wipid=${wip.id}'">
	          <td width="300px">${wip.keyValues['title']}</td>
	          <td>${wip.keyValues['requester']}</td>
	          <td></td>
	          <td></td>
	          <td></td>
	          <td></td>
	          <td><fmt:formatDate value="${wip.createDate}" pattern="yyyy-MM-dd"/></td>
	        </tr>
       </c:forEach>
      </tbody>
     </table>
    </div>
    
    <br>


  



 <!-- MODAL NEW REQUEST FORM -->
  
    <jsp:include page="new.jsp"/>
 	
    
 <!-- END MODAL -->
 
 
 <script src="https://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>

 
 
  
	<script>
	
	//TODO PUT THESE FUNCTIONS IN ONE JS FILE - THEY ARE REPEATED
	
	function closeFormModal(e,elementid) {
	
		  e.preventDefault();
		  var html = document.documentElement;
		  var target = document.getElementById(elementid);
		  target.classList.remove('is-active');
		  html.classList.remove('is-clipped');
	
	}

	function openFormModal(elementid) {
	  var html = document.documentElement;
	  var target = document.getElementById(elementid);
	  target.classList.add('is-active');
	  html.classList.add('is-clipped');
	}

	</script>
	


</body>


<script>
  document.addEventListener('DOMContentLoaded', function() {

    // Get all "navbar-burger" elements
    var $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

    // Check if there are any navbar burgers
    if ($navbarBurgers.length > 0) {

      // Add a click event on each of them
      $navbarBurgers.forEach(function($el) {
        $el.addEventListener('click', function() {

          // Get the target from the "data-target" attribute
          var target = $el.dataset.target;
          var $target = document.getElementById(target);

          // Toggle the class on both the "navbar-burger" and the "navbar-menu"
          $el.classList.toggle('is-active');
          $target.classList.toggle('is-active');

        });
      });
    }

  });
</script>


<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous">
</script>

<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.16/css/jquery.dataTables.css">

<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>

<script>
  $(document).ready(function() {
    $('#sortabletable').DataTable({
      paging: false,
      searching: false,
      "bInfo": false
    });
  });
  
  function choseStatus(event) {
	//console.log(event.target[event.target.selectedIndex].value);
	var selectedTask = event.target[event.target.selectedIndex].value;
	window.location="task?accountId=1&processId=" + ${task.processId} + "&taskId=" + selectedTask;
  }
  
</script>


<script>
//TODO PUT THESE FUNCTIONS IN ONE JS FILE - THEY ARE REPEATED
function createNewProcess() {
	   var formData = $("#create").serialize();
	   $.ajax({
	        type: "POST",
	        dataType: 'json',
	        data: formData,
	        url: "/service/wip/1/createWip",
	        success: requestSuccessful,
	        error: problem
	     });
}

function problem(response) {
	$('#errormsg').html(response.responseText + "  <a href='/login'>Login page</a>");
	$('#errormsg').addClass('is-danger');
	$('#errormsg').removeClass('is-hidden');
	$('#errormsgcol').removeClass('is-hidden');
	
}

function requestSuccessful() {
	closeFormModal(event,'newRequestModal');
	$('#errormsg').html("new request successful");
	$('#errormsg').removeClass('is-danger');
	$('#errormsg').addClass('is-primary');
	$('#errormsg').removeClass('is-hidden');
	$('#errormsgcol').removeClass('is-hidden');

}



</script>




<script type="text/javascript">
		var webSocket = new WebSocket("ws://localhost:8098/broadcastwebsocketendpoint");
		//var echoText = document.getElementById("echoText");
		var echoTwo = document.getElementById("errormsg");
		//echoText.value = "";
		var message = document.getElementById("message");
		webSocket.onopen = function(message){ wsOpen(message);};
		webSocket.onmessage = function(message){ wsGetMessage(message);};
		webSocket.onclose = function(message){ wsClose(message);};
		webSocket.onerror = function(message){ wsError(message);};
		function wsOpen(message){
			//echoText.value += "Connected ... \n";
		}
		function wsSendMessage(){
			webSocket.send(message.value);
			//echoText.value += "Message to the server : " + message.value + "\n";
			message.value = "";
		}
		function wsCloseConnection(){
			webSocket.close();
		}
		function wsGetMessage(message){
			//echoText.value += message.data + "\n";
			//var oldMessage = echoTwo.innerHTML;
			//echoTwo.innerHTML = message.data + "<br>" + oldMessage;
			console.log(message);
			$('#errormsg').html(message.data);
			$('#errormsg').removeClass('is-danger');
			$('#errormsg').addClass('is-primary');
			$('#errormsg').removeClass('is-hidden');
			$('#errormsgcol').removeClass('is-hidden');
		}
		function wsClose(message){
			//echoText.value += "Disconnect ... \n";
		}

		function wserror(message){
			//echoText.value += "Error ... \n";
		}
		


	</script>


</html>
