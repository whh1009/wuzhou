<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="Include.jsp"%>
<html lang="zh-cn">
<head>
<title>五洲传播--书目修改</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="../css/jquery.tagsinput.css" />

<style type="text/css">
.row {
	margin-top: 1em;
}
.yw{
	
}
.ww {
	
}
.input-group-addon,.input-group-myaddon {
	font-weight: bold;
}

.label {
	line-height: 1.5;
}
</style>
<script type="text/javascript">
	//获取当前用户
	//var userName="<s:property value='#session.userEntity.user_name'></s:property>";
	var booksn="";
    $(function() {
    	initFormVal();
    	if($("#wenzhong").val()=="001--英文") {
    		initWenzhong('yw','001');
    	}
		if(booksn.split("_")[0]=="B"){
			initColor("1");
		} else if(booksn.split("_")[0]=="Z"){
			initColor("3");
		}  else if(booksn.split("_")[0]=="W"){
			initColor("4");
		} else {
			initColor("2");
		}
		$("#book_serial_number").val(booksn);
   		//图书编号第四部分随机数
   		$("#bsn0").val(booksn.split("_")[0]);
   		$("#bsn1").val(booksn.split("_")[1]);
   		$("#bsn2").val(booksn.split("_")[2]);
   		$("#bsn3").val(booksn.split("_")[3]);
    	$("#bsn4").val(booksn.split("_")[4]);
		$("input[name='bookEntity.book_isbn']").change(function(){
			$("#bsn2").val($(this).val());
			//设置图书编号
			$("#book_serial_number").val($("#bsn0").val()+"_"+$("#bsn1").val()+"_"+$("#bsn2").val()+"_"+$("#bsn3").val()+"_"+$("#bsn4").val());
			//初始化FTP服务器路径
			initServerPath();
		});
		
        //标签页
        $("#myTab a").click(function (e) {
            e.preventDefault();
            $(this).tab("show");
        });
        $("#book_isbn").prop("readonly",true);
        
        //设置文种不可修改
        $(".twz").prop("disabled", "disabled");
    	
    });
    
    //初始化表单的值
    function initFormVal() {
    	//var bookId = getURLPara("bookId");
    	var bookId = "<s:property value="theBookId" />";
    	$.ajax({
    		url:"wzbase/getBookEntityByBookId.action",
    		type : 'POST',
			data : {bookId:bookId},
			async : false,
			success : function(data) {
				if(data=="0") {
					document.getElementById("container").style.display='none';
					alert("服务器未获取到图书ID，请重新登录后再试");
				} else {
					var json = eval('(' + data + ')');
					booksn = json.book_serial_number;
					for (name in json) {
						if (name=='book_keyword_cn'||name=='book_keyword_english'||name=='book_keyword_foreign'||name=="book_keyword_fa"||name=="book_keyword_xi"||name=="book_keyword_e") { //关键字特殊处理
							$("*[name='bookEntity." + name + "']").importTags(json[name]);
						} else {
							if (name == "book_paper_price" || name == "book_ebook_price"||name=="book_paper_dollar_price"||name=="book_ebook_dollar_price") {
								$("*[name='bookEntity." + name + "']").val(json[name].toFixed(2));
							} else {
								$("*[name='bookEntity." + name + "']").val(json[name]);
							}
						}
					}
					//设置“数据文档--文件路径”
					$(".spath").each(function() {
						if($(this).val()=="有") {
							$(this).parent().parent().next(".col-sm-8").show();
						} else {
							$(this).parent().parent().next(".col-sm-8").hide();
						}
					});
					//初始化文种“英文”
					if($("*[name='bookEntity.book_language']").val()=="001--英文") {
						$(".ww").each(function() {
							$(this).children("input").val("");
							$(this).hide();//隐藏
						});
					}
					//初始化纸质书、电子书
					var btype=booksn.split("_")[1];//纸质书、电子书
					if(btype=="E") {
						$(".zzs").each(function() {
							$(this).hide();
						});
						$(".dzs").each(function() {
							$(this).show();
						});
					} else if(btype=="P") {
						$(".zzs").each(function() {
							$(this).show();
						});
						$(".dzs").each(function() {
							$(this).hide();
						});
					} else {
						$(".zzs").each(function() {
							$(this).show();
						});
						$(".dzs").each(function() {
							$(this).show();
						});
					}
					
					//设置左排序或右排序   //阿文，波斯语，右排序direction: rtl; unicode-bidi: bidi-override;
			    	if($("#wenzhong").val().indexOf("阿语")>-1||$("#wenzhong").val().indexOf("波斯语")>-1) {
			    		$("textarea[name='bookEntity.book_content_intr_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});			
						$("textarea[name='bookEntity.book_author_intr_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});
						$("textarea[name='bookEntity.book_editor_recommend_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});
			    	} else {
			    		$("textarea[name='bookEntity.book_content_intr_foreign']").css({"direction":"inherit"});			
						$("textarea[name='bookEntity.book_author_intr_foreign']").css({"direction":"inherit"});
						$("textarea[name='bookEntity.book_editor_recommend_foreign']").css({"direction":"inherit"});
			    	}
				}
			}
    	});
    }
    
    //获取url参数值,name 参数名
    function getURLPara(name) {
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         if(r!=null) {
        	 return  unescape(r[2]);
         } else {
        	 return "";
         }
    }
        
    function updateBook() {
    	var bookId = $("#book_serial_number").val().replace(/(^\s*)|(\s*$)/g, "");
		if(bookId.length<1) {
			alert("请输入 编号（ID）");
			$("#bookId").focus();
			return;
		}
    	var bookNameCn = $("#bookNameCN").val().replace(/(^\s*)|(\s*$)/g, "");
    	var wz = $("#wenzhong").val().replace(/(^\s*)|(\s*$)/g, "");
		var isbn = $("#book_isbn").val().replace(/(^\s*)|(\s*$)/g, "");
    	if(bookNameCn.length<1) {
    		alert("请输入中文书名");
    		$("#bookNameCN").focus();
    	}  else if(wz.length<1){
			alert("请选择文种");
			$("#wenzhong").focus();
		} else if(isbn.length < 1){
			alert("请输入ISBN/ISSN");
			$("#book_isbn").focus();
		} else {
    		if(window.confirm("确定修改吗？")){
    	    	document.form.action="updateBook.action";
    	        document.form.submit();
    	        //window.frames[0].location.reload();
    	    }
    	}
    }
    
  	//出版社类型，本社图书或者其他
	//属性pt用于标识本社或其他 中国内出版社  国外出版社
	function publishType() {
		$("input[name='publishType']").click(function() {
			var pt = $(this).val();
			if (pt == "2") {
				$(".pt").each(function() {
					$(this).hide();
				});
				$("#book_serial_number").prop("readonly", false);
				$("#refBtn").prop("disabled", true);
			} else {
				$(".pt").each(function() {
					$(this).show();
				});
				$("#book_serial_number").prop("readonly", true);
				$("#refBtn").prop("disabled", false);
			}
		});
	}
  
	//根据填充的编号获取isbn号
	//若添加的是纸书则获取电子书的信息，反之亦然
	function refByISBN() {
		var bookType = $("input[name='bookEntity.book_type']").val();//图书类型
		var book_serial_number = $("#book_serial_number").val(); //图书编号
		book_serial_number = book_serial_number.split('_')[1];
		if (book_serial_number.length > 0) {
			if (confirm("部分数据将会被覆盖，确定吗？")) {
				$.ajax({
					url : 'refByISBN.action',
					type : 'POST',
					data : {
						bookType : bookType,
						book_serial_number : book_serial_number
					},
					success : function(data) {
						if (data == '0') {
							alert("没有找到相关联的书目信息");
						} else if (data == '-1') {
							alert("获取图书类型和图书编号失败");
						} else {
							var json = eval('(' + data + ')');
							for (name in json) {
								if (name == 'book_keyword_cn' || name == 'book_keyword_english' || name == 'book_keyword_foreign') { //关键字特殊处理
									$("*[name='bookEntity." + name + "']").importTags(json[name]);
								} else {
									$("*[name='bookEntity." + name + "']").val(json[name]);
								}
							}
						}
					}
				});
			}
		}
	}
		
	//初始化FTP服务器路径
	function initServerPath() {
		var path = $("input[name='bookEntity.book_neiwen_serverpath']").val();
		var userName = path.split("/")[1];
		//$("#book_serial_number").change(function() {
			var bookName = $("#bookNameCN").val();
			if(bookName.length>0) {
				bookName="_"+bookName;
			}
			//var bsn=$("#book_serial_number").val();
			//var bsn1=$("#bsn1").val();
			var bsn=$("#book_serial_number").val();
			
			$("#neiwen_serverpath").val("/"+userName+"/"+bsn+"/排版");//内文存储路径
			$("#cover_serverpath").val("/"+userName+"/"+bsn+"/封面");//封面存储路径
			$("#font_serverpath").val("/"+userName+"/"+bsn+"/字体");//字体存储路径
			$("#pdf_publish_serverpath").val("/"+userName+"/"+bsn+"/分层PDF");//分层PDF存储路径
			$("#word_serverpath").val("/"+userName+"/"+bsn+"/WORD");
			$("#xml_serverpath").val("/"+userName+"/"+bsn+"/XML");//
			$("#epub_serverpath").val("/"+userName+"/"+bsn+"/EPUB");//
			$("#mobi_serverpath").val("/"+userName+"/"+bsn+"/MOBI");//
			$("#pdf_read_serverpath").val("/"+userName+"/"+bsn+"/阅读PDF");//
			$("#html_serverpath").val("/"+userName+"/"+bsn+"/HTML");//
			$("#contract_serverpath").val("/"+userName+"/"+bsn+"/合同");//
		//});
	}
	
	function validateForm(obj, alertMessage) {
		var objVal = $(obj).val().replace(/(^\s*)|(\s*$)/g, "");
		if(objVal=="") {
			alert(alertMessage);
			$(obj).focus();
			return false;
		} else {
			return true;
		}
	}
	var printFlag=0;
	//验证
	//打印为docx
	function printDocx() {
		printFlag=0;
		var bId = "<s:property value="theBookId" />";
		//必须验证 ISBN/ISSN、文种、书名
		if(!validateForm($("input[name='bookEntity.book_isbn']"),"请填写 ISBN/ISSN"))
			return;
		if(!validateForm($("input[name='bookEntity.book_language']"),"请填写 文种"))
			return;
		var blan = $("input[name='bookEntity.book_language']").val();//文种
		if(blan=="500--双语对应") {
			if(!validateForm($("input[name='bookEntity.book_bilingual']"),"请填写 双语对应"))
				return;
		}
		if(!validateForm($("input[name='bookEntity.book_name_cn']"),"请填写 书名（中文）"))
			return;
		
		var bq = $("#book_serial_number").val().split("_")[0];//本社或其他
		var ep = $("#book_serial_number").val().split("_")[1];//电子书或纸质书
		if(bq=="B") {
			if(blan!="001--英文") {
				if(!validateForm($("input[name='bookEntity.book_name_foreign']"),"请填写 书名（外文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_foreign']"),"请填写 著者（外文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_translator_foreign']"),"请填写 译者（外文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_series_foreign']"),"请填写 丛书名（外文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_keyword_foreign']"),"请填写 外文关键词"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_foreign']"),"请填写 内容简介（外文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_foreign']"),"请填写 作者简介（外文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_foreign']"),"请填写 编辑推荐（外文）"))
					return;
			}
			//sssss
			if(!validateForm($("input[name='bookEntity.book_clcc']"),"请填写 中图分类号"))
				return;
			//sssss
			//电子书或纸质书都需要验证
			if(!validateForm($("input[name='bookEntity.book_publish_time']"),"请填写 出版时间"))
				return;
			if(!validateForm($("input[name='bookEntity.book_editor"),"请填写 责编"))
				return;
			if(!validateForm($("input[name='bookEntity.book_name_english']"),"请填写 书名（英文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_clcc']"),"请填写 中图分类号"))
				return;
			if(!validateForm($("input[name='bookEntity.book_author']"),"请填写 著者（中文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_author_english']"),"请填写 著者（英文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_translator']"),"请填写 译者（中文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_translator_english']"),"请填写 译者（英文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_series_cn']"),"请填写 丛书名（中文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_series_english']"),"请填写 丛书名（英文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_category1']"), "请填写 建议分类1"))
				return;
			if(!validateForm($("input[name='bookEntity.book_publish_count']"), "请填写 版次"))
				return;
			if(!validateForm($("input[name='bookEntity.book_copyright_word_count']"), "请填写 版权字数"))
				return;
			if(!validateForm($("input[name='bookEntity.book_copyright_expires']"), "请填写 版权到期时间"))
				return;
			if(!validateForm($("input[name='bookEntity.book_keyword_cn']"),"请填写 中文关键词"))
				return;
			if(!validateForm($("input[name='bookEntity.book_keyword_english']"),"请填写 英文关键词"))
				return;
			if(!validateForm($("*[name='bookEntity.book_content_intr_cn']"),"请填写 内容简介（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_content_intr_english']"),"请填写 内容简介（英文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_author_intr_cn']"),"请填写 作者简介（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_author_intr_english']"),"请填写 作者简介（英文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_editor_recommend_cn']"),"请填写 编辑推荐（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_editor_recommend_english']"),"请填写 编辑推荐（英文）"))
				return;
			
			if(ep=="EP") {
				if(!validateForm($("input[name='bookEntity.book_print_count']"),"请填写 印次"))
					return;
				if(!validateForm($("input[name='bookEntity.book_size']"),"请填写 用纸尺寸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_kaiben']"), "请填写 开本尺寸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_heigh']"),"请填写 书籍厚度"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_price']"),"请填写 纸质书定价（RMB）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_cover_paper']"),"请填写 封面用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_cover_publish_color']"),"请填写 封面-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_paper']"),"请填写 插页-用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_publish_color']"),"请填写 插页-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_paper']"),"请填写 内存-用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_publish_color']"),"请填写 内文-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_zhuangzhen_class']"),"请填写 装帧-类型"))
					return;
				if(!validateForm($("input[name='bookEntity.book_zhuangzhen_type']"),"请填写 装帧-方式"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_page_count']"),"请填写 内文页数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_page_count']"),"请填写 插页页数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_publish_page_count']"),"请填写 总印张数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_design_cover_company']"),"请填写 封面设计公司"))
					return;
				if(!validateForm($("input[name='bookEntity.book_design_style_company']"),"请填写 版式制作公司"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_publish_count']"),"请填写 新闻办印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_distribute_count']"),"请填写 发行印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_count']"),"请填写 总印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_shangjiao_count']"),"请填写 上缴样书册数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_publish_soft']"),"请填写 排版软件及版本"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_neiwen_style_file']"),"请填写 内文排版文件"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_cover_style_file']"),"请填写 封面版式文件"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_publish_pdf']"),"请填写 分层PDF文件"))
					return;
				if(!validateForm($("input[name='bookEntity.book_contract']"),"请填写 图书合同归档"))
					return;
			} else if(ep=="E"){
				if(!validateForm($("input[name='bookEntity.book_contract']"),"请填写 图书合同归档"))
					return;
			} else if(ep=="P") { //纸质书
				if(!validateForm($("input[name='bookEntity.book_print_count']"),"请填写 印次"))
					return;
				if(!validateForm($("input[name='bookEntity.book_size']"),"请填写 用纸尺寸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_kaiben']"), "请填写 开本尺寸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_heigh']"),"请填写 书籍厚度"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_price']"),"请填写 纸质书定价（RMB）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_cover_paper']"),"请填写 封面用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_cover_publish_color']"),"请填写 封面-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_paper']"),"请填写 插页-用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_publish_color']"),"请填写 插页-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_paper']"),"请填写 内存-用纸"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_publish_color']"),"请填写 内文-印刷颜色"))
					return;
				if(!validateForm($("input[name='bookEntity.book_zhuangzhen_class']"),"请填写 装帧-类型"))
					return;
				if(!validateForm($("input[name='bookEntity.book_zhuangzhen_type']"),"请填写 装帧-方式"))
					return;
				if(!validateForm($("input[name='bookEntity.book_neiwen_page_count']"),"请填写 内文页数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_image_page_count']"),"请填写 插页页数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_publish_page_count']"),"请填写 总印张数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_design_cover_company']"),"请填写 封面设计公司"))
					return;
				if(!validateForm($("input[name='bookEntity.book_design_style_company']"),"请填写 版式制作公司"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_publish_count']"),"请填写 新闻办印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_distribute_count']"),"请填写 发行印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_count']"),"请填写 总印数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_news_shangjiao_count']"),"请填写 上缴样书册数"))
					return;
				if(!validateForm($("input[name='bookEntity.book_publish_soft']"),"请填写 排版软件及版本"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_neiwen_style_file']"),"请填写 内文排版文件"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_cover_style_file']"),"请填写 封面版式文件"))
					return;
				if(!validateForm($("input[name='bookEntity.book_paper_publish_pdf']"),"请填写 分层PDF文件"))
					return;
			} else {
				alert("图书编号（ID）有问题");
			}
		} else {
			if(!validateForm($("input[name='bookEntity.book_publish_time']"),"请填写 出版时间"))
				return;
			if(!validateForm($("input[name='bookEntity.book_editor"),"请填写 责编"))
				return;
			if(!validateForm($("input[name='bookEntity.book_cooperate_press"),"请填写 合作出版社"))
				return;
			if(!validateForm($("input[name='bookEntity.book_author']"),"请填写 著者（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_content_intr_cn']"),"请填写 内容简介（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_author_intr_cn']"),"请填写 作者简介（中文）"))
				return;
			if(!validateForm($("*[name='bookEntity.book_editor_recommend_cn']"),"请填写 编辑推荐（中文）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_ebook_dollar_price']"),"请填写 电子书定价（USD）"))
				return;
			if(!validateForm($("input[name='bookEntity.book_category1']"),"请填写 建议分类1"))
				return;

			if(bq=="Z") { //中国国内出版社
				if(!validateForm($("input[name='bookEntity.book_clcc']"),"请填写 中图分类号"))
					return;
				if(!validateForm($("input[name='bookEntity.book_name_english']"),"请填写 书名（英文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_english']"),"请填写 著者（英文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_english']"),"请填写 内容简介（英文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_english']"),"请填写 作者简介（英文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_english']"),"请填写 编辑推荐（英文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_name_fa']"),"请填写 书名（法文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_fa']"),"请填写 著者（法文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_fa']"),"请填写 内容简介（法文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_fa']"),"请填写 作者简介（法文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_fa']"),"请填写 编辑推荐（法文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_name_xi']"),"请填写 书名（西文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_xi']"),"请填写 著者（西文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_xi']"),"请填写 内容简介（西文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_xi']"),"请填写 作者简介（西文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_xi']"),"请填写 编辑推荐（西文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_name_e']"),"请填写 书名（阿文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_e']"),"请填写 著者（阿文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_e']"),"请填写 内容简介（阿文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_e']"),"请填写 作者简介（阿文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_e']"),"请填写 编辑推荐（阿文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_ebook_price']"),"请填写 电子书定价（RMB）"))
					return;
			} else if(bq=="W") {
				if(!validateForm($("input[name='bookEntity.book_name_foreign']"),"请填写 书名（外文）"))
					return;
				if(!validateForm($("input[name='bookEntity.book_author_foreign']"),"请填写 著者（外文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_content_intr_foreign']"),"请填写 内容简介（外文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_author_intr_foreign']"),"请填写 作者简介（外文）"))
					return;
				if(!validateForm($("*[name='bookEntity.book_editor_recommend_foreign']"),"请填写 编辑推荐（外文）"))
					return;
			}
		}
		//checkFileUpload();
		if(printFlag==1) {
			if(window.confirm("请先保存数据后再打印")){
				$("input[name='printDocx']").val("print");
				document.form.action="updateBook.action";
				document.form.submit();
			}
		}

	}

	//打印word前线验证责编是否已经上传文件
	function checkFileUpload() {
		var bId = "<s:property value="theBookId" />";
		$.ajax({
			url:"checkFileUpload.action",
			method:"post",
			data:{bId:bId},
			async:false,
			beforeSend:function(XMLHttpRequest){
				$("body").showLoading();
			},
			success:function(data) {
				$("body").hideLoading();
				if(data=="-1"){
					alert("没有找到该图书ID信息");
				} else if(data=="-2"){
					alert("未获取到图书ID");
				} else if(data=="1"){
					alert("请上传内文排版文件");
				} else if(data=="2") {
					alert("请上传封面文件");
				} else if(data=="3") {
					alert("请上传分层PDF");
				} else if(data=="4") {
					alert("请上传合同");
				} else if(data=="0") { //可以打印
					printFlag=1;
				} else {
					alert("其它错误，不能打印");
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				$("body").hideLoading();
				alert("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
			}
		});
	}
	
	function createDocxByBookId(bId) {
		$.ajax({
			url:'createDocxByBookId.action',
			type:'post',
			async: true,
			data: {bookId: bId},
			success:function(data) {
				if(data=="-1") {
					alert("未获取到图书编号");
				} else if(data=="0")  {
					alert("未获取到图书信息");
				} else {
					window.location.href="downloadWord.action?wordDownloadName="+data;
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
					window.location.href="downloadWord.action?wordDownloadName="+data;
				} else {
					//alert("生成xml错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
					var ow = window.open("","newwindow");
					ow.document.write("生成WORD文档错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
					ow.document.close();
				}
			}
		});
	}
    </script>

</head>
<body>
	<div class="container" id='container'>
		<form name="form" method="post" target='submitframe'>
			<input type="hidden" name="printDocx" value="" />
			<input type="hidden" name="bookEntity.book_id" value="<s:property value="bookEntity.book_id" />">
			<input type="hidden" name="bookEntity.user_id" value="<s:property value='bookEntity.user_id'></s:property>" />
			<input type="hidden" name="bookEntity.book_flag" value="<s:property value='bookEntity.book_flag'></s:property>" />
			<input type="hidden" name="bookEntity.book_del_flag" value="<s:property value='bookEntity.book_del_flag'></s:property>" />
			<input type="hidden" name="bookEntity.book_type" id="book_type" value="<s:property value='bookEntity.book_type'></s:property>" />
			<div class="row">
				<div class="col-sm-4"></div>
				<div class="col-sm-4"></div>
	            <div class="col-sm-4">
	                <div class="btn-group pull-right">
			            <!-- <button class="btn btn-primary" onclick="add()" disabled><span class="glyphicon glyphicon-plus-sign"></span>&nbsp;新增</button>&nbsp;&nbsp; -->
					    <a class="btn btn-primary" href="javascript:updateBook();" title=""><span class="glyphicon glyphicon-saved"></span>&nbsp;更新数据</a>
					    <a class="btn btn-primary" href="javascript:printDocx();" title=""><span class="glyphicon glyphicon-print"></span>&nbsp;后台打印</a>
	                </div>
	            </div>
			</div>
			<div class="row">
				<div class="col-sm-4">
						<div class="input-group">
							<span class="input-group-addon">编号（ID）</span>
							<input class="form-control" id="book_serial_number" name="bookEntity.book_serial_number" type="text" readonly />
						</div>
						<input type="hidden" id="bsn0" value="B" />
						<input type="hidden" id="bsn1" value="EP" />
						<input type="hidden" id="bsn2" value="" />
						<input type="hidden" id="bsn3" value="" />
						<input type="hidden" id="bsn4" value="" />
				</div>
			</div>
			<jsp:include page="BookBase.jsp"></jsp:include>
		</form>
	</div>
	
	<script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
		$(function() {
			$(".form_date").datetimepicker({
				language : "zh-CN",
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				pickerPosition : "bottom-left"
			});
		});
	</script>
	<iframe name='submitframe' id='submitframe' style="display:none" />
</body>
</html>