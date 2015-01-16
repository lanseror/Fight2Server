<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/task" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="task!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="task.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <@s.else>
        <@s.hidden name="task.id" value="0" />
    </@s.else>
    <tr>
        <td>任务名</td>
        <td><@s.textfield size="80" name="task.title" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>坐标</td>
        <td>
           X: <@s.textfield size="30" name="task.x" cssClass="required validate-integer" />
           Y：<@s.textfield size="30" name="task.y" cssClass="required validate-integer" />
        </td>
    </tr>
    <tr>
        <td>对话</td>
        <td>
            <@s.textarea cols="50" rows="15" name="task.dialog" cssClass="required min-length-10" />
        </td>
    </tr>
    <tr>
        <td>提示</td>
        <td>
            <@s.textarea cols="50" rows="15" name="task.tips" cssClass="required min-length-10" />
        </td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>