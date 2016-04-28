<#import "/spring.ftl" as spring />
<script src="<@spring.url '/js/jquery.min.js'/>"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="<@spring.url '/js/vendor/jquery.ui.widget.js'/>"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="<@spring.url '/js/tmpl.min.js'/>"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="<@spring.url '/js/load-image.all.min.js'/>"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="<@spring.url '/js/canvas-to-blob.min.js'/>"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="<@spring.url '/js/bootstrap.min.js'/>"></script>
<!-- blueimp Gallery script -->
<script src="<@spring.url '/js/jquery.blueimp-gallery.min.js'/>"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="<@spring.url '/js/jquery.iframe-transport.js'/>"></script>
<!-- The basic File Upload plugin -->
<script src="<@spring.url '/js/jquery.fileupload.js'/>"></script>
<!-- The File Upload processing plugin -->
<script src="<@spring.url '/js/jquery.fileupload-process.js'/>"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="<@spring.url '/js/jquery.fileupload-image.js'/>"></script>
<!-- The File Upload audio preview plugin -->
<script src="<@spring.url '/js/jquery.fileupload-audio.js'/>"></script>
<!-- The File Upload video preview plugin -->
<script src="<@spring.url '/js/jquery.fileupload-video.js'/>"></script>
<!-- The File Upload validation plugin -->
<script src="<@spring.url '/js/jquery.fileupload-validate.js'/>"></script>
<!-- The File Upload user interface plugin -->
<script src="<@spring.url '/js/jquery.fileupload-ui.js'/>"></script>
<!-- The main application script -->
<script src="<@spring.url '/js/main.js'/>"></script>
<script src="<@spring.url '/js/sockjs-0.3.4.js'/>"></script>
<script src="<@spring.url '/js/stomp.js'/>"></script>

<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="<@spring.url '/js/cors/jquery.xdr-transport.js'/>"></script>
<![endif]-->