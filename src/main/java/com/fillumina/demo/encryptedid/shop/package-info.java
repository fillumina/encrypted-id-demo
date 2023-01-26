/**
 * This package represents a shop that takes shopping carts filled by the user, save them
 * locally and send the invoice to the accounting system with universal references to the
 * user and the shopping cart.
 * <p>
 * Using universal references (UUID in {@link WebUser} and TSID in {@link ShoppingCart}) allows
 * to:
 * <ul>
 * <li>Prevent problems if the database of the shop would be reset (messing with the eventual
 * automatically assigned long IDs)
 * <li>Add the UUID to the URL returned to actual user safely for future reference
 * <li>Differentiate users on different systems
 * </ul>
 */
package com.fillumina.demo.encryptedid.shop;
