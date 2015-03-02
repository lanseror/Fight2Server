<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/user" action="diamon-save" method="post" cssClass="required-validate">
<table>
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="user.id" readonly="true" /></td>
    </tr>
    <tr>
        <td>名字</td>
        <td><@s.textfield size="80" name="user.name" cssClass="required max-length-80" readonly="true" /></td>
    </tr>
    <tr>
        <td>加钻石</td>
        <td><@s.textfield size="80" name="user.salary" cssClass="required max-length-80" /></td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>