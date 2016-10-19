package com.netease.seckill.controller;

import com.netease.seckill.dto.Exposer;
import com.netease.seckill.dto.SeckillExcution;
import com.netease.seckill.dto.SeckillResult;
import com.netease.seckill.entity.Seckill;
import com.netease.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Jo on 10/19/16.
 */
@Controller
@RequestMapping("/seckill")  //module
public class SeckillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	SeckillService seckillService;

	/**
	 * get all seckill list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String list(Model model){
		//list.jsp + model = ModelAndView
		List<Seckill> seckills = seckillService.getSeckillList();
		model.addAttribute("seckills", seckills);
		return "list";  //WEB-INF/jsp/list.jsp    look at spring-web.xml
	}

	/**
	 * get seckill by seckillId
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model){
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(null == seckill){
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill",seckill);
		return "detail";
	}

	//ajax return json
	@RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,
			produces = {"application/json;charset=utf-8"})//http context
	@ResponseBody  //packging return result to json
	public SeckillResult<Exposer> exposer(Long seckillId){
		SeckillResult<Exposer> result;
		try{
			Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true,exposer);
		}catch (Exception e){
			logger.error(e.getMessage());
			result = new SeckillResult<Exposer>(false,e.getMessage());
		}
		return result;
	}

	public SeckillResult<SeckillExcution> execute(){
		
	}
}