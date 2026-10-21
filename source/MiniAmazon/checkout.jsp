<!DOCTYPE html>
<html>
<head>
    <title>Order Completed</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body style="background-color:#FFFACD; font-family:Arial;">
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


<!-- Mensaje de confirmación -->
<h2>Order Completed Successfully!</h2>

<!-- Mensaje de agradecimiento -->
<p>Thank you for your purchase.</p>

<!-- Enlace para seguir comprando -->
<a href="<%= request.getContextPath() %>/products">Continue Shopping</a>

</body>
</html>