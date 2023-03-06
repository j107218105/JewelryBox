package tw.jewelrybox.controller;

import java.util.*;
import javax.persistence.EntityManager;
import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;
import tw.jewelrybox.service.Services;

/**
 * 「權限」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/privilege")
public class PrivilegeController {

	@Autowired
	EntityManager entityManager;

	@Autowired
	private AccordionRepository accordionRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private FinancialInstitutionRepository financialInstitutionRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private OccupationRepository occupationRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	/**
	 * 權限 » 管理者一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有除了自己以外的管理者。
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
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
		Page<Someone> pageOfEntities;
		pageOfEntities = privilegeRepository.findAllAgentsButMe(me, pageRequest);
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
		for (Someone agent : pageOfEntities.getContent()) {
			String cellular = agent.getCellular();
			Long age;
			try {
				Date birthday = agent.getBirthday();
				GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
				gregorianCalendar.setTime(birthday);
				age = (new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN).getTimeInMillis() - gregorianCalendar.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 365;
			} catch (Exception ignore) {
				age = null;
			}

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, agent.getId().toString());
			Utils.createElementWithTextContent("name", rowElement, agent.getName());
			Utils.createElementWithTextContent("gender", rowElement, agent.getGender() ? "true" : "false");
			Utils.createElementWithTextContent("email", rowElement, agent.getEmail());
			Utils.createElementWithTextContent("cellular", rowElement, cellular == null ? "" : cellular);
			Utils.createElementWithTextContent("age", rowElement, age == null ? "" : age.toString());
			Utils.createElementWithTextContent("denied", rowElement, agent.isDenied() ? "true" : "false");
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("privilege/index");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 權限 » {管理者名稱}
	 *
	 * @param id 管理者主鍵
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有權限並標示該管理者被允許情形。
	 */
	@RequestMapping(value = "/agent/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView agent(@PathVariable Integer id, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		Someone agent = someoneRepository.findOne(id);
		if (agent == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(agent.getName()).concat("&#125;"));
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
		Pageable pageRequest = new PageRequest(number, size);
		Page<tw.jewelrybox.entity.Mapping> pageOfEntities;
		pageOfEntities = mappingRepository.findAll(pageRequest);
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
			Accordion accordion = mapping.getAccordion();
			String title = mapping.getTitle(), uri = mapping.getUri(), description = mapping.getDescription();
			Method method = mapping.getMethod();

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, mapping.getId().toString());
			Utils.createElementWithTextContent("accordion", rowElement, accordion == null ? "" : accordion.getName());
			Utils.createElementWithTextContent("title", rowElement, title == null ? "" : title);
			Utils.createElementWithTextContent("uri", rowElement, uri);
			Utils.createElementWithTextContent("method", rowElement, method.getName());
			Utils.createElementWithTextContent("description", rowElement, description);
			Utils.createElementWithTextContent("agent", rowElement, agent.getId().toString());
			Utils.createElementWithTextContent("enabled", rowElement, privilegeRepository.findOneByMappingAndAgent(mapping, agent) == null ? "false" : "true");
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("privilege/agent");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	@RequestMapping(value = "/agent/{agentId:[\\d]+}/mapping/{mappingId:[\\d]+}.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String grantOrRevoke(@PathVariable Integer agentId, @PathVariable Short mappingId, HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}
		if (services.howAmI(request, session) == null) {
			return jsonObject.put("reason", "您並未被授權進行此項操作！").toString();//被禁止
		}

		Someone agent = someoneRepository.findOne(agentId);
		if (agent == null) {
			return jsonObject.put("reason", "找不到該管理者！").toString();
		}
		tw.jewelrybox.entity.Mapping mapping = mappingRepository.findOne(mappingId);
		if (mapping == null) {
			return jsonObject.put("reason", "找不到該途徑！").toString();
		}

		Privilege privilege = privilegeRepository.findOneByMappingAndAgent(mapping, agent);
		if (privilege == null) {
			privilegeRepository.saveAndFlush(new Privilege(mapping, agent));
		} else {
			privilegeRepository.delete(privilege);
			privilegeRepository.flush();
		}

		return jsonObject.put("response", true).toString();
	}
}
