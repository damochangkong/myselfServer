<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<title>测试</title>
  </head>
  
  <body>
  
  	<div class="box" onclick="register()">register</div>
  	<br/>
  	<div class="box" onclick="login()">login</div>
  	<br/>
  	<div class="box" onclick="logout()">logout</div>
  	<br/>
    <div class="box" onclick="syncInfo()">sync</div>
    <br/>
    <div class="box" onclick="uploadNoteInfo()">upload</div>
    <br/>
    <div class="box" onclick="postSimpleData()">test</div>
    <br/>
  </body>
</html>
<style>
.box{
	width:30px;
	height:30px;
	border:1px solid;
}
</style>
<script type="text/javascript" src="http://script.suning.cn/public/js/jquery.1.7.2.js"></script>
<script type="text/javascript">

	function logout() {
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/spiderNote/login/logout",
	        contentType: "application/x-www-form-urlencoded; charset=UTF-8", //	必须有
	        dataType: "json", //	表示返回值类型，不必须
	        data: {
	        	userId: "10001",
	   			signature:"6445e79f898460811e321f8a82888273"
	        },
	        success: function (jsonResult) {
	            console.log(jsonResult);
	        }
	    });
	}

	function login() {
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/spiderNote/login/login",
	        contentType: "application/x-www-form-urlencoded; charset=UTF-8", //	必须有
	        dataType: "json", //	表示返回值类型，不必须
	        data: {
	        	userId: "dafenggang",
	    		password:"123456",
	   			deviceId :"11111122222",
	   			signature:"6445e79f898460811e321f8a82888273"
	        },
	        success: function (jsonResult) {
	            console.log(jsonResult);
	        }
	    });
	}

	function register() {
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/spiderNote/login/register",
	        contentType: "application/x-www-form-urlencoded; charset=UTF-8", //	必须有
	        dataType: "json", //	表示返回值类型，不必须
	        data: {
	        	userName: "776301577@qq.com",
        		password:"aa",
       			deviceId :"6445e79f898460811e321f8a82888273"
	        },
	        success: function (jsonResult) {
	            console.log(jsonResult);
	        }
	    });
	}

	function postSimpleData() {
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/spiderNote/note/test",
	        contentType: "application/x-www-form-urlencoded; charset=UTF-8", //	必须有
	        dataType: "json", //	表示返回值类型，不必须
	        data: { 'note': 'foovalue'},
	        success: function (jsonResult) {
	            alert(jsonResult);
	        }
	    });
	}

	function syncInfo(){
		var noteInfoJson= '{userId:"user0001",deviceId:"device0001",sectionSyncTimestamp:"2016-08-10 16:58:23",notes:[{"noteId":"pro0002","syncTimestamp":"2016-08-10 16:58:23","modifyFlag":0}]}';
		$.ajax({
		    type : "POST",
		    url : "http://localhost:8080/spiderNote/note/syncProjects",
		    data : {
		    	projectInfo : noteInfoJson,
		    	token : "sssss"
		    },
		    dataType : "json",
		    json:"callback",
		    async : true,
		    success : function(data) {
		    	console.log(data);
		    }
		});
	}

	function uploadNoteInfo(){
		//修改段落
		var noteInfoJson= '{"userId":"user0001","deviceId":"device0001","notes":[],"sections":[{"id":"section2001","userId":"user0001","noteId":"pro0002","undocFlag":"0","sectionType":"1","updateAt":"2016-08-26T08:29:18.476Z","deleteFlag":"0","audioId":{},"text":"","imageIds":[{"id":"image2099","imageUrl":"imageUrl2001","imageTagIds":[{"id":"imageTag20099","tagType":"0","directionX":"30%","directionY":"30%","text":"word tag","sourceUrl":"","duration":"","isLeft":"1"}]}]},{"id":"section4002","userId":"user0001","noteId":"pro0002","undocFlag":"0","sectionType":"2","updateAt":"2016-08-26T08:29:18.476Z","deleteFlag":"0","audioId":{"id":"audio4999","audioUrl":"audioUrl3002","duration":"03:39","audioTagIds":[{"id":"audioTag4999","tagType":"0","text":"section4001 audio4001 audioTag4001","imageUrl":"","timePoint":"03:30"}]},"imageIds":[],"text":""}]}';
		//删除段落
		//var noteInfoJson= '{"userId":"user0001","deviceId":"device0001","notes":[],"sections":[{"id":"section2001","userId":"user0001","noteId":"pro0002","undocFlag":"0","sectionType":"1","updateAt":"2016-08-26T08:29:18.476Z","deleteFlag":"1","audioId":{},"text":"","imageIds":[]},{"id":"section4002","userId":"user0001","noteId":"pro0002","undocFlag":"0","sectionType":"2","updateAt":"2016-08-26T08:29:18.476Z","deleteFlag":"1","audioId":{},"imageIds":[],"text":""}]}';
		$.ajax({
		    type : "POST",
		    url : "http://localhost:8080/spiderNote/note/uploadProjects",
		    data : {
		    	projectInfo : noteInfoJson
		    },
		    dataType : "json",
		    json:"callback",
		    async : true,
		    success : function(data) {
		    	console.log(data);
		    }
		});
	}
</script>