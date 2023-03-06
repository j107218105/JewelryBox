package tw.jewelrybox;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * 輔助程式
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public class Utils {

	public static GregorianCalendar DEFAULT_CALENDAR = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);

	/**
	 * createElement() 之後 appendChild()。
	 */
	public static Element createElement(String tagName, Node node) {
		Document document = node.getOwnerDocument();
		if (document == null) {
			document = (Document) node;
		}
		return (Element) node.appendChild(document.createElement(tagName));
	}

	/**
	 * createElement() 之後 appendChild() 最後 setAttribute()。
	 */
	public static Element createElementWithAttribute(String tagName, Node node, String name, String value) {
		Element newChild = createElement(tagName, node);
		newChild.setAttribute(name, value);
		return newChild;
	}

	/**
	 * createElement() 之後 appendChild() 最後 createCDATASection()。
	 */
	public static Element createElementWithCDATASection(String tagName, Node node, String data) {
		Element newChild = createElement(tagName, node);
		newChild.appendChild(newChild.getOwnerDocument().createCDATASection(data));
		return newChild;
	}

	/**
	 * createElement() 之後 appendChild() 然後 createCDATASection() 最後
	 * setAttribute()。
	 */
	public static Element createElementWithCDATASectionAndAttribute(String tagName, Node node, String data, String name, String value) {
		Element newChild = createElementWithCDATASection(tagName, node, data);
		newChild.setAttribute(name, value);
		return newChild;
	}

	/**
	 * createElement() 之後 appendChild() 最後 setTextContent()。
	 */
	public static Element createElementWithTextContent(String tagName, Node node, String textContent) {
		Element newChild = createElement(tagName, node);
		newChild.setTextContent(textContent);
		return newChild;
	}

	/**
	 * createElement() 之後 appendChild() 然後 setTextContent() 最後
	 * setAttribute()。
	 */
	public static Element createElementWithTextContentAndAttribute(String tagName, Node node, String textContent, String name, String value) {
		Element newChild = createElementWithTextContent(tagName, node, textContent);
		newChild.setAttribute(name, value);
		return newChild;
	}

	/**
	 * 建立 Document。
	 */
	public static Document newDocument() throws ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}

	/**
	 * 建立 Document。
	 *
	 * @param awareness true if the parser produced will provide support for
	 * XML namespaces; false otherwise. By default the value of this is set
	 * to <b>false</b>.
	 * @return A new instance of a DOM Document object.
	 * @throws ParserConfigurationException if a DocumentBuilder cannot be
	 * created which satisfies the configuration requested.
	 */
	public static Document newDocument(boolean awareness) throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(awareness);
		return documentBuilderFactory.newDocumentBuilder().newDocument();
	}

	/**
	 * 建立 Document。
	 */
	public static Document parseDocument(InputStream inputStream) throws IOException, ParserConfigurationException, SAXException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
	}

	/**
	 * 解析 InputStream 為 Document。
	 *
	 * @param inputStream InputStream containing the content to be parsed.
	 * @param awareness true if the parser produced will provide support for
	 * XML namespaces; false otherwise. By default the value of this is set
	 * to <b>false</b>.
	 * @return Document result of parsing the InputStream
	 * @throws ParserConfigurationException if a DocumentBuilder cannot be
	 * created which satisfies the configuration requested.
	 * @throws IOException If any IO errors occur.
	 * @throws SAXException If any parse errors occur.
	 */
	public static Document parseDocument(InputStream inputStream, boolean awareness) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(awareness);
		return documentBuilderFactory.newDocumentBuilder().parse(inputStream);
	}

	/**
	 * 轉化為 XML。
	 */
	public static void transform(Document document, HttpServletResponse response) throws Exception {
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(response.getOutputStream()));
	}

	/**
	 * 轉化為 XML。
	 */
	public static void transform(Document document, StreamResult streamResult) throws Exception {
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), streamResult);
	}

	/**
	 * @param string 解密前的「字串」
	 * @return 解密後的「字串」
	 */
	public static String decrypt(String string) {
		final String ALGORITHM_NAME = "Blowfish";
		final byte[] SECRET_KEY = {(byte) 187, (byte) 205, (byte) 191, (byte) 111, (byte) 176, (byte) 234, (byte) 187, (byte) 218, (byte) 166, (byte) 179, (byte) 173, (byte) 173, (byte) 164, (byte) 189, (byte) 165, (byte) 113};
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY, ALGORITHM_NAME));
			byte[] decryptedBytes = cipher.doFinal(Hex.decodeHex(string.toCharArray()));
			return org.apache.commons.codec.binary.StringUtils.newStringUtf8(decryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | DecoderException | IllegalBlockSizeException | BadPaddingException | NumberFormatException ignore) {
			return null;
		}
	}

	/**
	 * @param string 加密前的「字串」
	 * @return 加密後的「字串」
	 */
	public static String encrypt(String string) {
		final String ALGORITHM_NAME = "Blowfish";
		final byte[] SECRET_KEY = {(byte) 187, (byte) 205, (byte) 191, (byte) 111, (byte) 176, (byte) 234, (byte) 187, (byte) 218, (byte) 166, (byte) 179, (byte) 173, (byte) 173, (byte) 164, (byte) 189, (byte) 165, (byte) 113};
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY, ALGORITHM_NAME));
			byte[] encryptedBytes = cipher.doFinal(string.getBytes());
			return Hex.encodeHexString(encryptedBytes).toLowerCase().replace("a", "A").replace("e", "E");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ignore) {
			return null;
		}
	}

	public static String format(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String formatTimestamp(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
	}

	public static Date parse(String source) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(source);
	}
}
