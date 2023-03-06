package tw.jewelrybox.service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.Accordion;
import tw.jewelrybox.repository.AccordionRepository;

/**
 * 「手風琴」服務層
 *
 * @author	P-C Lin (a.accordion.a 高科技黑手)
 */
@org.springframework.stereotype.Service
public class AccordionService {

	@Autowired
	private AccordionRepository accordionRepository;

	/**
	 * 「載入」手風琴。
	 *
	 * @param accordion 手風琴
	 * @param parentNode 父層元素
	 * @param request 請求
	 * @return 新增或修改的頁面
	 */
	public void loadOne(Accordion accordion, Element parentNode, HttpServletRequest request) throws ParserConfigurationException {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());

		if (accordion != null) {
			String name = accordion.getName();//手風琴名稱
			if (name != null) {
				Utils.createElementWithTextContent("name", formElement, name);
			}

			//排序
			Utils.createElementWithTextContent("sort", formElement, Short.toString(accordion.getSort()));
		}
	}

	/**
	 * 「儲存」手風琴。
	 *
	 * @param accordion 手風琴
	 * @param name 手風琴名稱
	 * @param sort 排序
	 * @param parentNode 父層元素
	 * @param request 請求
	 * @return 成功與否。
	 */
	public boolean saveOne(Accordion accordion, String name, Short sort, Element parentNode, HttpServletRequest request) {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());
		Short id = accordion.getId();
		boolean isNew = id == null;

		String errorMessage = null;

		//手風琴名稱
		try {
			if (name != null) {
				name = name.trim();
			}
			if (name == null || name.length() == 0) {
				throw new NullPointerException();
			}

			if ((isNew && accordionRepository.countByName(name) > 0) || (!isNew && accordionRepository.countByNameAndIdNot(name, id) > 0)) {
				errorMessage = "「手風琴名稱」不能重複！";
			}
		} catch (NullPointerException nullPointerException) {
			name = null;
			errorMessage = "「手風琴名稱」為必填！";
		}
		Utils.createElementWithTextContent("name", formElement, name == null ? "" : name);

		//排序
		if (sort == null) {
			errorMessage = "「排序」為必填！";
		} else if ((isNew && accordionRepository.countBySort(sort) > 0) || (!isNew && accordionRepository.countBySortAndIdNot(sort, id) > 0)) {
			errorMessage = "「排序」不能重複！";
		}
		Utils.createElementWithTextContent("sort", formElement, sort == null ? "" : sort.toString());

		if (errorMessage == null) {
			accordion.setName(name);
			accordion.setSort(sort);
			accordionRepository.saveAndFlush(accordion);
			return true;
		}

		Utils.createElementWithTextContent("errorMessage", formElement, errorMessage);
		return false;
	}
}
