package tw.jewelrybox.controller;

import java.util.*;
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
import tw.jewelrybox.specification.SomeoneSpecs;

/**
 * 「會員」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/someone")
public class SomeoneController {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private FinancialInstitutionRepository financialInstitutionRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private MethodRepository methodRepository;

	@Autowired
	private OccupationRepository occupationRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	/**
	 * 後臺 » 會員一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有會員。
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView index(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, @RequestParam(value = "q", defaultValue = "") String query, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
		try {
			//搜尋字串
			query = query.trim();
			if (query.isEmpty()) {
				throw new Exception();
			}
		} catch (Exception exception) {
			query = null;
		}
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
		Page<Someone> pageOfEntities;
		if (query == null) {
			pageOfEntities = someoneRepository.findAll(pageRequest);
		} else {
			pageOfEntities = someoneRepository.findAll(SomeoneSpecs.nameLike(query), pageRequest);
			searchElement.setAttribute("query", query);
		}
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
		for (Someone someone : pageOfEntities.getContent()) {
			String facebookId = someone.getFacebookId(), googleId = someone.getGoogleID(), cellular = someone.getCellular(), financialAccountHolder = someone.getFinancialAccountHolder(), financialAccountNumber = someone.getFinancialAccountNumber();
			Occupation occupation = someone.getOccupation();
			FinancialInstitution financialInstitution = someone.getFinancialInstitution();
			District district = someone.getDistrict();
			Long age;
			try {
				Date birthday = someone.getBirthday();
				GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
				gregorianCalendar.setTime(birthday);
				age = (new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN).getTimeInMillis() - gregorianCalendar.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 365;
			} catch (Exception ignore) {
				age = null;
			}

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, someone.getId().toString());
			if (facebookId != null) {
				Utils.createElementWithTextContent("facebookId", rowElement, facebookId);
			}
			if (googleId != null) {
				Utils.createElementWithTextContent("googleId", rowElement, googleId);
			}
			Utils.createElementWithTextContent("age", rowElement, age == null ? "" : age.toString());
			Utils.createElementWithTextContent("gender", rowElement, someone.getGender() ? "true" : "false");
			Utils.createElementWithTextContent("name", rowElement, someone.getName());
			Utils.createElementWithTextContent("email", rowElement, someone.getEmail());
			Utils.createElementWithTextContent("denied", rowElement, someone.isDenied() ? "true" : "false");
			Utils.createElementWithTextContent("cellular", rowElement, cellular == null ? "" : cellular);
			Utils.createElementWithTextContent("occupation", rowElement, occupation == null ? "" : occupation.getName());
			Utils.createElementWithTextContent("financialInstitution", rowElement, financialInstitution == null ? "" : financialInstitution.getName());
			Utils.createElementWithTextContent("financialAccountHolder", rowElement, financialAccountHolder == null ? "" : financialAccountHolder);
			Utils.createElementWithTextContent("financialAccountNumber", rowElement, financialAccountNumber == null ? "" : financialAccountNumber);
			Utils.createElementWithTextContent("district", rowElement, district == null ? "" : district.getName());
			Utils.createElementWithTextContent("agent", rowElement, privilegeRepository.countByAgent(someone) > 0 ? "true" : "false");
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("someone/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * (開放或封鎖某使用者)
	 *
	 * @param someoneId 會員主鍵
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/{someoneId:[\\d]+}.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String blockOrOpen(@PathVariable("someoneId") Integer someoneId, HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}
		if (services.howAmI(request, session) == null) {
			return jsonObject.put("reason", "您並未被授權進行此項操作！").toString();//被禁止
		}

		Someone someone = someoneRepository.findOne(someoneId);
		if (someone == null) {
			return jsonObject.put("reason", "找不到該會員！").toString();
		}

		someone.setDenied(!someone.isDenied());
		someoneRepository.saveAndFlush(someone);

		return jsonObject.put("response", true).toString();
	}

	/**
	 * (授權或停權管理者)
	 *
	 * @param agentId 管理者主鍵
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/{agentId:[\\d]+}/agent.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String grantOrRevoke(@PathVariable("agentId") Integer agentId, HttpServletRequest request, HttpSession session) {
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
			return jsonObject.put("reason", "找不到該會員！").toString();
		}

		if (privilegeRepository.countByAgent(agent) > 0) {
			for (Privilege privilege : privilegeRepository.findByAgent(agent)) {
				privilegeRepository.delete(privilege);
			}
			privilegeRepository.flush();
		} else {
			privilegeRepository.saveAndFlush(new Privilege(mappingRepository.findOneByMethodAndPattern(methodRepository.findOneByName("GET"), "^\\/$"), agent));
		}

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 後臺 » {會員} » 活動一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現該會員所有動。
	 */
	@RequestMapping(value = "/{someoneId:[\\d]+}/activity/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView activity(@PathVariable("someoneId") Integer id, @RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		Someone someone = someoneRepository.findOne(id);
		if (someone == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(someone.getName()).concat("&#125;"));
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
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
		Page<Activity> pageOfEntities;
		pageOfEntities = candidateRepository.findBySomeoneOrderBySince(someone, pageRequest);
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
		for (Activity activity : pageOfEntities.getContent()) {
			Short age = activity.getAge();
			Boolean gender = activity.getGender();
			Occupation occupation = activity.getOccupation();
			District district = activity.getDistrict();
			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, activity.getId().toString());
			Utils.createElementWithTextContent("name", rowElement, activity.getName());
			Utils.createElementWithTextContent("headcount", rowElement, Integer.toString(activity.getHeadcount()));
			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));
			Utils.createElementWithTextContent("since", rowElement, Utils.formatTimestamp(activity.getSince()));
			Utils.createElementWithTextContent("until", rowElement, Utils.formatTimestamp(activity.getUntil()));
			Utils.createElementWithTextContent("age", rowElement, age == null ? "" : age.toString());
			if (gender != null) {
				Utils.createElementWithTextContent("gender", rowElement, activity.getGender() ? "true" : "false");
			}
			Utils.createElementWithTextContent("occupation", rowElement, occupation == null ? "" : occupation.getName());
			Utils.createElementWithTextContent("district", rowElement, district == null ? "" : district.getName());

			Candidate candidate = candidateRepository.findOneByActivityAndSomeone(activity, someone);
			Boolean replied = candidate.getReplied();
			Date repliedBy = candidate.getRepliedBy(), provedBy = candidate.getProvedBy();
			byte[] proof = candidate.getProof();
			boolean hasProof = proof != null && proof.length > 0;
			Someone agent = candidate.getAgent();
			if (replied != null) {
				Utils.createElementWithTextContent("replied", rowElement, replied ? "true" : "false");
				if (replied && repliedBy != null) {
					Utils.createElementWithTextContent("repliedBy", rowElement, Utils.formatTimestamp(repliedBy));
				}
			}
			Utils.createElementWithTextContent("proof", rowElement, hasProof ? "true" : "false");
			if (provedBy != null) {
				Utils.createElementWithTextContent("provedBy", rowElement, Utils.formatTimestamp(provedBy));
			}
			if (agent != null) {
				Utils.createElementWithTextContent("agent", rowElement, agent.getName());
			}
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("someone/activity");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » {會員} » 待審核的活動
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現該會員所有動。
	 */
//	@RequestMapping(value = "/{someoneId:[\\d]+}/activity/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
//	public ModelAndView activity(@PathVariable("someoneId") Integer id, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer number, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
//		Integer myId = services.whoAmI(session);
//		if (myId == null) {
//			return new ModelAndView("redirect:/");//未登入
//		}
//		if (services.howAmI(request, session) == null) {
//			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//			return null;//被禁止
//		}
//
//		/*
//		 登入了
//		 */
//		Someone me = someoneRepository.findOne(myId);
//
//		Someone agent = someoneRepository.findOne(id);
//		if (agent == null) {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			return null;//找不到
//		}
//
//		Document document = Utils.newDocument();
//		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(agent.getName()).concat("&#125;"));
//		documentElement.setAttribute("contextPath", request.getContextPath());
//		documentElement.setAttribute("name", me.getName());
//		documentElement.appendChild(services.whereAmI(document, request, session));
//
//		/*
//		 分頁
//		 */
//		Element searchElement = Utils.createElementWithAttribute("search", documentElement, "action", request.getRequestURI());
//		try {
//			size = Integer.parseInt(request.getParameter("s").trim());//每頁幾筆
//			if (size < 1) {
//				throw new Exception();
//			}
//		} catch (Exception exception) {
//			size = 10;
//		}
//		try {
//			number = Integer.parseInt(request.getParameter("p").trim());//第幾頁
//			if (number < 0) {
//				throw new Exception();
//			}
//		} catch (Exception exception) {
//			number = 0;
//		}
//		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
//		Page<Activity> pageOfEntities;
//		pageOfEntities = candidateRepository.findBySomeoneOrderBySince(agent, pageRequest);
//		number = pageOfEntities.getNumber();
//		Integer totalPages = pageOfEntities.getTotalPages();
//		Long totalElements = pageOfEntities.getTotalElements();
//		if (pageOfEntities.hasPrevious()) {
//			searchElement.setAttribute("previous", Integer.toString(number - 1));
//			if (!pageOfEntities.isFirst()) {
//				searchElement.setAttribute("first", "0");
//			}
//		}
//		searchElement.setAttribute("size", size.toString());
//		searchElement.setAttribute("number", number.toString());
//		for (Integer i = 0; i < totalPages; i++) {
//			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", searchElement, Integer.toString(i + 1), "value", i.toString());
//			if (number.equals(i)) {
//				optionElement.setAttribute("selected", null);
//			}
//		}
//		searchElement.setAttribute("totalPages", totalPages.toString());
//		searchElement.setAttribute("totalElements", totalElements.toString());
//		if (pageOfEntities.hasNext()) {
//			searchElement.setAttribute("next", Integer.toString(number + 1));
//			if (!pageOfEntities.isLast()) {
//				searchElement.setAttribute("last", Integer.toString(totalPages - 1));
//			}
//		}
//
//		/*
//		 表格
//		 */
//		Element tableElement = Utils.createElement("table", documentElement);
//		for (Activity activity : pageOfEntities.getContent()) {
//			Short age = activity.getAge();
//			Boolean gender = activity.getGender();
//			Occupation occupation = activity.getOccupation();
//			District district = activity.getDistrict();
//
//			Element rowElement = Utils.createElement("row", tableElement);
//			Utils.createElementWithTextContent("id", rowElement, activity.getId().toString());
//			Utils.createElementWithTextContent("name", rowElement, activity.getName());
//			Utils.createElementWithTextContent("headcount", rowElement, Integer.toString(activity.getHeadcount()));
//			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));
//			Utils.createElementWithTextContent("since", rowElement, Utils.formatTimestamp(activity.getSince()));
//			Utils.createElementWithTextContent("until", rowElement, Utils.formatTimestamp(activity.getUntil()));
//			Utils.createElementWithTextContent("age", rowElement, age == null ? "" : age.toString());
//			if (gender != null) {
//				Utils.createElementWithTextContent("gender", rowElement, activity.getGender() ? "true" : "false");
//			}
//			Utils.createElementWithTextContent("occupation", rowElement, occupation == null ? "" : occupation.getName());
//			Utils.createElementWithTextContent("district", rowElement, district == null ? "" : district.getName());
//		}
//
//		/*
//		 產生頁面
//		 */
//		ModelAndView modelAndView = new ModelAndView("agent/activity");
//		modelAndView.getModelMap().addAttribute(new DOMSource(document));
//		return modelAndView;
//	}
}
