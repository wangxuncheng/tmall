<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<script>
$(function(){
	$("ul.pagination li.disabled a").click(function(){
		return false;
	});
	
});

</script>
	
<html lang="zh-CN">

<nav>
  <ul class="pagination">
  <input id="page" value="${page.param}" type="hidden"></input>
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?page.start=0&outsideSearchKeyword=${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>

    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?page.start=${page.start-page.count}&outsideSearchKeyword=${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&lsaquo;</span>
      </a>
    </li>    

    <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
    
    	<c:if test="${status.count*page.count-page.start<=32 && status.count*page.count-page.start>=-16}">
		    <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
		    	<a  
		    	href="?page.start=${status.index*page.count}&outsideSearchKeyword=${page.param}"
		    	<c:if test="${status.index*page.count==page.start}">class="current"</c:if>
		    	>${status.count}</a>
		    </li>
		</c:if>
    </c:forEach>

    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?page.start=${page.start+page.count}&outsideSearchKeyword=${page.param}" aria-label="Next">
        <span aria-hidden="true">&rsaquo;</span>
      </a>
    </li>
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?page.start=${page.last}&outsideSearchKeyword=${page.param}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
</html>
