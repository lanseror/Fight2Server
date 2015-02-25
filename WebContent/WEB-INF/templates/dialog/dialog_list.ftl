<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>说话人</td>
        <td>内容</td>
        <td>类型</td>
        <td>操作</td>
    </tr>
    <@s.iterator value="datas" status="status">
    <tr>
        <td>${id}</td>
        <td>${speaker}</td>
        <td>${content}</td>
        <td>${orderType}</td>
        <td align="center">
            <a href="<@s.url namespace="/dialog" action="edit"><@s.param name="id" value="${id}" /></@s.url>">修改</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/dialog" action="delete"><@s.param name="id" value="${id}" /></@s.url>')">删除</a>
        </td>
    </tr>
     </@s.iterator>
</table>
<br/>
    <a href="<@s.url namespace="/dialog" action="add" />">添加</a>
</center>