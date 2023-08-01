$(function(){
    $('#datepicker').datepicker();
    $(".timepicker").timepicker({
            timeFormat: 'HH:mm',
            interval: 30,
            minTime: '9',
            maxTime: '18',
//            defaultTime: '9', //페이지 렌더링 되었을 때 기본값
            startTime: '9',
            dynamic: false,
            dropdown: true,
            scrollbar: true
     });

    let userEmails = []; //유저의 이메일을 담을 배열
    let userEmailsIndex = 0;

    /*사람 추가*/
    $('#addBtn').on("click", function() {
        let userEmail = $(".userEmail").val();
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; //이메일 정규표현식

        if(userEmails.includes(userEmail)){ //중복 추가하는 경우
            alert("이미 추가한 유저입니다.");
            return;
        }else if(emailRegex.test(userEmail)){ //정규표현식 조건에 부합한 경우
            $(".selectedUserList").append("<li class='userEmails' data-userIndex=" + userEmailsIndex + ">" + userEmail + "<span class='removeUser'>ⓧ</span>" + "</li>");
            userEmails.push(userEmail);
            $(".userEmail").val("");
            console.log(userEmailsIndex);
            userEmailsIndex++;
        }else{ //정규표현식 조건에 부합하지 않은 경우
            alert("이메일 형식이 아닙니다.");
        }
        console.log(userEmails);
    });
    /*메일 전송 버튼*/
    $('#reservate').on("click", function(){
        let meetTitle = $('.meetTitle').val(); //회의제목
        let meetRoom = $('.meetRoom').val(); //회의실
        let selectedOption = $('.selectOption').val(); //예약옵션
        let meetContent = $('.meetContent').val(); //회의내용
        let setDate = $('#datepicker').val(); //회의날짜
        let startTime = $('.startTime').val(); //회의 시작시간
        let endTime = $('.endTime').val(); //회의 종료시간

        if(meetTitle == "" || meetContent == "" || setDate == "" || startTime == "" || endTime == ""){
            alert("모든 값을 입력해주세요.");
            return false;
        }

        const param = {
            "mailType" : selectedOption,
            "receiveMail" : userEmails,
            "meetRoom" : meetRoom,
            "meetDate" : setDate,
            "startTime" : startTime,
            "endTime" : endTime,
            "meetTitle" : meetTitle,
            "meetContent" : meetContent
        }

        $.ajax({
            url: "/sendEmailWithJsp",
            type: "POST",
            data: JSON.stringify(param),
            dataType: "json",
            contentType : "application/json; charset=UTF-8",
            success: function(){
                console.log("성공");
            },
            error: function(){
                console.log("실패");
            }
        })
        console.log(param);
    })

 })
    //유저 목록에서 유저 삭제
$('.removeUser').click(function(){
    alert("아아아안녕?");
//        console.log(target);
//        let findParent = $(target).parent();
//        console.log(findParent);
//        let targetIndex = parseInt(findParent.data("userIndex"));
//        console.log(targetIndex);
//        userEmails.splice(targetIndex, 1);
//        console.log(userEmails);
//    findParent.remove();
})

 $.datepicker.setDefaults({
   dateFormat: 'yy-mm-dd',
   prevText: '이전 달',
   nextText: '다음 달',
   monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
   monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
   dayNames: ['일', '월', '화', '수', '목', '금', '토'],
   dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
   dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
   showMonthAfterYear: true,
   yearSuffix: '년'
 });
