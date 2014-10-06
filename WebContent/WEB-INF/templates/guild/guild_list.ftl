<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>名称</td>
        <td>QQ</td>
        <td>会长</td>
        <td>创建日期</td>
        <td>投票开启</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td><a href="<@s.url namespace="/guild" action="view"><@s.param name="id" value="${entry.id}" /></@s.url>">${entry.name?default("")}</a></td>
        <td>${entry.qq?default("")}</td>
        <td>${entry.president.name?default("")}</td>
        <td>${entry.createDate}</td>
        <td><#if entry.isPollEnabled()>是<#else>否</#if></td>
        <td>
            <#if entry.isPollEnabled()>
            <a href="<@s.url namespace="/guild" action="disable-poll"><@s.param name="id" value="${entry.id}" /></@s.url>">结束投票</a>
        <#else>
            <a href="<@s.url namespace="/guild" action="enable-poll"><@s.param name="id" value="${entry.id}" /></@s.url>">开启投票</a>
        </#if>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/user" action="list-json" />">List as Json</a>
</center>