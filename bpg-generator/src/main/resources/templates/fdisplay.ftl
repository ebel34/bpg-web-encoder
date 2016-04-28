<#import "/spring.ftl" as spring />
<!DOCTYPE HTML>
<html lang="en">
<head>
	<!-- Force latest IE rendering engine or ChromeFrame if installed -->
	<!--[if IE]>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<![endif]-->
	<meta charset="utf-8">
   <meta name="description" content="Display the BPG image encoded with the Web encoder.">
   <title>BPG Web Encoder : display image</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<#include "*/css.ftl">
	<script type="text/javascript" src="<@spring.url '/js/bpgdec8.js'/>"></script>
	
</head>
<body>
<#include "*/header.ftl">
<div class="container">
    <h1>Display the encoded BPG image</h1>
    <br/>
    <blockquote>
        <p>Display the encoded BPG image encoded with the web encoder.  
		<br>
	</p>
    </blockquote>
	<p>
	<img src="/view/${hash}.bpg" />
	</p>
</div>
<#include "*/footer.ftl">		
</body>
</html>