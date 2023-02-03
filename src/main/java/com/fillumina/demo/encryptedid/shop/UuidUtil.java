package com.fillumina.demo.encryptedid.shop;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidUtil {

    /**
     * It is IMPORTANT that the UUID generator is shared among all entities if you want to
     * avoid to have the same UUID assigned to different entities. That could be a minor
     * problem considering they are different contexts but it might lead to troubles if
     * the UUIDs should be consider universally unique somewhere in the application
     * (i.e. caches) and they aren't.
     */
    public static TimeBasedGenerator UUID_GENERATOR =
            Generators.timeBasedGenerator(EthernetAddress.fromInterface());

}
