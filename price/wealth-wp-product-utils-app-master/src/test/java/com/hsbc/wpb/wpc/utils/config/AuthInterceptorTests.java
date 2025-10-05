package com.dummy.wpb.wpc.utils.config;

import com.dummy.wpb.wpc.utils.AdminControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AuthInterceptorTests {

    @InjectMocks
    private AuthInterceptor authInterceptor;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    private MockedStatic<AdminControl> adminControlMockedStatic;

    @After
    public void tearDown() {
        adminControlMockedStatic.close();
    }

    @Test
    public void testPreHandle_givenHttpServletRequestAndHttpServletResponseAndObject_returnsBoolean() throws Exception {
        Mockito.when(request.getHeader("x-dummy-secret-key")).thenReturn("secretKey");
        Mockito.when(request.getRequestURI()).thenReturn("/api/health");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        boolean handle1 = authInterceptor.preHandle(request, response, new Object());
        Assert.assertTrue(handle1);
        Mockito.when(request.getRequestURI()).thenReturn("/api");
        adminControlMockedStatic = Mockito.mockStatic(AdminControl.class);
        adminControlMockedStatic.when(() -> AdminControl.validateSecretKey(anyString())).thenReturn(true);
        boolean handle2 = authInterceptor.preHandle(request, response, new Object());
        Assert.assertTrue(handle2);
        Mockito.when(request.getHeader("x-dummy-secret-key")).thenReturn(null);
        boolean handle3 = authInterceptor.preHandle(request, response, new Object());
        Assert.assertFalse(handle3);
    }
}
