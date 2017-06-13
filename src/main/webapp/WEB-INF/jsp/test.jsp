<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%--   <title>Basic CRUD Application - jQuery EasyUI CRUD Demo</title>--%>
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/color.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/demo/demo.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
</head>
<body>
<!--数据表-->
<table id="dg" title="My Users" class="easyui-datagrid" style="width:1850px;height:880px"
       url="/example/findAllUser"
       toolbar="#toolbar" pagination="true"
       rownumbers="true" fitColumns="true" singleSelect="true">
    <thead>
    <tr>
        <th field="account" width="50">account</th>
        <th field="password" width="50">password</th>
        <th field="level" width="50">level</th>
    </tr>
    </thead>
</table>
<!--工具栏-->
<div id="toolbar">
    <!-- 搜索工具 -->
    <div id="searchTool">
        <div>
            <span>玩家ID:</span>
            <input id="sPlayerId" style="line-height:26px;border:1px solid #ccc" class="easyui-numberbox" precision="0"
                   missingMessage="必须填写整数">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">Search</a>
        </div>
    </div>
    <!-- 编辑工具 -->
    <div id="editTool">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="newUser()">新增</a>
        <%-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">Edit
             User</a>--%>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="destroyUser()">删除</a>
    </div>
</div>
<!--二级对话框-->
<div id="dlg" class="easyui-dialog" style="width:400px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" novalidate style="margin:0;padding:20px 50px">
        <div style="margin-bottom:20px;font-size:14px;border-bottom:1px solid #ccc">User Information</div>
        <div style="margin-bottom:10px">
            <input name="account" class="easyui-textbox" required="true" label="account:" style="width:100%">
        </div>
        <div style="margin-bottom:10px">
            <input name="password" class="easyui-textbox" required="true" label="password:" style="width:100%">
        </div>
        <div style="margin-bottom:10px">
            <input name="level" class="easyui-textbox" required="true" label="level:" style="width:100%">
        </div>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveUser()" style="width:90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')" style="width:90px">Cancel</a>
</div>



<script type="text/javascript">
    var url;
    function newUser() {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'New User');
        $('#fm').form('clear');
        url = '/example/addUser';
    }
    function editUser() {
        var row = $('#dg').datagrid('getSelected');
        if (row) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit User');
            $('#fm').form('load', row);
            url = '/example/updateUser?id=' + row.id;
        }
    }
    function saveUser() {
        $('#fm').form('submit', {
            url: url,
            onSubmit: function () {
                return $(this).form('validate');
            },
            success: function (result) {
                var result = eval('(' + result + ')');
                if (result.success) {
                    $('#dlg').dialog('close');        // close the dialog
                    $('#dg').datagrid('reload');    // reload the user data
                } else {
                    $.messager.show({
                        title: 'Error',
                        msg: result.msg
                    });
                }
            }
        });
    }
    function destroyUser() {
        var row = $('#dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', 'Are you sure you want to destroy this user?', function (r) {
                if (r) {
                    $.post('/example/delUser', {id: row.id}, function (result) {
                        if (result.success) {
                            $('#dg').datagrid('reload');    // reload the user data
                        } else {
                            $.messager.show({    // show error message
                                title: 'Error',
                                msg: result.msg
                            });
                        }
                    }, 'json');
                }
            });
        }
    }
</script>
</body>
</html>