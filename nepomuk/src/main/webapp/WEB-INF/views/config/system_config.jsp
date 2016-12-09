<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>System Configuration</h1>

<form:form modelAttribute="systemConfigPage"
	action="${pageContext.request.contextPath}/admin/system/config"
	method="POST">

	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />

	<div class="page-header">
		<h3>Giles Ecosystem Integration</h3>
	</div>

	<div class="form-group">
		<label for="nepomukUrl">Nepomuk Base URL</label>
		<form:input type="text" class="form-control" id="nepomukUrl"
			placeholder="Nepomuk Base URL" path="nepomukUrl" value="${nepomukUrl}"></form:input>
		<small><form:errors class="error" path="nepomukUrl"></form:errors></small>
	</div>
	<div class="form-group">
		<label for="gilesAccessToken">Giles Access Token</label>
		<form:input type="text" class="form-control" id="gilesAccessToken"
			placeholder="Token for accessing Giles" path="gilesAccessToken"
			value="${gilesAccessToken}"></form:input>
		<small><form:errors class="error" path="gilesAccessToken"></form:errors></small>
	</div>

	<button class="btn btn-primary btn-md pull-right" type="submit">Save
		Changes!</button>
</form:form>