<#include "/WEB-INF/templates/head.ftl" />


<@sj.datepicker name="birthday" />



<@s.form namespace="/card" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="card!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield cssClass="post" size="80" name="card.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>名字</td>
        <td><@s.textfield cssClass="post" size="80" name="card.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>头像</td>
        <td><@s.textfield cssClass="post" size="80" name="card.avatar" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>图片</td>
        <td><@s.textfield cssClass="post" size="80" name="card.image" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>