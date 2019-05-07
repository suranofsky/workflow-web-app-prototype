<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Wayfinder: ${wip.keyValues['title']} ...</title>
  <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">


  <link href="https://fonts.googleapis.com/css?family=Special+Elite" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Old+Standard+TT" rel="stylesheet">

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
          <li><a href="/process?id=${process.id}">${process.title}</a></li>
          <li><a href="/task?accountId=1&processId=${wip.processId}&taskId=${wip.currentTaskId}">${task.title}</a></li>
          <li class="is-active"><a href="#">${wip.keyValues['title']}</a></li>
          <li>
          </li>
        </ul>
        

			
			
      </nav>

      <nav class="level">
        <!-- Left side -->
        <div class="level-left">
          <div class="level-item">
            <h2 class="title is-three" style="font-family: 'Old Standard TT', serif;" \>${wip.keyValues['title']} ....</h2>
          </div>
        </div>


        <!-- Left side -->
        <div class="level-right">
          <div class="level-item">


            <a class="button is-success" onclick="update()">
              <span class="icon is-small">
                <i class="fa fa-check"></i>
              </span>
              <span>Save</span>
            </a>


          </div>


          <div class="level-item">





          </div>
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



  </section>





  <!--CONTENTS-->
  <form name="detailsform" id="detailsform">
  <input class="input" type="hidden" name="wipid" value="${wip.id}">
  <div class="container">
    <div class="tile is-ancestor">
      <div class="tile is-vertical is-8">
        <div class="tile">
          <div class="tile is-parent is-vertical">
            <article class="tile is-child notification is-light">



              <div class="box">


                <div class="field">
                  <label class="label">Status</label>
                  <div class="control">
                    <div class="select">
                      <select name="status" id="status">
                         <c:forEach var="t" items="${tasklist}">
                           <option  ${t.id == task.id ? "SELECTED" : ""} value=${t.id} >${t.title}</option>
                         </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="field">
                  <label class="label">Title</label>
                  <div class="control">
                    <input class="input" name="title" type="text" placeholder="" value="${wip.keyValues['title']}">
                  </div>
                </div>

                <div class="field">
                  <label class="label">Author</label>
                  <div class="control">
                    <input class="input" name="author" type="text" placeholder="" value="${wip.keyValues['author']}">
                  </div>
                </div>

                <div class="field">
                  <label class="label">Requester</label>
                  <div class="control">
                    <input class="input" name="requester" type="text" placeholder="" value="${wip.keyValues['requester']}">
                  </div>
                </div>


                <div class="field">
                  <label class="label">Notes</label>
                  <div class="control">
                    <textarea class="textarea" name="notes" placeholder="">${wip.keyValues['notes']}</textarea>
                  </div>
                </div>

                <div class="field">
                  <label class="label">Barcode</label>
                  <div class="control">
                    <input class="input" name="barcode" type="text" placeholder="" value="${wip.keyValues['barcode']}">
                  </div>
                </div>

                <div class="field">
                  <label class="label">OCLC</label>
                  <div class="control">
                    <input class="input" name="oclc" type="text" placeholder="" value="${wip.keyValues['oclc']}">
                  </div>
                </div>

			</form>

            <div class="field is-grouped">
          	  <a class="button is-success" onclick="update()">
           		<span class="icon is-small">
            		 	<i class="fa fa-check"></i>
           		</span>
           		<span>Save</span>
         	  </a>
            </div>

              </div>
            </article>
          </div>
        </div>
        <div class="tile is-parent">
          <article class="tile is-child notification is-light">

            <div class="content">

			  <p class="title is-two" style="font-family: 'Old Standard TT', serif;">Log</p>
            
              <p class="subtitle"></p>
              <div id="logcontent" class="content" style="font-family: 'Special Elite', cursive;">

                <c:forEach var="log" items="${logs}">
                	<fmt:formatDate value="${log.updatedDate}" pattern="MM-dd-yyyy h:mm a"/>:
                	${log.newStatus} (${log.userid})
                	<br>
                 </c:forEach>

              </div>

            </div>
          </article>
        </div>
      </div>
      <div class="tile is-parent">
        <article class="tile is-child notification">



          <div class="content">


            <nav class="level">
              <!-- Left side -->
              <div class="level-left">
                <div class="level-item" style="padding-top:10px">
                  <h1 class="title is-two" style="font-family: 'Old Standard TT', serif;">Email</h1>
                </div>
              </div>


              <!-- Left side -->
              <div class="level-right">
                <div class="level-item">
                  <div class="control">
                    <a class="button is-success">
                         <span class="icon is-small">
                           <i class="fa fa-envelope"></i>
                         </span>
                         <span>Save & Send</span>
                       </a>
                  </div>
                </div>
              </div>
            </nav>

            <div class="content">
              <div class="field">
                <div class="control">
                  <div class="select">
                    <select>
                       <option>Select a template</option>
                       <option>With options</option>
                     </select>
                  </div>
                </div>
              </div>

              <div class="field">
                <label class="label">To</label>
                <div class="control">
                  <input class="input" type="text" placeholder="" value="">
                </div>
              </div>


              <div class="field">
                <label class="label">From</label>
                <div class="control">
                  <input class="input" type="text" placeholder="" value="">
                </div>
              </div>

              <div class="field">
                <label class="label">Subject</label>
                <div class="control">
                  <input class="input" type="text" placeholder="" value="">
                </div>
              </div>


              <div class="field">
                <label class="label">Message</label>
                <div class="control">
                  <textarea rows="30" class="textarea" placeholder=""></textarea>
                </div>
              </div>


            </div>
          </div>
        </article>
      </div>
    </div>

  </div>

  <!-- END CONTENTS-->
  
 <!-- MODAL NEW REQUEST FORM -->
    <jsp:include page="new.jsp"/>
 <!-- END MODAL -->


  <script src="https://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
  <script src="js/custom.js"></script>


	
	
	<script>

		function createNewProcess() {
			   var formData = $("#create").serialize();
			   $.ajax({
			        type: "POST",
			        dataType: 'json',
			        data: formData,
			        url: "service/wip/1/createWip",
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


</body>




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
  
  function update() {
		//FOR NOW JUST UDATE THE STATUS
		var formData = $("#detailsform").serialize();
		   $.ajax({
		        type: "POST",
		        dataType: 'json',
		        data: formData,
		        url: "service/wip/1/updateWip/" + ${wip.id},
		        success: updateSuccessful,
		        error: updateFailed
		     });
	}
</script>





<script src="js/handlebars-v4.0.2.js"></script>
<script src="js/moment.js"></script>

<script>

Handlebars.registerHelper('formatTime', function (date, format) {
    var mmnt = moment(date);
    return mmnt.format(format);
});
</script>


<script id="logTemplate" type="text/x-handlebars-template">
{{#each this}}
	{{formatTime updatedDate "MM-DD-YYYY h:mm a"}}:&nbsp; {{newStatus}}&nbsp;
	({{userid}})
    
	<br>
{{/each}}
</script>


</html>