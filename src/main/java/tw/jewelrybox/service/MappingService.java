package tw.jewelrybox.service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;

/**
 * 「途徑」服務層
 *
 * @author	P-C Lin (m.accordion.m 高科技黑手)
 */
@org.springframework.stereotype.Service
public class MappingService {

	@Autowired
	private AccordionRepository accordionRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private MethodRepository methodRepository;

	/**
	 * 「載入」途徑。
	 *
	 * @param mapping 途徑
	 * @param parentNode 父層元素
	 * @param request 請求
	 * @return 新增或修改的頁面
	 */
	public void loadOne(Mapping mapping, Element parentNode, HttpServletRequest request) throws ParserConfigurationException {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());

		Accordion accordion = null;//手風琴
		Method method = null;//方式

		if (mapping != null) {
			accordion = mapping.getAccordion();//手風琴

			method = mapping.getMethod();//方式

			Short sort = mapping.getSort();//排序
			if (sort != null) {
				Utils.createElementWithTextContent("sort", formElement, sort.toString());
			}

			String title = mapping.getTitle();//標題
			if (title != null) {
				Utils.createElementWithTextContent("title", formElement, title);
			}

			Utils.createElementWithTextContent("uri", formElement, mapping.getUri());//路徑

			Utils.createElementWithTextContent("pattern", formElement, mapping.getPattern());//規則

			Utils.createElementWithTextContent("description", formElement, mapping.getDescription());//描述
		}

		Element accordionElement = Utils.createElement("accordion", formElement);
		for (Accordion a : accordionRepository.findAllOrderBySort()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", accordionElement, a.getName(), "value", a.getId().toString());
			if (a.equals(accordion)) {
				optionElement.setAttribute("selected", null);
			}
		}

		Element methodElement = Utils.createElement("method", formElement);
		for (Method m : methodRepository.findAll()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", methodElement, m.getName(), "value", m.getId().toString());
			if (m.equals(method)) {
				optionElement.setAttribute("selected", null);
			}
		}
	}

	/**
	 * 「儲存」途徑。
	 *
	 * @param accordionId 途徑
	 * @param title 途徑名稱
	 * @param sort 排序
	 * @param parentNode 父層元素
	 * @param request 請求
	 * @return 成功與否。
	 */
	public boolean saveOne(Mapping mapping, Short accordionId, Short sort, String title, String uri, Short methodId, String pattern, String description, Element parentNode, HttpServletRequest request) {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());
		Short id = mapping.getId();
		boolean isNew = id == null;

		String errorMessage = null;

		//手風琴
		Accordion accordion = null;
		if (accordionId == null) {
			errorMessage = "「手風琴」為必選！";
		} else {
			accordion = accordionRepository.findOne(accordionId);
		}
		Element accordionElement = Utils.createElement("accordion", formElement);
		for (Accordion a : accordionRepository.findAllOrderBySort()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", accordionElement, a.getName(), "value", a.getId().toString());
			if (a.equals(accordion)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//排序
		if (sort != null && accordion != null && ((isNew && mappingRepository.countByAccordionAndSort(accordion, sort) > 0) || mappingRepository.countByAccordionAndSortAndIdNot(accordion, sort, id) > 0)) {
			errorMessage = "「排序」不能重複！";
		}

		//標題
		title = title.trim();
		if (title.length() == 0) {
			title = null;
		}
		Utils.createElementWithTextContent("title", formElement, title == null ? "" : title);

		//路徑
		uri = uri.trim();
		if (uri.length() == 0) {
			uri = null;
		}
		Utils.createElementWithTextContent("sort", formElement, uri == null ? "" : uri);

		//方式
		Method method = null;
		if (methodId == null) {
			errorMessage = "「方式」為必選！";
		} else {
			method = methodRepository.findOne(methodId);
		}
		Element methodElement = Utils.createElement("method", formElement);
		for (Method m : methodRepository.findAll()) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", methodElement, m.getName(), "value", m.getId().toString());
			if (m.equals(method)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//規則
		pattern = pattern.trim();
		if (pattern.length() == 0) {
			pattern = null;
			errorMessage = "「規則」為必填！";
		}
		Utils.createElementWithTextContent("pattern", formElement, pattern == null ? "" : pattern);

		//描述
		description = description.trim();
		if (description.length() == 0) {
			description = null;
			errorMessage = "「描述」為必填！";
		}
		Utils.createElementWithTextContent("description", formElement, description == null ? "" : description);

		if (errorMessage == null) {
			mapping.setAccordion(accordion);
			mapping.setSort(sort);
			mapping.setTitle(title);
			mapping.setUri(uri);
			mapping.setMethod(method);
			mapping.setPattern(pattern);
			mapping.setDescription(description);
			mappingRepository.saveAndFlush(mapping);
			return true;
		}

		Utils.createElementWithTextContent("errorMessage", formElement, errorMessage);
		return false;
	}
}
