<#include "/WEB-INF/templates/head.ftl" />

<center>

<@s.form namespace="/arena-reward" action="save" method="post" id="postFrom">
<table border="1" cellpadding="1" cellspacing="0">
    <@s.hidden name="arenaId" />
    <@s.if test="arenaReward!=null">
    <tr>
        <td>ID</td>
        <td><@s.textfield size="80" name="arenaReward.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td>类型</td>
        <td><@s.select name="arenaReward.type" list=r"#{'Might':'力量', 'Ranking':'排名'}" value="arenaReward.type" /></td>
    </tr>
    <tr>
        <td>最小值</td>
        <td><@s.textfield size="80" id="arenaRewardMin" name="arenaReward.min" cssClass="required validate-integer min-value-1" /></td>
    </tr>
    <tr>
        <td>最大值</td>
        <td><@s.textfield size="80" id="arenaRewardMax" name="arenaReward.max" cssClass="required validate-integer min-value-1 great-than-equal-arenaRewardMin" /></td>
    </tr>
     <tr height="150px">
        <td>
            <b>物品列表</b>
            <br/>
            <br/>
            <a href="###addItem" onclick="addItem();return false;">增加物品</a>
        </td>
        <td align="center">
            <table id="itemTable">
            <thead>
                <tr>
                    <td align="center" width="60px">物品类别</td>
                    <td align="center">数量</td>
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                </tr>
            </thead>
            <tbody id="itemBody">
                <@s.if test="arenaReward==null">
                <tr>
                    <td align="center">
                        <@s.select  name="rewardItemTypes" list=r"#{'ArenaTicket':'竞技场门票', 'Stamina':'精力药水', 'Card':'卡牌', 'GuildContribution':'公会贡献值'}" value="'ArenaTicket'" onchange="selectItemType(this)" />
                    </td>
                    <td align="center"><@s.textfield size="5" name="rewardItemAmounts" cssClass="required int-range-1-300" /></td>
                    <td align="center"></td>
                    <td align="right"><a href="###deleteItem" onclick="deleteItem(this);return false;">删除</a></td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator value="arenaReward.rewardItems" status="status">
                     <tr>
                        <td align="center"><@s.select  name="rewardItemTypes" list=r"#{'ArenaTicket':'竞技场门票', 'Stamina':'精力药水', 'Card':'卡牌', 'GuildContribution':'公会贡献值'}" value="type" onchange="selectItemType(this)" /></td>
                        <td align="center">
                            <@s.textfield size="5" name="rewardItemAmounts" cssClass="required int-range-1-300" value="${amount}" />
                        </td>
                        <td align="center" id="cardTd${status.index}">
                        <@s.if test="type.name()=='Card'">
                        <script>
                            $(document).ready(function() {
                                initItem($("#cardTd${status.index}"), ${cardTemplate.id});
                            });
                        </script>
                        </@s.if>
                        </td>
                        <td align="right"><a href="###deleteItem" onclick="deleteItem(this);return false;">删除</a></td>
                    </tr>
                    </@s.iterator>
                </@s.else>
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
    <@s.if test="#status.getCount()%5==0&&#status.last==false">
        </tr><tr>
    </@s.if>
    </@s.iterator>
    </tr>
</table>

<table id="sampleTable" border="1" cellpadding="1" cellspacing="0" style="display:none;">
    <tbody>
    <tr>
        <td align="center">
           <@s.select  name="rewardItemTypes" list=r"#{'ArenaTicket':'竞技场门票', 'Stamina':'精力药水', 'Card':'卡牌', 'GuildContribution':'公会贡献值'}" value="'ArenaTicket'" onchange="selectItemType(this)" />
        </td>
        <td align="center"><@s.textfield size="5" name="rewardItemAmounts" cssClass="required int-range-1-300" /></td>
        <td align="center"></td>
        <td align="right"><a href="###deleteItem" onclick="deleteItem(this);return false;">删除</a></td>
    </tr>
    </tbody>
</table>
<script>
    var valid = new Validation('postFrom', {immediate : true});
    var sampleTr = $("#sampleTable > tbody:last").children("tr:first");
    var cardTable = $("#cardTable");
    cardTable.detach();
    var cardTr= $("<tr>");
    var cardTd= $("<td>");
    cardTd.attr("colspan", 3); 
    cardTd.append(cardTable);
    cardTr.append(cardTd);
    function selectItemType(typeDom) {
        var type=$(typeDom);
        if (type.val()=="Card") {
            type.parent().parent().after(cardTr);
            cardTable.show("slow");
        } else {
            cardTable.hide("slow");
        }
    }
    function addItem() {
        var tbody = $('#itemTable > tbody:last');
        var copyTr = sampleTr.clone();
        tbody.append(copyTr);
        valid = new Validation('postFrom', {immediate : true});
    }
    function selectItem(selecthref, cardId) {
        var copyAvatar = $(selecthref).parent().parent().parent().parent().clone();
        copyAvatar.children("tbody:last").children("tr:last").remove();
        var td2Insert = cardTr.prev().children("td:nth-last-child(2)");
        td2Insert.empty();
        var cardIdField = $('<input>').attr({type: 'hidden', name: 'cardIds', value: cardId});
        td2Insert.append(copyAvatar);
        td2Insert.append(cardIdField);
        cardTr.detach();
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
        cardTr.detach();
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