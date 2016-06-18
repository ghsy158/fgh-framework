<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<base href='<%=request.getScheme() +"://" +request.getServerName()+":" + request.getServerPort()%>'/>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />

<script type="text/javascript" src="/fgh-sys/js/jquery-2.1.4.js"></script>

<title>首页</title>	
</head>

<body>
<h2>这是首页</h2>

<script type="text/javascript">

$.post("http://localhost:8088/fgh-sys/getList.json",{},function(result){
	debugger;
  });
  
</script>
</body>
</html>