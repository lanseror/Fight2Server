<#include "/WEB-INF/templates/head.ftl" />



<center>
<br/><br/>

    <span>总血量:${partyInfo.hp} 总攻击：${partyInfo.atk}</span>

<table border="1" width="80%">
    <#list partyInfo.parties as party>
    <tr>
        <td>队伍：${party.partyNumber}<br/>
         <span>血量:${party.hp}<br/> 攻击：${party.atk}</span>
        </td>
        <#list party.partyGrids as partyGrid>
        <td>
            <#if partyGrid.card?exists>
             ${partyGrid.card.name}<br/>
                 <img src="<@s.url value="${partyGrid.card.avatar}" />" height="60" width="60" />
            </#if>
        </td>
         </#list>
    </tr>
     </#list>
</table>
<br/>
    <a href="<@s.url namespace="/user" action="list-json" />">List as Json</a>
</center>