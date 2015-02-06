<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/combo-skill" action="save" method="post" id="postFrom" enctype="multipart/form-data">
<table border="1" cellpadding="1" cellspacing="0">
    <#assign isReuired="required " />
    <@s.if test="comboSkill!=null">
        <#assign isReuired="" />
    <tr>
        <td><b>ID</b></td>
        <td><@s.textfield size="80" name="comboSkill.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td><b>技能名</b></td>
        <td><@s.textfield size="80" name="comboSkill.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td><b>触发机率(百分之*)</b></td>
        <td><@s.textfield size="80" name="comboSkill.probability" cssClass="required int-range-0-100" />%</td>
    </tr>
    <tr>
        <td><b>技能图标</b></td>
        <td>
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <@s.if test="comboSkill!=null">
                 <tr align="center">
                        <td>
                            <img src="<@s.url value="${comboSkill.icon?default('')}" />" height="60" width="60" />
                        </td>
                 </tr>
                </@s.if>
                 <tr>
                    <td>
                        <@s.file id="icon" name="icon" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                <tr>
            </table>
        </td>
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
                <@s.if test="comboSkill==null">
                <tr>
                    <td align="center"><@s.select  name="signs" list=r"#{1:'加', -1:'减'}" value="1" /></td>
                    <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="'Self'" /></td>
                    <td align="center"><@s.select name="skillTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力', 'Revival':'复活'}" value="'HP'" /></td>
                    <td width="160px" align="right"><@s.select name="skillPointAttributes" list=r"#{'HP':'生命值', 'ATK':'攻击力'}" value="'HP'" onchange="switchSkillPointAttribute(this);" /></td>
                    <td align="center"><@s.textfield size="20" name="points" cssClass="required int-range-1-300" />%</td>
                    <td align="right"><a href="###deleteOperation" onclick="deleteOperation(this);return false;">删除</a></td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator value="comboSkill.operations">
                     <tr>
                        <td align="center"><@s.select  name="signs" list=r"#{1:'加', -1:'减'}" value="sign" /></td>
                        <td><@s.select name="skillApplyPartys" list=r"#{'Self':'本方', 'Opponent':'对方', 'Leader':'本方领队', 'OpponentLeader':'对方领队', 'SelfAll':'本方(全体)', 'OpponentAll':'对方(全体)'}" value="skillApplyParty" /></td>
                        <td align="center"><@s.select name="skillTypes" list=r"#{'HP':'生命值', 'ATK':'攻击力', 'Defence':'防御力', 'Revival':'复活'}" value="skillType"  /></td>
                        <td width="160px" align="right"><@s.select name="skillPointAttributes" list=r"#{'HP':'生命值', 'ATK':'攻击力'}" value="skillPointAttribute" onchange="switchSkillPointAttribute(this);" /></td>
                        <td align="center">
                            <#if skillPointAttribute=='ATK'>
                                <@s.textfield size="20" name="points" cssClass="required int-range-1-300" value="${point}" />%
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
        <td>队伍</td>
        <td>
             <table id="partyTable" border="1" cellpadding="1" cellspacing="0" height="200px" >
                <tbody id="partyBody">
                    <tr align="center">
                        <@s.if test="comboSkill==null">
                        <td width="125px" id="td0">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td1">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td2">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td3">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        </@s.if>
                        <@s.else>
                        <@s.iterator value="savedCards" status="status">
                            <td width="125px" id="td<@s.property value="#status.getIndex()" />">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <@s.if test="id!=0">
                                <tr align="center">
                                    <td>${name}<input type="hidden" name="cardIds" value="${id}"></td>
                                </tr>
                                <tr align="center">
                                    <td><img src="<@s.url value="${avatar}" />" height="60" width="60" /></td>
                                </tr>
                                
                                </@s.if>
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                            
                                
                        </td>
                        </@s.iterator>
                        </@s.else>
                    </tr>
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

<table id="cardTable" border="1" cellpadding="1" cellspacing="0" style="display:none;">
    <tr>
        <td align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                <tr align="center">
                    <td><a href="###cancelItem" onclick="cancelItem(this);return false;">取消</a></td>
                </tr>
            </table>
            
        </td>
    <@s.iterator value="cards" status="status">
        <td align="center">
            <table border="1" cellpadding="0" cellspacing="0" width="90px" id="cardTable${id}">
                <tr align="center">
                    <td>${name}</td>
                </tr>
                <tr align="center">
                    <td><img src="<@s.url value="${avatar}" />" height="60" width="60" /></td>
                </tr>
                <tr align="center">
                    <td>
                    <input type="hidden" class="required" />
                    <a href="###selectItem" onclick="selectItem(this, '${id}');return false;">选择</a></td>
                </tr>
            </table>
        </td>
    <@s.if test="(#status.getCount()+1)%5==0&&#status.last==false">
        </tr><tr>
    </@s.if>
    </@s.iterator>
    </tr>
</table>
<table id="sampleTable" border="1" cellpadding="1" cellspacing="0" style="display:none;">
    <tbody>
    <tr>
        <td align="center"></td>
        <td align="right"><a href="###deleteItem" onclick="deleteItem(this);return false;">删除</a></td>
    </tr>
    </tbody>
</table>
<a id="sampleSelectHref" href="###selectCard" onclick="selectCard(this);return false;">选择</a>
<table id="sampleSelectHrefTable" border="0" cellpadding="0" cellspacing="0" width="90px">
      <tr align="center">
          <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
      </tr>
</table>

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
        var oldCss = "int-range-1-300";
        var newCss = "int-range-1-300";
        if(type=='ATK'){
           oldCss = "int-range-1-300";
           newCss = "int-range-1-300";
        }
        tr.find("input[name=points]").each(function(){
            var point = $(this);
            point.removeClass(oldCss);
            point.addClass(newCss);
        });
        valid.validate();
    }
    
    var sampleTr = $("#sampleTable > tbody:last").children("tr:first");
    var cardTable = $("#cardTable");
    cardTable.detach();
    var sampleSelectHref = $("#sampleSelectHref");
    sampleSelectHref.detach();
    var sampleSelectHrefTable = $("#sampleSelectHrefTable");
    sampleSelectHrefTable.detach();
    var cardTd= $("<td>");
    cardTd.append(cardTable);
    function selectCard(selectHrefDom) {
        var selectHref = $(selectHrefDom);
        var selectTd = selectHref.parent().parent().parent().parent().parent();
        selectTd.empty();
        selectTd.css('background-color', '#ffb3a7');
        selectTd.after(cardTd);
        cardTable.show("slow");
    }
    function addItem() {
        var tbody = $('#itemTable > tbody:last');
        var copyTr = sampleTr.clone();
        tbody.append(copyTr);
        valid = new Validation('postFrom', {immediate : true});
    }
    function cancelItem(href) {
        var td2Insert = cardTd.prev();
        td2Insert.css('background-color', '#ffffff');
        td2Insert.append(sampleSelectHrefTable.clone());
        cardTd.detach();
    }
    function selectItem(selecthref, cardId) {
        var copyAvatar = $(selecthref).parent().parent().parent().parent().clone();
        var td2replaceHref=copyAvatar.children("tbody:last").children("tr:last").children("td:last");
        td2replaceHref.empty();
        var selectHref=sampleSelectHref.clone();
        td2replaceHref.append(selectHref);
        var td2Insert = cardTd.prev();
        td2Insert.css('background-color', '#ffffff');
        td2Insert.empty();
        var cardIdField = $('<input>').attr({type: 'hidden', name: 'cardIds', value: cardId});
        td2Insert.append(copyAvatar);
        td2Insert.append(cardIdField);
        var partyId = td2Insert.attr('id').replace("td","");
        var partyIdField = $('<input>').attr({type: 'hidden', name: 'partyIds', value: partyId});
        td2Insert.append(partyIdField);
        cardTd.detach();
    }
    function initItem(cardTd, cardId) {
        var copyAvatar = cardTable.find("#cardTable"+cardId).clone();
        copyAvatar.children("tbody:last").children("tr:last").remove();
        cardTd.empty();
        var cardIdField = $('<input>').attr({type: 'hidden', name: 'cardIds', value: cardId});
        cardTd.append(copyAvatar);
        cardTd.append(cardIdField);
    }
    function deleteItem(deletehref) {
        cardTd.detach();
        var count = $('#itemTable > tbody:last').children().length;
        if(count>1) {
            $(deletehref).parent().parent().remove();
            valid = new Validation('postFrom', {immediate : true});
        } else {
            alert("至少要有一张卡!");
        }
    }
    
</script>
</center>

<#include "/WEB-INF/templates/foot.ftl" />