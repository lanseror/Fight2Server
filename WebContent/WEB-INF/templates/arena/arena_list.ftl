<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>名字</td>
        <td>开始时间</td>
        <td>结束时间</td>
        <td>在线人数</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.name}</td>
        <td>${entry.startDate}</td>
        <td>${entry.endDate}</td>
        <td>${entry.onlineNumber}</td>
        <td align="center">
            <a href="<@s.url namespace="/arena" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">修改</a>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/arena" action="add" />">添加</a>&nbsp;&nbsp;
    <a href="<@s.url namespace="/arena" action="list-json" />">List as Json</a>
</center>