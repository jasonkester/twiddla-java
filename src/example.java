import TwiddlaAPI.TwiddlaHelper;
import TwiddlaAPI.TwiddlaHelper.ResponseFormat;


public class example {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			int sessionID = TwiddlaHelper.CreateMeeting("My Awesome Meeting", "hunter2", "http://www.google.com");
			System.out.println(sessionID);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	
		try
		{
			int userID = TwiddlaHelper.CreateUser("newguy", "hunter2", "Roger Rogerson", "roger@example.org");
			System.out.println(userID);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	
		try
		{
			String csv = TwiddlaHelper.ListActive(ResponseFormat.CSV);
			System.out.println(csv);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	
	}

}
