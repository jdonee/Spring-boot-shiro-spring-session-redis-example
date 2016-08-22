package org.jdonee.cooking.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/**
	 * Go Index
	 * 
	 * @return
	 */
	@RequestMapping(value = { "", "/", "index" })
	public String index() {
		return "index";
	}

	/**
	 * unauthor
	 * 
	 * @return
	 */
	@RequestMapping("unauthor")
	public String unauthor() {
		return "unauthor";
	}

	/**
	 * reports
	 * 
	 * @return
	 */
	@RequestMapping("reports")
	public String reports() {
		return "reports";
	}
}
