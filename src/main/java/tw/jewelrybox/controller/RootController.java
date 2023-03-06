package tw.jewelrybox.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;
import tw.jewelrybox.service.Services;

/**
 * 「根」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private FinancialInstitutionRepository financialInstitutionRepository;

	@Autowired
	private OccupationRepository occupationRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private Services services;

	/**
	 * 首頁。
	 *
	 * @param request 請求
	 * @param session 階段
	 * @return 網頁
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView index(HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", request.getContextPath());

		if (myId == null) {
			/*
			 未登入
			 */
			ModelAndView modelAndView = new ModelAndView("welcome");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		Someone me = someoneRepository.findOne(myId);

		/*
		 登入了
		 */
		documentElement.setAttribute("name", me.getName());

		if (privilegeRepository.countByAgent(me) > 0) {
			/*
			 管理者
			 */
			documentElement.setAttribute("title", "後臺");
			documentElement.appendChild(services.whereAmI(document, request, session));

			ModelAndView modelAndView = new ModelAndView("manager");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		/*
		 一般會員
		 */
		ModelAndView modelAndView = new ModelAndView("default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 使用Facebook登入。
	 *
	 * @param accessToken
	 * @param expiry 逾期秒
	 * @param session 階段
	 * @return 字串
	 */
	@RequestMapping(value = "/logIn.asp", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String logIn(@RequestParam("accessToken") String accessToken, @RequestParam("expiry") String expiry, HttpServletRequest request, HttpSession session) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (services.whoAmI(session) != null) {
			return jsonObject.put("reason", "您已經登入了！").toString();
		}

		Facebook facebook = new FacebookTemplate(accessToken);
		UserOperations userOperations = facebook.userOperations();
		FacebookProfile facebookProfile = userOperations.getUserProfile();
		String contextPath = request.getContextPath(), facebookId = facebookProfile.getId();
		Someone someone = someoneRepository.findOneByFacebookId(facebookId);

		if (someone == null || someone.getId() == null) {
			//來自於Facebook
			session.setAttribute("from", "F");

			//Facebook帳號
			session.setAttribute("socialId", facebookId);

			//生日
			try {
				System.err.print(facebookProfile.getBirthday());
				Date birthday = new SimpleDateFormat("MM/dd/yyyy", Locale.TAIWAN).parse(facebookProfile.getBirthday());
				session.setAttribute("birthday", Utils.format(birthday));
			} catch (Exception ignore) {
			}

			Boolean gender;//性別
			try {
				String genderString = facebookProfile.getGender();
				if (genderString == null) {
					throw new NullPointerException();
				}

				if (genderString.matches("^male$")) {
					gender = true;
				} else if (genderString.matches("^female$")) {
					gender = false;
				} else {
					throw new Exception();
				}
			} catch (Exception ignore) {
				gender = someoneRepository.countByGender(false) < someoneRepository.countByGender(true);
			}
			session.setAttribute("gender", gender.toString());

			//全名
			try {
				String name = facebookProfile.getName().trim().replaceAll("  ", " ");
				session.setAttribute("name", name);
			} catch (Exception ignore) {
			}

			//電子郵件
			try {
				String email = facebookProfile.getEmail().trim().toLowerCase();
				session.setAttribute("email", email);
			} catch (Exception ignore) {
			}

			return jsonObject.put("result", contextPath.concat("/register.asp")).toString();
		}
		session.setAttribute("me", someone.getId());

		return jsonObject.put("result", contextPath.concat("/")).toString();
	}

	/**
	 * 使用Google登入。
	 *
	 * @param accessToken
	 * @param expiry 逾期(秒)
	 * @param session 階段
	 * @return 字串
	 */
	@RequestMapping(value = "/signIn.asp", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String signIn(@RequestParam("accessToken") String accessToken, @RequestParam("expiry") String expiry, HttpServletRequest request, HttpSession session) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (services.whoAmI(session) != null) {
			return jsonObject.put("reason", "您已經登入了！").toString();
		}

		Google google = new GoogleTemplate(accessToken);
		PlusOperations plusOperations = google.plusOperations();
		Person person = plusOperations.getGoogleProfile();
		String contextPath = request.getContextPath(), googleId = person.getId();
		Someone someone = someoneRepository.findOneByGoogleID(googleId);

		if (someone == null || someone.getId() == null) {
			//來自於Google
			session.setAttribute("from", "G");

			//Facebook帳號
			session.setAttribute("socialId", googleId);

			//生日
			try {
				Date birthday = person.getBirthday();
				session.setAttribute("birthday", Utils.format(birthday));
			} catch (Exception ignore) {
			}

			Boolean gender;//性別
			try {
				String genderString = person.getGender();
				if (genderString == null) {
					throw new NullPointerException();
				}

				if (genderString.matches("^male$")) {
					gender = true;
				} else if (genderString.matches("^female$")) {
					gender = false;
				} else {
					throw new Exception();
				}
			} catch (Exception ignore) {
				gender = someoneRepository.countByGender(false) < someoneRepository.countByGender(true);
			}
			session.setAttribute("gender", gender.toString());

			//全名
			try {
				String name = person.getDisplayName().trim().replaceAll("  ", " ");
				session.setAttribute("name", name);
			} catch (Exception ignore) {
			}

			//電子郵件
			try {
				String email = person.getAccountEmail().trim().toLowerCase();
				session.setAttribute("email", email);
			} catch (Exception ignore) {
			}

			return jsonObject.put("result", contextPath.concat("/register.asp")).toString();
		}
		session.setAttribute("me", someone.getId());

		return jsonObject.put("result", contextPath.concat("/")).toString();
	}

	/**
	 * 註冊(首次登入)後填寫個人資料。
	 *
	 * @param request 請求
	 * @param session 階段
	 * @return 頁面
	 */
	@RequestMapping(value = "/register.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView register(HttpServletRequest request, HttpSession session) throws Exception {
		if (services.whoAmI(session) != null) {
			return new ModelAndView("redirect:/");
		}

		String from = (String) session.getAttribute("from"), socialId;
		if (from != null && from.matches("^(F|G)$")) {
			socialId = (String) session.getAttribute("socialId");
		} else {
			return new ModelAndView("redirect:/");
		}

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", contextPath.concat("/register.asp"));

		Utils.createElementWithTextContent("from", formElement, from);//使用(Facebook|Google)註冊
		Utils.createElementWithTextContent(from.matches("^F$") ? "facebookId" : "googleId", formElement, socialId);//(Facebook|Google)帳號
		Utils.createElementWithTextContent("birthday", formElement, (String) session.getAttribute("birthday"));//生日
		Utils.createElementWithTextContent("gender", formElement, (String) session.getAttribute("gender"));//性別
		Utils.createElementWithTextContent("name", formElement, (String) session.getAttribute("name"));//全名
		Utils.createElementWithTextContent("email", formElement, (String) session.getAttribute("email"));//電子郵件
		Element occupationElement = Utils.createElement("occupation", formElement);//職業
		Element districtElement = Utils.createElement("district", formElement);//行政區
		for (District district : districtRepository.findAll(new Sort("id"))) {
			Utils.createElementWithTextContentAndAttribute("option", districtElement, district.getName(), "value", district.getId().toString());
		}
		for (Occupation occupation : occupationRepository.findAll(new Sort("name"))) {
			Utils.createElementWithTextContentAndAttribute("option", occupationElement, occupation.getName(), "value", occupation.getId().toString());
		}
		Element financialInstitutionElement = Utils.createElement("financialInstitution", formElement);//金融機構
		for (FinancialInstitution financialInstitution : financialInstitutionRepository.findByShownTrueOrderByCode()) {
			Utils.createElementWithTextContentAndAttribute("option", financialInstitutionElement, financialInstitution.getCode().concat(" &#187; ").concat(financialInstitution.getName()), "value", financialInstitution.getId().toString());
		}

		ModelAndView modelAndView = new ModelAndView("register");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 註冊(首次登入)後處理個人資料。
	 *
	 * @param from 使用(F|G)註冊
	 * @param id 社群ID
	 * @param birthday 生日
	 * @param gender 性別
	 * @param name 全名
	 * @param email 電子郵件
	 * @param cellular 手機
	 * @param occupationId 職業(外鍵)
	 * @param districtId 行政區(外鍵)
	 * @param financialInstitutionId 金融機構(外鍵)
	 * @param financialAccountHolder 金融帳戶戶名
	 * @param financialAccountNumber 金融帳戶號碼
	 * @param request 請求
	 * @param session 階段
	 * @return 失敗呈現網頁；成功則重導向。
	 */
	@RequestMapping(value = "/register.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView register(@RequestParam String from, @RequestParam(defaultValue = "") String facebookId, @RequestParam(defaultValue = "") String googleId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday, @RequestParam Boolean gender, @RequestParam String name, @RequestParam String email, @RequestParam(required = false) String cellular, @RequestParam(required = false, value = "occupation") Short occupationId, @RequestParam(required = false, value = "district") Short districtId, @RequestParam(required = false, value = "financialInstitution") Short financialInstitutionId, @RequestParam(defaultValue = "") String financialAccountHolder, @RequestParam(defaultValue = "") String financialAccountNumber, HttpServletRequest request, HttpSession session) throws Exception {
		if (services.whoAmI(session) != null) {
			return new ModelAndView("redirect:/");
		}

		String contextPath = request.getContextPath(), errorMessage = null, requestURI = request.getRequestURI();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", contextPath.concat(requestURI));
		Utils.createElementWithTextContent("from", formElement, from);//使用(Facebook|Google)註冊

		facebookId = facebookId.trim();//Facebook帳號
		if (from.matches("^F$") && facebookId.isEmpty()) {
			errorMessage = "取得 Facebook ID 時發生不明的錯誤，請稍後再嘗試。";
		}
		Utils.createElementWithTextContent("facebookId", formElement, facebookId);//Facebook帳號ID

		googleId = googleId.trim();//Google帳號
		if (from.matches("^G$") && googleId.isEmpty()) {
			errorMessage = "取得 Google ID 時發生不明的錯誤，請稍後再嘗試。";
		}
		Utils.createElementWithTextContent("googleId", formElement, googleId);//Google帳號ID

		if (birthday == null) {
			errorMessage = "生日不得為空值。";
		} else {
			Utils.createElementWithTextContent("birthday", formElement, Utils.format(birthday));//生日
		}

		Utils.createElementWithTextContent("gender", formElement, gender.toString());//性別

		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "全名不得為空值。";
		}
		Utils.createElementWithTextContent("name", formElement, name);//全名

		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "電子郵件不得為空值。";
		}
		if (!email.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")) {
			errorMessage = "無效的電子郵件。";
		}
		try {
			if (Objects.equals(someoneRepository.findOneByEmail(email).getEmail(), email)) {
				errorMessage = "您的電子郵件已被其他會員註冊。";
			}
		} catch (NullPointerException ignore) {
		}
		Utils.createElementWithTextContent("email", formElement, email);//電子郵件

		cellular = cellular.trim();
		if (!cellular.isEmpty()) {
			cellular = cellular.replaceAll("\\D", "");
			if (!cellular.matches("^09[\\d]{8}$")) {
				errorMessage = "手機須為十碼且以「09」開頭。";
			}
		}
		Utils.createElementWithTextContent("cellular", formElement, (String) cellular);//手機

		Element occupationElement = Utils.createElement("occupation", formElement);//職業
		Occupation occupation = occupationId == null ? null : occupationRepository.findOne(occupationId);
		for (Occupation o : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, o.getName(), "value", o.getId().toString());
			if (o.equals(occupation)) {
				optionElement.setAttribute("selected", null);
			}
		}

		Element districtElement = Utils.createElement("district", formElement);//行政區
		District district = districtId == null ? null : districtRepository.findOne(districtId);
		for (District d : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, d.getName(), "value", d.getId().toString());
			if (d.equals(district)) {
				optionElement.setAttribute("selected", null);
			}
		}

		Element financialInstitutionElement = Utils.createElement("financialInstitution", formElement);//金融機構
		FinancialInstitution financialInstitution = financialInstitutionId == null ? null : financialInstitutionRepository.findOne(financialInstitutionId);
		for (FinancialInstitution fI : financialInstitutionRepository.findByShownTrueOrderByCode()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", financialInstitutionElement, fI.getCode().concat(" &#187; ").concat(fI.getName()), "value", fI.getId().toString());
			if (fI.equals(financialInstitution)) {
				optionElement.setAttribute("selected", null);
			}
		}

		financialAccountHolder = financialAccountHolder.trim();
		if (financialInstitutionId != null && financialAccountHolder.isEmpty()) {
			errorMessage = "由於您有選擇金融機構，請填寫「金融帳戶戶名」。";
		}
		Utils.createElementWithTextContent("financialAccountHolder", formElement, financialAccountHolder);//金融帳戶戶名

		financialAccountNumber = financialAccountNumber.trim();
		if (financialInstitutionId != null) {
			if (financialAccountNumber.isEmpty()) {
				errorMessage = "由於您有選擇金融機構，請填寫「金融帳戶號碼」。";
			} else if (someoneRepository.countByFinancialInstitutionAndFinancialAccountNumber(financialInstitutionRepository.findOne(financialInstitutionId), financialAccountNumber) > 0) {
				errorMessage = "您的「金融帳戶號碼」已被其他會員註冊。";
			}
		}
		Utils.createElementWithTextContent("financialAccountNumber", formElement, financialAccountNumber);//金融帳戶號碼

		if (errorMessage == null) {
			Someone someone = new Someone(birthday, gender, name, email, false);
			if (from.matches("^F$") && !facebookId.isEmpty()) {
				someone.setFacebookId(facebookId);
			}
			if (from.matches("^G$") && !googleId.isEmpty()) {
				someone.setGoogleID(googleId);
			}
			if (!cellular.isEmpty()) {
				someone.setCellular(cellular);
			}
			someone.setOccupation(occupation);
			someone.setDistrict(district);
			if (financialInstitution != null) {
				someone.setFinancialInstitution(financialInstitution);
				someone.setFinancialAccountHolder(financialAccountHolder);
				someone.setFinancialAccountNumber(financialAccountNumber);
			}
			someoneRepository.saveAndFlush(someone);

			session.removeAttribute("from");
			session.removeAttribute("socialId");
			session.removeAttribute("birthday");
			session.removeAttribute("gender");
			session.removeAttribute("name");
			session.removeAttribute("email");
			session.setAttribute("me", someone.getId());

			return new ModelAndView("redirect:/");
		}

		Utils.createElementWithTextContent("errorMessage", formElement, errorMessage);//錯誤訊息

		ModelAndView modelAndView = new ModelAndView("register");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 未開始但候選中的活動。
	 *
	 * @param request 請求
	 * @param session 階段
	 * @return 網頁
	 */
	@RequestMapping(value = "/available.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView available(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element tableElement = Utils.createElement("table", documentElement);
		for (tw.jewelrybox.entity.Activity activity : candidateRepository.findAvailableActivity(me)) {
			final int headcount = activity.getHeadcount();
			long participant = candidateRepository.countByActivityAndRepliedTrueAndRepliedByNotNull(activity);
			participant = participant <= headcount ? headcount - participant : 0;

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, activity.getId().toString());//主鍵
			Utils.createElementWithTextContent("since", rowElement, Utils.format(activity.getSince()));//開始時戳
			Utils.createElementWithTextContent("name", rowElement, activity.getName());//活動名稱
			Utils.createElementWithTextContent("headcount", rowElement, Long.toString(participant));//活動剩餘名額
			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));//點數
			Utils.createElementWithTextContent("until", rowElement, Utils.format(activity.getUntil()));//結束時戳
		}

		ModelAndView modelAndView = new ModelAndView("available");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 我要參與活動。
	 *
	 * @param id 活動的主鍵
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/participate/{id:[\\d]+}.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String participate(@PathVariable Integer id, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "您尚未登入！").toString();
		}
		Someone me = someoneRepository.findOne(myId);

		tw.jewelrybox.entity.Activity activity = activityRepository.findOne(id);
		if (activity == null) {
			return jsonObject.put("reason", "找不到活動！").toString();
		}
		final int headcount = activity.getHeadcount();//人數上限

		Candidate candidate = candidateRepository.findOneByActivityAndSomeone(activity, me);
		if (candidate == null) {
			return jsonObject.put("reason", "您未被受邀活動！").toString();
		}

		if (candidateRepository.countByActivityAndRepliedTrueAndRepliedByNotNull(activity) >= headcount) {
			return jsonObject.put("reason", "已到達活動人數上限！").toString();
		}

		candidate.setReplied(true);
		candidate.setRepliedBy(Utils.DEFAULT_CALENDAR.getTime());
		candidateRepository.saveAndFlush(candidate);

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 我要棄權活動。
	 *
	 * @param id 活動的主鍵。
	 * @param request 請求
	 * @param session 階段
	 * @return JSON
	 */
	@RequestMapping(value = "/forfeit/{id:[\\d]+}.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String forfeit(@PathVariable Integer id, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return jsonObject.put("reason", "您尚未登入！").toString();
		}
		Someone me = someoneRepository.findOne(myId);

		tw.jewelrybox.entity.Activity activity = activityRepository.findOne(id);
		if (activity == null) {
			return jsonObject.put("reason", "找不到活動！").toString();
		}
		final int headcount = activity.getHeadcount();//人數上限

		Candidate candidate = candidateRepository.findOneByActivityAndSomeone(activity, me);
		if (candidate == null) {
			return jsonObject.put("reason", "您未被受邀活動！").toString();
		}

		if (candidateRepository.countByActivityAndRepliedTrueAndRepliedByNotNull(activity) >= headcount) {
			return jsonObject.put("reason", "已到達活動人數上限！").toString();
		}

		candidate.setReplied(false);
		candidate.setRepliedBy(Utils.DEFAULT_CALENDAR.getTime());
		candidateRepository.saveAndFlush(candidate);

		return jsonObject.put("response", true).toString();
	}

	@RequestMapping(value = "/underway.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView underway(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element tableElement = Utils.createElement("table", documentElement);
		for (tw.jewelrybox.entity.Activity activity : candidateRepository.findUnderwayActivity(me)) {
			Candidate candidate = candidateRepository.findOneByActivityAndSomeone(activity, me);
			boolean proof = candidate.getProof() != null && candidate.getProvedBy() != null;
			boolean approval = candidate.getAgent() != null;

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("name", rowElement, activity.getName());//活動名稱
			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));//價值點數
			Utils.createElementWithTextContent("until", rowElement, Utils.formatTimestamp(activity.getUntil()));//結束時間
			//活動進行情況
			if (approval) {
				Utils.createElementWithTextContent("status", rowElement, "恭喜你已完成");
			} else {
				Utils.createElementWithTextContent("status", rowElement, proof ? "待審核" : "未上傳");
			}
		}

		ModelAndView modelAndView = new ModelAndView("underway");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	@RequestMapping(value = "/finished.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView finished(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number, HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element tableElement = Utils.createElement("table", documentElement);
		for (tw.jewelrybox.entity.Activity activity : candidateRepository.findFinishedActivity(me)) {
			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("since", rowElement, Utils.format(activity.getSince()));//開始時戳
			Utils.createElementWithTextContent("name", rowElement, activity.getName());//活動名稱
			Utils.createElementWithTextContent("headcount", rowElement, Integer.toString(activity.getHeadcount()));//人數
			Utils.createElementWithTextContent("score", rowElement, Short.toString(activity.getScore()));//點數
			Utils.createElementWithTextContent("until", rowElement, Utils.format(activity.getUntil()));//結束時戳
		}

		ModelAndView modelAndView = new ModelAndView("finished");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 顯示「個人資料」。
	 *
	 * @param request 帳號
	 * @param session 階段
	 * @return 網頁
	 */
	@RequestMapping(value = "/account.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView account(HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", contextPath.concat(request.getRequestURI()));

		//生日
		Utils.createElementWithTextContent("birthday", formElement, Utils.format(me.getBirthday()));

		//性別
		Utils.createElementWithTextContent("gender", formElement, Boolean.toString(me.getGender()));

		//全名
		Utils.createElementWithTextContent("name", formElement, me.getName());

		//電子郵件
		Utils.createElementWithTextContent("email", formElement, me.getEmail());

		//手機
		String cellular = me.getCellular();
		if (cellular != null) {
			Utils.createElementWithTextContent("cellular", formElement, cellular);
		}

		//職業
		Element occupationElement = Utils.createElement("occupation", formElement);
		Occupation occupation = me.getOccupation();
		for (Occupation o : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, o.getName(), "value", o.getId().toString());
			if (Objects.equals(o, occupation)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//行政區
		Element districtElement = Utils.createElement("district", formElement);
		District district = me.getDistrict();
		for (District d : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, d.getName(), "value", d.getId().toString());
			if (Objects.equals(d, district)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//金融機構
		Element financialInstitutionElement = Utils.createElement("financialInstitution", formElement);
		FinancialInstitution financialInstitution = me.getFinancialInstitution();
		for (FinancialInstitution fI : financialInstitutionRepository.findByShownTrueOrderByCode()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", financialInstitutionElement, fI.getCode().concat(" &#187; ").concat(fI.getName()), "value", fI.getId().toString());
			if (Objects.equals(fI, financialInstitution)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//金融帳戶戶名
		String financialAccountHolder = me.getFinancialAccountHolder();
		if (financialAccountHolder != null) {
			Utils.createElementWithTextContent("financialAccountHolder", formElement, financialAccountHolder);
		}

		//金融帳戶號碼
		String financialAccountNumber = me.getFinancialAccountNumber();
		if (financialAccountHolder != null) {
			Utils.createElementWithTextContent("financialAccountNumber", formElement, financialAccountNumber);
		}

		ModelAndView modelAndView = new ModelAndView("account");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 儲存「個人資料」。
	 *
	 * @param name 全名
	 * @param email 電子郵件
	 * @param cellular 手機
	 * @param occupationId 職業(外鍵)
	 * @param districtId 行政區(外鍵)
	 * @param financialInstitutionId 金融機構(外鍵)
	 * @param financialAccountHolder 金融帳戶戶名
	 * @param financialAccountNumber 金融帳戶號碼
	 * @param request 請求
	 * @param session 階段
	 * @return 失敗呈現原頁面；成功則重導向至原網址。
	 */
	@RequestMapping(value = "/account.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView account(@RequestParam String name, @RequestParam String email, @RequestParam(required = false) String cellular, @RequestParam(required = false, value = "occupation") Short occupationId, @RequestParam(required = false, value = "district") Short districtId, @RequestParam(required = false, value = "financialInstitution") Short financialInstitutionId, @RequestParam(defaultValue = "") String financialAccountHolder, @RequestParam(defaultValue = "") String financialAccountNumber, HttpServletRequest request, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);

		if (myId == null) {
			return new ModelAndView("redirect:/");
		}
		Someone someone = someoneRepository.findOne(myId);
		if (someone == null) {
			session.removeAttribute("me");
			session.invalidate();
			return new ModelAndView("redirect:/");
		}

		String contextPath = request.getContextPath(), errorMessage = null, requestURI = request.getRequestURI();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", contextPath.concat(requestURI));

		//生日
		Utils.createElementWithTextContent("birthday", formElement, Utils.format(someone.getBirthday()));

		//性別
		Utils.createElementWithTextContent("gender", formElement, someone.getGender() ? "true" : "false");

		//全名
		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "全名不得為空值。";
		}
		Utils.createElementWithTextContent("name", formElement, name);

		//電子郵件
		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "電子郵件不得為空值。";
		}
		if (!email.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")) {
			errorMessage = "無效的電子郵件。";
		}
		try {
			if (someoneRepository.countByIdNotAndEmail(myId, email) > 0) {
				errorMessage = "您的電子郵件已被其他會員註冊。";
			}
		} catch (NullPointerException ignore) {
		}
		Utils.createElementWithTextContent("email", formElement, email);

		//手機
		cellular = cellular.trim();
		if (cellular.isEmpty()) {
			cellular = null;
		} else {
			cellular = cellular.replaceAll("\\D", "");
			if (!cellular.matches("^09[\\d]{8}$")) {
				errorMessage = "手機須為十碼且以「09」開頭。";
			}
		}
		Utils.createElementWithTextContent("cellular", formElement, cellular == null ? "" : cellular);

		//職業
		Occupation occupation = occupationId == null ? null : occupationRepository.findOne(occupationId);
		Element occupationElement = Utils.createElement("occupation", formElement);
		for (Occupation o : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, o.getName(), "value", o.getId().toString());
			if (o.equals(occupation)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//行政區
		District district = districtId == null ? null : districtRepository.findOne(districtId);
		Element districtElement = Utils.createElement("district", formElement);
		for (District d : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, d.getName(), "value", d.getId().toString());
			if (Objects.equals(d, district)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//金融機構
		FinancialInstitution financialInstitution = financialInstitutionId == null ? null : financialInstitutionRepository.findOne(financialInstitutionId);
		Element financialInstitutionElement = Utils.createElement("financialInstitution", formElement);
		for (FinancialInstitution fI : financialInstitutionRepository.findByShownTrueOrderByCode()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", financialInstitutionElement, fI.getCode().concat(" &#187; ").concat(fI.getName()), "value", fI.getId().toString());
			if (fI.equals(financialInstitution)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//金融帳戶戶名
		financialAccountHolder = financialAccountHolder.trim();
		if (financialInstitutionId != null && financialAccountHolder.isEmpty()) {
			errorMessage = "由於您有選擇金融機構，請填寫「金融帳戶戶名」。";
		}
		Utils.createElementWithTextContent("financialAccountHolder", formElement, financialAccountHolder);

		//金融帳戶號碼
		financialAccountNumber = financialAccountNumber.trim();
		if (financialInstitutionId != null) {
			if (financialAccountNumber.isEmpty()) {
				errorMessage = "由於您有選擇金融機構，請填寫「金融帳戶號碼」。";
			} else if (someoneRepository.countByIdNotAndFinancialInstitutionAndFinancialAccountNumber(myId, financialInstitutionRepository.findOne(financialInstitutionId), financialAccountNumber) > 0) {
				errorMessage = "您的「金融帳戶號碼」已被其他會員註冊。";
			}
		}
		Utils.createElementWithTextContent("financialAccountNumber", formElement, financialAccountNumber);

		if (errorMessage == null) {
			someone.setName(name);
			someone.setEmail(email);
			someone.setCellular(cellular);
			someone.setOccupation(occupation);
			someone.setDistrict(district);
			someone.setFinancialInstitution(financialInstitution);
			if (financialInstitution == null) {
				someone.setFinancialAccountHolder(null);
				someone.setFinancialAccountNumber(null);
			} else {
				someone.setFinancialAccountHolder(financialAccountHolder);
				someone.setFinancialAccountNumber(financialAccountNumber);
			}
			someoneRepository.saveAndFlush(someone);

			return new ModelAndView("redirect:".concat(requestURI));
		}

		Utils.createElementWithTextContent("errorMessage", formElement, errorMessage);//錯誤訊息

		ModelAndView modelAndView = new ModelAndView("account");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 登出。
	 *
	 * @param session 階段
	 */
	@RequestMapping(value = "/bye.asp", method = RequestMethod.GET)
	protected void bye(HttpServletResponse response, HttpSession session) throws Exception {
		session.invalidate();

		response.sendRedirect("/");
	}
}
