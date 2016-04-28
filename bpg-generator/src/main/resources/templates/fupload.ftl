<!DOCTYPE HTML>
<html lang="en">
<head>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>BPG Web Encoder</title>
<meta name="description" content="BPG Web encoder with file Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. BPG image will be provided by link">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<#include "*/css.ftl">
</head>
<body>
<#include "*/header.ftl">
<div class="container">
    <h1>BPG Web Encoder</h1>
    <br>
    <blockquote>
        <p>BPG Web Encoder provides online transcoding of tradional jpg/png image to superior bpg files.
        The original <a href="http://bellard.org/bpg">libbpg</a> encoder version is used to transcode the images with the default encoder options. 
		<br>
	</p>
    </blockquote>
    <br>
    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="//uploads" method="POST" enctype="multipart/form-data">
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">

            <!-- captcha and email -->
			<div>
				<div class="col-lg-7">
					<input type="hidden" name="hash" value="${hash}"/>
					<img src="captcha-${hash}.jpg" />
					Captcha: <input type="text" name="captcha" value="" /> The captcha has 3 mn validity.<br/><br/>
				</div>
				<!--
				<div class="col-lg-7">
					Email: <input type="text" name="name" /> A link to the bpg file will be sent by email.<br/><br/>
				</div>
				-->
			</div>            
            <!-- captcha and email -->
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>Add files...</span>
                    <input type="file" name="files[]" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start upload</span>
                </button>
                <button type="reset" class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel upload</span>
                </button>
                <#include "*/encodingOptionModal.ftl"/>
                <!-- The global file processing state -->
                <span class="fileupload-process"></span>
            </div>
            <!-- The global progress state -->
            <div class="col-lg-5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                </div>
                <!-- The extended global progress state -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
    </form>
    <br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Notes</h3>
        </div>
        <div class="panel-body">
            <ul>
                <li><strong>News : </strong>usage of libbpg <strong>0.9.6</strong> and bpg encoding options.</li>
                <li>The maximum file size for uploads is <strong>6 MB</strong>.</li>
                <li>The maximum file count per captcha identification is <strong>10</strong>.</li>
                <li>Only image files (<strong>JPG, GIF, PNG</strong>) are allowed.</li>
                <li>Encoded and uploaded files will be deleted automatically <strong>10 minutes</strong> after the encoding end.</li>
                <li>3 minutes encoding timeout. It may  be an issue with image data close to the maximum allowed size.</li>
                <li>The application is hosted in a one core cpu openstack container. The encoding time is therefore <strong>SLOW</strong>.</li>
                <li>The encoding time on a multicore modern CPU PC is dramatically <strong>FASTER</strong>.</li>
            </ul>
        </div>
    </div>
</div>
<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel</span>
                </button>
            {% } %}
        </td>
     </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                <span>{%=file.name%}</span>
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            <span id="size-{%=file.uuid%}" class="size" style="display:none;"></span>
        </td>
        <td>
            {% if (!file.error) { %}
            <p id="p-{%=file.uuid%}" style="display:inline;"> BPG image processing...</p
            {% } %}
        </td>
        <td>
            {% if (!file.error) { %}
            <button id="bd-{%=file.uuid%}" class="btn btn-primary" style="display:none;" title="Download {%=file.bpgname%}" onClick="downloadBpg('/download/{%=file.suuid%}/{%=file.uuid%}','{%=file.bpgname%}');return false;">
	            <i class="glyphicon glyphicon-download"></i>
	            <span>Download</span>
            </button>
            <button id="bv-{%=file.uuid%}" class="btn btn-primary" style="display:none;" title="Display {%=file.bpgname%}" onClick="viewBpg('/display/{%=file.suuid%}/{%=file.uuid%}');return false;">
	            <i class="glyphicon glyphicon-eye-open"></i>
	            <span>Display</span>
            </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<#include "*/js.ftl">
<script>

var stompClient = null;
var socket = new SockJS('/bpgws');
stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/updateData', function(data){
        updateData(JSON.parse(data.body));
    });
    //stompClient.send("/app/hello", {}, JSON.stringify({ 'name': 'toto' }));
});


function updateData(data) {
    console.log('Receive data!');
	if (data != null) {
		var p = document.getElementById("p-"+data.uuid);
		p.style.display='none';
		var q = document.getElementById("bv-"+data.uuid);
		q.style.display='inline';
		var r = document.getElementById("bd-"+data.uuid);
		r.style.display='inline';
		var s = document.getElementById("size-"+data.uuid);
		s.style.display='inline';
		s.innerHTML = '<b>BPG size : ' + formatFileSize(data.bpgSize) + '</b><br/>Encoding time : ' + data.encodingTime;
		
	}
}

function viewBpg(url) {
	window.open(url);
}

function downloadBpg(url,filename) {
  var dl = document.createElement('a');
  dl.setAttribute('href', url);
  dl.setAttribute('download', filename);
  document.body.appendChild(dl);
  dl.click();
}

function formatFileSize(bytes) {
    if (typeof bytes !== 'number') {
        return '';
    }
    if (bytes >= 1000000000) {
        return (bytes / 1000000000).toFixed(2) + ' GB';
    }
    if (bytes >= 1000000) {
        return (bytes / 1000000).toFixed(2) + ' MB';
    }
    return (bytes / 1000).toFixed(2) + ' KB';
}



</script>
<#include "*/footer.ftl">
</body> 
</html>
