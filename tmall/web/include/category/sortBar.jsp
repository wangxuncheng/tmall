<!-- 模仿天猫整站j2ee 教程 为how2j.cn 版权所有-->
<!-- 本教程仅用于学习使用，切勿用于非法用途，由此引起一切后果与本站无关-->
<!-- 供购买者学习，请勿私自传播，否则自行承担相关法律责任-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
var b = true;
$(function(){
	
	$("input.sortBarPrice").keyup(function(){
		var num= $(this).val();
		if(num.length==0){
			$("div.productUnit").show();
			return;
		}
			
		num = parseInt(num);
		if(isNaN(num))
			num= 1;
		if(num<=0)
			num = 1;
		$(this).val(num);		
		
		
		var begin = $("input.beginPrice").val();
		var end = $("input.endPrice").val();
		if(!isNaN(begin) && !isNaN(end)){
			console.log(begin);
			console.log(end);
			$("div.productUnit").hide();
			$("div.productUnit").each(function(){
				var price = $(this).attr("price");
				price = new Number(price);
				
				if(price<=end && price>=begin)
					$(this).show();
			});
		}
		
	});
	$("#p1").mouseenter(function(){
		$(".jg").css("visibility","visible");
	});
	$(".jg").mouseenter(function(){
		$(".jg").css("visibility","visible");
	});
	$(".jg").mouseleave(function(){
		$(".jg").css("visibility","hidden");
	});
	$("#p1").mouseleave(function(){
		$(".jg").css("visibility","hidden");
	});
});
</script>
<style>
table td {
    font-size: 12px;
    width: 50px;
}
.jg{
	visibility:hidden;
	position: relative;
    left: 209px;
    margin: 10px;
    top: -24px;
    background-color: #f3f3f3;
    width: 50px;
}

div.categoryProducts {
    position: relative;
    top: -40px;
    padding: 0px 20px 40px 20px;
}
</style>
<div>

<div class="categorySortBar">
	

	<table class="categorySortBarTable categorySortTable">
		<tr>
			<td <c:if test="${'all'==param.sort||empty param.sort}">class="grayColumn"</c:if>> <a href="?cid=${c.id}&sort=all">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${'review'==param.sort}">class="grayColumn"</c:if> ><a href="?cid=${c.id}&sort=review">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${'date'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=date">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${'saleCount'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=saleCount">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td class="pp" id="p1" <c:if test="${'price'==param.sort}">class="grayColumn"</c:if>>
				<a href="#">价格<span  class="glyphicon glyphicon-resize-vertical"></span></a>
				
			</td>
			
		</tr>
		
	</table>
	
	
	<table class="categorySortBarTable">
		<tr>
			<td><input class="sortBarPrice beginPrice" type="text" placeholder="请输入"></td>
			<td class="grayColumn priceMiddleColumn">-</td>
			<td><input class="sortBarPrice endPrice" type="text" placeholder="请输入"></td>
		</tr>
	</table>
	
</div>
	<div class="jg">	
	<ui  style="list-style-type:none">
		<li><a href="?cid=${c.id}&sort=dprice">从高到低</a>
		</li>
		<li><a href="?cid=${c.id}&sort=price">从低到高</a>
		</li>
	</ui>
	</div>
</div>