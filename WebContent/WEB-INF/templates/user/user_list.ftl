<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>用户名</td>
        <td>名字</td>
        <td>拥有卡片数</td>
        <td>等级</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.username?default("")}</td>
        <td>${entry.name?default("")}</td>
        <td>${entry.cardCount?default("")}</td>
        <td>${entry.level?default("")}</td>
        <td><a href="<@s.url namespace="/user" action="view"><@s.param name="id" value="${entry.id}" /></@s.url>">查看</a></td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/user" action="list-json" />">List as Json</a>
</center>