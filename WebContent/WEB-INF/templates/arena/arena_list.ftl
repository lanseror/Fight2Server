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
        <td>是否公会竞技场</td>
        <td>状态</td>
        <td>操作</td>
    </tr>
    <@s.iterator value="datas" status="status">
    <tr>
        <td>${id}</td>
        <td>${name}</td>
        <td>${startDate}</td>
        <td>${endDate}</td>
        <td>${onlineNumber}</td>
        <td><#if guildArena>是<#else>否</#if>
        </td>
        <td>${status.text}</td>
        <td align="center">
            <a href="<@s.url namespace="/arena" action="cancel"><@s.param name="id" value="${id}" /></@s.url>">取消</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/arena" action="delete"><@s.param name="id" value="${id}" /></@s.url>')">删除</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="<@s.url namespace="/arena-reward" action="list-by-arena"><@s.param name="arenaId" value="${id}" /></@s.url>">竞技场奖励</a>
        </td>
    </tr>
     </@s.iterator>
</table>
<br/>
    <a href="<@s.url namespace="/arena" action="add" />">添加</a>&nbsp;&nbsp;
    <a href="<@s.url namespace="/arena" action="list-json" />">List as Json</a>
</center>