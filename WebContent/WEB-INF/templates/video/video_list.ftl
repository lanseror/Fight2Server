<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>Title</td>
        <td>URL</td>
        <td>Description</td>
        <td>Operation</td>
    </tr>
    <#list datas as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.title?default("")}</td>
        <td>${entry.url?default("")}</td>
        <td>${entry.desc?default("")}</td>
        <td><a href="<@s.url namespace="/video" action="edit"><@s.param name="id" value="${entry.id}" /></@s.url>">Edit</a></td>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/video" action="add" />">Add</a>&nbsp;&nbsp;
    <a href="<@s.url namespace="/video" action="list-json" />">List as Json</a>
</center>


<#include "/WEB-INF/templates/foot.ftl" />