<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/dialog" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="dialog!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="dialog.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td><b>类型</b></td>
        <td><@s.select name="dialog.orderType" list=r"#{'Random':'随机', 'Sequence':'顺序'}" value="dialog.orderType"  /></td>
    </tr>
    <tr>
        <td>说话人</td>
        <td><@s.select name="dialog.speaker" list=r"#{'Self':'自己', 'NPC':'NPC', 'OtherPlayer':'其他玩家'}" value="dialog.speaker"  /></td>
    </tr>
    <tr>
        <td>内容</td>
        <td><@s.textarea cols="50" rows="15" name="dialog.content" cssClass="required max-length-80" /></td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>