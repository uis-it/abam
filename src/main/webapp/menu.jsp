<!--<f:view
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<f:loadBundle basename="Language" var="msgs" />
	<ice:portlet styleClass="ephorte-portlet">
		<ice:form partialSubmit="true">
			<h:commandLink action="createAssignment">menu1</h:commandLink>
			<h:commandLink action="createAssignment">menu2</h:commandLink>
		</ice:form>
	</ice:portlet>
</f:view>-->
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://www.icesoft.com/icefaces/component" prefix="ice" %>

<ice:panelGroup>
	<h:commandLink action="createAssignment">menu1</h:commandLink>
	<br/>
	<h:commandLink action="createAssignment">menu2</h:commandLink>
</ice:panelGroup>