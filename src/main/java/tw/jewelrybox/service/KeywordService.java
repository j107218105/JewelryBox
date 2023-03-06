package tw.jewelrybox.service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import tw.quanying.groupimage.Utils;
//import tw.quanying.groupimage.entity.Keyword;
//import tw.quanying.groupimage.repository.*;

/**
 * 「關鍵字」服務層
 *
 * @author	P-C Lin (a.keyword.a 高科技黑手)
 */
@org.springframework.stereotype.Service
public class KeywordService {
//
//	@Autowired
//	private KeywordRepository keywordRepository;
//
//	@Autowired
//	private Services services;
//
//	/**
//	 * 「載入」關鍵字。
//	 *
//	 * @param keyword 關鍵字
//	 * @param request 不解釋
//	 * @param errorMessage 錯誤訊息
//	 * @return DOM 文件
//	 */
//	public Document loadOne(Keyword keyword, HttpServletRequest request, String errorMessage) throws ParserConfigurationException {
//		Short id = keyword.getId();
//		String word = keyword.getWord();
//		Short ordinal = keyword.getOrdinal();
//
//		/*
//		 頭
//		 */
//		Document document = Utils.newDocument();
//		Element documentElement = Utils.createElement("document", document);
//		documentElement.setAttribute("contextPath", request.getContextPath());
//		documentElement.setAttribute("requestURI", request.getRequestURI());
//
//		/*
//		 連結
//		 */
//		services.createNavigatorElement(document, request);
//
//		/*
//		 身體
//		 */
//		Element articleElement = Utils.createElement("article", documentElement);
//		if (errorMessage != null) {
//			Utils.createElementWithTextContent("errorMessage", articleElement, errorMessage);
//		}
//		if (id != null) {//主鍵
//			Utils.createElementWithTextContent("id", articleElement, id.toString());
//		}
//		if (word != null) {//字詞
//			Utils.createElementWithTextContent("word", articleElement, word);
//		}
//		if (ordinal != null) {//排序
//			Utils.createElementWithTextContent("ordinal", articleElement, ordinal.toString());
//		}
//
//		return document;
//	}
//
//	/**
//	 * 「儲存」關鍵字。
//	 *
//	 * @param keyword 關鍵字
//	 * @param word 字詞
//	 * @param isOrdinal (啟|停)用
//	 */
//	public String saveOne(Keyword keyword, String word, boolean enabled) {
//		String errorMessage = null;
//
//		//字詞
//		try {
//			if (word.trim().isEmpty()) {
//				throw new NullPointerException();
//			}
//
//			Short id = keyword.getId();
//			if (id == null) {
//				if (keywordRepository.countByWord(word) > 0) {
//					errorMessage = "「字詞」已存在！";
//				}
//			} else {
//				if (keywordRepository.countByWordAndIdNot(word, id) > 0) {
//					errorMessage = "「字詞」已存在！";
//				}
//			}
//		} catch (NullPointerException nullPointerException) {
//			word = null;
//			errorMessage = "「字詞」為必填！";
//		}
//		Short ordinal = null;//排序
//		if (enabled) {
//			ordinal = (short) (keywordRepository.countByOrdinalIsNotNul() + 1);
//		}
//
//		/*
//		 寫入資料庫
//		 */
//		try {
//			if (errorMessage == null) {
//				keyword.setWord(word);
//				keyword.setOrdinal(ordinal);
//				keywordRepository.saveAndFlush(keyword);
//			}
//			reOrdinal();
//		} catch (Exception exception) {
//			return exception.getLocalizedMessage();
//		}
//
//		return errorMessage;
//	}
//
//	/**
//	 * 重新排序。
//	 */
//	public void reOrdinal() {
//		short ordinal = 1;
//		for (Keyword keyword : keywordRepository.findByOrdinalIsNotNullOrderByOrdinal()) {
//			keyword.setOrdinal(ordinal);
//			keywordRepository.saveAndFlush(keyword);
//			ordinal++;
//		}
//	}
}
