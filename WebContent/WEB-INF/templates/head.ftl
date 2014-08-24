<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" dir="ltr">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>管理</title>
    <link href="<@s.url value="/style/validation_style.css" />" media="screen" rel="stylesheet" type="text/css" />
    <link href="<@s.url value="/style/validation_tooltips.css" />" media="screen" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<@s.url value="/js/validation/prototype_for_validation.js" />"></script>
    <script type="text/javascript" src="<@s.url value="/js/validation/tooltips.js" />"></script>
    <script type="text/javascript" src="<@s.url value="/js/validation/effects.js" />"></script>
    <script type="text/javascript" src="<@s.url value="/js/validation/validation_cn.js" />"></script>
    <script type="text/javascript" src="<@s.url value="/js/lib/jquery.js" />"></script>

    <#assign sj=JspTaglibs["/struts-jquery-tags"]>
    <@sj.head locale="cn" />
    <script type="text/javascript">
        <!--
        function confirmDelete(deleteUrl)
        {
            if (confirm("真的要删除吗？")) {
                 document.location = deleteUrl;
            }
        }
        
        -->
    </script>
  </head>
<body>
<h3>
<a href="<@s.url value="/" />">回到首页</a>
</h3>
<br/>
<br/>