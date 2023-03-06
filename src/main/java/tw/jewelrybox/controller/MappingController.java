package tw.jewelrybox.controller;

import javax.persistence.EntityManager;
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
 * 「途徑」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/mapping")
public class MappingController {

	@Autowired
	EntityManager entityManager;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	@Autowired
	private MappingService mappingService;

	/**
	 * 後臺 » 途徑一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有途徑。
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView index(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
			size = Integer.parseInt(request.getParameter("s").trim());//每頁幾筆
			if (size < 1) {
				throw new Exception();
			}
		} catch (Exception exception) {
			size = 10;
		}
		try {
			number = Integer.parseInt(request.getParameter("p").trim());//第幾頁
			if (number < 0) {
				throw new Exception();
			}
		} catch (Exception exception) {
			number = 0;
		}
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.ASC, "sort"));
		Page<tw.jewelrybox.entity.Mapping> pageOfEntities;
		pageOfEntities = mappingRepository.findByAccordionIsNotNull(pageRequest);
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
		ModelAndView modelAndView = new ModelAndView("mapping/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 途徑 » {途徑描述}
	 *
	 * @param id 主鍵
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 某途徑的修改頁面。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView read(@PathVariable("id") Short id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		tw.jewelrybox.entity.Mapping mapping = mappingRepository.findOne(id);
		if (mapping == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(mapping.getDescription()).concat("&#125;"));
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		mappingService.loadOne(mapping, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("mapping/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 途徑 » {途徑名稱}
	 *
	 * @param id 主鍵
	 * @param name 途徑名稱
	 * @param sort 排序
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public ModelAndView update(@PathVariable("id") Short id, @RequestParam(value = "accordion", required = false) Short accordionId, @RequestParam(value = "sort", required = false) Short sort, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "uri", required = false) String uri, @RequestParam(value = "method", required = false) Short methodId, @RequestParam(value = "pattern", required = false) String pattern, @RequestParam(value = "description", required = false) String description, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		tw.jewelrybox.entity.Mapping mapping = mappingRepository.findOne(id);
		if (mapping == null) {
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
		if (mappingService.saveOne(mapping, accordionId, sort, title, uri, methodId, pattern, description, documentElement, request)) {
			return new ModelAndView("redirect:/mapping/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("mapping/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 途徑 » 新增
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
		mappingService.loadOne(null, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("mapping/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 途徑 » 新增
	 *
	 * @param name 途徑名稱
	 * @param sort 排序
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/add.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public ModelAndView create(@RequestParam(value = "accordion", required = false) Short accordionId, @RequestParam(value = "sort", required = false) Short sort, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "uri", required = false) String uri, @RequestParam(value = "method", required = false) Short methodId, @RequestParam(value = "pattern", required = false) String pattern, @RequestParam(value = "description", required = false) String description, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
		if (mappingService.saveOne(new tw.jewelrybox.entity.Mapping(), accordionId, sort, title, uri, methodId, pattern, description, documentElement, request)) {
			return new ModelAndView("redirect:/mapping/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("mapping/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
}
