/**
 * Copyright (c) 2008-2011 Sonatype, Inc.
 *
 * All rights reserved. Includes the third-party code listed at http://www.sonatype.com/products/nexus/attributions.
 * Sonatype and Sonatype Nexus are trademarks of Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation.
 * M2Eclipse is a trademark of the Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package com.sonatype.nexus.proxy.p2.its.nxcm0792;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonatype.nexus.test.utils.TaskScheduleUtil;

import com.sonatype.nexus.proxy.p2.its.AbstractNexusProxyP2IntegrationIT;

public class NXCM792UpdateSiteWithTransitiveDependenciesProxyIT
    extends AbstractNexusProxyP2IntegrationIT
{
    public NXCM792UpdateSiteWithTransitiveDependenciesProxyIT()
    {
        super( "p2group" );
    }

    @Test
    public void updatesiteproxy() throws Exception {
        /*
         * feature3 includes bundle3 that depends on bundle.
         * bundle3->bundle dependency is not discovered by standard p2 runtime
         */

        String nexusTestRepoUrl = getNexusTestRepoUrl();

        File installDir = new File("target/eclipse/nxcm0792");

        TaskScheduleUtil.run( "1" );
        TaskScheduleUtil.waitForAllTasksToStop();

        installUsingP2(
            nexusTestRepoUrl,
            "com.sonatype.nexus.p2.its.feature3.feature.group",
            installDir.getCanonicalPath() );

        File feature3 = new File(installDir, "features/com.sonatype.nexus.p2.its.feature3_1.0.0");
        Assert.assertTrue(feature3.exists() && feature3.isDirectory());

        File bundle = new File(installDir, "plugins/com.sonatype.nexus.p2.its.bundle_1.0.0.jar");
        Assert.assertTrue(bundle.canRead());

        File bundle3 = new File(installDir, "plugins/com.sonatype.nexus.p2.its.bundle3_1.0.0.jar");
        Assert.assertTrue(bundle3.canRead());
    }
}