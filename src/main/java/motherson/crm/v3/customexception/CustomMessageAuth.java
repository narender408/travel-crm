package motherson.crm.v3.customexception;

public class CustomMessageAuth {
	private String message;
	private boolean sucess;
	
	public CustomMessageAuth() {
		
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
}
