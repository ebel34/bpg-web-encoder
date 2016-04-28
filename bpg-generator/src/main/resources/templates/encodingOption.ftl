		<td>
        Encoding option :
        <br/>
        <label for="quantizer">quantizer 0-51 (smaller better quality) :</label> <select name="quantizer[]" class="form-control">
	      <#list 0..51 as x>
	      	<#if x == 29>
		  <option value="${x}" selected>${x}</option>
			<#else>
		  <option value="${x}">${x}</option>
			</#if>
		  </#list>		  
		</select>  
        <br/><label for="chroma">Chroma format :</label> <select name="chroma[]" class="form-control">
		  <option value="420" selected>420</option>
		  <option value="422">422</option>
		  <option value="444">444</option>
		</select>
        <br/><label for="colorSpace">Prefered color space :</label> <select name="colorSpace[]" class="form-control">
		  <option value="ycbcr">ycbcr</option>
		  <option value="rgb">rgb</option>
		  <option value="ycgco">ycgco</option>
		  <option value="ycbcr_bt709">ycbcr_bt709</option>
		  <option value="ycbcr_bt2020">ycbcr_bt2020</option>
		</select>
        <br/><label for="level">Compression level (1=fast,9=slow) :</label> <select name="level[]" class="form-control">
	        <#list 1..9 as x>
		      	<#if x == 8>
			  <option value="${x}" selected>${x}</option>
				<#else>
			  <option value="${x}">${x}</option>
				</#if>
		  	</#list>
		</select>
        <br/><label for="bitDepth">Bit depth 8-12 :</label> <select name="bitDepth[]" class="form-control">
        	<#list 8..12 as x>
		    <option value="${x}">${x}</option>
		  	</#list>
		</select>
        </td>
