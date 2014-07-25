<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>名字</td>
        <td>星级</td>
        <td>初始生命值</td>
        <td>机率</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.name?default("")}</td>
        <td>${entry.star?default("")}</td>
        <td>${entry.hp?default("")}</td>
        <td>万分之${entry.probability?default("")}</td>
        <td>
            <a href="<@s.url namespace="/card-template" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">修改</a>
            <a href="<@s.url namespace="/card-template" action="delete"><@s.param name="id" value="${entry.id}" /></@s.url>">删除</a>
        </td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/card-template" action="add" />">添加</a>&nbsp;&nbsp;
    <a href="<@s.url namespace="/card-template" action="list-json" />">List as Json</a>
</center>