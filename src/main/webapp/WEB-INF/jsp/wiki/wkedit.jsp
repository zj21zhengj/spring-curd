<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文章</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/wiki/static/css/style.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/wiki/static/css/editormd.min.css" />
<style type="text/css">
	.thisInput {height:31px; padding: 8px;}
</style>
</head>
<body>
	<br>
	<div id="layout" style="margin-left:8px;margin-right:8px;">
	    <input type="hidden" id="pacher" value="${pageContext.request.contextPath}">
		<input type="text" class="thisInput" id="ART_TITLE" name="ART_TITLE" size="50" value="" placeholder="标题">
		<input type="text" class="thisInput" id="ART_KEYWORDS" name="ART_KEYWORDS" size="50" value="" placeholder="关键字">
		<button class="btn" onclick="javascript:saveArticle()">保存</button>
	    <button class="btn" onclick="javascript:history.go(-1)">取消</button>
	    <br><br>
	    <div class="row-fluid">
	        <div id="test-editormd">
	            <textarea style="display:none;" id="ART_CONTENT" name="ART_CONTENT"></textarea>
	        </div>
	    </div>
	</div>
	<script src="${pageContext.request.contextPath}/lib/wiki/static/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/lib/wiki/static/editormd.min.js"></script>
	<script type="text/javascript">
	var testEditor;
	var path;
    jQuery(function() {
        var markdoc = jQuery('#ART_CONTENT').val();
        path = $("#pacher").val();
        testEditor = editormd("test-editormd", {
            width: "100%",
            height: 710,
            path : 'static/lib/',
            markdown : markdoc,
            codeFold : true,
            //syncScrolling : false,
            saveHTMLToTextarea : true,    // 保存HTML到Textarea
            searchReplace : true,
            //watch : false,                // 关闭实时预览
            htmlDecode : "style,script,iframe",            // 开启HTML标签解析，为了安全性，默认不开启
            //toolbar  : false,             //关闭工具栏
            //previewCodeHighlight : false, // 关闭预览HTML的代码块高亮，默认开启
            emoji : true,
            taskList : true,
            toc             : true,         // Using [TOC]
            tocm            : false,        // Using [TOCM]
            tex : false,                   // 开启科学公式TeX语言支持，默认关闭
            flowChart : true,             // 开启流程图支持，默认关闭
            sequenceDiagram : false,       // 开启时序/序列图支持，默认关闭,
            //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
            //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
            //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
            //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
            //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
            imageUpload : true,
            imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : "./rst/img",
            atLink    : false,    // disable @link
            emailLink : false     // disable email address auto link
        });
    });
    var ajaxErrorHandle = function (XMLHttpRequest, textStatus, errorThrown) {
        if (XMLHttpRequest.status==500){
            alert(XMLHttpRequest.status + " : " + errorThrown);
        } else if (XMLHttpRequest.responseText) {
            alert(XMLHttpRequest.responseText);
        } else if (textStatus) {
            alert(textStatus);
        }
    }
    function saveArticle() {
        if (!jQuery("#ART_TITLE").val()) {
            alert('标题不能为空！');
            jQuery("#ART_TITLE").focus();
            return;
        }
        if (!testEditor.getMarkdown()) {
            alert('正文不能为空！');
            return;
        }
        var inData = {"artId":jQuery('#ART_ID').val(), "artTitle":jQuery("#ART_TITLE").val(), "artKeywords":jQuery("#ART_KEYWORDS").val(), "artContent":testEditor.getMarkdown() };
        jQuery.ajax({
            type: "POST",
            url: path+"/wiki/art",
            data: inData,
            async: false,
            error: ajaxErrorHandle,
            success: function(data) {
                console.log(data);
                if (data.code=200) {
                    alert(data.msg);
                } /* else if (data.ART_ID) {
                    jQuery('#ART_ID').val(data.ART_ID)
                } */
                if (data.code!=200) {
                    alert(data.msg);
                }
            }
        });
    }
	</script>
</body>
</html>