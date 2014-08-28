package ozone.utils

import java.text.DecimalFormat;

import java.text.SimpleDateFormat

import org.apache.log4j.Logger;

import java.lang.reflect.Method

import org.hibernate.proxy.HibernateProxy

import java.text.BreakIterator

class Utils {
	private static final Logger log = Logger.getLogger(Utils)
    public static final String ELLIPSES = '...'

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	public static String getMimeType(String fileUrl) throws java.io.IOException
	{
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(fileUrl);

		return type;
	}

	static String formatQryDate(def ozoneDate)
    {

	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd", Locale.US );
	String formattedDate = sdf.format( ozoneDate );

        return formattedDate
    }

	/***
	 * Attempts to set by "setter" method, if not, then directly.
	 * @param obj - Domain Class Instance
	 * @param prop - Field property
	 * @param propVal - Value to set on Field Property
	 */
	static void setDomainClassFieldByReflection(def obj, prop, propVal){
		def nonHibProxyObj = getNonHibernateProxyObject(obj)
		Class objClass = nonHibProxyObj?.getClass()
		Class propValClass = propVal?.getClass()
		Method propSetterMethod = null

		try{
			propSetterMethod = objClass.getMethod("set${prop?.substring(0,1)?.toUpperCase() + prop?.substring(1)}", propValClass)
		}catch(NoSuchMethodException nsme){
			log.debug "${nsme.getMessage()}"
		}
		if(propSetterMethod != null){
			Object argList = new Object[1]
			argList[0] = propVal
			propSetterMethod.invoke(nonHibProxyObj, argList)
		}else{
			nonHibProxyObj[prop] = propVal
		}
	}

	/***
	 *
	 * @param obj - Domain Class Instance
	 * @return
	 */
	static Object getNonHibernateProxyObject(def obj){
		if(obj == null){return null}
		Class objClass = obj?.getClass()
		if((objClass != null) && (objClass.getName().contains("_\$\$_javassist_"))){
			obj = ((HibernateProxy)obj).getHibernateLazyInitializer().getImplementation()
		}
		return obj
	}

    static String generateUUID() {
        return UUID.randomUUID();
    }


	static def rangeCheck(def rate){
		return rangeCheck(rate, null)
	}

	static def rangeCheck(def rate, def username){
		def range = 1..5
		if (!range.contains(Math.round(rate))){
			log.error "Invalid rate: [raw = ${rate} : rounded = ${Math.round(rate)}] by [${username}], ignoring"
			return 0;
		}
		return 1;
	}

	//Returns the new average rate from the removal of a single rate
	static def removeRatingFromAverageRate(def avgRate, def totalVotes, def rateToRemove){
		def newAvgRate = 0F
		if(totalVotes > 1){//A vote of 1, removing it's rate means the average is 0; a vote of 0 means no average
			newAvgRate = (((avgRate*totalVotes) - rateToRemove)/(totalVotes - 1))
		}
		return newAvgRate
	}

	//Returns the new average rate from the addition of a single rate
	static def addRatingToAverageRate(def avgRate, def totalVotes, def rateToAdd){
		def newAvgRate = (((avgRate * totalVotes) + rateToAdd)/(totalVotes + 1.0F))
		return newAvgRate
	}

    /**
     * Truncates the string at the word boundary and add ellipses to make sure the total length does not exceed
     * the imposed limit
     * @param str
     * @param limit
     * @return
     */
    static def ellipsizeString(String str, int limit) {
        if (str?.length() > limit) {
            int numberChars = limit - ELLIPSES.length()
            BreakIterator bi = BreakIterator.getWordInstance()
            bi.setText(str)
            int firstAfter = bi.preceding(numberChars)
            def result = str?.substring(0, firstAfter)
            if (result != str)  result += ELLIPSES
            result
        } else {
            return str
        }
    }

    //This function is like collect on maps, but returns a map instead
    //of a list
    //if the groovy version we were using was just
    //one number higher, this would be built in.
    static Map collectEntries(Map m, Closure closure) {
        Map retval = [:]

        m.collect(closure).grep {it != null}.each {retval.put(it)}

        retval
    }

    /**
     * If obj is a collection, invoke the closure on each element.
     * Otherwise, invoke the closure on obj itself
     */
    static void singleOrCollectionDo(obj, Closure closure) {
        if (obj instanceof Collection) {
            obj.each(closure)
        }
        else {
            closure(obj)
        }
    }
}
