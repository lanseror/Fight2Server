<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/arena-reward" action="save" method="post" cssClass="required-validate">
<table>
    <@s.hidden name="arenaId" />
    <@s.if test="arenaReward!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="arenaReward.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>类型</td>
        <td><@s.select name="arenaReward.type" list=r"#{'Might':'力量', 'Ranking':'排名'}" value="arenaReward.type" /></td>
    </tr>
    <tr>
        <td>最小值</td>
        <td><@s.textfield size="80" id="arenaRewardMin" name="arenaReward.min" cssClass="required validate-integer min-value-1" /></td>
    </tr>
    <tr>
        <td>最大值</td>
        <td><@s.textfield size="80" id="arenaRewardMax" name="arenaReward.max" cssClass="required validate-integer min-value-1 great-than-equal-arenaRewardMin" /></td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>