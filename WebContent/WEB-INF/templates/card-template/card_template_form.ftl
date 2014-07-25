<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>
         <font color="red">
         <@s.actionerror />
         <@s.actionmessage />
        <@s.fielderror />
        </font>
<@s.form namespace="/card-template" action="save" method="post" enctype="multipart/form-data" cssClass="required-validate">
<table>
    <@s.if test="cardTemplate!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>名字</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>头像</td>
        <td>
        <span>头像 1:</span><@s.file id="avatar1" name="avatar" accept="image/png,image/jpeg,image/pjpeg" cssClass="required validate-file-png-jpg" />
        <span>头像 2:</span><@s.file id="avatar2" name="avatar" accept="image/png,image/jpeg,image/pjpeg" cssClass="required validate-file-png-jpg" />:
        <span>头像 3:</span><@s.file id="avatar3" name="avatar" accept="image/png,image/jpeg,image/pjpeg" cssClass="required validate-file-png-jpg" />
        <span>头像 4:</span><@s.file id="avatar4" name="avatar" accept="image/png,image/jpeg,image/pjpeg" cssClass="required validate-file-png-jpg" />
        </td>
    </tr>
    <tr>
        <td>图片</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.image" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>星级</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.star" cssClass="required int-range-1-7" /></td>
    </tr>

    <tr>
        <td>初始生命值</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.hp" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td>初始攻击力</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.atk" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td>召唤机率(万分之*)</td>
        <td><@s.textfield cssClass="post" size="80" name="cardTemplate.probability" cssClass="required int-range-0-10000" /></td>
    </tr>


    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>