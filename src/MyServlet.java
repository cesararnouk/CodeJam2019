
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MyServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("index.html");
		view.forward(request, response);
		System.out.println(" * Get recieved...");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" * Post recieved...");	
		String idreview = java.net.URLDecoder.decode(getBody(request), StandardCharsets.UTF_8.name());

		int id = getID(idreview);
		String review = getReview(idreview);

		System.out.println("   * Professor ID: " + id);
		System.out.println("   * Review: " + review);

		// Instantiates a client
		try (LanguageServiceClient language = LanguageServiceClient.create()) {
			// The text to analyze
			Document doc = Document.newBuilder()
					.setContent(review).setType(Type.PLAIN_TEXT).build();

			// Detects the sentiment of the text
			Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
			
			System.out.printf("   * Sentiment: %s%n", sentiment.getScore());
		}
	}

	private int getID(String idreview) throws UnsupportedEncodingException, IOException {
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(idreview.substring(3,6));
		List<String> tokens = new LinkedList<String>();
		int id = 0;

		while(m.find())
		{
			String token = m.group( 1 ); //group 0 is always the entire match
			tokens.add(token);
			id = Integer.parseInt(token);
		}

		return id;
	}

	private String getReview(String idreview) throws UnsupportedEncodingException, IOException {
		String[] newStrings = idreview.split("=", 3);
		return newStrings[2];
	}

	private String getBody(HttpServletRequest request) throws IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;

	}

}
