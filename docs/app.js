import {Store,money} from './store.js';
const $=s=>document.querySelector(s), KEY='polo-mini-amazon-demo-v1';
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
let store,category='All',sort='featured',admin=false,timer;
const categories=['Electronics','Clothing and Accessories','Home and Kitchen','Beauty and Personal Care','Sports and Fitness','Toys and Games','Books and Education','Automotive'];
function notify(message){$('#toast').textContent=message;$('#toast').classList.add('visible');clearTimeout(timer);timer=setTimeout(()=>$('#toast').classList.remove('visible'),3200);}
function persist(){try{localStorage.setItem(KEY,JSON.stringify(store.serialize()));}catch{notify('Browser storage is unavailable. Changes last for this visit.');}}
function route(){const r=location.hash.slice(1);return ['shop','cart','orders','admin'].includes(r)?r:'shop';}
function image(p){return esc(/^assets\/[a-z0-9_.-]+$/i.test(p.image)?p.image:'assets/placeholder.svg');}
function productCard(p){return `<article class="product-card"><button class="image-button" data-detail="${p.id}" aria-label="View ${esc(p.name)}"><img src="${image(p)}" alt="${esc(p.name)}" loading="lazy"></button><div class="body"><p class="category">${esc(p.category)}</p><h3><button class="text-button" data-detail="${p.id}">${esc(p.name)}</button></h3><p class="description">${esc(p.description)}</p><div class="purchase"><div class="price">${money(p.cents)}</div><p class="stock">${p.stock?p.stock+' in stock':'Out of stock'}</p><button class="add" data-add="${p.id}" ${p.stock<1?'disabled':''}>${p.stock?'Add to cart':'Sold out'}</button></div></div></article>`;}
function empty(title,text){return `<div class="empty"><h2>${title}</h2><p>${text}</p><a class="button" href="#shop">Explore products</a></div>`;}
function shop(){
 const query=$('#search').value.trim().toLowerCase();
 let products=store.products.filter(p=>(category==='All'||p.category===category)&&[p.name,p.description,p.category].some(s=>s.toLowerCase().includes(query)));
 if(sort==='low')products.sort((a,b)=>a.cents-b.cents);if(sort==='high')products.sort((a,b)=>b.cents-a.cents);if(sort==='name')products.sort((a,b)=>a.name.localeCompare(b.name));
 $('#main').innerHTML=`${!query&&category==='All'?`<section class="hero"><div><p class="eyebrow">THE MINIAMAZON PROJECT</p><h1>Your everyday finds.<br>One little storefront.</h1><p>Explore the original project catalog, build a cart, and follow a sample order from start to finish.</p><a class="button" href="#catalog">Browse the catalog ↓</a></div><div class="hero-art" aria-hidden="true"><img src="assets/hp_laptop.jpg" alt=""><img src="assets/dior_perfume.jpg" alt=""></div></section>`:''}<section id="catalog"><div class="toolbar"><div><h2>${query?'Search results':'Explore the catalog'}</h2><p>${products.length} product${products.length===1?'':'s'}${query?' matching “'+esc(query)+'”':''}</p></div><label><span>Sort by </span><select id="sort"><option value="featured">Featured</option><option value="low">Price: low to high</option><option value="high">Price: high to low</option><option value="name">Name: A–Z</option></select></label></div><div class="category-tabs" role="group" aria-label="Filter products by category">${['All',...categories].map(c=>`<button data-category="${esc(c)}" aria-pressed="${category===c}">${esc(c)}</button>`).join('')}</div>${products.length?'<div class="products">'+products.map(productCard).join('')+'</div>':empty('No products found','Try another search or choose a different category.')}</section>`;
 $('#sort').value=sort;$('#sort').onchange=e=>{sort=e.target.value;shop();};
}
function cart(){
 const lines=store.lines();
 $('#main').innerHTML=`<p class="eyebrow">YOUR SHOPPING JOURNEY</p><h1>Shopping cart</h1>${!lines.length?empty('Your cart is empty','Something catch your eye? Add a product to get started.'):`<div class="cart-layout"><section class="panel" aria-label="Cart items">${lines.map(({product:p,quantity:q})=>`<article class="cart-item"><img src="${image(p)}" alt="${esc(p.name)}"><div class="info"><h3>${esc(p.name)}</h3><p class="muted">${money(p.cents)} each · ${p.stock} available</p><div class="quantity"><button data-quantity="${p.id}" data-q="${q-1}" aria-label="Decrease ${esc(p.name)} quantity">−</button><span>${q}</span><button data-quantity="${p.id}" data-q="${q+1}" aria-label="Increase ${esc(p.name)} quantity" ${q>=p.stock?'disabled':''}>+</button></div><button class="text-button remove" data-quantity="${p.id}" data-q="0">Remove</button></div><div class="price">${money(p.cents*q)}</div></article>`).join('')}</section><aside class="panel summary"><h2>Order summary</h2><div class="total-line"><span>Items (${store.count()})</span><span>${money(store.total())}</span></div><div class="total-line"><span>Shipping & tax</span><span>Not applied</span></div><div class="total-line order-total"><span>Total</span><span>${money(store.total())}</span></div><button id="checkout">Place sample order</button><p class="muted">This is a portfolio demo. No payment, shipping address, or real purchase is involved.</p><a href="#shop">Continue shopping</a></aside></div>`}`;
 $('#checkout')?.addEventListener('click',async()=>{if(await confirmAction('Place a sample order?','Your demo inventory will update and this cart will move to My orders. No money will be charged.','Place sample order')){try{const o=store.checkout();persist();location.hash='orders';notify(o.id+' created. No payment was collected.');}catch(e){notify(e.message);}}});
}
function orders(){
 $('#main').innerHTML=`<p class="eyebrow">SAVED ON THIS DEVICE</p><h1>My orders</h1>${!store.orders.length?empty('No orders yet','Your sample orders will appear here after checkout.'):store.orders.map(o=>`<article class="panel order"><header><div><h3>${esc(o.id)}</h3><p class="muted">${esc(new Date(o.date).toLocaleString())}</p></div><span class="pill">Sample order</span></header><ul>${o.items.map(i=>`<li>${esc(i.name)} × ${i.quantity} <span class="muted">— ${money(i.cents*i.quantity)}</span></li>`).join('')}</ul><strong>Total ${money(o.total)}</strong></article>`).join('')}`;
}
function manage(){
 if(!admin){$('#main').innerHTML=empty('Try the admin view','Select “Try admin view” in the header to explore product management.');return;}
 $('#main').innerHTML=`<p class="eyebrow">DEMO ADMIN VIEW</p><h1>Product management</h1><p class="muted">Add, edit, or remove products in your local copy. This demo role switch is not real authentication.</p><div class="admin-tools"><button id="new-product">+ Add product</button><button id="reset" class="outline">Reset demo data</button></div><section class="panel table-wrap"><table><thead><tr><th>Image</th><th>Product</th><th>Price</th><th>Stock</th><th>Actions</th></tr></thead><tbody>${store.products.map(p=>`<tr><td><img src="${image(p)}" alt=""></td><td>${esc(p.name)}<br><span class="muted">${esc(p.category)}</span></td><td>${money(p.cents)}</td><td>${p.stock}</td><td><button data-edit="${p.id}" class="outline">Edit</button><button data-delete="${p.id}" class="outline">Delete</button></td></tr>`).join('')}</tbody></table></section>`;
 $('#new-product').onclick=()=>editor();$('#reset').onclick=async()=>{if(await confirmAction('Reset this demo?','This removes your demo orders, cart, and inventory edits on this device.','Reset demo')){store.reset();persist();render();notify('Original demo catalog restored.');}};
}
function render(){
 const r=route();for(const a of document.querySelectorAll('[data-route]')){if(a.dataset.route===r)a.setAttribute('aria-current','page');else a.removeAttribute('aria-current');}
 $('#cart-count').textContent=store.count();$('#admin-nav').hidden=!admin;$('#role-toggle').textContent=admin?'Back to customer view':'Try admin view';
 ({shop,cart,orders,admin:manage}[r])();
}
function detail(id){const p=store.find(id);if(!p)return;$('#product-details').innerHTML=`<div class="detail-grid"><img src="${image(p)}" alt="${esc(p.name)}"><div><p class="eyebrow">${esc(p.category)}</p><h2>${esc(p.name)}</h2><p>${esc(p.description)}</p><div class="price">${money(p.cents)}</div><p class="stock">${p.stock} in stock</p><button data-add="${id}" ${p.stock===0?'disabled':''}>Add to cart</button></div></div>`;$('#product-dialog').showModal();}
function editor(id){if(!admin)return;const f=$('#product-form');f.reset();const p=store.find(id);f.elements.id.value=p?.id||'';$('#editor-title').textContent=p?'Edit product':'Add a product';f.elements.category.innerHTML=categories.map(c=>`<option>${esc(c)}</option>`).join('');if(p){for(const k of ['name','description','stock','category'])f.elements[k].value=p[k];f.elements.price.value=(p.cents/100).toFixed(2);}$('#edit-dialog').showModal();}
function confirmAction(title,text,label){return new Promise(resolve=>{const d=$('#confirm-dialog');$('#confirm-title').textContent=title;$('#confirm-text').textContent=text;$('#confirm-yes').textContent=label;let answer=false;$('#confirm-yes').onclick=()=>{answer=true;d.close();};$('#confirm-no').onclick=()=>d.close();d.addEventListener('close',()=>resolve(answer),{once:true});d.showModal();});}
document.addEventListener('click',async e=>{
 const b=e.target.closest('button');if(!b||!store)return;
 try{
  if(b.dataset.add){store.add(Number(b.dataset.add));persist();$('#cart-count').textContent=store.count();notify(store.find(Number(b.dataset.add)).name+' added to cart.');}
  if(b.dataset.detail)detail(Number(b.dataset.detail));
  if(b.dataset.quantity){store.setQuantity(Number(b.dataset.quantity),Number(b.dataset.q));persist();render();}
  if(b.dataset.category){category=b.dataset.category;shop();}
  if(b.dataset.edit)editor(Number(b.dataset.edit));
  if(b.dataset.delete&&admin&&await confirmAction('Remove this product?','The product will be removed from your demo catalog and cart. Existing orders remain unchanged.','Remove product')){store.remove(Number(b.dataset.delete));persist();render();}
 }catch(err){notify(err.message);}
});
$('#search-form').onsubmit=e=>{e.preventDefault();category='All';if(route()!=='shop')location.hash='shop';else shop();};
$('#search').addEventListener('input',()=>{if(store&&route()==='shop')shop();});
$('#role-toggle').onclick=()=>{admin=!admin;if(admin){location.hash='admin';render();}else{location.hash='shop';render();}notify(admin?'Demo admin view enabled. Changes stay on this device.':'Customer view enabled.');};
$('#product-form').onsubmit=e=>{e.preventDefault();if(!admin)return;try{store.saveProduct(Object.fromEntries(new FormData(e.target)));persist();$('#edit-dialog').close();render();notify('Product saved to your demo.');}catch(err){notify(err.message);}};
for(const d of document.querySelectorAll('dialog')){d.querySelector('.close')?.addEventListener('click',()=>d.close());d.addEventListener('click',e=>{if(e.target===d){const r=d.getBoundingClientRect();if(e.clientX<r.left||e.clientX>r.right||e.clientY<r.top||e.clientY>r.bottom)d.close();}});}
window.addEventListener('hashchange',()=>{if(store){render();window.scrollTo({top:0});}});
async function init(){try{const r=await fetch('catalog.json');if(!r.ok)throw Error('Catalog unavailable');const seed=await r.json();let saved;try{saved=JSON.parse(localStorage.getItem(KEY));}catch{}store=new Store(seed,saved);render();}catch{$('#main').innerHTML='<section class="empty"><h1>Unable to load the catalog</h1><p>Please refresh this page to try again.</p><button onclick="location.reload()">Try again</button></section>';}}
init();
