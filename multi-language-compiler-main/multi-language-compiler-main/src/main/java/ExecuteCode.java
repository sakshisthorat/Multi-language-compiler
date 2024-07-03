

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.json.JSONException;
//import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
//import org.json.JSONObject;

//import Compiler.CallAPI;
/**
 * Servlet implementation class ExecuteCode
 */
@WebServlet("/ExecuteCode")
public class ExecuteCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String languageid;

    /**
     * Default constructor. 
     */
    public ExecuteCode() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html;charset=UTF-8");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		try {	
		String code = request.getParameter("code");
		String input = request.getParameter("input");
		String languages = request.getParameter("Languages");
		System.out.println(languages);
		assignLanguageID(languages);
			
			String output = acceptCodeForCompilation(code,input);
			PrintWriter out = response.getWriter();
			out.println(output);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
			}
	public void assignLanguageID(String language)
	{
		if(language.equals("Javascript"))
		{
			System.out.println("in js");
			this.languageid = "&language_id=63";
		}
		else if(language.equals("C++"))
		{
			this.languageid = "&language_id=52";
		}
		else if(language.equals("C"))
		{
			this.languageid = "&language_id=50";
		}
	}
	public String acceptCodeForCompilation(String code, String input) throws IOException, InterruptedException
	{
		 //code = "class Main {public static void main(String[] args){int a = 10;System.out.println(\"hello\");}}";
		System.out.println(input);
        URI uriObject = URI.create("https://ce.judge0.com/submissions?source_code="+ URLEncoder.encode(code, StandardCharsets.UTF_8)+this.languageid+"&stdin="+input);
        
         HttpRequest request1 = HttpRequest.newBuilder()
		.uri(uriObject)
		.header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
		.header("X-RapidAPI-Key", "0291be081fmsh384ed61bcc913a1p100c42jsn6e59ab19bfe6")
		.method("POST", HttpRequest.BodyPublishers.noBody())
		.build();
              HttpResponse<String> responseToken = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
              //JSONObject tokenObject = new JSONObject(responseToken.body());
              String token = responseToken.body().substring(10,responseToken.body().length() - 2);
              System.out.println(responseToken.body() + "\n" + token );
              //String token = tokenObject.getString("token");
              HttpRequest outputRequest = HttpRequest.newBuilder().uri(URI.create("https://ce.judge0.com/submissions/"+token+"?base64_encoded=false&fields=stdout,stderr,status_id,language_id")).method("GET",HttpRequest.BodyPublishers.noBody()).build();
              HttpResponse<String> outputResponse = HttpClient.newHttpClient().send(outputRequest, HttpResponse.BodyHandlers.ofString());
              System.out.println(outputResponse.body());
              return outputResponse.body().toString();
	}

}
