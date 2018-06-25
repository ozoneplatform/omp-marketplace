package ozone.marketplace.enums

import org.ozoneplatform.appconfig.server.domain.model.ApplicationSetting

public enum MarketplaceApplicationSetting implements ApplicationSetting{

		FRANCHISE_STORE("store.is.franchise"),
		SCORE_CARD_ENABLED("store.enable.scoreCard"),
		INSIDE_OUTSIDE_BEHAVIOR('store.insideOutside.behavior'),
		STORE_FOOTER_FEATURED_TITLE("store.footer.featured.title"),
		STORE_FOOTER_FEATURED_CONTENT("store.footer.featured.content"),
		STORE_NAME("store.name"),
		STORE_LOGO("store.logo"),
		STORE_ICON("store.icon"),
		STORE_CONTACT_EMAIL("store.contact.email"),
		ABOUT_BOX_CONTENT("about.box.content"),
		ABOUT_BOX_IMAGE("about.box.image"),
		FREE_WARNING_CONTENT("free.warning.content"),
		ACCESS_ALERT_CONTENT("access.alert.content"),
		ACCESS_ALERT_ENABLED("access.alert.enable"),
		STORE_DEFAULT_THEME("store.default.theme"),
		EXTERNAL_SERVICE_ITEM_ENABLED("store.enable.ext.serviceitem"),
		OWF_SYNC_URLS("store.owf.sync.urls"),
		OPEN_SEARCH_TITLE_MESSAGE("store.open.search.title.message"),
		OPEN_SEARCH_DESCRIPTION_MESSAGE("store.open.search.description.message"),
		OPEN_SEARCH_FAV_ICON("store.open.search.fav.icon"),
		OPEN_SEARCH_SITE_ICON("store.open.search.site.icon"),
		AMP_SEARCH_RESULT_SIZE("store.amp.search.result.size"),
		AMP_SEARCH_DEFAULT_TIMOUT("store.amp.search.default.timeout"),
		AMP_IMAGE_MAX_SIZE("store.amp.image.max.size"),
		ALLOW_IMAGE_UPLOAD("store.image.allow.upload"),
		TYPE_IMAGE_MAX_SIZE("store.type.image.max.size"),
		ALLOW_OWNER_TO_EDIT_APPROVED_LISTING("store.allow.owner.to.edit.approved.listing"),
		DISABLE_INACTIVE_ACCOUNTS("store.disable.inactive.accounts"),
		INACTIVITY_THRESHOLD("store.inactivity.threshold"),
		JOB_DISABLE_ACCOUNTS_INTERVAL("store.job.disable.accounts.interval"),
		JOB_DISABLE_ACCOUNTS_START("store.job.disable.accounts.start.time"),
		SESSION_CONTROL_ENABLED("store.session.control.enabled"),
		SESSION_CONTROL_MAX_CONCURRENT("store.session.control.max.concurrent"),
		CEF_LOGGING_ENABLED("store.enable.cef.logging"),
		CEF_OBJECT_ACCESS_LOGGING_ENABLED("store.enable.cef.object.access.logging"),
		CEF_LOG_LOCATION("store.cef.log.location"),
		CEF_LOG_SWEEP_LOCATION("store.cef.sweep.log.location"),
		CEF_LOG_SWEEP_ENABLED("store.enable.cef.log.sweep"),
		CUSTOM_HEADER_URL("store.custom.header.url"),
		CUSTOM_FOOTER_URL("store.custom.footer.url"),
		CUSTOM_HEADER_HEIGHT("store.custom.header.height"),
		CUSTOM_FOOTER_HEIGHT("store.custom.footer.height"),
		CUSTOM_JS_IMPORTS("store.custom.js"),
		CUSTOM_CSS_IMPORTS("store.custom.css"),
		SECURITY_LEVEL("store.security.level"),
		QUICK_VIEW_DETAIL_FIELDS("store.quick.view.detail.fields"),
		URL_PUBLIC('url.public')

	MarketplaceApplicationSetting(String code) {
		this.code = code;
	}

	private final String code


	public String getCode() {
		return code
	}


}
