<%@ page import="marketplace.*" %>
<%@ page import="grails.converters.JSON" %>
<html>
    <body>
        <h3 id="header"></h3>
        !HTTP Status Code is <span style="font-weight:bold">200</span>

        <script id="owfTransport" type="text/javascript">
            var json = '${raw((jsonData as JSON).toString())}'

            window.name = json;
            document.getElementById('header').appendChild(
                document.createTextNode(json)
            );
        </script>
    </body>
</html>
