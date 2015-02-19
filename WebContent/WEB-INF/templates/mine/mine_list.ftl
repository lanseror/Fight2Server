<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>类型</td>
        <td>x</td>
        <td>y</td>
        <td>数量</td>
        <td>操作</td>
    </tr>
    <@s.iterator value="datas" status="status">
    <tr>
        <td>${id}</td>
        <td>${type}</td>
        <td>${col}</td>
        <td>${row}</td>
        <td>${amount}</td>
        <td align="center">
            <a href="<@s.url namespace="/mine" action="edit"><@s.param name="id" value="${id}" /></@s.url>">修改</a>
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:confirmDelete('<@s.url namespace="/mine" action="delete"><@s.param name="id" value="${id}" /></@s.url>')">删除</a>
        </td>
    </tr>
     </@s.iterator>
</table>
<br/>
    <a href="<@s.url namespace="/mine" action="add" />">添加</a>
</center>