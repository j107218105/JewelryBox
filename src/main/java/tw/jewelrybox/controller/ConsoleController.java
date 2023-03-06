package tw.jewelrybox.controller;

import java.util.GregorianCalendar;
import java.util.Locale;
import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;
import tw.jewelrybox.service.Services;

/**
 * 「後臺」控制器。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
public class ConsoleController {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CandidateRepository candidateRepository;

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
	 * 會員 » 全部
	 *
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現所有會員。
	 */
	@RequestMapping(value = "/everyone/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView everyone(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element tableElement = Utils.createElement("table", documentElement);

		for (Someone someone : someoneRepository.findAll()) {
			String facebookId = someone.getFacebookId(), googleId = someone.getGoogleID(), cellular = someone.getCellular(), financialAccountHolder = someone.getFinancialAccountHolder(), financialAccountNumber = someone.getFinancialAccountNumber();
			Occupation occupation = someone.getOccupation();
			FinancialInstitution financialInstitution = someone.getFinancialInstitution();

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, someone.getId().toString());
			if (facebookId != null) {
				Utils.createElementWithTextContent("facebookId", rowElement, facebookId);
			}
			if (googleId != null) {
				Utils.createElementWithTextContent("googleId", rowElement, googleId);
			}
			//年齡
			Long ageInMillis = (new GregorianCalendar(Locale.TAIWAN).getTimeInMillis() - someone.getBirthday().getTime()) / 1000 / 60 / 60 / 24 / 365;
			Utils.createElementWithTextContent("age", rowElement, ageInMillis.toString());
			Utils.createElementWithTextContent("gender", rowElement, Boolean.toString(someone.getGender()));
			Utils.createElementWithTextContent("name", rowElement, someone.getName());
			Utils.createElementWithTextContent("email", rowElement, someone.getEmail());
			Utils.createElementWithTextContent("denied", rowElement, Boolean.toString(someone.isDenied()));
			if (cellular != null) {
				Utils.createElementWithTextContent("cellular", rowElement, cellular);
			}
			if (occupation != null) {
				Utils.createElementWithTextContent("occupation", rowElement, occupation.getName());
			}
			if (financialInstitution != null && financialAccountHolder != null && financialAccountNumber != null) {
				Utils.createElementWithTextContent("financialInstitution", rowElement, financialInstitution.getName());
				Utils.createElementWithTextContent("financialAccountHolder", rowElement, financialAccountHolder);
				Utils.createElementWithTextContent("financialAccountNumber", rowElement, financialInstitution.getCode().concat(financialAccountNumber));
			}
		}

		ModelAndView modelAndView = new ModelAndView("someone/index");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 會員 » 待審核
	 *
	 * @param request 請求
	 * @param response 回應
	 * @param session 階段
	 * @return 未登入者回登入註冊頁、無權限者顯示錯誤頁、其它則以列表呈現有「待審核」狀態的會員。
	 */
	@RequestMapping(value = "/unproven/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView unproven(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		Integer myId = services.whoAmI(session);
		if (myId == null) {
			return new ModelAndView("redirect:/");//未登入
		}
		Privilege privilege = services.howAmI(request, session);
		if (privilege == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		Someone me = someoneRepository.findOne(myId);

		String contextPath = request.getContextPath();
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "contextPath", contextPath);
		documentElement.setAttribute("name", me.getName());

		Element tableElement = Utils.createElement("table", documentElement);

		for (Someone someone : candidateRepository.findThoseWhoNeedApprovals()) {
			String facebookId = someone.getFacebookId(), googleId = someone.getGoogleID(), cellular = someone.getCellular(), financialAccountHolder = someone.getFinancialAccountHolder(), financialAccountNumber = someone.getFinancialAccountNumber();
			Occupation occupation = someone.getOccupation();
			FinancialInstitution financialInstitution = someone.getFinancialInstitution();

			Element rowElement = Utils.createElement("row", tableElement);
			Utils.createElementWithTextContent("id", rowElement, someone.getId().toString());
			if (facebookId != null) {
				Utils.createElementWithTextContent("facebookId", rowElement, facebookId);
			}
			if (googleId != null) {
				Utils.createElementWithTextContent("googleId", rowElement, googleId);
			}
			//年齡
			Long ageInMillis = (new GregorianCalendar(Locale.TAIWAN).getTimeInMillis() - someone.getBirthday().getTime()) / 1000 / 60 / 60 / 24 / 365;
			Utils.createElementWithTextContent("age", rowElement, ageInMillis.toString());
			Utils.createElementWithTextContent("gender", rowElement, Boolean.toString(someone.getGender()));
			Utils.createElementWithTextContent("name", rowElement, someone.getName());
			Utils.createElementWithTextContent("email", rowElement, someone.getEmail());
			Utils.createElementWithTextContent("denied", rowElement, Boolean.toString(someone.isDenied()));
			if (cellular != null) {
				Utils.createElementWithTextContent("cellular", rowElement, cellular);
			}
			if (occupation != null) {
				Utils.createElementWithTextContent("occupation", rowElement, occupation.getName());
			}
			if (financialInstitution != null && financialAccountHolder != null && financialAccountNumber != null) {
				Utils.createElementWithTextContent("financialInstitution", rowElement, financialInstitution.getName());
				Utils.createElementWithTextContent("financialAccountHolder", rowElement, financialAccountHolder);
				Utils.createElementWithTextContent("financialAccountNumber", rowElement, financialInstitution.getCode().concat(financialAccountNumber));
			}
		}

		ModelAndView modelAndView = new ModelAndView("someone/index");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
}
