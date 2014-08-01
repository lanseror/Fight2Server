<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/skill" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="skill!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="skill.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>技能名</td>
        <td><@s.textfield size="80" name="skill.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>触发机率(万分之*)</td>
        <td><@s.textfield size="80" name="skill.probability" cssClass="required int-range-0-10000" /></td>
    </tr>
    <tr>
        <td>动作列表</td>
        <td>
            <table>
                <tr>
                    <td><@s.textfield size="80" name="operation" /></td>
                </tr>
            </table>
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