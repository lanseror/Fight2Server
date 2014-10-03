<#include "/WEB-INF/templates/head.ftl" />

<center>

<table>
    <@s.if test="userStoreroom!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="userStoreroom.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>精力药水</td>
        <td><@s.textfield size="80" name="userStoreroom.stamina" cssClass="required max-length-80" readonly="true" /></td>
    </tr>
    <tr>
        <td>竞技场门票</td>
        <td><@s.textfield size="80" name="userStoreroom.ticket" cssClass="required max-length-80" readonly="true" /></td>
    </tr>
   
   <tr>
        <td>卡牌</td>
        <td>
            <table border="1">
            <tr>
             <@s.iterator value="userStoreroom.cards" status="status">
                    <td>
                         ${name}<br/><span>HP：${hp}，攻击：${atk}</span><br/>
                         <img src="<@s.url value="${image}" />" height="150" width="100" />
                    </td>
                    <@s.if test="#status.getCount()%5==0&&#status.last==false">
                        </tr><tr>
                    </@s.if>
             </@s.iterator>
             </tr>
             </table>
        </td>
   </tr>
</table>

</center>