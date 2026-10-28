import { Store, money, validImage } from "./store.js?v=polish-2";
import { saveChange } from "./persistence.js?v=polish-2";
const $ = (s) => document.querySelector(s),
  KEY = "polo-mini-amazon-demo-v1";
const esc = (s) =>
  String(s ?? "").replace(
    /[&<>"']/g,
    (c) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        c
      ],
  );
let store,
  category = "All",
  sort = "featured",
  admin = false,
  timer,
  lastOrder = null,
  editorImage = "assets/placeholder.svg",
  imageBusy = false,
  imageVersion = 0;
const categories = [
  "Electronics",
  "Clothing and Accessories",
  "Home and Kitchen",
  "Beauty and Personal Care",
  "Sports and Fitness",
  "Toys and Games",
  "Books and Education",
  "Automotive",
];
function notify(message) {
  $("#toast").textContent = message;
  $("#toast").classList.add("visible");
  clearTimeout(timer);
  timer = setTimeout(() => $("#toast").classList.remove("visible"), 3200);
}
// Commit changes only after storage accepts the complete candidate state.
function commit(change) {
  try {
    const next = saveChange(
      store,
      change,
      { setItem: (key, value) => localStorage.setItem(key, value) },
      KEY,
    );
    store = next.store;
    $("#save-status").hidden = true;
    return next.result;
  } catch (error) {
    if (error.code === "SAVE_FAILED") {
      $("#save-status").textContent = error.message;
      $("#save-status").hidden = false;
    }
    throw error;
  }
}
function available(p) {
  return Math.max(0, p.stock - (store.cart[p.id] || 0));
}
function route() {
  const r = location.hash.slice(1);
  return ["shop", "cart", "orders", "admin"].includes(r) ? r : "shop";
}
function image(p) {
  return esc(validImage(p.image) ? p.image : "assets/placeholder.svg");
}
function productCard(p) {
  return `<article class="product-card"><button class="image-button" data-detail="${p.id}" aria-label="View ${esc(p.name)}"><img src="${image(p)}" alt="${esc(p.name)}" loading="lazy"></button><div class="body"><p class="category">${esc(p.category)}</p><h3><button class="text-button" data-detail="${p.id}">${esc(p.name)}</button></h3><p class="description">${esc(p.description)}</p><button class="text-button view-details" data-detail="${p.id}" aria-label="View details for ${esc(p.name)}">View details →</button><div class="purchase"><div class="price">${money(p.cents)}</div><p class="stock">${p.stock ? p.stock + " in stock" : "Out of stock"}</p><button class="add" data-add="${p.id}" ${available(p) < 1 ? "disabled" : ""}>${!p.stock ? "Sold out" : available(p) < 1 ? "All in cart" : "+ Add to cart"}</button><p class="in-cart">${store.cart[p.id] ? store.cart[p.id] + " in your cart" : ""}</p></div></div></article>`;
}
function empty(title, text) {
  return `<div class="empty"><h2>${title}</h2><p>${text}</p><a class="button" href="#shop" data-reset-search>Explore products</a></div>`;
}
function shop() {
  const query = $("#search").value.trim().toLowerCase();
  let products = store.products.filter(
    (p) =>
      (category === "All" || p.category === category) &&
      [p.name, p.description, p.category].some((s) =>
        s.toLowerCase().includes(query),
      ),
  );
  if (sort === "low") products.sort((a, b) => a.cents - b.cents);
  if (sort === "high") products.sort((a, b) => b.cents - a.cents);
  if (sort === "name") products.sort((a, b) => a.name.localeCompare(b.name));
  $("#main").innerHTML =
    `${!query && category === "All" ? `<section class="hero"><div><p class="eyebrow">THE MINIAMAZON PROJECT</p><h1>Your everyday finds.<br>One little storefront.</h1><p>Explore the original project catalog, build a cart, and follow a sample order from start to finish.</p><a class="button" href="#catalog">Explore the catalog <span aria-hidden="true">↘</span></a></div><div class="hero-art" aria-hidden="true"><img src="assets/hp_laptop.jpg" alt=""><img src="assets/dior_perfume.jpg" alt=""></div></section><div class="shop-benefits" aria-label="Demo features"><span>Browse & discover</span><span>Build your cart</span><span>Try a sample checkout</span></div>` : ""}<section id="catalog"><div class="toolbar"><div><h2>${query ? "Search results" : "Explore the catalog"}</h2><p>${products.length} product${products.length === 1 ? "" : "s"}${query ? " matching “" + esc(query) + "”" : ""}</p></div><label><span>Sort by </span><select id="sort"><option value="featured">Featured</option><option value="low">Price: low to high</option><option value="high">Price: high to low</option><option value="name">Name: A–Z</option></select></label></div><div class="category-tabs" role="group" aria-label="Filter products by category">${["All", ...categories].map((c) => `<button data-category="${esc(c)}" aria-pressed="${category === c}">${esc(c)}</button>`).join("")}</div>${query || category !== "All" ? '<button class="search-clear" id="clear-filters">Clear search & filters</button>' : ""}${products.length ? '<div class="products">' + products.map(productCard).join("") + "</div>" : empty("No matches this time.", "Try a different keyword or clear your filters to see all products.")}</section>`;
  $("#clear-filters")?.addEventListener("click", () => {
    $("#search").value = "";
    category = "All";
    shop();
    $("#search").focus();
  });
  $("#sort").value = sort;
  $("#sort").onchange = (e) => {
    sort = e.target.value;
    shop();
    $("#sort").focus({ preventScroll: true });
  };
}
function cart() {
  const lines = store.lines();
  $("#main").innerHTML =
    `<div class="page-intro"><div><p class="eyebrow">YOUR SHOPPING JOURNEY</p><h1>Your cart.</h1><p>Review your picks before placing a sample order.</p></div><a href="#shop">Continue shopping →</a></div>${!lines.length ? empty("Your cart is empty", "Something catch your eye? Add a product to get started.") : `<div class="cart-layout"><section class="panel" aria-label="Cart items">${lines.map(({ product: p, quantity: q }) => `<article class="cart-item"><img src="${image(p)}" alt="${esc(p.name)}"><div class="info"><h3><button class="text-button product-link" data-detail="${p.id}">${esc(p.name)}</button></h3><p class="muted">${money(p.cents)} each · ${p.stock} available</p><div class="quantity"><button data-quantity="${p.id}" data-q="${q - 1}" aria-label="Decrease ${esc(p.name)} quantity">−</button><span>${q}</span><button data-quantity="${p.id}" data-q="${q + 1}" aria-label="Increase ${esc(p.name)} quantity" ${q >= p.stock ? "disabled" : ""}>+</button></div><button class="text-button remove" data-quantity="${p.id}" data-q="0">Remove</button></div><div class="price">${money(p.cents * q)}</div></article>`).join("")}</section><aside class="panel summary"><h2>Order summary</h2><div class="total-line"><span>Items (${store.count()})</span><span>${money(store.total())}</span></div><div class="total-line"><span>Shipping & tax</span><span>Not applied</span></div><div class="total-line order-total"><span>Total</span><span>${money(store.total())}</span></div><button id="checkout">Place sample order</button><p class="muted">This is a portfolio demo. No payment, shipping address, or real purchase is involved.</p><a href="#shop">Continue shopping</a></aside></div>`}`;
  $("#checkout")?.addEventListener("click", async () => {
    if (
      await confirmAction(
        "Place a sample order?",
        "Your demo inventory will update and this cart will move to My orders. No money will be charged.",
        "Place sample order",
      )
    ) {
      try {
        const o = commit((s) => s.checkout());
        lastOrder = o.id;
        location.hash = "orders";
        notify(o.id + " created. No payment was collected.");
      } catch (e) {
        notify(e.message);
      }
    }
  });
}
function orders() {
  $("#main").innerHTML =
    `<div class="page-intro"><div><p class="eyebrow">SAVED ON THIS DEVICE</p><h1>Your orders.</h1><p>Sample purchases, collected in one place.</p></div><a href="#shop">Back to the shop →</a></div>${lastOrder ? '<section class="success-note" role="status"><h2>✓ Your sample order is confirmed.</h2><p>No payment was collected. Your inventory and order history are saved on this device.</p></section>' : ""}${!store.orders.length ? empty("No orders yet", "Your sample orders will appear here after checkout.") : store.orders.map((o) => `<article class="panel order"><header><div><h3>${esc(o.id)}</h3><p class="muted">${esc(new Date(o.date).toLocaleString())}</p></div><span class="pill">Sample order</span></header><ul>${o.items.map((i) => `<li><span>${esc(i.name)} <span class="muted">× ${i.quantity}</span></span><span>${money(i.cents * i.quantity)}</span></li>`).join("")}</ul><div class="order-receipt"><strong>Total ${money(o.total)}</strong><span class="muted">${o.items.reduce((sum, i) => sum + i.quantity, 0)} item${o.items.reduce((sum,i)=>sum+i.quantity,0)===1?"":"s"} · No payment collected</span></div></article>`).join("")}`;
}
function manage() {
  if (!admin) {
    $("#main").innerHTML = empty(
      "Try the admin view",
      "Select “Try admin view” in the header to explore product management.",
    );
    return;
  }
  $("#main").innerHTML =
    `<p class="eyebrow">DEMO ADMIN VIEW</p><h1>Product management</h1><p class="muted">Add, edit, or remove products in your local copy. This demo role switch is not real authentication.</p><div class="admin-stats"><span><strong>${store.products.length}</strong> products</span><span><strong>${store.products.filter((p) => p.stock > 0).length}</strong> in stock</span><span><strong>${store.orders.length}</strong> sample orders</span></div><div class="admin-tools"><button id="new-product">+ Add product</button><button id="reset" class="outline">Reset demo data</button></div><section class="panel table-wrap"><table><thead><tr><th>Image</th><th>Product</th><th>Price</th><th>Stock</th><th>Actions</th></tr></thead><tbody>${store.products.map((p) => `<tr><td><img src="${image(p)}" alt=""></td><td>${esc(p.name)}<br><span class="muted">${esc(p.category)}</span></td><td>${money(p.cents)}</td><td>${p.stock}</td><td><button data-image="${p.id}" class="outline">Add / change image</button><button data-edit="${p.id}" class="outline">Edit</button><button data-delete="${p.id}" class="outline">Delete</button></td></tr>`).join("")}</tbody></table></section>`;
  $("#new-product").onclick = () => editor();
  $("#reset").onclick = async () => {
    if (
      await confirmAction(
        "Reset this demo?",
        "This removes your demo orders, cart, and inventory edits on this device.",
        "Reset demo",
      )
    ) {
      try {
        commit((s) => s.reset());
        lastOrder = null;
        render();
        notify("Original demo catalog restored.");
      } catch (error) {
        notify(error.message);
      }
    }
  };
}
function render() {
  const r = route();
  for (const a of document.querySelectorAll("[data-route]")) {
    if (a.dataset.route === r) a.setAttribute("aria-current", "page");
    else a.removeAttribute("aria-current");
  }
  $("#cart-count").textContent = store.count();
  $("#admin-nav").hidden = !admin;
  $("#role-toggle").textContent = admin
    ? "Back to customer view"
    : "Try admin view";
  ({ shop, cart, orders, admin: manage })[r]();
}
function detail(id) {
  const p = store.find(id);
  if (!p) return;
  const left = available(p);
  $("#detail-feedback").textContent = "";
  $("#product-details").innerHTML =
    `<div class="detail-grid"><div class="detail-photo"><img src="${image(p)}" alt="${esc(p.name)}"></div><div><p class="eyebrow">${esc(p.category)}</p><h2 id="detail-title">${esc(p.name)}</h2><h3 class="description-heading">About this product</h3><p class="full-description">${esc(p.description)}</p><div class="price">${money(p.cents)}</div><p class="stock">${p.stock ? left + " available to add" + (store.cart[id] ? " · " + store.cart[id] + " in your cart" : "") : "Currently out of stock"}</p><div class="detail-controls">${left ? `<label>Quantity<select id="detail-quantity">${Array.from({ length: Math.min(10, left) }, (_, i) => '<option value="' + (i + 1) + '">' + (i + 1) + "</option>").join("")}</select></label>` : ""}<button data-add="${id}" data-from-detail="true" ${left < 1 ? "disabled" : ""}>${p.stock ? (left ? "+ Add to cart" : "All available units in cart") : "Sold out"}</button></div><a class="detail-cart-link" href="#cart" data-close-detail>View your cart →</a><p class="detail-note">Sample catalog · USD prices · No real purchases</p></div></div>`;
  if (!$("#product-dialog").open) $("#product-dialog").showModal();
}
function editor(id) {
  if (!admin) return;
  const f = $("#product-form");
  f.reset();
  const p = store.find(id);
  imageVersion++;
  imageBusy = false;
  editorImage = p?.image || "assets/placeholder.svg";
  $("#image-preview").src = editorImage;
  $("#image-url").value = editorImage.startsWith("https:") ? editorImage : "";
  $("#image-status").textContent = "";
  f.querySelector("[type=submit]").disabled = false;
  f.elements.id.value = p?.id || "";
  $("#editor-title").textContent = p ? "Edit product" : "Add a product";
  f.elements.category.innerHTML = categories
    .map((c) => `<option>${esc(c)}</option>`)
    .join("");
  if (p) {
    for (const k of ["name", "description", "stock", "category"])
      f.elements[k].value = p[k];
    f.elements.price.value = (p.cents / 100).toFixed(2);
  }
  $("#edit-dialog").showModal();
}
function confirmAction(title, text, label) {
  return new Promise((resolve) => {
    const d = $("#confirm-dialog");
    $("#confirm-title").textContent = title;
    $("#confirm-text").textContent = text;
    $("#confirm-yes").textContent = label;
    let answer = false;
    $("#confirm-yes").onclick = () => {
      answer = true;
      d.close();
    };
    $("#confirm-no").onclick = () => d.close();
    d.addEventListener("close", () => resolve(answer), { once: true });
    d.showModal();
  });
}
document.addEventListener("click", async (e) => {
  const b = e.target.closest("button");
  if (!b || !store) return;
  try {
    if (b.dataset.add) {
      const id = Number(b.dataset.add),
        amount = b.dataset.fromDetail
          ? Number($("#detail-quantity")?.value || 1)
          : 1;
      commit((s) => s.setQuantity(id, (s.cart[id] || 0) + amount));
      const message = store.find(id).name + " added to cart.";
      render();
      if ($("#product-dialog").open) {
        detail(id);
        $("#detail-feedback").textContent = message;
        const add = $("#product-details [data-add]");
        (add.disabled ? $(".detail-cart-link") : add).focus();
      } else {
        const card = $("#main [data-add='" + id + "']");
        if (card && !card.disabled) card.focus({ preventScroll: true });
      }
      notify(message);
    }
    if (b.dataset.detail) detail(Number(b.dataset.detail));
    if (b.dataset.quantity) {
      const label = b.getAttribute("aria-label");
      commit((s) =>
        s.setQuantity(Number(b.dataset.quantity), Number(b.dataset.q)),
      );
      render();
      const next = [...$("#main").querySelectorAll("button")].find(
        (button) =>
          button.getAttribute("aria-label") === label && !button.disabled,
      );
      (next || $("#main")).focus({ preventScroll: true });
      if (Number(b.dataset.q) === 0) notify("Item removed from your cart.");
    }
    if (b.dataset.category) {
      category = b.dataset.category;
      shop();
      [...$("#main").querySelectorAll("[data-category]")]
        .find((button) => button.dataset.category === category)
        ?.focus({ preventScroll: true });
    }
    if (b.dataset.edit) editor(Number(b.dataset.edit));
    if (b.dataset.image) {
      editor(Number(b.dataset.image));
      $("#image-file").focus();
    }
    if (
      b.dataset.delete &&
      admin &&
      (await confirmAction(
        "Remove this product?",
        "The product will be removed from your demo catalog and cart. Existing orders remain unchanged.",
        "Remove product",
      ))
    ) {
      commit((s) => s.remove(Number(b.dataset.delete)));
      render();
    }
  } catch (err) {
    notify(err.message);
    if ($("#product-dialog").open)
      $("#detail-feedback").textContent = err.message;
  }
});
$("#search-form").onsubmit = (e) => {
  e.preventDefault();
  if (!store) return;
  category = "All";
  if (route() !== "shop") location.hash = "shop";
  else shop();
};
$("#search").addEventListener("input", () => {
  if (store && route() === "shop") shop();
});
$("#role-toggle").onclick = () => {
  if (!store) return;
  admin = !admin;
  if (admin) {
    location.hash = "admin";
    render();
  } else {
    location.hash = "shop";
    render();
  }
  notify(
    admin
      ? "Demo admin view enabled. Changes stay on this device."
      : "Customer view enabled.",
  );
};
$("#image-url").oninput = (e) => {
  imageVersion++;
  imageBusy = false;
  $("#product-form [type=submit]").disabled = false;
  const url = e.target.value.trim();
  $("#image-file").value = "";
  if (url && validImage(url)) {
    editorImage = url;
    $("#image-preview").src = url;
    $("#image-status").textContent = "Image URL selected.";
  } else {
    $("#image-status").textContent = url
      ? "Enter a valid HTTPS image URL."
      : "Current image kept.";
  }
};
$("#image-preview").onerror = () => {
  $("#image-preview").src = "assets/placeholder.svg";
  $("#image-status").textContent =
    "Image could not load. Choose another image or check the URL.";
};
$("#image-file").onchange = async (e) => {
  const file = e.target.files[0];
  if (!file) return;
  const version = ++imageVersion;
  if (
    !["image/jpeg", "image/png", "image/webp"].includes(file.type) ||
    file.size > 10 * 1024 * 1024
  ) {
    $("#image-status").textContent = "Choose a JPG, PNG, or WebP up to 10 MB.";
    e.target.value = "";
    return;
  }
  imageBusy = true;
  $("#product-form [type=submit]").disabled = true;
  $("#image-status").textContent = "Preparing image…";
  try {
    const bitmap = await createImageBitmap(file);
    const canvas = document.createElement("canvas");
    const scale = Math.min(1, 600 / Math.max(bitmap.width, bitmap.height));
    canvas.width = Math.max(1, Math.round(bitmap.width * scale));
    canvas.height = Math.max(1, Math.round(bitmap.height * scale));
    const ctx = canvas.getContext("2d");
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(bitmap, 0, 0, canvas.width, canvas.height);
    bitmap.close();
    const data = canvas.toDataURL("image/jpeg", 0.8);
    if (!validImage(data))
      throw Error("Image is too large. Choose a smaller image.");
    if (version !== imageVersion) return;
    editorImage = data;
    $("#image-url").value = "";
    $("#image-preview").src = data;
    $("#image-status").textContent =
      "Image ready. Save the product to keep it.";
  } catch (err) {
    if (version === imageVersion)
      $("#image-status").textContent =
        err.message || "Unable to read this image. Choose another file.";
  } finally {
    if (version === imageVersion) {
      imageBusy = false;
      $("#product-form [type=submit]").disabled = false;
    }
  }
};
$("#product-form").onsubmit = (e) => {
  e.preventDefault();
  if (!admin || imageBusy) return;
  try {
    const url = $("#image-url").value.trim();
    commit((s) =>
      s.saveProduct({
        ...Object.fromEntries(new FormData(e.target)),
        image: url || editorImage,
      }),
    );
    $("#edit-dialog").close();
    render();
    notify("Product and image saved to your demo.");
  } catch (err) {
    $("#image-status").textContent =
      err.name === "QuotaExceededError"
        ? "Device storage is full. Choose a smaller image or remove unused demo products."
        : err.message;
  }
};
for (const d of document.querySelectorAll("dialog")) {
  d.querySelector(".close")?.addEventListener("click", () => d.close());
  d.addEventListener("click", (e) => {
    if (e.target === d) {
      const r = d.getBoundingClientRect();
      if (
        e.clientX < r.left ||
        e.clientX > r.right ||
        e.clientY < r.top ||
        e.clientY > r.bottom
      )
        d.close();
    }
  });
}
document.addEventListener("click", (e) => {
  if (e.target.closest("[data-close-detail]")) $("#product-dialog").close();
  if (e.target.closest("[data-reset-search]")) {
    $("#search").value = "";
    category = "All";
    if (store && route() === "shop") shop();
  }
});
// Failed remote images fall back without looping if the fallback itself fails.
document.addEventListener(
  "error",
  (e) => {
    const img = e.target;
    if (
      img.tagName === "IMG" &&
      !img.src.endsWith("/assets/placeholder.svg") &&
      !img.classList.contains("project-brand-logo")
    )
      img.src = "assets/placeholder.svg";
  },
  true,
);
window.addEventListener("hashchange", () => {
  if (store) {
    render();
    if (location.hash === "#catalog") $("#catalog")?.scrollIntoView();
    else {
      window.scrollTo({ top: 0 });
      $("#main").focus({ preventScroll: true });
    }
  }
});
async function init() {
  try {
    const r = await fetch("catalog.json?v=polish-2");
    if (!r.ok) throw Error("Catalog unavailable");
    const seed = await r.json();
    let saved;
    try {
      saved = JSON.parse(localStorage.getItem(KEY));
    } catch {}
    store = new Store(seed, saved);
    render();
  } catch {
    $("#main").innerHTML =
      '<section class="empty"><h1>Unable to load the catalog</h1><p>Please refresh this page to try again.</p><button onclick="location.reload()">Try again</button></section>';
  }
}
init();
