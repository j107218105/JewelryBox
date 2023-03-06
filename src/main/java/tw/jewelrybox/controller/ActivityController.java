package tw.jewelrybox.controller;

import java.util.*;
import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;
import tw.jewelrybox.service.*;
import tw.jewelrybox.specification.*;

/**
 * 「活動」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/activity")
public class ActivityController {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private OccupationRepository occupationRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	@Autowired
	private ActivityService activityService;

	/**
	 * 後臺 » 活動一覽
	 *
	 * @param size 每頁幾筆
	 * @param number 第幾頁
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupationId 職業需求
	 * @param districtId 行政區需求
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有活動。
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView index(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") Integer headcount, @RequestParam(defaultValue = "") Short score, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(defaultValue = "") Date since, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(defaultValue = "") Date until, @RequestParam(defaultValue = "") Short age, @RequestParam(defaultValue = "") Boolean gender, @RequestParam(value = "occupation", defaultValue = "") Short occupationId, @RequestParam(value = "district", defaultValue = "") Short districtId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
			//搜尋活動名稱
			name = name.trim();
			if (name.isEmpty()) {
				throw new Exception();
			}
		} catch (Exception exception) {
			name = null;
		}
		Pageable pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "since"));
		Page<Activity> pageOfEntities = activityRepository.findAll(ActivitySpecs.anySpec(name, headcount, score, since, until, age, gender, occupationId == null ? null : occupationRepository.findOne(occupationId), districtId == null ? null : districtRepository.findOne(districtId)), pageRequest);
		if (name != null) {
			searchElement.setAttribute("name", name);
		}
		if (headcount != null) {
			searchElement.setAttribute("headcount", headcount.toString());
		}
		if (score != null) {
			searchElement.setAttribute("score", score.toString());
		}
		if (since != null) {
			searchElement.setAttribute("since", Utils.format(since));
		}
		if (until != null) {
			searchElement.setAttribute("until", Utils.format(until));
		}
		if (age != null) {
			searchElement.setAttribute("age", age.toString());
		}
		if (gender != null) {
			searchElement.setAttribute("gender", gender ? "true" : "false");
		}
		Element occupationElement = Utils.createElement("occupation", searchElement);
		for (Occupation occupation : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, occupation.getName(), "value", occupation.getId().toString());
			if (occupation.getId().equals(occupationId)) {
				optionElement.setAttribute("selected", "");
			}
		}
		Element districtElement = Utils.createElement("district", searchElement);
		for (District district : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, district.getName(), "value", district.getId().toString());
			if (district.getId().equals(districtId)) {
				optionElement.setAttribute("selected", "");
			}
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
		Element pagesElement = Utils.createElement("pages", searchElement);
		for (Integer i = 0; i < totalPages; i++) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", pagesElement, Integer.toString(i + 1), "value", i.toString());
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
			Date activitySince = activity.getSince();
			Short activityAge = activity.getAge();
			Boolean activityGender = activity.getGender();
			Occupation activityOccupation = activity.getOccupation();
			District activityDistrict = activity.getDistrict();
			boolean notified = activity.isNotified();

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, activity.getId().toString());
			Utils.createElementWithTextContent("name", rowElement, activity.getName());
			Utils.createElementWithTextContent("headcount", rowElement, Integer.toString(activity.getHeadcount()));
			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));
			Utils.createElementWithTextContent("since", rowElement, Utils.formatTimestamp(activitySince));
			Utils.createElementWithTextContent("until", rowElement, Utils.formatTimestamp(activity.getUntil()));
			if (activityAge != null) {
				Utils.createElementWithTextContent("age", rowElement, activityAge.toString());
				Utils.createElementWithTextContent("minimumBirth", rowElement, Utils.formatTimestamp(Services.getMinimumBirthDateByAge(activityAge, activitySince)));
			}
			if (activityGender != null) {
				Utils.createElementWithTextContent("gender", rowElement, activityGender ? "true" : "false");
			}
			if (activityOccupation != null) {
				Utils.createElementWithTextContent("occupation", rowElement, activityOccupation.getName());
			}
			if (activityDistrict != null) {
				Utils.createElementWithTextContent("district", rowElement, activityDistrict.getName());
			}
			Utils.createElementWithTextContent("editable", rowElement, notified ? "false" : "true");
			Utils.createElementWithTextContent("notify", rowElement, Utils.DEFAULT_CALENDAR.getTime().before(activitySince) && !notified ? "true" : "false");
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("activity/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 發簡訊通知候選者。
	 *
	 * @param id 活動的主鍵
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/notify.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String notify(@PathVariable Integer id, HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}
		if (services.howAmI(request, session) == null) {
			return jsonObject.put("reason", "您並未被授權進行此項操作！").toString();//被禁止
		}

		Activity activity = activityRepository.findOne(id);
		if (activity == null) {
			return jsonObject.put("reason", "找不到活動！").toString();
		}
		if (activity.isNotified()) {
			return jsonObject.put("reason", "已通知完畢！").toString();
		}

		Short age = activity.getAge();//年齡需求
		Date birthday = null;//年齡需求的最晚出生日期
		if (age != null) {
			GregorianCalendar gregorianCalendar = Utils.DEFAULT_CALENDAR;
			gregorianCalendar.setTime(activity.getSince());
			gregorianCalendar.add(Calendar.YEAR, age * -1);
			birthday = gregorianCalendar.getTime();
		}

		/*
		 候選者
		 */
		for (Someone someone : someoneRepository.findAll(SomeoneSpecs.guesstimate(birthday, activity.getGender(), activity.getOccupation(), activity.getDistrict()))) {
			if (privilegeRepository.countByAgent(someone) == 0 && candidateRepository.findOneByActivityAndSomeone(activity, someone) == null) {
				candidateRepository.saveAndFlush(new Candidate(activity, someone));
			}
		}

		activity.setNotified(true);
		activityRepository.saveAndFlush(activity);

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 後臺 » 活動 » {活動名稱}
	 *
	 * @param id 主鍵
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 某活動的修改頁面。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		Activity activity = activityRepository.findOne(id);
		if (activity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;//找不到
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "&#123;".concat(activity.getName()).concat("&#125;"));
		documentElement.setAttribute("contextPath", request.getContextPath());
		documentElement.setAttribute("name", me.getName());
		documentElement.appendChild(services.whereAmI(document, request, session));

		/*
		 表單
		 */
		activityService.loadOne(activity, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("activity/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 後臺 » 活動 » {活動名稱}
	 *
	 * @param id 主鍵
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupationId 職業需求
	 * @param districtId 行政區需求
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView update(@PathVariable Integer id, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") Integer headcount, @RequestParam(defaultValue = "") Short score, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(defaultValue = "") Date since, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(defaultValue = "") Date until, @RequestParam(defaultValue = "") Short age, @RequestParam(defaultValue = "") Boolean gender, @RequestParam(value = "occupation", defaultValue = "") Short occupationId, @RequestParam(value = "district", defaultValue = "") Short districtId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		Activity activity = activityRepository.findOne(id);
		if (activity == null) {
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
		if (activityService.saveOne(activity, name, headcount, score, since, until, age, gender, occupationId, districtId, documentElement, request)) {
			return new ModelAndView("redirect:/activity/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("activity/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增活動
	 *
	 * @param request 請求
	 * @param session 階段
	 * @return 網頁
	 */
	@RequestMapping(value = "/add.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView create(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
		activityService.loadOne(null, documentElement, request);

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("activity/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增活動
	 *
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupationId 職業需求
	 * @param districtId 行政區需求
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 錯誤則顯示錯誤訊息；成功則重導向至列表。
	 */
	@RequestMapping(value = "/add.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(
		@RequestParam(defaultValue = "") String name,
		@RequestParam(defaultValue = "") Integer headcount,
		@RequestParam(defaultValue = "") Short score,
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(defaultValue = "") Date since,
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(defaultValue = "") Date until,
		@RequestParam(defaultValue = "") Short age,
		@RequestParam(defaultValue = "") Boolean gender,
		@RequestParam(value = "occupation", defaultValue = "") Short occupationId,
		@RequestParam(value = "district", defaultValue = "") Short districtId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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
		if (activityService.saveOne(new Activity(), name, headcount, score, since, until, age, gender, occupationId, districtId, documentElement, request)) {
			return new ModelAndView("redirect:/activity/");//返回列表
		}

		/*
		 產生頁面
		 */
		ModelAndView modelAndView = new ModelAndView("activity/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * {估算符合活動需求的人數}
	 *
	 * @param since 開始時戳
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupationId 職業需求
	 * @param districtId 行政區需求
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/guesstimate.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String guesstimate(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(defaultValue = "") Date since, @RequestParam(defaultValue = "") Short age, @RequestParam(defaultValue = "") Boolean gender, @RequestParam(value = "occupation", defaultValue = "") Short occupationId, @RequestParam(value = "district", defaultValue = "") Short districtId, HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}
		if (services.howAmI(request, session) == null) {
			return jsonObject.put("reason", "您並未被授權進行此項操作！").toString();//被禁止
		}

		Date birthday = null;
		if (since != null && age != null) {
			GregorianCalendar gregorianCalendar = Utils.DEFAULT_CALENDAR;
			gregorianCalendar.setTime(since);
			gregorianCalendar.add(Calendar.YEAR, age * -1);
			birthday = gregorianCalendar.getTime();
		}

		Occupation occupation = null;
		if (occupationId != null) {
			occupation = occupationRepository.findOne(occupationId);
		}

		District district = null;
		if (districtId != null) {
			district = districtRepository.findOne(districtId);
		}

		StringBuilder stringBuilder = new StringBuilder("符合需求的會員約有");
		stringBuilder.append(someoneRepository.count(SomeoneSpecs.guesstimate(birthday, gender, occupation, district)));
		stringBuilder.append("人。");
		jsonObject.put("result", stringBuilder.toString());

		return jsonObject.put("response", true).toString();
	}
}
