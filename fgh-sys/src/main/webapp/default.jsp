<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<base href='<%=request.getScheme() +"://" +request.getServerName()+":" + request.getServerPort()%>'/>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<title>首页</title>	

<link  rel="stylesheet" href="/fgh-sys/css/common-neptune.css" >
<link  rel="stylesheet" href="/fgh-sys/extjs/resources/ext-theme-neptune/ext-theme-neptune-all.css" >

<script type="text/javascript" src="/fgh-sys/extjs/ext-all.gzjs"></script>
<script type="text/javascript" src="/fgh-sys/extjs/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="/fgh-sys/js/jquery-2.1.4.js"></script>

<script type="text/javascript">

	Ext.onReady(function(){
		Ext.MessageBox.alert('提示信息','Welcome to Extjs world');
	});
	
	
</script>
</head>
<body>

<script type="text/javascript">
// 	top.location.replace('/fgh-sys/sysindex.html');
</script>
</body>
</html>
