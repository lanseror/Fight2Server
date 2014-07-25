<#include "/WEB-INF/templates/head.ftl" />


<@sj.datepicker name="birthday" />



<@s.form namespace="/video" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="video!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield cssClass="post" size="80" name="video.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>Title</td>
        <td><@s.textfield cssClass="post" size="80" name="video.title" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>URL</td>
        <td><@s.textfield cssClass="post" size="80" name="video.url" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>Description</td>
        <td><@s.textfield cssClass="post" size="80" name="video.desc" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

<#include "/WEB-INF/templates/foot.ftl" />