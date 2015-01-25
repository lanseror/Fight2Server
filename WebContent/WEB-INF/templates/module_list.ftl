<#include "/WEB-INF/templates/head.ftl" />


<center>
<h2>
	<br/>
    <a href="<@s.url namespace="/user" action="list" />">用户管理</a>
    <br/><br/>
    <a href="<@s.url namespace="/npc" action="list" />">NPC管理</a>
    <br/><br/>
    <a href="<@s.url namespace="/card-template" action="list" />">卡片管理</a>
    <br/><br/>
    <a href="<@s.url namespace="/arena" action="list" />">竞技场</a>
    <br/><br/>
    <a href="<@s.url namespace="/guild" action="list" />">公会</a>
    <br/><br/>
    <a href="<@s.url namespace="/task" action="list" />">任务</a>
    <br/><br/>
    <a href="<@s.url namespace="/combo-skill" action="list" />">组合技能</a>
    <br/>
    
</h2>
</center>

<#include "/WEB-INF/templates/foot.ftl" />