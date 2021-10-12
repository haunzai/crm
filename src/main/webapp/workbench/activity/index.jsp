<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		$("#addBtn").click(function () {
			//日历拾取器
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			$.ajax({
				//发送请求的url
				url:"workbench/activity/getUserList.do",
				//不要传值，所以不需要写data
				type:"get",
				dataType:"json",
				success:function(data){
					var html="<option></option>";
					$.each(data,function(i,n){
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-owner").html(html);
					//在js中用el表达式需要使用双引号引起来
					var id="${user.id}"
					$("#create-owner").val(id);
					//处理万下拉框之后，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})
		//为保存按钮绑定事件
		$("#saveBtn").click(function(){
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
			"owner":$.trim($("#create-owner").val()),
			"name":$.trim($("#create-name").val()),
			"startDate":$.trim($("#create-startDate").val()),
			"endDate":$.trim($("#create-endDate").val()),
			"cost":$.trim($("#create-cost").val()),
			"description":$.trim($("#create-description").val()),
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.success){
						//刷新市场活动列表
                        // pageList(1,2);
                        /*
                        *   $("#activityPage").bs_pagination('getOption', 'currentPage')
                        *   操作后停留在当前页
                        *
                        *   $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                        *   操作后维持已经设计好的每页展示记录数
                        */
                        //操作后停留在第一页且维持已经设计好的每页展示记录数
                        pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//清空窗口内容
						//jquery无法使用reset方法，必须转换为dom对象才能使用
						//由于jquery中只有一个dom对象,所以数组[0]即可
						$("#activityAddForm")[0].reset();
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
					}else{
						alert("添加市场活动失败！");
					}
				}
			})
		})

		//页面加载完毕触发
		pageList(1,2);
		//为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function () {
			/*
			再点击查询时，我们应该将搜索框中的信息保存起来，保存到隐藏域中
			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);
		})

		//为全选复选框绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})

		//以下这种做法不行的
		// $("input[name=xz]").click(function () {
		// 	alert(123)
		// })

		//因为是动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		/*
		动态生成的元素应该使用on方式的形式要触发事件
		语法：
			$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {
			//找到复选框中打勾的jquery对象
			var $xz=$("input[name=xz]:checked");
			if ($xz.length==0){
				alert("请选择删除的选项");
			}else{
			    if (confirm("您确认删除吗？")){
                    var param="";
                    for (i = 0;i<$xz.length;i++){
                        param+="id="+$($xz[i]).val();
                        if (i<$xz.length-1){
                            param+="&";
                        }
                    }
                    $.ajax({
                        url:"workbench/activity/delete.do",
                        data:param,
                        type:"post",
                        dataType:"json",
                        success:function (data) {
                            if (data.success){
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                alert(param+"已删除！");
                            }else{
                                alert("删除失败！")
                            }
                        }
                    })
                }

			}
			})
        //为修改按钮绑定事件，打开修改的模态窗口
        $("#editBtn").click(function () {
            var $xz=$("input[name=xz]:checked");
            if ($xz.length==0){
                alert("请选择需要修改的记录")
            }else if ($xz.length > 1) {
                alert("仅能选择一条记录")
            }else{
                var id=$xz.val();
                $.ajax({
                    url:"workbench/activity/getUserListAndActivity.do",
                    data:{"id":id},
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        var html="<option></option>";
                        $.each(data.uList,function (i,n) {
                            html+="<option value='"+n.id+"'>"+n.name+"</option>"
                        })
                        $("#edit-owner").html(html);
                        $("#edit-id").val(data.ac[0].id);
                        $("#edit-name").val(data.ac[0].name);
                        $("#edit-owner").val(data.ac[0].owner);
                        $("#edit-startDate").val(data.ac[0].startDate);
                        $("#edit-endDate").val(data.ac[0].endDate);
                        $("#edit-cost").val(data.ac[0].cost);
                        $("#edit-description").val(data.ac[0].description);


                        $("#editActivityModal").modal("show");
                    }
                })
            }
        })
        //为更新按钮绑定事件
        $("#updateBtn").click(function () {
            $.ajax({
                url:"workbench/activity/update.do",
                data:{
                    "id":$.trim($("#edit-id").val()),
                    "owner":$.trim($("#edit-owner").val()),
                    "name":$.trim($("#edit-name").val()),
                    "startDate":$.trim($("#edit-startDate").val()),
                    "endDate":$.trim($("#edit-endDate").val()),
                    "cost":$.trim($("#edit-cost").val()),
                    "description":$.trim($("#edit-description").val())
                },
                type:"post",
                dataType:"json",
                success:function (data){
                    if (data.success){
                        alert("修改成功！");
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        $("#editActivityModal").modal("hide");
                    }else{
                        alert("修改失败！");
                    }
            }
            })
        })
	});
	/*
	点击市场活动超链接的时候
	查询
	删除，修改，增加
	点击页码组件
	 */
	//页码和每页展示的数据条数
	function pageList(pageNo,pageSize) {
		//刷新时，取消全选选项
		$("#qx").prop("checked",false);
		//查询前，将隐藏域中的数据取出来，重新赋给搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				var html="";
				$.each(data.dataList,function (i,n) {
							html +='<tr class="active">';
							html +='<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
							html +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
							html +='<td>'+n.owner+'</td>';
							html +='<td>'+n.startDate+'</td>';
							html +='<td>'+n.endDate+'</td>';
							html +='</tr>';
				})
				$("#activityBody").html(html);
                //计算总页数
                var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1
				//数据处理完毕后结合分页插件对前端展现分页相关的信息
                $("#activityPage").bs_pagination({
                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });
			}
		})
	}

</script>
</head>
<body>
	<%--隐藏域--%>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<%--data-dismiss="modal"表示关闭模态窗口--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

                        <input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
                                <%--textarea
                                    一定要以标签对的形式来呈现，正常情况下标签对要紧紧挨着
                                    取值和赋值使用val()方法
                                --%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text"  id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id=searchBtn class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">--%>
							<%--<td><input type="checkbox" /></td>--%>
							<%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
							<%--<td>2020-10-10</td>--%>
							<%--<td>2020-10-20</td>--%>
						<%--</tr>--%>
                        <%--<tr class="active">--%>
                            <%--<td><input type="checkbox" /></td>--%>
                            <%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
                            <%--<td>2020-10-10</td>--%>
                            <%--<td>2020-10-20</td>--%>
                        <%--</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

                <div id="activityPage">

                </div>
			</div>
			
		</div>
		
	</div>
</body>
</html>