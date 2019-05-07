<div id="newRequestModal" class="modal">
    <div class="modal-background"></div>
	  <div class="modal-card">
	    <header class="modal-card-head">
	      <p class="modal-card-title">Create Patron Request</p>
	      <button class="delete"  onclick="closeFormModal(event,'newRequestModal')"></button>
	    </header>
	    <section class="modal-card-body">
	      <div class="content">
	        <span id="formholder">
	        <h1>Instructions</h1>
	        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla accumsan, 
	        metus ultrices eleifend gravida, nulla nunc varius lectus, nec rutrum justo nibh eu lectus. 
	        Ut vulputate semper dui. Fusce erat odio, sollicitudin vel erat vel, interdum mattis neque.</p>
	        
	        <form name="create" id="create">
	            <!-- TODO: THESE HIDDEN VALUES SHOULD BE DYNAMIC -->
	        	<input type="hidden" name="processId" value="1">
	        	<input type="hidden" name="currenTaskId" value="1">
	        	<div class="field">
				  <label class="label">Requester</label>
				  <div class="control">
				    <input class="input" name="requester" type="text" placeholder="">
				  </div>
				</div>
		        <div class="field">
				  <label class="label">Title</label>
				  <div class="control">
				    <input class="input" name="title" type="text" placeholder="">
				  </div>
				</div>
				<div class="field">
				  <label class="label">Author</label>
				  <div class="control">
				    <input class="input" name="author" type="text" placeholder="">
				  </div>
				</div>
				<div class="field">
				  <label class="label">Barcode</label>
				  <div class="control">
				    <input class="input" name="barcode" type="text" placeholder="">
				  </div>
				</div>
				<div class="field">
				  <label class="label">ISSN</label>
				  <div class="control">
				    <input class="input" name="issn" type="text" placeholder="">
				  </div>
				</div>
				<div class="field">
				  <label class="label">OCLC</label>
				  <div class="control">
				    <input class="input" name="oclc" type="text" placeholder="">
				  </div>
				</div>
				<div class="field">
				  <label class="label">Notes</label>
				  <div class="control">
				    <textarea class="textarea" name="notes" placeholder="Textarea"></textarea>
				  </div>
				</div>
			</form>  
			</span>
	      </div>
	    </section>
	    <footer class="modal-card-foot">
	     <span id="footerspan">
	      <a class="button is-success" onclick="createNewProcess()">Save changes</a>
	      <a class="button" onclick="closeFormModal(event,'newRequestModal')">Cancel</a>
	      </span>
	    </footer>
	  </div>
 </div>
 
 
