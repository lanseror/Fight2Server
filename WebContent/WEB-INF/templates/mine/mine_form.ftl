<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/mine" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="mine!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="mine.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td><b>类型</b></td>
        <td><@s.select name="mine.type" list=r"#{'Mineral':'石矿', 'Wood':'木矿', 'Crystal':'水晶矿', 'Diamon':'钻石矿'}" value="mine.type"  /></td>
    </tr>
    <tr>
        <td>x</td>
        <td><@s.textfield size="80" name="mine.col" cssClass="required validate-integer" /></td>
    </tr>
    <tr>
        <td>y</td>
        <td><@s.textfield size="80" name="mine.row" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>