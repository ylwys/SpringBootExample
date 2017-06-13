<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	    <% 
			response.setHeader("Cache-Control","no-store"); 
			response.setHeader("Pragrma","no-cache"); 
			response.setDateHeader("Expires",0); 
	    %> 
		<meta charset="UTF-8">
		<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
        <meta HTTP-EQUIV="expires" CONTENT="0"> 
		<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/demo/demo.css">
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.min.js"></script>
		<script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
		<style type="text/css">
			#fm {
				margin: 0;
				padding: 10px 30px;
			}
			
			.ftitle {
				font-size: 14px;
				font-weight: bold;
				padding: 5px 0;
				margin-bottom: 10px;
				border-bottom: 1px solid #ccc;
			}
			
			.fitem {
				margin-bottom: 5px;
			}
			
			.fitem label {
				display: inline-block;
				width: 80px;
			}
			body {
			    font-family:verdana,helvetica,arial,sans-serif;
			    /* padding:20px; */
			    font-size:12px;
			    margin:0;
            }
		</style>
		<script type="text/javascript">	
		$(document).ready(function(){
		   var level = "<%=session.getAttribute("level")%>";
		   if(level > 1){
		      $('#editTool').remove();
		   }
		});
		
		
		var url;
		function addSeal(){
			$('#dlg').dialog('open').dialog('setTitle','新增封号');
			$('#fm').form('clear');//二级表单清空
			url = 'addSeal.action';
		}
		function updateSeal(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$('#dlg').dialog('open').dialog('setTitle','编辑封号');
				$('#fm').form('load',row);//二级表单加载选中的数据
				url = 'updateSeal.action?id='+row.id;
			}
		}
		function save(){
			$('#fm').form('submit',{
				url: url,
				onSubmit: function(){
					return $(this).form('validate');
				},
				success: function(result){
					var result = eval('('+result+')');
					if (result.success){
						$('#dlg').dialog('close');		
						$('#dg').datagrid('reload');//数据表格重新读取数据	
					} else {
						$.messager.show({
							title: 'Error',
							msg: result.msg
						});
					}
				}
			});
		}
		function deleteSeal(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('Confirm','确定删除?',function(r){
					if (r){
						$.post('deleteMgr.action',{id:row.id , modelName:'MgrSeal'},function(result){
							if (result.success){
								$('#dg').datagrid('reload');	// reload the user data
							} else {
								$.messager.show({	// show error message
									title: 'Error',
									msg: result.msg
								});
							}
						},'json');
					}
				});
			}
		}
		
		function doSearch(){
			$('#dg').datagrid('load',{
				sPlayerId: $('#sPlayerId').val(),
				sTime:$('#sTime').datebox('getValue'),
			});
		}
		
	</script>
	</head>
	<body style="padding:0 0;">
	    <!-- 数据表 -->
		<table id="dg" title="封号列表" class="easyui-datagrid" style="width:1850px;height:880px"
				url="loadSeals.action"
				toolbar="#toolbar" pagination="true"
				rownumbers="true" fitColumns="true" singleSelect="true">
			<thead>
				<tr>
					<th field="playerId" width="50">玩家ID</th>
					<th field="startTimeTemp" width="50">封号开始时间</th>
					<th field="finishTimeTemp" width="50">封号结束时间</th>
				</tr>
			</thead>
		</table>
		
		<!-- 工具栏 -->
		<div id="toolbar">
		   <!-- 搜索工具 -->
		   <div id = "searchTool">
			   	<div>
					<span>玩家ID:</span>
					<input id="sPlayerId" style="line-height:26px;border:1px solid #ccc" class="easyui-numberbox" precision="0" missingMessage="必须填写整数">
					<span>日期:</span>
					<input id="sTime" style="line-height:26px;border:1px solid #ccc" class="easyui-datetimebox" data-options="required:true,showSeconds:true" missingMessage="必须填写日期">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">Search</a>
				</div>
		   </div>
		   <!-- 编辑工具 -->
		   <div id ="editTool">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addSeal()">新增封号</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateSeal()">编辑封号</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteSeal()">删除封号</a>
		   </div>
		</div>
		
		<!-- 二级表单 -->
		<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
				closed="true" buttons="#dlg-buttons">
			<div class="ftitle">活动信息</div>
			<form id="fm" method="post" novalidate>
				<div class="fitem">
					<label>玩家ID:</label>
					<input name="playerId" class="easyui-validatebox" required="true" missingMessage="必须填写字符串" >
				</div>
				<div class="fitem">
					<label>封号开始时间:</label>
					<input name="startTimeTemp" class="easyui-datetimebox" data-options="required:true,showSeconds:true" missingMessage="必须填写日期">
				</div>
				<div class="fitem">
					<label>封号结束时间:</label>
					<input name="finishTimeTemp" class="easyui-datetimebox" data-options="required:true,showSeconds:true" missingMessage="必须填写日期">
				</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
		</div>
	</body>	
</html>