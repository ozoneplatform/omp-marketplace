/**
 * 
 */
package ozone.marketplace.util.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sean T Booker
 *
 */
public abstract class AbstractEvent {
	protected DateFormat eventDateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z", Locale.US)
	private Date eventDate = new Date() //Today's Date
	
	protected String getEventCreatedDate(){
		return this.eventDateFormatter.format(this.eventDate)
	}
}
