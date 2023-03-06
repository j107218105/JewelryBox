package tw.jewelrybox.service;

import java.util.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.*;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;

/**
 * 服務層
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
public class Services {

	@Autowired
	private AccordionRepository accordionRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private MethodRepository methodRepository;

	@Autowired
	private SomeoneRepository someoneRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	/**
	 * 俺行不!?
	 *
	 * @param someone 會員
	 * @param request 請求
	 * @return 權限
	 */
	public Privilege howAmI(HttpServletRequest request, HttpSession session) {
		Integer myId = whoAmI(session);
		if (myId == null) {
			return null;
		}
		Someone me = someoneRepository.findOne(myId);
		if (me == null) {
			return null;
		}

		Method method = methodRepository.findOneByName(request.getMethod());
		if (method == null) {
			return null;
		}

		Privilege privilege = null;
		String uri = request.getRequestURI().replaceAll("^".concat(request.getContextPath()), "");
		for (Mapping mapping : mappingRepository.findByMethod(method)) {
			if (uri.matches(mapping.getPattern())) {
				privilege = privilegeRepository.findOneByMappingAndAgent(mapping, me);
				break;
			}
		}

		return privilege;
	}

	/**
	 * 俺在那兒!?
	 *
	 * @param request 請求
	 * @param session session 階段
	 * @return 領航員
	 */
	public Element whereAmI(Document document, HttpServletRequest request, HttpSession session) {
		Element navigatorElement = Utils.createElement("navigator", document.getDocumentElement());

		Integer myId = whoAmI(session);
		if (myId == null) {
			return navigatorElement;
		}
		Someone agent = someoneRepository.findOne(myId);
		if (agent == null || privilegeRepository.countByAgent(agent) == 0) {
			return navigatorElement;
		}

		for (Accordion accordion : accordionRepository.findAllOrderBySort()) {
			Element accordionElement = Utils.createElementWithAttribute("accordion", navigatorElement, "id", accordion.getId().toString());
			accordionElement.setAttribute("name", accordion.getName());
			accordionElement.setAttribute("ordinal", Short.toString(accordion.getSort()));

			String contextPath = request.getContextPath(), requestURI = request.getRequestURI();
			for (Mapping mapping : mappingRepository.findByAccordionAndUriNotNullOrderBySort(accordion)) {
				if (privilegeRepository.findOneByMappingAndAgent(mapping, agent) != null) {
					String uri = mapping.getUri();

					Element mappingElement = Utils.createElementWithTextContentAndAttribute("mapping", accordionElement, mapping.getTitle(), "id", mapping.getId().toString());
					mappingElement.setAttribute("ordinal", Short.toString(mapping.getSort()));
					mappingElement.setAttribute("uri", uri);

					StringBuilder regex = new StringBuilder();
					regex.append("^").append(uri.replaceAll("/", "\\\\/").replaceAll("\\.", "\\\\.")).append("$");
					if (requestURI.replaceAll("^".concat(contextPath), "").matches(regex.toString())) {
						accordionElement.setAttribute("toggled", "true");
						mappingElement.removeAttribute("uri");
					}
				}
			}

			if (!accordionElement.hasChildNodes()) {
				navigatorElement.removeChild(accordionElement);
			}
		}

		return navigatorElement;
	}

	/**
	 * 俺素哪位!?
	 *
	 * @param session 階段
	 * @return 「會員」的「主鍵」
	 */
	public Integer whoAmI(HttpSession session) {
		Integer myId = null;

		if (session.isNew()) {
			return myId;
		}

		try {
			myId = (Integer) session.getAttribute("me");
		} catch (Exception exception) {
			return myId;
		}

		if (myId == null) {
			return myId;
		}

		Someone someone = someoneRepository.findOneByIdAndDeniedFalse(myId);
		return someone == null || someone.getId() == null ? null : myId;
	}

	/**
	 * @param age 年齡
	 * @param since 日期
	 * @return 在「日期」當天能滿「年齡」的出生日期
	 */
	public static Date getMinimumBirthDateByAge(int age, Date since) {
		GregorianCalendar gregorianCalendar = Utils.DEFAULT_CALENDAR;
		gregorianCalendar.setTime(since);
		gregorianCalendar.add(Calendar.YEAR, age * -1);
		return gregorianCalendar.getTime();
	}
}
