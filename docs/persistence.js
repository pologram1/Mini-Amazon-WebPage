import { Store, clone } from "./store.js";

/** Save a candidate state before exposing it to the UI. A failed write cannot
 * consume stock, empty the cart, or create an order that disappears on reload. */
export function saveChange(current, change, storage, key) {
  const candidate = new Store(current.seed, clone(current.serialize()));
  const result = change(candidate);
  try {
    storage.setItem(key, JSON.stringify(candidate.serialize()));
  } catch (cause) {
    const error = new Error(
      "Your change could not be saved. Free some browser storage or allow site storage, then try again. Your previous data is unchanged.",
    );
    error.code = "SAVE_FAILED";
    error.cause = cause;
    throw error;
  }
  return { store: candidate, result };
}
