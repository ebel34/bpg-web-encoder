package org.bellard.bpg.web;

import java.awt.image.BufferedImage;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bellard.bpg.service.InstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Controller
@Configuration
public class CaptchaController {

	@Autowired
	private InstantService instantService;
	
	@Bean(name = "captchaGenerator")
	public Producer getCaptchaGenerator() {
		Properties p = new Properties();
		p.put("kaptcha.image.width", "200");
		p.put("kaptcha.image.height", "40");
		p.put("kaptcha.textproducer.char.string", "0123456789");
		p.put("kaptcha.textproducer.char.length", "4");
		Config config = new Config(p);
		DefaultKaptcha producer = new DefaultKaptcha();
		producer.setConfig(config);
		return producer;
	}
	
	@RequestMapping(value = "captcha-{uuid}.jpg", method = RequestMethod.GET)
	public void getCaptcha(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Set to expire far in the past.
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		// return a jpeg
		response.setContentType("image/jpeg");

		// create the text for the image
		String capText = getCaptchaGenerator().createText();

		// store the text in the session
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY + uuid, capText);
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_DATE + uuid, instantService.now());

		// create the image with the text
		BufferedImage bi = getCaptchaGenerator().createImage(capText);

		// write the data out
		ImageIO.write(bi, "jpg", response.getOutputStream());

		response.flushBuffer();
	}

	
	
}
