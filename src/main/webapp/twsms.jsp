<%@page contentType="text/html; charset=Big5" import = "java.io.*,java.net.*"%><%
// �ܼƫŧi
	String msg;
	String thisLine;
	URL url;
	URLConnection urlConnection;

// �]�w�Ѽ�
	String username = "";//�b��
	String password = "";//�K�X
	String mobile = "09xxxxxxxx";//�q��
	String message = "²�T����";//²�T���e

	msg = "http://api.twsms.com/smsSend.php?username=" + username + "&password=" + password + "&mobile=" + mobile + "&message=" + URLEncoder.encode(message);

	url = new URL(msg);
	try {
		urlConnection = url.openConnection();
		BufferedReader theHTML = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		thisLine = theHTML.readLine();
%>
�^�ǰT��: <%= thisLine%>
<%
} catch (Exception e) {
%>
�L�k�s������ <%=e.getMessage()%>
<%
	}
%>
