<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${session.marketplaceLayout}" />
        <title>Configuration</title>
        <marketplaceTheme:vendor src="requirejs/requirejs-2.1.9.js"/>
    </head>
    <body>
        <div id="marketContentWrapper">
            <div class="body">
                <div id="app-config-page">
                    <div class="bootstrap-active">
                        <h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
                    </div>
                    <!-- Start the menu here -->
                    <div>

                        <div class="app_config_menu">

                            <div class="item branding active">
                                <a href="#/config/BRANDING">Branding</a>
                            </div>

                            <div class="item scorecard">
                                <a href="#/config/SCORECARD">Scorecard</a>
                            </div>

                            <div class="item additional_config">
                                <a href="#/config/ADDITIONAL_CONFIGURATION">Additional Configurations</a>
                            </div>

                            <div class="item user_account_settings">
                                <a href="#/config/USER_ACCOUNT_SETTINGS">User Account Settings</a>
                            </div>

                            <div class="item auditing">
                                <a href="#/config/AUDITING">Auditing</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
         </div>

        <marketplaceTheme:javascript src="applicationConfiguration/main.js"/>
        <!--<script type="text/javascript" src="${request.contextPath}/static/vendor/backbone/json2.js"></script>-->
        <!--[if lt IE 8]>
            <marketplaceTheme:vendor src="backbone/json2.js"/>
        <![endif]-->

        <script type="text/javascript">
            jQuery.noConflict();

            jQuery(function($){
                var $ = jQuery;
                var $menuItems = $('.app_config_menu .item');

                $menuItems.on('click', function () {
                    $menuItems.removeClass('active');
                    $(this).addClass('active');
                });
            });

        </script>
    </body>

</html>
