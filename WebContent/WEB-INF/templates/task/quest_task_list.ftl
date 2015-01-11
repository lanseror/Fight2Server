<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>任务名</td>
        <td align="center">操作</td>
    </tr>
    <@s.iterator value="datas" status="status">
    <tr>
        <td>${id}</td>
        <td>${title}</td>
        <td align="center">
            <a href="<@s.url namespace="/task" action="edit"><@s.param name="id" value="${id}" /></@s.url>">修改</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/task" action="delete"><@s.param name="id" value="${id}" /></@s.url>')">删除</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="<@s.url namespace="/task-reward" action="list-by-task"><@s.param name="taskId" value="${id}" /></@s.url>">任务奖励</a>
        </td>
    </tr>
     </@s.iterator>
</table>
<br/>
    <a href="<@s.url namespace="/task" action="add" />">添加</a>&nbsp;&nbsp;
</center>