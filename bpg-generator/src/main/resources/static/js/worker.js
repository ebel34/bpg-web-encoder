$(function worker() {
	'use strict';
	$.ajax({
		url: 'ajax/test.html', 
	    success: function(data) {
	      $('.result').html(data);
	    },
	    complete: function() {
	      // Schedule the next request when the current one's complete
	      setTimeout(worker, 5000);
	    }
	});
})();