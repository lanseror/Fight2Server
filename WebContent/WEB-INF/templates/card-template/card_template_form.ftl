<#include "/WEB-INF/templates/head.ftl" />
    <script>
        <!--  
        javascript:window.history.forward(1);
        //-->
    </script>
<#--
<@sj.datepicker name="birthday" />
-->
<center>
         <font color="red">
         <@s.actionerror />
         <@s.actionmessage />
        <@s.fielderror />
        </font>
<@s.form namespace="/card-template" action="save" method="post" enctype="multipart/form-data" cssClass="required-validate">
<table border="1" cellpadding="1" cellspacing="1">
    <@s.token />
    <#assign isReuired="required " />
    <@s.if test="cardTemplate!=null">
    <#assign isReuired="" />
    <tr>
        <td><b>ID</b></td>
        <td><@s.textfield size="80" name="cardTemplate.id" readonly="true" /></td>
    </tr>
    </@s.if>
    <tr>
        <td><b>名字</b></td>
        <td><@s.textfield size="80" name="cardTemplate.name" cssClass="required max-length-80" /></td>
    </tr>
    <tr>
        <td><b>头像</b></td>
        <td>
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <tr align="center">
                    <td><b>1阶</b></td>
                    <td><b>2阶</b></td>
                    <td><b>3阶</b></td>
                    <td><b>4阶</b></td>
                <tr>
                <@s.if test="cardTemplate!=null">
                 <tr align="center">
                    <#list cardTemplate.avatars as avatar>
                        <td>
                            <img src="<@s.url value="${avatar.url}" />" height="60" width="60" />
                        </td>
                    </#list>
                 </tr>
                </@s.if>
                 <tr>
                    <td>
                        <@s.file id="avatar1" name="avatar1" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="avatar2" name="avatar2" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />:
                    </td>
                    <td>
                        <@s.file id="avatar3" name="avatar3" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="avatar4" name="avatar4" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                <tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><b>缩略图</b></td>
        <td>
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <tr align="center">
                    <td><b>1阶</b></td>
                    <td><b>2阶</b></td>
                    <td><b>3阶</b></td>
                    <td><b>4阶</b></td>
                <tr>
                <@s.if test="cardTemplate!=null">
                <tr align="center">
                    <#list cardTemplate.thumbImages as thumbImage>
                        <td><img src="<@s.url value="${thumbImage.url}" />" height="150" width="100" /></td>
                    </#list>
                </tr>
                </@s.if>
                <tr>
                    <td>
                        <@s.file id="thumbImage1" name="thumbImage1" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="thumbImage2" name="thumbImage2" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />:
                    </td>
                    <td>
                        <@s.file id="thumbImage3" name="thumbImage3" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="thumbImage4" name="thumbImage4" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                <tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><b>清晰图片</b></td>
        <td>
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <tr align="center">
                    <td><b>1阶</b></td>
                    <td><b>2阶</b></td>
                    <td><b>3阶</b></td>
                    <td><b>4阶</b></td>
                <tr>
                <@s.if test="cardTemplate!=null">
                <tr align="center">
                    <#list cardTemplate.mainImages as mainImage>
                        <td><img src="<@s.url value="${mainImage.url}" />" height="240" width="160" /></td>
                    </#list>
                </tr>
                </@s.if>
                <tr>
                    <td>
                        <@s.file id="mainImage1" name="mainImage1" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="mainImage2" name="mainImage2" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />:
                    </td>
                    <td>
                        <@s.file id="mainImage3" name="mainImage3" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                    <td>
                        <@s.file id="mainImage4" name="mainImage4" accept="image/png,image/jpeg,image/pjpeg" cssClass="${isReuired}validate-file-png-jpg" />
                    </td>
                <tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><b>星级</b></td>
        <td><@s.textfield size="80" name="cardTemplate.star" cssClass="required int-range-1-7" /></td>
    </tr>

    <tr>
        <td><b>初始生命值</b></td>
        <td><@s.textfield size="80" name="cardTemplate.hp" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td><b>初始攻击力</b></td>
        <td><@s.textfield size="80" name="cardTemplate.atk" cssClass="required validate-integer" /></td>
    </tr>

    <tr>
        <td><b>召唤机率(万分之*)</b></td>
        <td><@s.textfield size="80" name="cardTemplate.probability" cssClass="required int-range-0-10000" /></td>
    </tr>
    
    <@s.if test="cardTemplate!=null">
    <tr>
        <td><b>技能</b></td>
        <@s.if test="cardTemplate.skill!=null">
            <td><a href="<@s.url namespace="/skill" action="edit"><@s.param name="id" value="${cardTemplate.skill.id}" /></@s.url>">修改</a></td>
        </@s.if>
        <@s.else>
            <td><a href="<@s.url namespace="/skill" action="add"><@s.param name="cardTemplateId" value="${cardTemplate.id}" /></@s.url>">添加</a></td>
        </@s.else>
    </tr>

    </@s.if>
    
    <tr>
        <td><b>操作</b></td>
        <td align="right"> <@s.submit value="更新" /></td>
    </tr>
   
</table>

</@s.form>

</center>

<#include "/WEB-INF/templates/foot.ftl" />