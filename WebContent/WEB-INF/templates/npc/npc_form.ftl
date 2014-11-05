<#include "/WEB-INF/templates/head.ftl" />

<#--
<@sj.datepicker name="birthday" />
-->
<center>

<@s.form namespace="/npc" action="save" method="post" id="postFrom">
<@s.if test="hasActionErrors()">
   <ul>
    <li><font color="red"><@s.actionerror /></font></li>
  </ul>
</@s.if>
<table>
    <@s.if test="user!=null">
    <tr>
        <td>ID</td>
        <td>
            <@s.textfield size="80" name="user.id" readonly="true" />
        </td>
    </tr>
    </@s.if>
    <tr>
        <td>名字</td>
        <td><@s.textfield size="80" name="user.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td>类别</td>
        <td><@s.select name="user.type" list=r"#{'ArenaGuardian':'竞技场守卫', 'QuestNpc':'野外NPC'}" value="user.salary"  /></td>
    </tr>
    <tr>
        <td>身价级别</td>
        <td>
            <@s.select name="user.salary" list=r"#{1000:1000, 2000:2000, 3000:3000, 4000:4000, 5000:5000}" value="user.salary"  />
        </td>
    </tr>
    
    <tr>
        <td>队伍</td>
        <td>
             <table id="partyTable" border="1" cellpadding="1" cellspacing="0" height="200px" >
                <tbody id="partyBody">
                    <tr align="center">
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
                    </tr>
                     <tr align="center">
                        <td width="125px" id="td4">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td5">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td6">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td7">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                     <tr align="center">
                        <td width="125px" id="td8">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td9">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td10">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                        <td width="125px" id="td11">
                            <table border="0" cellpadding="0" cellspacing="0" width="90px">
                                <tr align="center">
                                    <td><a href="###selectCard" onclick="selectCard(this);return false;">选择</a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </tbody>
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
            alert("至少要有一个物品!");
        }
    }
    
</script>
</center>