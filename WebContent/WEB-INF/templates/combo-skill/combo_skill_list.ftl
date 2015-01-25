<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>名字</td>
        <td>机率</td>
        <td>操作</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.name?default("")}</td>
        <td>${entry.probability?default("")}</td>
        <td><a href="<@s.url namespace="/combo-skill" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">Edit</a></td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/combo-skill" action="add" />">Add</a>
</center>

<#include "/WEB-INF/templates/foot.ftl" />