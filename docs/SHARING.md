# MiniAmazon demo tour

MiniAmazon is an academic e-commerce project by **Miguel A. De Jesus**, presented through Polo Studios. The public site is a browser adaptation; the Java/JSP/MySQL application runs separately.

## Suggested 45–60 second walkthrough

1. Start on Shop and show the responsive storefront and category filters.
2. Search for a product and open its details. Show the description, price, available stock, and quantity selection.
3. Add it to the cart, change the quantity, and review the exact item total.
4. Place a **sample order**, then show the confirmation and saved order history.
5. Briefly show the demo admin view and its inventory controls.
6. End with the GitHub repository and the separate Java/JSP/MySQL implementation.

## Engineering points to discuss

- Cart quantities cannot exceed available stock; money is calculated in integer cents.
- Checkout records immutable order items and reduces inventory.
- Browser changes are saved as a complete candidate state before being shown as successful. Failed writes leave the previous cart, stock, and order history intact.
- Demo inventory changes preserve historical order details.
- Light/dark themes, keyboard focus, native dialogs, and responsive layouts support the shopping flow.

## Scope to describe accurately

No real payment, shipping, or fulfillment takes place. The browser demo's admin switch demonstrates the interface, not authentication. Data is saved on the visitor's device. The original server application uses Java/JSP/JDBC/MySQL and has separate setup instructions in the repository README.

Use the deployed app for current screenshots. Before recording, keep any demo data you want to retain; the **Reset demo data** action in admin view clears the current browser's local cart, orders, and edits after confirmation.
