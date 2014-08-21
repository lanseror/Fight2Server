<#include "/WEB-INF/templates/head.ftl" />


<center>
<h2>
	<br/>
    <a href="<@s.url namespace="/user" action="list" />">用户管理</a>
    <br/><br/>
    <a href="<@s.url namespace="/card-template" action="list" />">卡片管理</a>
    <br/><br/>
    <a href="<@s.url namespace="/arena" action="list" />">竞技场</a>
    <br/>
    
</h2>
</center>

<#include "/WEB-INF/templates/foot.ftl" />