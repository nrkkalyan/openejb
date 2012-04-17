/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.openejb.arquillian.embedded;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(Arquillian.class)
// @RunAsClient
public class EmbeddedTomEEContainerTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClass(AnEJB.class).addClass(AServlet.class).addClass(ARestService.class).setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).version("3.0").exportAsString()));
    }

    @EJB
    private AnEJB ejb;

    @ArquillianResource
    private URL url;

    @Test
    public void testEjbIsNotNull() throws Exception {
        assertNotNull(ejb);
    }

    @Test
    public void servletIsDeployed() throws Exception {
        final String read = IOUtils.toString(new URL(url.toExternalForm() + "a-servlet").openStream());
        assertEquals("ok=true", read);
    }

    @Test
    public void restServiceIsDeployed() throws Exception {
        final String read = IOUtils.toString(new URL("http://localhost:" + System.getProperty("tomee.httpPort", "8080") + "/test/rest/foo").openStream());
        assertEquals("foo", read);
    }
}
