<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
    <b><a href="<@s.url namespace="/task" action="list" />">任务名：${questTask.title}</a></b>
<br/>
<br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>
            <a href="<@s.url namespace="/task-reward" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">修改</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/task-reward" action="delete"><@s.param name="id" value="${entry.id}" /></@s.url>')">删除</a>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/task-reward" action="add"><@s.param name="taskId" value="${taskId}" /></@s.url>">添加</a>&nbsp;&nbsp;
</center>