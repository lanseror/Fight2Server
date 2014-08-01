<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/skill" action="save" method="post" id="postFrom">
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
                    <td align="center" width="60px">符号</td>
                    <td>作用方</td>
                    <td align="center">类型</td>
                    <td width="160px" align="right">数值参考本方的属性</td>
                    <td align="center">百分比</td>
                    <td width="60px">&nbsp;</td>
                </tr>
            </thead>
            <tbody id="operationBody">
                <@s.if test="skill==null">
                <tr>
                    <td align="center"><@s.select  name="signs" list=r"#{1:'加', -1:'减'}" value="1" /></td>
                    <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="'Self'" /></td>
                    <td align="center"><@s.select name="skillTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力', 'Skip':'击晕'}" value="'HP'" /></td>
                    <td width="160px" align="right"><@s.select name="skillPointAttributes" list=r"#{'HP':'生命值', 'ATK':'攻击力'}" value="'HP'" onchange="switchSkillPointAttribute(this);" /></td>
                    <td align="center"><@s.textfield size="20" name="points" cssClass="required int-range-1-300" />%</td>
                    <td align="right"><a href="###deleteOperation" onclick="deleteOperation(this);return false;">删除</a></td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator value="skill.operations">
                     <tr>
                        <td align="center"><@s.select  name="signs" list=r"#{1:'加', -1:'减'}" value="sign" /></td>
                        <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="skillApplyParty" /></td>
                        <td align="center"><@s.select name="skillTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力'}" value="skillType"  /></td>
                        <td width="160px" align="right"><@s.select name="skillPointAttributes" list=r"#{'HP':'生命值', 'ATK':'攻击力'}" value="skillPointAttribute" onchange="switchSkillPointAttribute(this);" /></td>
                        <td align="center">
                            <#if skillPointAttribute=='ATK'>
                                <@s.textfield size="20" name="points" cssClass="required int-range-100-1000" value="${point}" />%
                            <#else>
                                <@s.textfield size="20" name="points" cssClass="required int-range-1-300" value="${point}" />%
                            </#if>
                        </td>
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

<script>
    var valid = new Validation('postFrom', {immediate : true});
    function addOperation() {
        var tbody = $('#operationTable > tbody:last');
        var copyTr = tbody.children("tr:first").clone();
        tbody.append(copyTr);
        valid = new Validation('postFrom', {immediate : true});
    }
    function deleteOperation(deletehref) {
        var count = $('#operationTable > tbody:last').children().length;
        if(count>1) {
            $(deletehref).parent().parent().remove();
            valid = new Validation('postFrom', {immediate : true});
        } else {
            alert("至少要有一个动作!");
        }
    }
    function switchSkillPointAttribute(pointAttribute){
        var skillPointAttribute = $(pointAttribute);
        var tr = skillPointAttribute.parent().parent();
        var type = skillPointAttribute.val();
        var oldCss = "int-range-100-1000";
        var newCss = "int-range-1-300";
        if(type=='ATK'){
           oldCss = "int-range-1-300";
           newCss = "int-range-100-1000";
        }
        tr.find("input[name=points]").each(function(){
            var point = $(this);
            point.removeClass(oldCss);
            point.addClass(newCss);
        });
        valid.validate();
    }
    
    
</script>
</center>

<#include "/WEB-INF/templates/foot.ftl" />