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








function updateSuccessful(response) {
	//WILL USE THE RESPONSE TO UPDATE THE LOG SECTION OF THIS PAGE
	$('#errormsg').html("Update was successful");
	$('#errormsg').removeClass('is-danger');
	$('#errormsg').addClass('is-primary');
	$('#errormsg').removeClass('is-hidden');
	$('#errormsgcol').removeClass('is-hidden');
	
	//DISPLAY UPDATED LOG
	var source = document.getElementById("logTemplate").innerHTML;
    var template = Handlebars.compile(source);
    var context = response;
    var output = template(context);
    document.getElementById("logcontent").innerHTML = output;

}

function updateFailed(response) {
	$('#errormsg').html(response.responseText + "  <a href='/login'>Login page</a>");
	$('#errormsg').addClass('is-danger');
	$('#errormsg').removeClass('is-hidden');
	$('#errormsgcol').removeClass('is-hidden');
}




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


function closeModal(e) {
    e.preventDefault();
    var $html = document.documentElement;
    var $target = document.getElementById("m-test");
    $target.classList.remove('is-active');
    $html.classList.remove('is-clipped');
}


  function testmodal() {
    var $html = document.documentElement;
    var $target = document.getElementById("m-test");
    $target.classList.add('is-active');
    $html.classList.add('is-clipped');
}

