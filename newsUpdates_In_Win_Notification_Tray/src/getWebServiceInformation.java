import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;



public class getWebServiceInformation {
	public static void main(String[] args) {

		try {
			URL url = new URL("https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=top&apiKey=31cfe0eeb8dc4b2eb8788f25863b38eb");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			
			SystemTray tray = SystemTray.getSystemTray();

			//If the icon is a file
			Image image = Toolkit.getDefaultToolkit().createImage("VersionDetails.png");
			//Alternative (if the icon is on the classpath):
			//Image image = Toolkit.getToolkit().createImage(getClass().getResource("VersionDetails.png"));
			TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
			//Let the system resizes the image if needed
			trayIcon.setImageAutoSize(true);
			//Set tooltip text for the tray icon
			trayIcon.setToolTip("System tray icon demo");
			tray.add(trayIcon);
			
			String output;
			System.out.println("Output from Server ..... \n");
			if (SystemTray.isSupported()) {
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					JSONObject jsonObject = new JSONObject(output);

					String status = (String) jsonObject.get("status");
					String source = (String) jsonObject.get("source");
					JSONArray articles = (JSONArray) jsonObject.get("articles");

					System.out.println("Name: " + status);
					System.out.println("source: " + source);
					System.out.println("\nCompany List:"+articles);

					for (int i = 0, size = articles.length(); i < size; i++){
						JSONObject objectInArray = articles.getJSONObject(i);
						String title = (String) objectInArray.get("title");
						String description = (String) objectInArray.get("description");
						String urlInfo = (String) objectInArray.get("url");
						String publishedAt = (String) objectInArray.get("publishedAt");
						
						trayIcon.displayMessage(title, description , MessageType.INFO);
						Thread.sleep(30000);
					}
				}
			}
			conn.disconnect();
	
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		} catch (IOException e) {
			System.out.println("IOException");
		}catch (Exception e) {
			System.out.println("Exception : "+e);
		}

	}
}
