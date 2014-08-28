<!DOCTYPE html>
<%@ page contentType="text/html; UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Session Expired</title>

        <link rel="shortcut icon" href="images/favicon.ico" />

        <p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='true'/>

        <style type="text/css">
            body {
                background-color: #e6e7e8;
            }
        </style>

        <g:javascript src="../config.js"/>
        <p:javascript src="marketplace-js-bundle"/>
        <script src="${request.contextPath}/vendor/requirejs/requirejs-2.1.9.js"></script>
        <script src="${request.contextPath}/js/requirejsConfig.js"></script>
    </head>
    <body>
        <script type="text/javascript">
            require(['views/ConfirmationDialog'], function(ConfirmationDialog) {
                (function showSessionExpired() {
                    ConfirmationDialog.show(
                        'Session Expired',
                        "Your session has expired. Click OK to reload to page."
                    ).promise().done(function() {
                        //reload entire OWF in case we are in a widget
                        window.location.href = '.';

                    //if they click cancel, show the dialog again
                    }).fail(showSessionExpired);
                })();
            });
        </script>
    </body>
</html>
