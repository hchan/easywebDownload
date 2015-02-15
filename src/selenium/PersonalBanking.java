package selenium;

import java.io.File;

public class PersonalBanking extends Banking {
	public static void main (String... args) throws Exception {
		try {
			PersonalBanking banking = new PersonalBanking();
			banking.appName = PersonalBanking.class.getName();
			banking.initByProperties(System.getProperty("user.home") + File.separator
					+ ".easyweb" + File.separator
					+ "personal.properties");
			banking.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}