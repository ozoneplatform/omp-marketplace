<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<% def keySet = fileMap.keySet() as List %>

<div id="gridView" >
    <common:convertListToGrid listSize="5" elementList="${keySet}" var="gridRowLists">
        <table>
            <g:each in="${gridRowLists}" var="row">
                <tr>
                    <g:each status="i" in="${row}" var="item">
                        <td class="media_cell">
                            <g:render template="/media/media_badge" var="fileInfo" bean="${fileMap.get(item)}" />
                        </td>
                    </g:each>
                </tr>
            </g:each>
        </table>
    </common:convertListToGrid>
</div>