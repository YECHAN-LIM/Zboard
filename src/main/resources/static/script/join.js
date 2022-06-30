const loadSajin = ()=>{
	const file = $("#sajin")[0].files[0];
	const maxSize = 1024*1024;			
	if(file.size>maxSize) {
		Swal.fire('프로필 크기 오류', '프로필 사진은 1MB를 넘을 수 없습니다','error');
		$("#sajin").val("");
		$("#show_sajin").removeAttr("src");
		return false;
	}
	const reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function() {
		$("#show_sajin").attr("src", reader.result);
	}
	return true;
}

// 아이디 체크, 이메일 체크, 비밀번호 체크, 이름 체크, 생일 체크 : 입력값을 읽어와서 비어있는 지 확인 -> 패턴을 통과하는 지 확인 후 필요하면 에러메시지 출력 -> return true
// ()value, pattern, msgElement, message) => (값, 패턴, 메시지를 출력할 요소, 메시지)
const check = (value, pattern, msgElement, message)=>{
	if(value=="") {
		msgElement.text("필수 입력입니다").attr("class", "fail");
		return false;
	}
	if(pattern.test(value)==false) {
		msgElement.text(message).attr("class", "fail");
		return false;
	}
	return true;
}

const usernameCheck = ()=>{
	// 대문자로 변환해서 출력
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	
	const pattern = /^[0-9A-Z]{8,10}$/;
	return check($username, pattern, $("#username_msg"), "아이디는 대문자와 숫자 8~10자입니다")
}

const emailCheck = ()=>{
	const $email = $("#email").val();
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return check($email, pattern, $("#email_msg"), "정확한 이메일을 입력하세요")
}

const passwordCheck = ()=>{
	$("#password_msg").text("");
	const $password = $("#password").val();
	const pattern = /^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
	return check($password, pattern, $("#password_msg"), "비밀번호는 영숫자와 특수문자 8~10자입니다")
}

const password2Check = ()=>{
	$("#password2_msg").text("");
	const $password2 = $("#password2").val();
	if($password2=="") {
		$("#password2_msg").text("필수입력입니다").attr("class","fail");
		return false;
	} 
	if($password2!==$("#password").val()) {
		$("#password2_msg").text("비밀번호가 일치하지 않습니다").attr("class","fail");
		return false;
	}
	return true;
}

const irumCheck = ()=>{
	$("#irum_msg").text("");
	const $irum = $("#irum").val();
	const pattern = /^[가-힣]{2,}$/;
	return check($irum, pattern, $("#irum_msg"), "이름은 한글 2자 이상입니다")
}

const birthdayCheck = ()=>{
	$("#birthday_msg").text("");
	const $birthday = $("#birthday").val();
	const pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	return check($birthday, pattern, $("#birthday_msg"), "정확한 생일을 입력하세요")
}

function printMessage(msgElement, message, css) {
	msgElement.text(message).attr("class",css);
}

function join() {
	const formData = new FormData($("#join_form")[0]);
	$.ajax({
		url: "/member/join",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).done(()=>Swal.fire("가입신청 완료","이메일을 확인하세요", "success"))
	.fail((msg)=>Swal.fire('가입신청 실패', msg,'error'));
}

$(document).ready(function() {
	// 이벤트와 처리함수(핸들러) 등록
	$("#sajin").on("change", loadSajin);
	$("#password").on("blur", passwordCheck);
	$("#irum").on("blur", irumCheck);
	$("#email").on("blur", emailCheck);
	$("#password2").on("blur", password2Check);
	$("#birthday").on("blur", birthdayCheck);

	const usernameUrl = "/members/username?username=";
	const emailUrl = "/members/email?email=";
	
	$("#username").on("blur", function() {
		if(usernameCheck()==false)
			return false;
		$.ajax(usernameUrl+$("#username").val())
			.done(()=>printMessage($("#username_msg"), "좋은 아이디네요", "success"))
			.fail(()=>printMessage($("#username_msg"), "사용중인 아이디입니다", "fail"));	
	});
	
	$("#email").on("blur", function() {
		if(emailCheck()==false)
			return false;
		$.ajax(usernameUrl+$("#username").val())
			.done(()=>printMessage($("#email_msg"), "사용가능한 이메일이에요", "success"))
			.fail(()=>printMessage($("#email_msg"), "사용중인 이메일입니다", "fail"));	
	});

	$("#join").on("click", function() {
		const r1 = usernameCheck();
		const r2 = passwordCheck();
		const r3 = password2Check();
		const r4 = irumCheck();
		const r5 = emailCheck();
		const r6 = birthdayCheck();
		if((r1&&r2&&r3&r4&&r5&&r6)==false)
			return false;

		$.when($.ajax(usernameUrl+$("#username").val()), $.ajax(emailUrl+$("#email").val()))
			.done(()=>join()).fail(()=>Swal.fire('확인 실패','아이디나 이메일이 사용중입니다',"error"));
	})
	
})