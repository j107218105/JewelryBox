package tw.jewelrybox.controller;

import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;
import tw.jewelrybox.service.*;

/**
 * 「手風琴」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/accordion")
public class AccordionController {

	@Autowired
	private AccordionRepository accordionRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	@Autowired
	private AccordionService accordionService;

	/**
	 * 後臺 » 手風琴一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有手風琴。
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView index(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}
		String pageTitle = privilege.getMapping().getTitle();

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", pageTitle == null ? "" : pageTitle);
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 分頁
		 */
		Element searchElement = Utils.createElementWithAttribute("search", documentElement, "action", request.getRequestURI());
		try {
			//每頁幾筆
			if (size < 1) {
				throw new Exception();
			}
		} catch (Exception exception) {
			size = 10;
		}
		try {
			//第幾頁
			if (number < 0) {
				throw new Exception();
			}
		} catch (Exception exception) {
			number = 0;
		}
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.ASC, "sort"));
		Page<Accordion> pageOfEntities;
		pageOfEntities = accordionRepository.findAll(pageRequest);
		number = pageOfEntities.getNumber();
		Integer totalPages = pageOfEntities.getTotalPages();
		Long totalElements = pageOfEntities.getTotalElements();
		if (pageOfEntities.hasPrevious()) {
			searchElement.setAttribute("previous", Integer.toString(number - 1));
			if (!pageOfEntities.isFirst()) {
				searchElement.setAttribute("first", "0");
			}
		}
		searchElement.setAttribute("size", size.toString());
		searchElement.setAttribute("number", number.toString());
		for (Integer i = 0; i < totalPages; i++) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", searchElement, Integer.toString(i + 1), "value", i.toString());
			if (number.equals(i)) {
				optionElement.setAttribute("selected", null);
			}
		}
		searchElement.setAttribute("totalPages", totalPages.toString());
		searchElement.setAttribute("totalElements", totalElements.toString());
		if (pageOfEntities.hasNext()) {
			searchElement.setAttribute("next", Integer.toString(number + 1));
			if (!pageOfEntities.isLast()) {
				searchElement.setAttribute("last", Integer.toString(totalPages - 1));
			}
		}

		/*
		 表格
		 */
		Element tableElement = Utils.createElement("table", documentElement);
		for (Accordion accordion : pageOfEntities.getContent()) {
			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, accordion.getId().toString());
			Utils.createElementWithTextContent("name", rowElement, accordion.getName());
			Utils.createElementWithTextContent("sort", rowElement, Short.toString(accordion.getSort()));
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 手風琴 » {手風琴名稱}
	 *
	 * @param id 主鍵
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 某手風琴的修改頁面。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView read(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Accordion accordion = accordionRepository.findOne(id);
		if (accordion == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(accordion.getName()).concat("&#125;"));
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		accordionService.loadOne(accordion, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 手風琴 » {手風琴名稱}
	 *
	 * @param id 主鍵
	 * @param name 手風琴名稱
	 * @param sort 排序
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public ModelAndView update(@PathVariable("id") Short id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "sort", required = false) Short sort, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}
		String pageTitle = privilege.getMapping().getTitle();

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Accordion accordion = accordionRepository.findOne(id);
		if (accordion == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", pageTitle == null ? "" : pageTitle);
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		if (accordionService.saveOne(accordion, name, sort, documentElement, request)) {
			return new ModelAndView("redirect:/accordion/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 手風琴 » 新增
	 *
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 新增的頁面。
	 */
	@RequestMapping(value = "/add.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}
		String pageTitle = privilege.getMapping().getTitle();

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", pageTitle == null ? "" : pageTitle);
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		accordionService.loadOne(null, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 手風琴 » 新增
	 *
	 * @param name 手風琴名稱
	 * @param sort 排序
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/add.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public ModelAndView create(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "sort", required = false) Short sort, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}
		String pageTitle = privilege.getMapping().getTitle();

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", pageTitle == null ? "" : pageTitle);
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		if (accordionService.saveOne(new Accordion(), name, sort, documentElement, request)) {
			return new ModelAndView("redirect:/accordion/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » {手風琴} » 途徑一覽
	 *
	 * @param id 手風琴主鍵
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現該手風琴下所有途徑。
	 */
	@RequestMapping(value = "/{id:[\\d]+}/mapping/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView mapping(@PathVariable("id") Short id, @RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		if (services.howAmI(request, session) == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;//被禁止
		}

		/*
		 登入了
		 */
		Someone me = someoneRepository.findOne(myId);

		Accordion accordion = accordionRepository.findOne(id);
		if (accordion == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(accordion.getName()).concat("&#125;"));
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 分頁
		 */
		Element searchElement = Utils.createElementWithAttribute("search", documentElement, "action", request.getRequestURI());
		try {
			//每頁幾筆
			if (size < 1) {
				throw new Exception();
			}
		} catch (Exception exception) {
			size = 10;
		}
		try {
			//第幾頁
			if (number < 0) {
				throw new Exception();
			}
		} catch (Exception exception) {
			number = 0;
		}
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.ASC, "sort"));
		Page<tw.jewelrybox.entity.Mapping> pageOfEntities;
		pageOfEntities = mappingRepository.findByAccordionOrderBySort(accordion, pageRequest);
		number = pageOfEntities.getNumber();
		Integer totalPages = pageOfEntities.getTotalPages();
		Long totalElements = pageOfEntities.getTotalElements();
		if (pageOfEntities.hasPrevious()) {
			searchElement.setAttribute("previous", Integer.toString(number - 1));
			if (!pageOfEntities.isFirst()) {
				searchElement.setAttribute("first", "0");
			}
		}
		searchElement.setAttribute("size", size.toString());
		searchElement.setAttribute("number", number.toString());
		for (Integer i = 0; i < totalPages; i++) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", searchElement, Integer.toString(i + 1), "value", i.toString());
			if (number.equals(i)) {
				optionElement.setAttribute("selected", null);
			}
		}
		searchElement.setAttribute("totalPages", totalPages.toString());
		searchElement.setAttribute("totalElements", totalElements.toString());
		if (pageOfEntities.hasNext()) {
			searchElement.setAttribute("next", Integer.toString(number + 1));
			if (!pageOfEntities.isLast()) {
				searchElement.setAttribute("last", Integer.toString(totalPages - 1));
			}
		}

		/*
		 表格
		 */
		Element tableElement = Utils.createElement("table", documentElement);
		for (tw.jewelrybox.entity.Mapping mapping : pageOfEntities.getContent()) {
			Short sort = mapping.getSort();
			String title = mapping.getTitle();

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, mapping.getId().toString());
			Utils.createElementWithTextContent("accordion", rowElement, mapping.getAccordion().getName());
			Utils.createElementWithTextContent("sort", rowElement, sort == null ? "" : sort.toString());
			Utils.createElementWithTextContent("title", rowElement, title == null ? "" : title);
			Utils.createElementWithTextContent("uri", rowElement, mapping.getUri());
			Utils.createElementWithTextContent("method", rowElement, mapping.getMethod().getName());
			Utils.createElementWithTextContent("pattern", rowElement, mapping.getPattern());
			Utils.createElementWithTextContent("description", rowElement, mapping.getDescription());
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("accordion/mapping");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
}
