package de.tkoehler.rezepttool.manager.restcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appInfo")
public class AppInfoController {
	@Value("${appInfo.version}")
	private String version;
	
	@RequestMapping(path="/version", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public String getVersion() {
		return version;
	}
}
