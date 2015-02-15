package selenium;

import java.io.File;

public class BusinessBanking extends Banking {
	public static void main (String... args) throws Exception {
		try {
			BusinessBanking banking = new BusinessBanking();
			banking.appName = BusinessBanking.class.getName();
			banking.initByProperties(System.getProperty("user.home") + File.separator
					+ ".easyweb" + File.separator
					+ "business.properties");

			banking.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
