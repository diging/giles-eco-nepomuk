<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h2>Migrate User Data</h2>

<p>
<dl class="dl-horizontal">
  <dt>Last finished:</dt> <dd>${result.finished}</dd>
  <dt>Files:</dt> <dd>${result.migratedFiles}</dd>
</dl>
</p>

<c:url value="/admin/migrate" var="actionUrl" />

<form method="POST" action="${actionUrl}">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
<div class="form-group">
<input type="text" name="username" class="form-control">
<small>Stored usernames: ${fn:join(usernames, ',')}</small>
</div>
<button type="submit" class="btn btn-primary">Migrate</button>
</form>