<#assign ctx=request.contextPath >
<html>
  <head>
    <meta charset="UTF-8">
    <title>shop | 后台登陆</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.4 -->
    <link href="${ctx}/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="${ctx}/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
    <!-- iCheck -->
    <link href="${ctx}/plugins/iCheck/square/blue.css" rel="stylesheet" type="text/css" />
    <style>#imgVerify{width: 120px;margin: 0 auto; text-align: center;display: block;}	</style>
    <script>    
    function detectBrowser()
    {
	    var browser = navigator.appName
	    if(navigator.userAgent.indexOf("MSIE")>0){ 
		    var b_version = navigator.appVersion
			var version = b_version.split(";");
			var trim_Version = version[1].replace(/[ ]/g,"");
		    if ((browser=="Netscape"||browser=="Microsoft Internet Explorer"))
		    {
		    	if(trim_Version == 'MSIE8.0' || trim_Version == 'MSIE7.0' || trim_Version == 'MSIE6.0'){
		    		alert('请使用IE9.0版本以上进行访问');
		    		return;
		    	}
		    }
	    }
   }
    detectBrowser();
   </script>
  <meta name="__hash__" content="35a35d71936253d091570f5dcdf3efda_36195b7c33bcc5ab73f67451e5438f65" /></head>
  <body class="login-page">
    <div class="login-box">
      <div class="login-logo">
        <a href="#"><b>shop</b></a>
      </div>
      <div class="login-box-body">
        <p class="login-box-msg">管理后台</p>
          <div class="form-group has-feedback">
            <input type="text" name="username" id="username" class="form-control" placeholder="账号" />
            <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input type="password" name="password" class="form-control" id="password" placeholder="密码" />
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
          	<opinioncontrol realtime="true" opinion_name="vertify_code" default="true">
                <div class="row" style="padding-right: 65px;">
                    <div class="col-xs-8">
                        <input style="width: 135px" type="text" id="verify" name="verify" class="form-control" placeholder="验证码"/>
                    </div>
                    <div class="col-xs-4">
                        <img id="imgVerify" style="cursor:pointer;" src="${ctx}/image/getKaptchaImage"
														alt="点击更换" title="点击更换"/>
                    </div>
                </div>
            </opinioncontrol>
          </div>
          <!-- 
          <div class="row">
            <div class="col-xs-8">
              <div class="checkbox icheck">
                <label><input type="checkbox"> 记住密码  </label>
              </div>
            </div>
            <div class="col-xs-4">
              <div class="checkbox icheck">
                <label><a href="#">找回密码</a></label>
              </div>
            </div>
          </div> -->
        <div class="form-group">
          <button type="button" class="btn btn-primary btn-block btn-flat"
                  onclick="userLogin()">立即登陆</button>
        </div>
      </div>
      
	    <div class="margin text-center">
	        <div class="copyright">
	            2014-2016 &copy; <a href="http://www.shop.cn">shop v1.3.3</a>
	            <br/>
	            <a href="http://www.shop.cn">北京506网络有限公司</a>出品
	        </div>
	    </div>
    </div><!-- /.login-box -->
    <!-- jQuery 2.1.4 -->
    <script src="${ctx}/plugins/jQuery/jQuery-2.1.4.min.js" type="text/javascript"></script>
    <!-- Bootstrap 3.3.2 JS -->
    <script src="${ctx}/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- iCheck -->
    <script src="${ctx}/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
	<script src="${ctx}/js/layer/layer.js"></script>


  <script type="text/javascript">

    $("#imgVerify").on("click", function () {
      var url = "${ctx}" + "/image/getKaptchaImage?time=" + new Date();
      $(this).attr("src", url);
    });


    // 用户登录
    function userLogin() {
      $.ajax({
        url: "${ctx}/user/login",
        type: "POST",
        data: {
          userName: $("#username").val(),
          password: $("#password").val(),
          verify: $("#verify").val()
        },
        dataType: "JSON",
        success: function (result) {
          if (200 == result.code) {
            location.href = "${ctx}/index";
          } else {
            //layer.alert("用户名或密码错误，请重新输入！");
            alert(result.message);
          }
        },
        error: function () {
          alert("亲，系统正在升级中，请稍后再试！");
        }
      });
    }
  </script>
  </body>
</html>