<%
    /* Cierro la sesión */
    session.invalidate();

    /* Redirijo al home */
    response.sendRedirect("index.html");
%>
