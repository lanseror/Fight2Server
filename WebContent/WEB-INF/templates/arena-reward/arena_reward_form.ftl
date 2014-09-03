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
                    <td>数量</td>
                </tr>
            </thead>
            <tbody id="itemBody">
                <@s.if test="arenaReward==null">
                <tr>
                    <td align="center"><@s.select  name="rewardItemTypes" list=r"#{'ArenaTicket':'竞技场门票', 'Stamina':'精力药水', 'Card':'卡牌'}" value="'ArenaTicket'" /></td>
                    <td align="center"><@s.textfield size="20" name="rewardItemAmounts" cssClass="required int-range-1-300" />%</td>
                    <td align="right"><a href="###deleteItem" onclick="deleteItem(this);return false;">删除</a></td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator value="arenaReward.rewardItems">
                     <tr>
                        <td align="center"><@s.select  name="rewardItemTypes" list=r"#{'ArenaTicket':'竞技场门票', 'Stamina':'精力药水', 'Card':'卡牌'}" value="type" /></td>
                        <td align="center">
                            <@s.textfield size="20" name="rewardItemAmounts" cssClass="required int-range-100-1000" value="${amount}" />%
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

<script>
    var valid = new Validation('postFrom', {immediate : true});
    function addItem() {
        var tbody = $('#itemTable > tbody:last');
        var copyTr = tbody.children("tr:first").clone();
        tbody.append(copyTr);
        valid = new Validation('postFrom', {immediate : true});
    }
    function deleteItem(deletehref) {
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