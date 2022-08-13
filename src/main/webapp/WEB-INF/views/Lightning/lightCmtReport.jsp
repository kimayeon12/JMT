<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<title>번개 모임 댓글 신고</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
	table {
	    width:70%; 
	    margin-left:auto; 
	    margin-right:auto;
	    position: static;
	}
</style>
</head>
<body>
		<table>
			<tr>
				<th>사유 선택</th>
			</tr>
			<tr>
				<td>	
					<input type="hidden" id ="comment_no" value="${comment_no}"/>
					<input type="radio" name ="report_reason" value="욕설 및 비방"/>욕설 및 비방
				</td>
			</tr>
			<tr>
				<td><input type="radio" name ="report_reason" value="광고 및 홍보"/>광고 및 홍보</td>
			</tr>
			<tr>
				<td><input type="radio" name ="report_reason" value="부적절한 컨텐츠"/>부적절한 컨텐츠</td>
			</tr>
			<tr>
				<td>
					<input type="radio" name ="report_reason"  value="기타"/>
					<textarea placeholder="기타(직접작성)" style = "width:200px; height:30px; resize:none;"  disabled></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<input type="button" value="취소" onclick="lightCmtReportclose()"/>
					<input type="button" value="신고" onclick="checkCmtReport()"/>
				</th>
			</tr>
		</table>
</body>
<script>
	
	//"기타" 선택 시 textarea 활성화
	$('input[name="report_reason"]').on("change",function(){	
		if($('input[name="report_reason"]:checked').val()=="기타"){
			$('textarea').attr("disabled",false);
		}else{
			$('textarea').val("");
			$('textarea').attr("disabled",true);
		}
	})

	//글자 수 100자 제한
	$('textarea').keyup(function(){
		  var content = $(this).val();
		  if (content.length > 100){
		    alert("최대 100자까지 입력 가능합니다.");
		    $(this).val(content.substring(0, 100));
		  }
	});
	
	
	//유효성 검사
	function checkCmtReport(){
		
		var comment_no = $("#comment_no").val();
		var report_reason = $('input[name="report_reason"]:checked').val();
		var report_reason_text = $('textarea').val();
		
		
		if(!$('input[name="report_reason"]').is(":checked")){
			alert("신고 사유를 선택해 주세요");
		}else if($('input[name="report_reason"]:checked').val()=="기타" && $("textarea").val()==""){
			alert("신고 사유를 입력해 주세요");
			$("textarea").focus();
		}else{
			$.ajax({
				type:'get',
				url:'lightCmtReport.ajax',
				data:{
					'comment_no' : comment_no,
					'report_reason' : report_reason,
					'report_reason_text' : report_reason_text
				},
				dataType:'JSON',
				success:function(data){
					if(data.lightCmtReport){
						opener.parent.location.reload();
						window.close();
					}else{
						alert("신고가 접수되지 않았습니다.");
					}
				},
				error:function(e){
					console.log(e);
				}
			});
		
		}	
	}
	

	function lightCmtReportclose() {
		opener.parent.location.reload();
		window.close();
	}

</script>
</html>
				