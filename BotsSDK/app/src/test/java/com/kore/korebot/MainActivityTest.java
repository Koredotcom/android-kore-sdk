package com.kore.korebot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.io.InputStream;

import kore.botssdk.net.SDKConfiguration;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Mock
    private MainActivity mainActivity;
    Context mockContext;

    @Before
    public void setUp() {
        mainActivity = new MainActivity();
    }

    @Test
    public void testGetResources() {
        // Use ApplicationProvider to get a Context
        mockContext = ApplicationProvider.getApplicationContext();

        // This will not throw an error because Robolectric provides a working Context
        Resources resources = mockContext.getResources();
        assertNotNull(resources);
    }

    @Test
    public void testMainLooper() {
        // This will not throw an error because Robolectric provides a working Looper
        Looper mainLooper = Looper.getMainLooper();
        assertNotNull(mainLooper);
    }

    @Test
    public void botIdNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.botId.isEmpty());
    }

    @Test
    public void botNameNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.botName.isEmpty());
    }

    @Test
    public void clientIdNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.clientId.isEmpty());
    }

    @Test
    public void clientSecretNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.clientSecret.isEmpty());
    }

    @Test
    public void identityNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.identity.isEmpty());
    }

    @Test
    public void jwtTokenNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.jwtToken.isEmpty());
    }

    @Test
    public void serverUrlNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.serverUrl.isEmpty());
    }

    @Test
    public void jwtServerUrlNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.jwtServerUrl.isEmpty());
    }

    @Test
    public void brandingServerUrlNotEmpty_ReturnsTrue() {
        assertFalse(mainActivity.brandingUrl.isEmpty());
    }
}
