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
        <td>身价</td>
        <td>类别</td>
        <td>操作</td>
    </tr>
    <@s.iterator value="datas">
    <tr>
        <td>${id}</td>
        <td>${username?default("")}</td>
        <td>${name?default("")}</td>
        <td>${cardCount?default("")}</td>
        <td><#if disabled>停用<#else>正常</#if></td>
        <td>${level?default("")}</td>
        <td>${salary?default("")}</td>
        <td><@s.select name="type" list=r"#{'User':'玩家', 'ArenaGuardian':'竞技场守卫', 'QuestNpc':'野外NPC', 'Boss':'Boss'}" value="type" disabled=true /></td>
        <td>
        <#if disabled>
            <a href="<@s.url namespace="/user" action="enable"><@s.param name="id" value="${id}" /></@s.url>">启用</a>
        <#else>
            <a href="<@s.url namespace="/user" action="disable"><@s.param name="id" value="${id}" /></@s.url>">停用</a>
        </#if>&nbsp;&nbsp;|&nbsp;&nbsp;
         <a href="<@s.url namespace="/user" action="parties"><@s.param name="id" value="${id}" /></@s.url>">玩家队伍</a>
         &nbsp;&nbsp;|&nbsp;&nbsp;
         <#if storeroom?exists>
         <a href="<@s.url namespace="/user-storeroom" action="view"><@s.param name="id" value="${storeroom.id}" /></@s.url>">玩家仓库</a>
         &nbsp;&nbsp;|&nbsp;&nbsp;
         </#if>
         <a href="<@s.url namespace="/user" action="add-diamon"><@s.param name="id" value="${id}" /></@s.url>">加钻石</a>
         <a href="javascript:confirmDelete('<@s.url namespace="/user" action="delete"><@s.param name="id" value="${id}" /></@s.url>')">删除</a>
        </td>
    </tr>
    </@s.iterator>
</table>
<br/>
    <a href="<@s.url namespace="/user" action="list-json" />">List as Json</a>
</center>