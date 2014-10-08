<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/arena" action="save" method="post" cssClass="required-validate">
<table>
    <@s.if test="arena!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="arena.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>名字</td>
        <td><@s.textfield size="80" name="arena.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>开始时间</td>
        <td><@sj.datepicker name="arena.startDate" timepicker="true" displayFormat="m/d/yy" timepickerAmPm="true" timepickerFormat="hh:mm tt" timepickerHourText="小时" timepickerTimeText="时间" />
        
         </td>
    </tr>
    <tr>
        <td>结束时间</td>
        <td><@sj.datepicker name="arena.endDate" timepicker="true"  displayFormat="m/d/yy" timepickerAmPm="true" timepickerFormat="hh:mm tt" /></td>
    </tr>
     <tr>
        <td>是否公会竞技场</td>
        <td><@s.checkbox name="arena.guildArena" />
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