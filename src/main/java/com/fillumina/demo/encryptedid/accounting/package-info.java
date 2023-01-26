/**
 * This package represents the accounting system that lives in a different environment than the actual shop
 * and needs to communicate with it through the API. That's why there aren't relationships between
 * entities on the two packages.
 * <p>
 * When 2 or more systems need to communicate they cannot use their internal references for entities
 * (the same as with object memory locations in programs). This is because internal references might
 * easily change in real practice and it's better to relay on a more universal identifier.
 * <p>
 * The most known identifier is the
 * <a href='https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch20.html#performance-collections-mostefficentinverse'>
 * UUID</a> which has the disadvantage of being 16 bytes long (128 bits) and because of that it
 * wastes precious memory and disk space on the databases involved. There are several kind of
 * universal identifier which are more compact in size and so better suited for the purpose, I have
 * used
 * <a href='https://github.com/f4b6a3/tsid-creator'>TSID</a> here.
 *
 */
package com.fillumina.demo.encryptedid.accounting;
