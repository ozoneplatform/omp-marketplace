<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<myui:bannerBeanNorth/>
<g:if test="${comingFromAccessAlert != 'true'}">
    <g:render template="/theme/marketplace/header"/>
    <div id="omp_content">
        <div id="omp_content_center" class="one-center-edge-shadow">
            <g:render template="/theme/marketplace/body"/>
        </div>
    </div>
    <g:if test="${comingFromLogout != 'true'}">
        <div id="footer" class="one-top-edge-shadow">
            <div id="footer_content_center">
            <table  class="widget-footer-table">
                <tr>
                    <td class="featured-table">
                        <div class="widget_footer_text"></div>
                        <div><p class="widget_footer_featured_subtext"></p></div>
                    </td>

                    <td class="franchise-options-table">
                        <div class="widget_footer_text">Resources</div>
                        <a href='${request.contextPath}${session.spaEnabled ? "/spa" : "/serviceItem/shoppe?max=5&offset=0"}'>
                            <div class="widget_footer_subtext">Home</div>
                        </a>

                        <g:link controller="media" action='show'>
                            <div class="widget_footer_subtext">Tutorials</div>
                        </g:link>

                        <a href="#about" ><div class="widget_footer_subtext"><g:message code="label.about" default="About" encodeAs="HTML" /></div></a>
                        <a id="contact-us" href="mailto:${contactEmailAddress.encodeAsHTML()}" ><div class="widget_footer_subtext"><g:message code="label.contactUs" default="Contact Us"  encodeAs="HTML" /></div></a>
                    </td>

                    <td class="browse-options-table">
                        <div class="widget_footer_text">Browse</div>
                        <g:link action="search" controller="serviceItem"
                                params="[sort:'title',order:'asc',offset:0,accessType:session.accessType]">
                            <div class="see-all-text widget_footer_subtext">All Listings</div>
                        </g:link>
                        <g:link action="search" controller="serviceItem"
                                params="[sort:'approvedDate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox: 'on']">
                            <div class="see-all-text widget_footer_subtext">New Arrivals</div>
                        </g:link>
                        <g:link action="search" controller="serviceItem"
                                params="[sort:'avgRate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox:'on']">
                            <div class="see-all-text widget_footer_subtext">Highest Rated</div>
                        </g:link>
                        <a href="#tags"><div class="see-all-text widget_footer_subtext">Tags</div></a>
                    </td>
                    <td class="account-options-table">
                        <div class="widget_footer_text">My Account</div>
                        <a href="#profile/self">
                            <div class="widget_footer_subtext"><g:message code="userMenu.profile" default="Profile" encodeAs="HTML" /></div>
                        </a>
                        <a class="create-listing" href="#">
                            <div class="widget_footer_subtext"><g:message code="userMenu.createListing" default="Add a Listing" encodeAs="HTML" /></div>
                        </a>
                    </td>
                </tr>
            </table>
            </div>
        </div>
        <script type="text/javascript">Marketplace.widget.initOwfAPI();</script>
        <g:if test="${myui.bannersAvailable() == 'true'}">
            <script type="text/javascript">
                if (!Marketplace.widget.isRunningInOWF()) {
                    jQuery('body').addClass('hasBanners');
                }
            </script>
        </g:if>
    </g:if>

    <script>
        jQuery(document).on('click', '.create-listing', function (evt) {
            evt.preventDefault();
            require(['createEditListing/index'], function (CreateEditListing) {
                CreateEditListing.create();
            });
        });

        require([
            'underscore',
            'jquery'
        ], function(_, $) {
            var footerTitle = _.find(Marketplace.appconfigs, function(conf) {
                return conf.code === 'store.footer.featured.title';
            }).value;
            var footerContents = _.find(Marketplace.appconfigs, function(conf) {
                return conf.code === 'store.footer.featured.content';
            }).value;
            var contactEmail = _.find(Marketplace.appconfigs, function(conf) {
                return conf.code === 'store.contact.email';
            }).value;
            var logo = _.find(Marketplace.appconfigs, function(conf) {
                return conf.code === 'store.logo';
            }).value;

            $('.widget-footer-table .featured-table')
                .children('.widget_footer_text').text(footerTitle)
                .end()
                .find('.widget_footer_featured_subtext').text(footerContents);

            $('#omp_market_box_img_link')
                .find('.omp_header_logo_img_cls')
                .attr('src', logo);

            $('#contact-us').attr('href', 'mailto:' + contactEmail);
        });
    </script>
</g:if>
<g:else>
    <div id="session-data-error-msg" style="display: none;">
        <g:message code="ErrorMessageString.settingSessionDataMsg" encodeAs="HTML" />
    </div>

    <div class="marketplaceBody">
        <div id="access-alert" class="bootstrap-active marketplace-container" style="visibility: hidden;">
            <div class="modal-header text-center">
                <h1>Warning</h1>
            </div>
            <div class="modal-body"></div>
            <div class="modal-footer">
                <button class="btn btn-primary">
                    <g:message code="button.Ok" encodeAs="HTML" />
                </button>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        jQuery(document).ready(function() {
            Marketplace.handleShowAccessAlertWindow({
                showAccessAlert: Marketplace.accessAlertConfig.show,
                accessAlertMsg: _.unescape(Marketplace.accessAlertConfig.message),
                settingSessionDataErrorMsg: ""+jQuery("#session-data-error-msg").html(),
                redirectUrl: "${initialUrl}"
            });
        });

    </script>
</g:else>

<myui:bannerBeanSouth/>
