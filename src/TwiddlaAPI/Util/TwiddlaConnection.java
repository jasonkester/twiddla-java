package TwiddlaAPI.Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class TwiddlaConnection extends HttpURLConnection {

	protected TwiddlaConnection(URL u) {
		super(u);
	}

}
