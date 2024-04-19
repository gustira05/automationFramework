package stepDef;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.model.Result;

public class Hooks {

	static String comment;
	static String body;
	static int statusID;
	public static Scenario myScenario;
	
	@Before
	public void before(Scenario sc) {
		System.out.println("===========================");
		System.out.println("====== Test Started =======");
		System.out.println("===========================");
		System.out.println("Tag      : "+getTagFromScenario(sc));
		System.out.println("Scenario : "+sc.getName());
		myScenario = sc;
	}
	
//	@After
	public void after(Scenario sc) throws Exception {	
		//setPostBody(sc); //untuk testrail
		if (sc.isFailed()) {
			sc.embed(((TakesScreenshot) StepDefinition.driver).getScreenshotAs(OutputType.BYTES), "image/png");		
			//sendPost(sc); //update testrail
			System.out.println("===========================");
			System.out.println("=== SUCCESS POST FAILED ===");
			System.out.println("===========================");
			System.out.println("");
		} else {	
			//sendPost(sc);
			System.out.println("===========================");
			System.out.println("=== SUCCESS POST PASSED ===");
			System.out.println("===========================");
			System.out.println("");
		}
		if ( StepDefinition.driver != null) {
			StepDefinition.driver.quit();
		}
		else {
			System.out.println("=== THIS IS API TEST ===");
		}
	}

	public String getTagFromScenario(Scenario sc) {
		String tag = null;
		tag = sc.getSourceTagNames().toString().replaceAll("[(\\W)]", "").replaceAll("[(a-z , A-Z)]", "");
		return tag;
	}

	public String getStatusScenario(Scenario sc) {
		String status = null;
		status = sc.getStatus();
		return status;
	}


	public void setErrorComment(Scenario sc) {
		Field field = FieldUtils.getField(((ScenarioImpl) sc).getClass(), "stepResults", true);
		field.setAccessible(true);
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Result> results = (ArrayList<Result>) field.get(sc);
			for (Result result : results) {
				if (result.getError() != null)
					comment = result.getErrorMessage();
			}
		} catch (Exception e) {
			sc.write(e.toString());
		}
	}

	public String getErrorComment() {
		return comment;
	}

	public void setPostBody(Scenario sc) {
		if (sc.getStatus() == "passed" ) {
			setComment();
			statusID = 1;
			body = "{\"status_id\": "+statusID+",\"comment\": \"PASSED! Run with WebUI BDD\"}";
		}else {
			setErrorComment(sc);
			statusID = 5;
			body = "{\"status_id\": "+statusID+",\"comment\": \""+getErrorComment().replaceAll("\n", "\\\\\\n").replaceAll("\t"," ").replaceAll("\"", "\\\\\"")+"\"}";
		}
	}

	public String getBody() {
		return body;
	}

	public int getStatusID() {
		return statusID;
	}

	public void setComment() {
		comment = "PASSED! Run with WebUI BDD";
	}

	public String getComment() {
		return comment;
	}

	public void post(Scenario sc) throws IOException {
		URL url = new URL("https://jenius.testrail.net/index.php?/api/v2/add_result/"+getTagFromScenario(sc));
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Authorization", "Basic ZmhhZGVsLmZhZGlsbGFoQGJ0cG4uY29tOlNhdmVyMTIzNEFB");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(getBody().getBytes());
		os.flush();
		os.close();
		conn.disconnect();
		System.out.println("--------------------good");
	}

	public void sendPost(Scenario sc) throws IOException{
		URL url = new URL("https://jenius.testrail.net/index.php?/api/v2/add_result/"+getTagFromScenario(sc));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", "Basic ZmhhZGVsLmZhZGlsbGFoQGJ0cG4uY29tOlNhdmVyMTIzNEFB");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("status_id", getStatusID());
		jsonParam.put("comment", getComment());
		DataOutputStream os = new DataOutputStream(conn.getOutputStream());
		os.writeBytes(jsonParam.toString());
		os.flush();
		os.close();
		System.out.println("Status : "+ String.valueOf(conn.getResponseCode()));
		System.out.println("Message"+conn.getResponseMessage());
		conn.disconnect();
	}
	
}
