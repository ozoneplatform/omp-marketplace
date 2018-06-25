/**
 * 
 */
package ozone.marketplace.util.event;



/**
 * @author Sean T Booker
 *
 */
public class LogInEvent extends AbstractEvent {
	
	/* User Credentials */
	private Long   user_userId
	private String user_username
	private String user_email
	private String user_profileCreatedDate
	private String user_profileLastLoginDate
	
	/* Session/Request Specifics */
	private String request_accessURI
	private String request_params
	
	/* Login Status */
	public static final String SUCCESS_LOGIN_STATUS = "LOGIN SUCCESS - ACCESS GRANTED"
	public static final String FAILURE_LOGIN_STATUS = "LOGIN FAILURE - ACCESS DENIED"
	private String  loginStatus
	
	/***
	 * Default Constructor : Inits values to UNKNOWN/NONE State
	 */
	LogInEvent(){
		user_userId = -1
		user_username = "UNKNOWN"
		user_email = "UNKNOWN"
		user_profileCreatedDate = "NONE"
		user_profileLastLoginDate = "NONE"
		request_accessURI = "NONE"
		request_params = "NONE"
		loginStatus = "NONE"
	}
	
	/**
	* @return the loginStatus
	*/
	protected final String getLoginStatus(){
		return loginStatus;
	}
	
	/**
	* @return the user_userId
	*/
	protected final Long getUser_userId(){
		return user_userId;
	}
	
	/**
	 * @return the user_username
	 */
	protected final String getUser_username() {
		return user_username;
	}
	/**
	 * @return the user_email
	 */
	protected final String getUser_email() {
		return user_email;
	}
	/**
	 * @return the user_profileCreatedDate
	 */
	protected final String getUser_profileCreatedDate() {
		return user_profileCreatedDate;
	}
	/**
	 * @return the user_profileLastLoginDate
	 */
	protected final String getUser_profileLastLoginDate() {
		return user_profileLastLoginDate;
	}
	/**
	 * @return the request_accessURI
	 */
	protected final String getRequest_accessURI() {
		return request_accessURI;
	}
	/**
	 * @return the request_params
	 */
	protected final String getRequest_params() {
		return request_params;
	}
	
	/**
	* @param user_userId the user_userId to set
	*/
	protected void setUser_userId(final Long user_userId){
		this.user_userId = user_userId;
	}
	
	/**
	 * @param user_username the user_username to set
	 */
	protected void setUser_username(final String user_username) {
		this.user_username = user_username;
	}
	/**
	 * @param user_email the user_email to set
	 */
	protected void setUser_email(final String user_email) {
		this.user_email = user_email;
	}
	/**
	 * @param user_profileCreatedDate the user_profileCreatedDate to set
	 */
	protected void setUser_profileCreatedDate(final Date user_profileCreatedDate) {
		this.user_profileCreatedDate = this.eventDateFormatter.format(user_profileCreatedDate);
	}
	/**
	 * @param user_profileLastLoginDate the user_profileLastLoginDate to set
	 */
	protected void setUser_profileLastLoginDate(final Date user_profileLastLoginDate) {
		this.user_profileLastLoginDate = this.eventDateFormatter.format(user_profileLastLoginDate);
	}
	/**
	 * @param request_accessURI the request_accessURI to set
	 */
	protected void setRequest_accessURI(final String request_accessURI) {
		this.request_accessURI = request_accessURI;
	}
	/**
	 * @param request_params the request_params to set
	 */
	protected void setRequest_params(final String request_params) {
		this.request_params = request_params;
	}
	
	
	/**
	* @param loginStatus the loginStatus to set
	*/
   protected void setLoginStatus(final String loginStatus) {
	   this.loginStatus = loginStatus;
   }
	
}
