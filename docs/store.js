export const clone = value => JSON.parse(JSON.stringify(value));
export const money = cents => new Intl.NumberFormat('en-US',{style:'currency',currency:'USD'}).format(cents/100);
export const validImage = value => typeof value==='string' && (/^assets\/[a-z0-9_.-]+$/i.test(value) || /^data:image\/(jpeg|png|webp);base64,[a-z0-9+/=]+$/i.test(value) && value.length<=700000 || (()=>{try{const u=new URL(value);return u.protocol==='https:'&&!u.username&&!u.password;}catch{return false;}})());
export class Store {
  constructor(seed, saved) {
    this.seed=clone(seed);
    this.products=clone(seed); this.cart={}; this.orders=[]; this.nextId=100;
    if(saved?.version===1 && Array.isArray(saved.products) && Array.isArray(saved.orders) && saved.cart && typeof saved.cart==='object') {
      // Ignore corrupt saved data rather than accepting invalid quantities or prices.
      if(saved.products.every(p=>Number.isSafeInteger(p.id)&&p.id>0&&typeof p.name==='string'&&typeof p.description==='string'&&typeof p.category==='string'&&Number.isSafeInteger(p.cents)&&p.cents>0&&Number.isInteger(p.stock)&&p.stock>=0)) {
        this.products=clone(saved.products);this.orders=clone(saved.orders);this.nextId=Math.max(100,...this.products.map(p=>p.id+1));
        for(const [key,q] of Object.entries(saved.cart)) {
          const p=this.find(Number(key));if(p&&Number.isInteger(q)&&q>0&&q<=p.stock)this.cart[p.id]=q;
        }
      }
    }
    // Upgrade only untouched original descriptions; preserve local inventory edits.
    for (const p of this.products) {
      const original=this.seed.find(item=>item.id===p.id);
      if(original?.previousDescription && p.name===original.name && p.description===original.previousDescription) p.description=original.description;
    }
  }
  find(id){return this.products.find(p=>p.id===id);}
  lines(){return Object.entries(this.cart).map(([id,quantity])=>({product:this.find(Number(id)),quantity})).filter(x=>x.product);}
  total(){return this.lines().reduce((t,x)=>t+x.product.cents*x.quantity,0);}
  count(){return this.lines().reduce((t,x)=>t+x.quantity,0);}
  setQuantity(id,q){
    const p=this.find(id);
    if(!p||!Number.isInteger(q)||q<0||q>p.stock)throw Error('That quantity is not available.');
    if(q===0)delete this.cart[id];else this.cart[id]=q;
  }
  add(id){this.setQuantity(id,(this.cart[id]||0)+1);}
  checkout(){
    const lines=this.lines();if(!lines.length)throw Error('Your cart is empty.');
    for(const x of lines)if(x.quantity>x.product.stock)throw Error('Stock changed. Please update your cart.');
    const order={id:'MA-'+(1001+this.orders.length),date:new Date().toISOString(),total:this.total(),items:lines.map(x=>({name:x.product.name,cents:x.product.cents,quantity:x.quantity})),status:'Sample order'};
    for(const x of lines)x.product.stock-=x.quantity;
    this.orders.unshift(order);this.cart={};return order;
  }
  saveProduct(input){
    const cents=Math.round(Number(input.price)*100),stock=Number(input.stock);
    if(!input.name?.trim()||!input.description?.trim()||!input.category?.trim()||!Number.isSafeInteger(cents)||cents<=0||cents>10000000||!Number.isInteger(stock)||stock<0||stock>10000)throw Error('Enter a valid name, description, price, category, and stock.');
    if(input.image!==undefined&&!validImage(input.image))throw Error('Choose an image or enter a valid HTTPS image URL.');
    let p=input.id?this.find(Number(input.id)):null;
    if(input.id&&!p)throw Error('Product not found.');
    if(!p){p={id:this.nextId++,image:'assets/placeholder.svg'};this.products.push(p);}
    Object.assign(p,{name:input.name.trim().slice(0,150),description:input.description.trim().slice(0,1000),cents,stock,category:input.category});
    if(input.image!==undefined)p.image=input.image;
    if(this.cart[p.id]>stock){if(stock===0)delete this.cart[p.id];else this.cart[p.id]=stock;}
    return p;
  }
  remove(id){this.products=this.products.filter(p=>p.id!==id);delete this.cart[id];}
  serialize(){return {version:1,products:this.products,cart:this.cart,orders:this.orders};}
  reset(){this.products=clone(this.seed);this.cart={};this.orders=[];this.nextId=100;}
}
