<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
	table,th,td {
		border:1px solid black;
		border-collapse:collapse;
		padding:5px 10px;
	}

	table, input[type='text'] {
			width:100%;
		}
		
	textarea {
		width: 100%;
		height: 150px;
		resize: none;
	}
	
	#editable{
		width: 99%;
		height: 500px;
		border: 1px solid gray;
		margin: 5px;
		overflow: auto;
		padding: 5px;
		text-align: left;
	}
	
	img{
		cursor: pointer;
	}
	
	img:hover {
		opacity: 0.5;
	}

</style>
</head>
<body>
	<h3>모임 후기 작성</h3>
	<form action="groupReviewUpdate" method="POST">
		<table>
			<tr>
				<th>나의 모임</th>
				<td>
					<fieldset style="width:40%; float:left;">
					<legend>번개모임</legend>
					<c:forEach items="${lightGroupList}" var="lightGroupList">
							<input type="radio" name="idx" value="${lightGroupList.lightning_no}" <c:if test="${dto.lightning_no eq lightGroupList.lightning_no}">checked</c:if>/>
							${lightGroupList.lightning_title} (${lightGroupList.class_name}) 
							<input type="hidden" name="class_no" value="${lightGroupList.class_no}"><br/>
					</c:forEach>
					</fieldset>
					<fieldset style="width:40%; float:left;">
						<legend>도장깨기</legend>
						<c:forEach items="${groupList}" var="groupList">
								<input type="radio" name="idx" value="${groupList.dojang_no}" <c:if test="${dto.dojang_no eq groupList.dojang_no}">checked</c:if>/>
								${groupList.dojang_title} (${groupList.class_name}) 
								<input type="hidden" name="class_no" value="${groupList.class_no}"><br/>
						</c:forEach>
					</fieldset>
				

					<%-- <c:forEach items="${groupList}" var="groupList">
						<input type="radio" name="idx" value="${groupList.group_no}" <c:if test="${dto.group_no eq groupList.group_no}">checked</c:if>/>
							${groupList.title} (${groupList.class_name}) 
						<input type="hidden" name="class_no" value="${groupList.class_no}"><br/>
					</c:forEach> --%>
	
					<%-- <input type="text" id="title" style="width:30%" placeholder="검색버튼을 눌러주세요" readonly/>
					<!-- <p id="groupName"></p> -->
					${joinGroup.title}
					<!-- <input type="button" value="검색" id="groupSearch"/> -->
					<input type="hidden" id="lightning_no" name="" value=""/>
					<input type="hidden" id="dojang_no" name="" value=""/>
					<input type="button" value="검색" onclick="groupSearchPop()"/>
					<!-- <input type="button" value="초기화" id="groupReset" onclick="groupReset()"/> --> --%>
				</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td><input type="text" name="member_id" value="${sessionScope.loginId}" style="width:20%" readonly/></td>
			</tr>
			<tr>
				<th>글 제목</th>
				<td><input type="text" name="review_title" id="review_title" value="${dto.review_title}"/></td>
			</tr>
			<tr>
				<th>내용</th>
				<td id="reviewContents">
					<!-- html 태그를 인식하기 위해 div 사용(type="text"나 textarea는 html을 그냥 글자취급) -->
					<div id="editable" contenteditable="true" value="${dto.review_content}">${dto.review_content}</div>
					<!-- 하지만 div 는 서버에 값을 전송할 수가 없다. -->
					<!-- 결국엔 div의 내용을 input에 담아 서버에 전송할 예정 -->
					<input type="hidden" name="review_content" id="review_content"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="button" value="파일 업로드" onclick="fileUp()"/>
				</td>
			</tr>
			<tr>
				<th colspan="2">
					<button type="button" onclick="location.href='groupReviewDetail.do?groupReview_no=${dto.groupReview_no}'">취소</button>
					<button type="button" onclick="save()">저장</button>
				</th>
			</tr>
		</table>
	</form>	
	
</body>
<script>

//모임 검색 팝업
/* function groupSearchPop(){
	window.open("/groupSearchPop.go","new","width=500, height=400, left=400 ,top=200, resizable=no, scrollbars=no, status=no, location=no, directories=no;");
} */
var radioChk = $('input[type="radio"]').val();
var groupNum = "${dto.group_no}";



//글 업로드
function save(){
	
	var review_title = $('#review_title').val();
	var review_content = $('#editable').text();
	
	if($('input[type="radio"]:checked').is(":checked") == false){
		alert("모임을 선택해 주세요");
	} else if(review_title == "") {
		alert("제목을 입력해주세요");
		review_title.focus();
	} /* else if(review_content == "") { //여기 아직 안됨...
		alert("내용을 입력해주세요.");
		//review_content.focus();
	} */ else if($('img').length > 3) {
		alert('이미지 업로드 제한 갯수를 초과했습니다.');
	} else {
		$('#review_content a').removeAttr('onclick');
		//id가 content인 태그의 자식태그 a 태그에서 onclick 속성 삭제
		$('#review_content').val($('#editable').html());
		//content 안에 editable 넣음
		$('form').submit();
	}
	
}

//파일업로드 팝업
function fileUp(){
	window.open('grFileUploadForm.go','','width=400, height=100');
}

//사진 삭제
function del(elem){
	console.log(elem);
	//id에서 삭제할 파일명을 추출
	var id = $(elem).attr("id");
	var fileName = id.substring(id.lastIndexOf("/")+1);
	console.log(fileName);
	//해당 파일 삭제 요청
	$.ajax({
		url:'grFileDelete.ajax',
		type:'get',
		data:{'fileName':fileName},
		dataType:'json',
		success:function(data){
			console.log(data)
			//a 태그를 포함한 img 태그를 삭제
			$(elem).remove();
		},
		error:function(e){
			console.log(e);
		}
	});
}

</script>
</html>