<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>用户名</td>
        <td>名字</td>
        <td>拥有卡片数</td>
        <td>状态</td>
        <td>等级</td>
        <td>类别</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.username?default("")}</td>
        <td>${entry.name?default("")}</td>
        <td>${entry.cardCount?default("")}</td>
        <td><#if entry.isDisabled()>停用<#else>正常</#if></td>
        <td>${entry.level?default("")}</td>
        <td>${entry.type?default("")}</td>
        <td>
        <#if entry.isDisabled()>
            <a href="<@s.url namespace="/user" action="enable"><@s.param name="id" value="${entry.id}" /></@s.url>">启用</a>
        <#else>
            <a href="<@s.url namespace="/user" action="disable"><@s.param name="id" value="${entry.id}" /></@s.url>">停用</a>
        </#if>&nbsp;&nbsp;|&nbsp;&nbsp;
         &nbsp;&nbsp;|&nbsp;&nbsp;
         <a href="<@s.url namespace="/user" action="parties"><@s.param name="id" value="${entry.id}" /></@s.url>">玩家队伍</a>
         &nbsp;&nbsp;|&nbsp;&nbsp;
         <a href="<@s.url namespace="/user" action="delete"><@s.param name="id" value="${entry.id}" /></@s.url>">删除</a>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/npc" action="add" />">添加</a>&nbsp;&nbsp;
    <a href="<@s.url namespace="/npc" action="list-json" />">List as Json</a>
</center>