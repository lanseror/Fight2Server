<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/card" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="card!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="card.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>名字</td>
        <td><@s.textfield size="80" name="card.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>头像</td>
        <td><@s.textfield size="80" name="card.avatar" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>图片</td>
        <td><@s.textfield size="80" name="card.image" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>星级</td>
        <td><@s.textfield size="80" name="card.star" cssClass="required int-range-1-7" /></td>
    </tr>

    <tr>
        <td>生命值</td>
        <td><@s.textfield size="80" name="card.hp" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td>攻击力</td>
        <td><@s.textfield size="80" name="card.atk" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>