package ozone.banner

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class MP_BannerBean implements BannerBean {
	
	private Boolean enableBanner
	
	private String header
	
	private String footer
	
	private String css
	
	private String js
	
	//This is different from header/footer in that its the fully decorated text.
	private String northBanner, southBanner

	@Override
	public List<String> getCssContents() {
		if(!getEnableBanner()){return null;}
		ArrayList<String> cssContents = new ArrayList<String>();
		//ADD NEW LINK CSS CONTENTS HERE
        String cssContent = wrapCSSContentWStyleTag(
    		getCss()
		);
        if(cssContent != null){
        	cssContents.add(cssContent);
        }
        //Done, now return.
    	return cssContents;
	}

	@Override
	public List<String> getJsContents() {
		if(!getEnableBanner()){return null;}
		ArrayList<String> jsContents = new ArrayList<String>();
        //ADD NEW LINK JS CONTENTS HERE
		String jsContent = wrapJSContentWScriptTag(
    		getJs()
		);
        if(jsContent != null){
        	jsContents.add(jsContent);
        }
        //Done, now return.
    	return jsContents;
	}

	@Override
	public InputStream getBannerBeanUINorthAsStream() {
		if(!getEnableBanner()){return null;}
		// TODO Auto-generated method stub
		return ((InputStream)IOUtils.toInputStream(getHeader()));
	}

	@Override
	public InputStream getBannerBeanUISouthAsStream() {
		if(!getEnableBanner()){return null;}
		// TODO Auto-generated method stub
		return ((InputStream)IOUtils.toInputStream(getFooter()));
	}
	
	
	
	//Util methods
	private String wrapCSSContentWStyleTag(String cssContent){
		String styleBeginningTag = "<style type=\"text/css\">"
		if(cssContent != null && !cssContent.isEmpty() && !cssContent.startsWith(styleBeginningTag)){
			return styleBeginningTag + cssContent + "</style>";
		}
		return null;
	}
	
	private String wrapJSContentWScriptTag(String jsContent){
		String scriptBeginningTag = "<script type=\"text/javascript\">"
		if(jsContent != null && !jsContent.isEmpty() && !jsContent.startsWith(scriptBeginningTag)){
			return scriptBeginningTag + jsContent + "</script>";
		}
		return null;
	}
	
	//Setters
	public void setHeader(String header){
		this.header = header
	}
	
	public void setFooter(String footer){
		this.footer = footer
	}
	
	public void setCss(String css){
		this.css = css
	}
	
	public void setJs(String js){
		this.js = js
	}
	
	public void setEnableBanner(Boolean enableBanner){
		this.enableBanner = enableBanner
	}
	
	//Getters
	public String getHeader(){
		return this.header
	}
	
	public String getFooter(){
		return this.footer
	}
	
	public String getCss(){
		return this.css
	}
	
	public String getJs(){
		return this.js
	}
	
	public Boolean getEnableBanner(){
		return this.enableBanner
	}
	
	public boolean hasBanner(){
		return this.footer || this.header
	}
}
