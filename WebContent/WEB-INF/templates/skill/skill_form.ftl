<#include "/WEB-INF/templates/head.ftl" />

<script>
    function addOperation() {
        var tbody = $('#operationTable > tbody:last');
        var copyTr = tbody.children("tr:first").clone();
        tbody.append(copyTr);
    }
    function deleteOperation(deletehref) {
        var count = $('#operationTable > tbody:last').children().length;
        if(count>1) {
            $(deletehref).parent().parent().remove();
        } else {
            alert("至少要有一个动作!");
        }
    }
</script>
<center>

<@s.form namespace="/skill" action="save" method="post" cssClass="required-validate">
<table border="1" cellpadding="1" cellspacing="0">
    <@s.hidden name="cardTemplateId" />
    <@s.if test="skill!=null">
    <tr>
        <td><b>ID</b></td>
        <td><@s.textfield size="80" name="skill.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td><b>技能名</b></td>
        <td><@s.textfield size="80" name="skill.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td><b>触发机率(万分之*)</b></td>
        <td><@s.textfield size="80" name="skill.probability" cssClass="required int-range-0-10000" /></td>
    </tr>
    <tr height="150px">
        <td>
            <b>动作列表</b>
            <br/>
            <br/>
            <a href="###addOperation" onclick="addOperation();return false;">增加动作</a>
        </td>
        <td align="center">
            <table id="operationTable">
            <thead>
                <tr>
                    <td align="center">属性</td>
                    <td>百分比</td>
                    <td>作用方</td>
                    <td width="60px">&nbsp;</td>
                </tr>
            </thead>
            <tbody id="operationBody">
                <@s.if test="skill==null">
                <tr>
                    <td><@s.select name="skillPointTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力', 'Skip':'击晕'}" value="'HP'" /></td>
                    <td><@s.textfield size="20" name="points" cssClass="required int-range--100-100" />%</td>
                    <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="'Self'" /></td>
                    <td align="right"><a href="###deleteOperation" onclick="deleteOperation(this);return false;">删除</a></td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator value="skill.operations">
                     <tr>
                        <td><@s.select name="skillPointTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力'}" value="skillPointType" /></td>
                        <td><@s.textfield size="20" name="points" cssClass="required int-range--100-100" value="${point}" />%</td>
                        <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="skillApplyParty" /></td>
                        <td align="right"><a href="###deleteOperation" onclick="deleteOperation(this);return false;">删除</a></td>
                    </tr>
                    </@s.iterator>
                </@s.else>
            </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td>操作</td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>
<@s.token />
</@s.form>

</center>

<#include "/WEB-INF/templates/foot.ftl" />