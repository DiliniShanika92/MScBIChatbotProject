package com.xatkit.core.recognition;

import com.xatkit.AbstractXatkitTest;
import com.xatkit.core.EventDefinitionRegistry;
import com.xatkit.core.XatkitBot;
import com.xatkit.core.server.XatkitServer;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntentRecognitionProviderFactoryTest extends AbstractXatkitTest {

    private IntentRecognitionProvider provider;

    private XatkitBot xatkitBot;

    @Before
    public void setUp() {
        xatkitBot = mock(XatkitBot.class);
        when(xatkitBot.getEventDefinitionRegistry()).thenReturn(new EventDefinitionRegistry());
        when(xatkitBot.getXatkitServer()).thenReturn(mock(XatkitServer.class));
    }

    @After
    public void tearDown() {
        if(nonNull(provider) && !provider.isShutdown()) {
            try {
                provider.shutdown();
            } catch(IntentRecognitionProviderException e) {
                /*
                 * Nothing to do, the provider will be re-created anyways.
                 */
            }
        }
    }

    @Test
    public void getIntentRecognitionProviderInfluxDBProperties() {
        Configuration configuration = new BaseConfiguration();
        configuration.addProperty(IntentRecognitionProviderFactoryConfiguration.ENABLE_RECOGNITION_ANALYTICS, true);
        configuration.addProperty(IntentRecognitionProviderFactory.LOGS_DATABASE, RecognitionMonitorMapDB.LOGS_DATABASE_MAPDB);
        provider = IntentRecognitionProviderFactory.getIntentRecognitionProvider(xatkitBot, configuration);
        assertThat(provider.getRecognitionMonitor()).isInstanceOf(RecognitionMonitorMapDB.class);
    }
}
