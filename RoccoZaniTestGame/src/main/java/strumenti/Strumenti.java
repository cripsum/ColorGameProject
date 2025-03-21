package strumenti;

import java.util.UUID;

public abstract class Strumenti {
	public static boolean isEmailValid(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(regex);
	}	
	
	public static String generaId() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}
	
}
