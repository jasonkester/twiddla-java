package TwiddlaAPI.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.net.HttpURLConnection;

public class APICaller
{
	public enum StatusCodes
	{
		Success,
		Fail
	}

	public static final String CHARSET = "UTF-8";

	public String Endpoint;
	protected HashMap<String, String> _params = new HashMap<String, String>();

	private String _html;
	private Exception _lastException;
	private int _lastErrorCode;
	private StatusCodes _status;

	public String GetHtml()
	{
		return _html;
	}

	public Exception GetLastException()
	{
		return _lastException;
	}

	public int GetLastErrorCode()
	{
		return _lastErrorCode; 
	}

	public int GetIntValue()
	{
		try
		{
			return Integer.parseInt(_html);
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}

	public StatusCodes GetStatus()
	{
		return _status;
	}

	public APICaller()
	{
	}

	public APICaller(String endpoint)
	{
		Endpoint = endpoint;
	}

	public void Add(String key, String value)
	{
		_params.put(key, value);
	}

	public void Add(String key, int value)
	{
		_params.put(key, Integer.toString(value));
	}

	public void Add(String query)
	{
		
		String[] names = query.split("=");
		if (names.length > 1)
		{
			Add(names[0], names[1]);
		}
	}

	public void Clear()
	{
		_params.clear();
	}

	private static HttpURLConnection createConnection(String url) throws IOException {
		URL properURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) properURL.openConnection(); //enforce SSL URLs
		conn.setConnectTimeout(30000); // 30 seconds
		conn.setReadTimeout(80000); // 80 seconds
		conn.setUseCaches(false);
//		for(Map.Entry<String, String> header: getHeaders().entrySet()) {
//			conn.setRequestProperty(header.getKey(), header.getValue());
//		}
		return conn;
	}

	private static HttpURLConnection createPostConnection(String url, String query) throws IOException {
		HttpURLConnection conn = createConnection(url);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", CHARSET));
		OutputStream output = null;
		try {
		     output = conn.getOutputStream();
		     output.write(query.getBytes(CHARSET));
		} finally {
			if (output != null) output.close();
		}
		return conn;
	}
	
	private static String getResponseBody(InputStream responseStream) throws IOException {
		String rBody = new Scanner(responseStream, CHARSET).useDelimiter("\\A").next(); // \A is the beginning of the stream boundary
		responseStream.close();
		return rBody;
	}

	public Boolean Call()
	{
		try
		{
			HttpURLConnection conn = createPostConnection(Endpoint, BuildQueryString());

			//System.out.print(BuildQueryString());
			
			int rCode = conn.getResponseCode(); //triggers the request
			String rBody = null;
			if (rCode >= 200 && rCode < 300) {
				rBody = getResponseBody(conn.getInputStream());
			} else {
				rBody = getResponseBody(conn.getErrorStream());
			}
	
			_html = rBody;
			if (rBody.startsWith("-1"))
			{
				_lastException = new Exception("API call failed: " + _html);
				return false;
			}

			_lastErrorCode = 200;
			_status = StatusCodes.Success;


		}
		catch (Exception e)
		{
			ParseErrorCode(e.getMessage());
			_lastException = e;
			_status = StatusCodes.Fail;
			return false;
		}

		_lastException = null;
		return true;
	}

//	private HttpWebRequest PreparePostRequest(String url, String fields)
//	{
//		// set up header and POST request
//		HttpWebRequest webRequest = WebRequest.Create(url) as HttpWebRequest;
//		if (webRequest != null)
//		{
//			webRequest.Method = "POST";
//			webRequest.ContentType = "application/x-www-form-urlencoded";
//			webRequest.CookieContainer = new CookieContainer();
//			webRequest.UserAgent = @"Twiddla .NET Client Library v1.0; (+http://www.twiddla.com/api)";
//
//			StreamWriter requestWriter = new StreamWriter(webRequest.GetRequestStream());
//			requestWriter.Write(fields);
//			requestWriter.Close();
//		}
//		return webRequest;
//	}

	public String BuildQueryString() throws UnsupportedEncodingException
	{
		String qs = "";
		if (_params.size() > 0)
		{
			String separator = "";
			for (Map.Entry<String, String> param : _params.entrySet())
			{
				String value = (param.getValue() == null) ? "" : URLEncoder.encode(param.getValue(), CHARSET);

				if (param.getKey() != "")
				{
					qs += separator
						  + param.getKey()
						  + "="
						  + value;
				}
				else
				{
					qs += separator
						  + value;
				}
				separator = "&";
			}
		}
		
		return qs;
		//return URLEncoder.encode(qs, CHARSET);
		
	}

	private void ParseErrorCode(String message)
	{
		// TODO
		_lastErrorCode = 0;

		//The remote server returned an error: (404)
//		String[] tokens = message.Split("()".ToCharArray());
//		if (tokens.Length > 1)
//		{
//			int.TryParse(tokens[1], out _lastErrorCode);
//		}
	}
}
