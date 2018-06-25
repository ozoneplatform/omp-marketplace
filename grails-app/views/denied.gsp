<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Marketplace : Denied</title>

  <p:favicon/>

  <p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='true'/>
  <style type="text/css">
    body {
        background-color: #e6e7e8;
    }
  </style>

  <!-- ** JavaScript ** -->
  <g:javascript src="../config.js"/>
  <p:javascript src="marketplace-js-bundle"/>
  <script src="${request.contextPath}/vendor/requirejs/requirejs-2.1.9.js"></script>
  <script src="${request.contextPath}/js/requirejsConfig.js"></script>

  <script type="text/javascript">


    require(['views/Dialog', 'views/ConfirmationDialog'], function(Dialog, ConfirmationDialog) {
        function showAdminDeniedDialog() {
            ConfirmationDialog.show(
                'Authorization Error',
                'You are not authorized to access this ADMIN page. You will be ' +
                'redirected to Marketplace.'
            ).promise().done( function() {
                window.location.href = Marketplace.contextPath;
            }).fail(showAdminDeniedDialog);
        }

        function showBasicDeniedDialog() {
            Dialog.show(
                'Authorization Error',
                'You are not authorized to access this page.'
            );
        }

        var adminRegex = new RegExp('^.*' + Marketplace.contextPath + '\/admin[\/\?]?.*$');

        //check current location to see if we failed from /marketplace/admin
        //if so try to redirect to main Marketplace
        if (window.location.href.match(adminRegex)) {
            showAdminDeniedDialog();
        }
        else {
            showBasicDeniedDialog();
        }
    });
  </script>

</head>
<body>
</body>

</html>
