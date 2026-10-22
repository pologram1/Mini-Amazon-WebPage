import {test} from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {Store} from '../docs/store.js';
const seed=JSON.parse(readFileSync(new URL('../docs/catalog.json',import.meta.url),'utf8'));
test('existing visitors receive descriptions without losing carts, stock, or orders',()=>{
  const old=seed.map(p=>({...p,description:p.previousDescription}));
  const before=new Store(old);before.add(1);before.checkout();before.add(2);
  const after=new Store(seed,before.serialize());
  assert.equal(after.find(1).description,seed[0].description);
  assert.equal(after.find(1).stock,seed[0].stock-1);
  assert.equal(after.cart[2],1);assert.deepEqual(after.orders,before.orders);
  assert.ok(after.products.every(p=>p.description===seed.find(s=>s.id===p.id).description));
});
test('description updates preserve admin edits and deleted products',()=>{
  const old=seed.map(p=>({...p,description:p.previousDescription}));
  const before=new Store(old);before.find(1).description='My custom laptop description';before.find(2).name='My phone';before.remove(3);
  const after=new Store(seed,before.serialize());
  assert.equal(after.find(1).description,'My custom laptop description');
  assert.equal(after.find(2).description,old[1].description);
  assert.equal(after.find(3),undefined);
});
