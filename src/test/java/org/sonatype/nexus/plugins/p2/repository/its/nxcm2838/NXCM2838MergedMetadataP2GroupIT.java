/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.p2.repository.its.nxcm2838;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.contains;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.exists;

import java.io.File;
import java.net.URL;

import org.codehaus.plexus.util.FileUtils;
import org.sonatype.nexus.plugins.p2.repository.its.AbstractNexusProxyP2IT;
import org.testng.annotations.Test;

public class NXCM2838MergedMetadataP2GroupIT
    extends AbstractNexusProxyP2IT
{

    public NXCM2838MergedMetadataP2GroupIT()
    {
        super( "nxcm2838" );
    }

    @Test
    public void test()
        throws Exception
    {
        final File artifactsXmlFile = new File( "target/downloads/nxcm2838/artifacts.xml" );
        assertThat( artifactsXmlFile, not( exists() ) );

        downloadFile(
            new URL( getGroupUrl( getTestRepositoryId() ) + "artifacts.xml" ),
            artifactsXmlFile.getAbsolutePath()
        );
        assertThat( artifactsXmlFile, exists() );

        final String artifactsXmlContent = FileUtils.fileRead( artifactsXmlFile );

        // has 5 mappings
        assertThat( artifactsXmlFile, contains(
            "<mappings size=\"5\">",
            "(&amp; (classifier=osgi.bundle) (format=packed))",
            "(&amp; (classifier=osgi.bundle))"
        ) );
        // packed is before non-packed
        final int indexOfPacked = artifactsXmlContent.indexOf( "(&amp; (classifier=osgi.bundle) (format=packed))" );
        final int indexOfBundle = artifactsXmlContent.indexOf( "(&amp; (classifier=osgi.bundle))" );
        assertThat( indexOfPacked < indexOfBundle, is( true ) );
    }

}
