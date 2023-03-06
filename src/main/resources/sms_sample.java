
import java.io.*;
import java.net.*;

public class sms_sample {

	private HttpURLConnection httpURLConnection = null;

	public sms_sample() {
	}

	private void SendSMS() {
		try {
			// 設定變數
			StringBuilder MSGData = new StringBuilder();

			// 設定參數
			String username = "";	// 帳號
			String password = "";		// 密碼
			String mobile = "09xxxxxxxx";	// 電話
			String message = "簡訊測試";	// 簡訊內容	

			MSGData.append("username=").append(username);
			MSGData.append("&password=").append(password);
			MSGData.append("&mobile=").append(mobile);
			MSGData.append("&message=");
			MSGData.append(UrlEncode(message.getBytes("big5")));
			SendToGW(MSGData.toString());
		} catch (Exception e) {
			System.out.println("程式錯誤!");
		}
	}

	// 傳送至 TwSMS API server
	private boolean SendToGW(String post) {
		try {
			URL url = new URL("http://api.twsms.com/smsSend.php");
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");

			DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			dataOutputStream.writeBytes(post);
			dataOutputStream.flush();
			dataOutputStream.close();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String strResponse = "";
			String readLine;
			while ((readLine = bufferedReader.readLine()) != null) {
				strResponse += readLine;
			}
			bufferedReader.close();
			System.out.println("回傳碼:" + strResponse);
			return true;
		} catch (Exception e) {
			System.out.println("無法連接 TwSMS API Server!");
			return false;
		}
	}

	// UrlEncode Function
	private String UrlEncode(byte[] src) {
		byte[] ASCIIMAP = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
		int pivot = 0;
		byte[] data = new byte[src.length * 3];
		for (int i = 0; i < src.length; i++) {
			if (src[i] == 0) {
				data[pivot++] = 37;
				data[pivot++] = 48;
				data[pivot++] = 48;
			} else if (src[i] < 0) {
				data[pivot++] = 37;
				data[pivot++] = ASCIIMAP[(src[i] >> 4) & 0x0f];
				data[pivot++] = ASCIIMAP[src[i] & 0x0f];
			} else {
				char cc = (char) src[i];

				if (Character.isLetterOrDigit(cc)) {
					data[pivot++] = src[i];
				} else if (cc == ' ') {
					data[pivot++] = 43;
				} else if (cc == '.' || cc == '-' || cc == '*' || cc == '_') {
					data[pivot++] = src[i];
				} else {
					data[pivot++] = 37;
					data[pivot++] = ASCIIMAP[(src[i] >> 4) & 0x0f];
					data[pivot++] = ASCIIMAP[src[i] & 0x0f];
				}
			}
		}
		if (pivot > 0) {
			return new String(data, 0, pivot);
		}
		return "";
	}

	void Process(String[] args) {
		SendSMS();
	}

	public static void main(String[] args) {
		new sms_sample().Process(args);
	}
}
