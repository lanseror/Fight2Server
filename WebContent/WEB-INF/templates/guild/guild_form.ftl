<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>
<table border="1" width="80%">
    <tr>
        <td>ID</td>
        <td>候选人</td>
        <td>选票</td>
    </tr>
    <#list polls as entry>
    <tr>
        <td>${entry.id}</td>
        <td>${entry.candidate.name?default("")}</td>
        <td>${entry.votes}</td>
    </tr>
     </#list>
</table>

</center>