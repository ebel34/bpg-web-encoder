<#import "/spring.ftl" as spring />
<!DOCTYPE HTML>
<html lang="en">
<head>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>Load and display a BPG image in the browser</title>
<meta name="description" content="Load and display a BPG image in the browser. Select the BPG image in your computer and display it into your browser.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<#include "*/css.ftl">
<script type="text/javascript" src="<@spring.url '/js/bpgdec8.js'/>"></script>
</head>
<body>
<#include "*/header.ftl">
<div class="container">
    <h1>Load and display a BPG image in the browser</h1>
    <br>
    <blockquote>
        <p>Select a BPG image in your computer and display it in the browser.  
		<br>
	</p>
    </blockquote>
    <br>
	<span class="btn btn-success fileinput-button">
        <i class="glyphicon glyphicon-plus"></i>
        <span>Select BPG...</span>
	<input id="fileInput" type=file>
    </span>
    <br/>
    <br/>
	<canvas id="mycanvas" width="0" height="0"></canvas>

 	<div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Notes</h3>
        </div>
        <div class="panel-body">
            <ul>
                <li>Select a BPG image from your computer, load it into the browser and show the BPG image in the browser using the javascript bpg library.</li>
                <li>The image is obviously *not* send to the server.</li>
                <li>Browser compatible with File &amp; FileReader API is required.</li>
            </ul>
        </div>
    </div>
</div>

<script>
	var img, canvas, ctx, upload;

    canvas = document.getElementById("mycanvas");
    ctx = canvas.getContext("2d");
	upload = document.getElementsByTagName('input')[0];
 
upload.onchange = function (e) {
  e.preventDefault();

  var file = upload.files[0],
  reader = new FileReader();
  reader.onload = function (event) {
	  
	    img = new BPGDecoder(ctx);
	    img.onload = function() {
	        /* draw the image to the canvas */
	        canvas.width=this.imageData.width;
	        canvas.height=this.imageData.height;
	        ctx.putImageData(this.imageData, 0, 0);
	    };
	    img.load(event.target.result);
  };
  reader.readAsDataURL(file);
  return false;
};



(function ()
{
    var img, canvas, ctx;


})();


</script>  
  
  
<#include "*/footer.ftl">
</body>
</html>
