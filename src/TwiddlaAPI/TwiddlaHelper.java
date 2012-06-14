package TwiddlaAPI;

import TwiddlaAPI.Util.APICaller;



public class TwiddlaHelper
{

	public final static String TwiddlaUsername = "YourTwiddlaUsername";
	public final static String TwiddlaPassword = "YourTwiddlaPassword";

	/// <summary>
	/// A list of valid response formats, for calls that return data
	/// </summary>
	public enum ResponseFormat
	{
		CSV,
		XML
	}

	/// <summary>
	/// For future expansion:
	/// </summary>
	/// <param name="request"></param>
	/// <returns></returns>
	public static String GetTwiddlaHost()
	{
		return "www.twiddla.com";
	}

	/// <summary>
	/// Create a new Twiddla Meeting, returning the SessionID if successful
	/// </summary>
	/// <param name="meetingpassword"></param>
	/// <param name="meetingtitle"></param>
	/// <param name="url">For custom URLs.  Leave blank or null for a standard, numbered, meetingID</param>
	/// <returns></returns>
	/// 
	public static int CreateMeeting(String meetingtitle, String meetingpassword, String url) throws Exception
	{
		String endpoint = String.format("http://%s/new.aspx", GetTwiddlaHost());

		APICaller caller = new APICaller(endpoint);
		caller.Add("username", TwiddlaUsername);
		caller.Add("password", TwiddlaPassword);
		caller.Add("meetingtitle", meetingtitle);
		caller.Add("meetingpassword", meetingpassword);
		if (url != null && url != "")
		{
			caller.Add("url", url);
		}

		if (caller.Call())
		{
			return caller.GetIntValue();
		}

		throw caller.GetLastException();
	}

	/// <summary>
	/// Create a new Twiddla User, returning the UserID if successful
	/// </summary>
	/// <param name="newusername"></param>
	/// <param name="newpassword"></param>
	/// <param name="displayname"></param>
	/// <param name="email"></param>
	/// <returns></returns>
	public static int CreateUser(String newusername, String newpassword, String displayname, String email) throws Exception
	{
		String endpoint = String.format("http://%s/API/CreateUser.aspx", GetTwiddlaHost());

		APICaller caller = new APICaller(endpoint);
		caller.Add("username", TwiddlaUsername);
		caller.Add("password", TwiddlaPassword);
		caller.Add("newusername", newusername);
		caller.Add("newpassword", newpassword);
		caller.Add("displayname", displayname);
		caller.Add("email", email);

		if (caller.Call())
		{
			return caller.GetIntValue();
		}

		throw caller.GetLastException();
	}

	/// <summary>
	/// List active meetings for this account
	/// </summary>
	/// <param name="format"></param>
	/// <returns></returns>
	public static String ListActive(ResponseFormat format) throws Exception
	{
		String endpoint = String.format("http://%s/API/ListActive.aspx", GetTwiddlaHost());

		APICaller caller = new APICaller(endpoint);
		caller.Add("username", TwiddlaUsername);
		caller.Add("password", TwiddlaPassword);
		caller.Add("format", format.toString().toLowerCase());

		if (caller.Call())
		{
			return caller.GetHtml();
		}

		throw caller.GetLastException();
	}

	/// <summary>
	/// List all snapshots for this account
	/// </summary>
	/// <param name="format"></param>
	/// <returns></returns>
	public static String ListSnapshots(ResponseFormat format) throws Exception
	{
		return ListSnapshots(format, 0);
	}

	/// <summary>
	/// List all snapshots for the supplied sessionID
	/// </summary>
	/// <param name="format"></param>
	/// <param name="sessionID"></param>
	/// <returns></returns>
	public static String ListSnapshots(ResponseFormat format, int sessionID) throws Exception
	{
		String endpoint = String.format("http://%s/API/ListSnapshots.aspx", GetTwiddlaHost());

		APICaller caller = new APICaller(endpoint);
		caller.Add("username", TwiddlaUsername);
		caller.Add("password", TwiddlaPassword);
		caller.Add("format", format.toString().toLowerCase());
		if (sessionID > 0)
		{
			caller.Add("sessionid", sessionID);
		}

		if (caller.Call())
		{
			return caller.GetHtml();
		}

		throw caller.GetLastException();
	}
}


