 package motherson.crm.v3.customexception;

public class CustomException extends RuntimeException{
	
	private String message;
	private String status;
	
	public CustomException(String message,String status) {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	public CustomException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public CustomException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public CustomException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
