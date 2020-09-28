<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<link
	href="${pageContext.request.contextPath}/resource/select2/select2.min.css"
	rel="stylesheet" type="text/css">
<script
	src="${pageContext.request.contextPath}/resource/select2/select2.min.js"></script>

<link
	href="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.css"
	rel="stylesheet">
<script
	src="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.js"></script>

<style type="text/css">
.table td.fit, .table th.fit {
	white-space: nowrap;
	width: 1%;
}
</style>


</head>

<body>
	<!-- Page heading start -->
	<div class="page-head">
		<c:choose>
			<c:when test="${edit}">
				<h2 class="pull-left" style="color: #428bca;">EMPLOYEE Edit
					FORM</h2>
			</c:when>
			<c:otherwise>
				<h2 class="pull-left" style="color: #428bca;">NEW EMPLOYEE
					ENTRY FORM</h2>
			</c:otherwise>
		</c:choose>
		<div class="clearfix"></div>
	</div>
	<!-- Page heading ends -->

	<!-- Matter -->

	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">


				<div class="widget wgreen">

					<div class="widget-head">
						<div class="pull-left"></div>
						<div class="widget-icons pull-right">
							<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
							<a href="#" class="wclose"><i class="fa fa-times"></i></a>
						</div>
						<div class="clearfix"></div>
					</div>

					<div class="widget-content">
						<div class="padd">

							<br />

							<!-- Form starts.  -->
							<form:form cssClass="form-horizontal" method="POST"
								id="employeeForm" class="form-horizontal"
								modelAttribute="employeeForm"
								action="${pageContext.request.contextPath}/saveEmployee">

								<div class="form-group">
									<label class="col-lg-2 control-label">ID :</label>
									<div class="col-lg-5">
										<form:input id="id" path="id" value="${employee.id}"
											class="form-control" readonly="true" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Name <span
										class="red">*</span>:
									</label>
									<div class="col-lg-5">
										<form:input id="name" path="name" value="${employee.name}"
											class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Employee ID<span
										class="red">*</span>:
									</label>
									<div class="col-lg-5">
										<c:choose>
											<c:when test="${!empty employee}">
												<input type="text" readonly="readonly"
													value="${employee.empId}" class="form-control">
												<input type="hidden" id="empId" name="empId"
													value="${employee.empId}">
											</c:when>
											<c:otherwise>
												<form:input id="empId" path="empId"
													value="${employee.empId}" class="form-control" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Department:
									</label>
									<div class="col-lg-5">
										<form:select path="departmentId" id="departmentId"
											class="form-control select-2-field">
											<c:if test="${!empty employee}">
												<form:option value="${employee.department.id}">${employee.department.name} - (${employee.department.businessType})</form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<c:forEach items="${departmentList}" var="dept">
												<form:option value="${dept.id}">${dept.name} - (${dept.businessType})</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Sub Department:
									</label>
									<div class="col-lg-5">
										<form:select path="subDepartmentId" id="subDepartmentId"
											class="form-control select-2-field">
											<c:if test="${!empty employee}">
												<form:option value="${employee.subDepartment.id}">${employee.subDepartment.name} - (${employee.subDepartment.businessType})</form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<c:forEach items="${departmentList}" var="dept">
												<form:option value="${dept.id}">${dept.name} - (${dept.businessType})</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Designation<span
										class="red">*</span>:
									</label>
									<div class="col-lg-5">
										<form:select path="designationId" id="designationId"
											class="form-control select-2-field">
											<c:if test="${!empty employee}">
												<form:option value="${employee.designation.id}">${employee.designation.name}</form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<c:forEach items="${designationList}" var="desig">
												<form:option value="${desig.id}">${desig.name}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Point:
									</label>
									<div class="col-lg-5">
										<form:select path="pointId" id="pointId"
											class="form-control select-2-field">
											<c:if test="${!empty employee}">
												<form:option value="${employee.point.id}">${employee.point.name}-(${employee.point.pointKeyword})</form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<c:forEach items="${pointList}" var="point">
												<form:option value="${point.id}">${point.name}-(${point.pointKeyword})</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Supervisor:
									</label>
									<div class="col-lg-5">
										<form:select path="managerId" id="managerId"
											class="form-control select-2-field">
											<c:if test="${!empty employee}">
												<form:option value="${employee.manager.id}">${employee.manager.name} - (${employee.manager.empId})</form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<c:forEach items="${managerList}" var="manager">
												<form:option value="${manager.id}">${manager.name} - (${manager.empId})</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<%-- <div class="form-group">
									<label class="col-lg-2 control-label">Sort Order:
									</label>
									<div class="col-lg-5">
										<input type="number" id="sortOrder" name="sortOrder"
											value="${employee.sortOrder}" class="form-control">
									</div>
								</div> --%>

								<div class="form-group">
									<label class="col-lg-2 control-label">Status<span
										class="red">*</span>:
									</label>
									<div class="col-lg-5">
										<form:select path="status" id="status" class="form-control">
											<c:if test="${!empty employee}">
												<c:if test="${employee.status eq '1'}">
													<form:option value="${employee.status}">Active</form:option>
												</c:if>
												<c:if test="${employee.status eq '0'}">
													<form:option value="${employee.status}">Inactive</form:option>
												</c:if>
											</c:if>
											<form:option value="1">Active</form:option>
											<form:option value="0">Inactive</form:option>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Remarks :</label>
									<div class="col-lg-5">
										<textarea id="remarks" class="form-control" name="remarks"
											rows="4" type="textarea">${employee.remarks}</textarea>
									</div>
								</div>

								<div class="form-group">
									<c:choose>
										<c:when test="${edit}">
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="button"
													class="btn btn-sm btn-primary btn-success req-save-update-btn">
													Update</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<a class="btn btn-sm btn-danger"
													href="${pageContext.request.contextPath}/">Cancel</a>
											</div>
										</c:when>
										<c:otherwise>
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="button"
													class="btn btn-sm btn-primary req-save-update-btn">
													Save</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<button type="reset" class="btn btn-sm btn-danger ">Reset</button>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</form:form>
						</div>

					</div>
					<div class="widget-foot">
						<!-- Footer goes here -->
					</div>
				</div>

				<!-- Table -->

			</div>

		</div>

	</div>

	<!-- table ends -->

	<!-- Matter ends -->

	<!-- Mainbar ends -->
	<div class="clearfix"></div>


	<script type="text/javascript">
		$(document).ready(function() {
			$('.select-2-field').select2();

			$('.req-save-update-btn').click(function() {
				var flag = false;
				$(".req-save-update-btn").prop('disabled', true);

				if ($('#name').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Input Employee Name.");
					return flag;
				}

				if ($('#empId').val().trim().length > 0) {
					flag = false;

				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Input Employee ID.");
					return flag;
				}

				if ($('#designationId').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Input Designation.");
					return flag;
				}

				if ($('#status').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Input Employee Status.");
					return flag;
				}

				if (!flag) {
					$(".req-save-update-btn").prop('disabled', true);
					$('#employeeForm').submit();
				} else {
					$(".req-save-update-btn").prop('disabled', false);
					return flag;
				}
			});

		});

		$(function() {
			var month = (new Date()).getMonth() + 1;
			var year = (new Date()).getFullYear();
			// US Format
			$('input[type=eu-date1]').w2field('date', {
				format : 'yyyy-mm-dd',
				end : $('input[type=eu-date2]')
			});
			$('input[type=eu-date2]').w2field('date', {
				format : 'yyyy-mm-dd',
				start : $('input[type=eu-date1]')
			});
		});
	</script>

</body>
</html>