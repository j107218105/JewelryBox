package tw.jewelrybox.service;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.w3c.dom.Element;
import tw.jewelrybox.Utils;
import tw.jewelrybox.entity.*;
import tw.jewelrybox.repository.*;

/**
 * 「活動」服務層
 *
 * @author	P-C Lin (d.accordion.d 高科技黑手)
 */
@org.springframework.stereotype.Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private OccupationRepository occupationRepository;

	/**
	 * 「載入」活動。
	 *
	 * @param activity 活動
	 * @param parentNode 父層元素
	 * @param request 請求
	 */
	public void loadOne(Activity activity, Element parentNode, HttpServletRequest request) throws ParserConfigurationException {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());

		if (activity != null) {
			Utils.createElementWithTextContent("name", formElement, activity.getName());//活動名稱

			Utils.createElementWithTextContent("headcount", formElement, Integer.toString(activity.getHeadcount()));//人數

			Utils.createElementWithTextContent("score", formElement, Short.toString(activity.getScore()));//價值點數

			Utils.createElementWithTextContent("since", formElement, Utils.formatTimestamp(activity.getSince()));//開始時戳

			Utils.createElementWithTextContent("until", formElement, Utils.formatTimestamp(activity.getUntil()));//結束時戳

			//年齡需求
			Short age = activity.getAge();
			if (age != null) {
				Utils.createElementWithTextContent("age", formElement, age.toString());
			}

			//性別需求
			Boolean gender = activity.getGender();
			if (gender != null) {
				Utils.createElementWithTextContent("gender", formElement, gender ? "true" : "false");
			}
		}

		//職業需求
		Occupation occupation = activity == null ? null : activity.getOccupation();
		Element occupationElement = Utils.createElement("occupation", formElement);
		for (Occupation o : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, o.getName(), "value", o.getId().toString());
			if (o.equals(occupation)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//行政區需求
		District district = activity == null ? null : activity.getDistrict();
		Element districtElement = Utils.createElement("district", formElement);
		for (District d : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, d.getName(), "value", d.getId().toString());
			if (d.equals(district)) {
				optionElement.setAttribute("selected", null);
			}
		}
	}

	/**
	 * 「儲存」活動。
	 *
	 * @param activity 活動
	 * @param name 活動名稱
	 * @param headcount 人數上限
	 * @param score 價值點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 * @param age 年齡需求主鍵
	 * @param gender 性別需求主鍵
	 * @param occupationId 職業需求主鍵
	 * @param districtId 行政區需求主鍵
	 * @param parentNode 父層元素
	 * @param request 請求
	 * @return 成功與否。
	 */
	public boolean saveOne(Activity activity, String name, Integer headcount, Short score, Date since, Date until, Short age, Boolean gender, Short occupationId, Short districtId, Element parentNode, HttpServletRequest request) {
		Element formElement = Utils.createElement("form", parentNode);
		formElement.setAttribute("action", request.getRequestURI());
		Integer id = activity.getId();
		boolean isNew = id == null;

		String errorMessage = null;

		//活動名稱
		try {
			if (name != null) {
				name = name.trim();
			}
			if (name == null || name.length() == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			name = null;
			errorMessage = "「活動名稱」為必填！";
		}
		Utils.createElementWithTextContent("name", formElement, name == null ? "" : name);

		//人數
		if (headcount == null) {
			errorMessage = "「人數」為必填！";
		} else if (headcount <= 0) {
			errorMessage = "「人數」必須大於零！";
		}
		Utils.createElementWithTextContent("headcount", formElement, headcount == null ? "" : headcount.toString());

		//價值點數
		if (score == null) {
			errorMessage = "「價值點數」為必填！";
		} else if (score <= 0) {
			errorMessage = "「價值點數」必須大於零！";
		}
		Utils.createElementWithTextContent("score", formElement, score == null ? "" : score.toString());

		//開始時戳
		if (since == null) {
			errorMessage = "「開始時戳」為必填！";
		}
		Utils.createElementWithTextContent("since", formElement, since == null ? "" : Utils.formatTimestamp(since));

		//結束時戳
		if (until == null) {
			errorMessage = "「結束時戳」為必填！";
		}
		Utils.createElementWithTextContent("until", formElement, until == null ? "" : Utils.formatTimestamp(until));

		if ((since != null && until != null) && (since.after(until) || until.before(since) || since.equals(until))) {
			errorMessage = "「開始時戳」不得遲於「結束時戳」！";
		}

		//年齡需求
		Utils.createElementWithTextContent("age", formElement, age == null ? "" : age.toString());

		//性別需求
		if (gender != null) {
			Utils.createElementWithTextContent("gender", formElement, gender ? "true" : "false");
		}

		//職業需求
		Occupation occupation = occupationId == null ? null : occupationRepository.findOne(occupationId);
		Element occupationElement = Utils.createElement("occupation", formElement);
		for (Occupation o : occupationRepository.findAll(new Sort("name"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", occupationElement, o.getName(), "value", o.getId().toString());
			if (o.equals(occupation)) {
				optionElement.setAttribute("selected", null);
			}
		}

		//行政區需求
		District district = districtId == null ? null : districtRepository.findOne(districtId);
		Element districtElement = Utils.createElement("district", formElement);
		for (District d : districtRepository.findAll(new Sort("id"))) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", districtElement, d.getName(), "value", d.getId().toString());
			if (d.equals(district)) {
				optionElement.setAttribute("selected", null);
			}
		}

		if (errorMessage == null) {
			activity.setName(name);
			activity.setHeadcount(headcount);
			activity.setScore(score);
			activity.setSince(since);
			activity.setUntil(until);
			activity.setAge(age);
			activity.setGender(gender);
			activity.setOccupation(occupation);
			activity.setDistrict(district);
			activityRepository.saveAndFlush(activity);
			return true;
		}

		Utils.createElementWithTextContent("errorMessage", formElement, errorMessage);
		return false;
	}
}
