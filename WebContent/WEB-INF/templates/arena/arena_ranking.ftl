<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>用户</td>
        <td>竞技场</td>
        <td>排名</td>
        <td>力量</td>
        <td>胜利</td>
        <td>失败</td>
        <td>操作</td>
    </tr>
    <#list ranks as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.user}</td>
        <td>${entry.arena}</td>
        <td>${entry.rankNumber}</td>
        <td>${entry.might}</td>
        <td>${entry.win}</td>
        <td>${entry.lose}</td>
        <td align="center">
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/arena" action="list-json" />">List as Json</a>
</center>