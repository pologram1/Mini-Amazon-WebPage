import { test } from "node:test";
import assert from "node:assert/strict";
import { Store, clone } from "../docs/store.js";
import { saveChange } from "../docs/persistence.js";
const seed = [
  {
    id: 1,
    name: "Demo product",
    description: "Description",
    category: "Electronics",
    cents: 1234,
    stock: 2,
    image: "assets/hp_laptop.jpg",
  },
];
test("a failed checkout save preserves cart, stock and order history", () => {
  const current = new Store(seed);
  current.add(1);
  const before = clone(current.serialize());
  assert.throws(
    () =>
      saveChange(
        current,
        (s) => s.checkout(),
        {
          setItem() {
            throw Error("Quota exceeded");
          },
        },
        "test",
      ),
    { code: "SAVE_FAILED" },
  );
  assert.deepEqual(current.serialize(), before);
});
test("successful checkout persists the same candidate returned to the UI", () => {
  const current = new Store(seed);
  current.add(1);
  let saved;
  const next = saveChange(
    current,
    (s) => s.checkout(),
    {
      setItem(key, value) {
        saved = JSON.parse(value);
      },
    },
    "test",
  );
  assert.deepEqual(next.store.serialize(), saved);
  assert.equal(next.result.total, 1234);
  assert.equal(next.store.find(1).stock, 1);
  assert.equal(current.count(), 1);
  assert.equal(current.orders.length, 0);
});
test("invalid stock changes never write to storage", () => {
  const current = new Store(seed);
  let writes = 0;
  assert.throws(() =>
    saveChange(
      current,
      (s) => s.setQuantity(1, 3),
      {
        setItem() {
          writes++;
        },
      },
      "test",
    ),
  );
  assert.equal(writes, 0);
  assert.equal(current.count(), 0);
});
