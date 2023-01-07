import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtract {

	public static void main(String[] args) throws IOException {
		// smallbasic id�� ����� text ����
		File file = new File("C:\\Users\\Hwangsooyeon\\Desktop\\smallbasic-program-list.txt");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		InputStream is = null;
		
		// �� text ������ smallbasic �ڵ常�� ����
		Pattern pattern = Pattern.compile("<div id=\"codeListing\">(.*?)</div>", Pattern.DOTALL);
		
		String str;
		while((str = bufferedReader.readLine()) != null) {
			try {
				URL url = new URL("http://smallbasic.com/program/?" + str);
				
				URLConnection urlConnection = url.openConnection();
				is = urlConnection.getInputStream();
				byte[] buffer = new byte[1024];
				StringBuilder result = new StringBuilder();
				int readBytes;
				
				// url �����͸� ���ڿ��� �����´�
				while((readBytes = is.read(buffer)) != -1) {
					String part = new String(buffer, 0, readBytes);
					result.append(part);
				}
				
				String extract = "";
				// <div id=\"codeListing\"> �ش� �±� ���� �ڵ常 ����
				Matcher matcher = pattern.matcher(result);
				
				while(matcher.find()) {
					extract = matcher.group(1).trim();
				}
				
				// �ڵ忡 �����ִ� <br /> �±� ����
				extract = extract.replaceAll("<br />", "\n");
				
				// ���� ����
				String newFile = "C:\\Users\\Hwangsooyeon\\Desktop\\smallbasic-list\\"+str+".sb";
				FileWriter fileWriter = new FileWriter(newFile);
				fileWriter.write(extract);
				
				fileWriter.close();
				
				// ���� �ٿ�ε� Ȯ�ο� ���
				System.out.println(str + " file downloaded successfully");
				
			} catch(MalformedURLException e) {
				System.err.println(e);
			}
		}
	}

}
