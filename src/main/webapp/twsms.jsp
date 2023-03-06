<%@page contentType="text/html; charset=Big5" import = "java.io.*,java.net.*"%><%
// 變數宣告
	String msg;
	String thisLine;
	URL url;
	URLConnection urlConnection;

// 設定參數
	String username = "";//帳號
	String password = "";//密碼
	String mobile = "09xxxxxxxx";//電話
	String message = "簡訊測試";//簡訊內容

	msg = "http://api.twsms.com/smsSend.php?username=" + username + "&password=" + password + "&mobile=" + mobile + "&message=" + URLEncoder.encode(message);

	url = new URL(msg);
	try {
		urlConnection = url.openConnection();
		BufferedReader theHTML = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		thisLine = theHTML.readLine();
%>
回傳訊息: <%= thisLine%>
<%
} catch (Exception e) {
%>
無法連結網站 <%=e.getMessage()%>
<%
	}
%>
