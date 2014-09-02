<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
    <b><a href="<@s.url namespace="/arena" action="list" />">竞技场：${arena.name}</a></b>
<br/>
<br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>类别</td>
        <td>最小值</td>
        <td>最大值</td>
        <td>操作</td>
    </tr>
    <#assign typeMap = {"Might":"力量", "Ranking":'排名'} />
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${typeMap[entry.type]}</td>
        <td>${entry.min}</td>
        <td>${entry.max}</td>
        <td>
            <a href="<@s.url namespace="/arena-reward" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">修改</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/arena-reward" action="delete"><@s.param name="id" value="${entry.id}" /></@s.url>')">删除</a>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/arena-reward" action="add"><@s.param name="arenaId" value="${arenaId}" /></@s.url>">添加</a>&nbsp;&nbsp;
</center>