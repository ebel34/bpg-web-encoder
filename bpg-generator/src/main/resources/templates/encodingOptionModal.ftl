
<!-- Button trigger modal -->
<button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">
	<i class="glyphicon glyphicon-cog"></i>
	<span>Encoding options</span>
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">BPG encoding options</h4>
      </div>
      <div class="modal-body">
	<label for="quantizer">quantizer (0-51, smaller better quality, default=29) :</label> <select name="quantizer" class="form-control">
	  <#list 0..51 as x>
	  	<#if x == 29>
	  <option value="${x}" selected>${x}</option>
		<#else>
	  <option value="${x}">${x}</option>
		</#if>
	  </#list>		  
	</select>  
	<br/><label for="chroma">Chroma format (default=420) :</label> <select name="chroma" class="form-control">
	  <option value="420" selected>420</option>
	  <option value="422">422</option>
	  <option value="444">444</option>
	</select>
	<br/><label for="colorSpace">Prefered color space (default=ycbcr) :</label> <select name="colorSpace" class="form-control">
	  <option value="ycbcr">ycbcr</option>
	  <option value="rgb">rgb</option>
	  <option value="ycgco">ycgco</option>
	  <option value="ycbcr_bt709">ycbcr_bt709</option>
	  <option value="ycbcr_bt2020">ycbcr_bt2020</option>
	</select>
	<br/><label for="level">Compression level (1=fast,9=slow, default=8) :</label> <select name="level" class="form-control">
	    <#list 1..9 as x>
	      	<#if x == 8>
		  <option value="${x}" selected>${x}</option>
			<#else>
		  <option value="${x}">${x}</option>
			</#if>
	  	</#list>
	</select>
	<br/><label for="bitDepth">Bit depth (8-12, default=8) :</label> <select name="bitDepth" class="form-control">
	    <option value="8">8</option>
	    <option value="10">10</option>
	    <option value="12">12</option>
	</select>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
