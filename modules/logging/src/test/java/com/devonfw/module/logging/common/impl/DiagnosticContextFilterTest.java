package com.devonfw.module.logging.common.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link DiagnosticContextFilter}.
 */
public class DiagnosticContextFilterTest extends ModuleTest {

  private static final String CORRELATION_ID_HEADER_NAME_PARAM = "correlationIdHttpHeaderName";

  private static final String CORRELATION_ID_HEADER_NAME_PARAM_FIELD_NAME = "CORRELATION_ID_HEADER_NAME_PARAM";

  @Test
  public void testCorrelationIdHttpHeaderNameAfterConstructor() {

    // setup
    DiagnosticContextFilter filter = new DiagnosticContextFilter();

    // exercise
    String correlationIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter,
        CORRELATION_ID_HEADER_NAME_PARAM);

    // verify
    assertThat(correlationIdHttpHeaderName).isNotNull();
  }

  @Test
  public void testInitWithNullInitParameter() throws Exception {

    // setup
    DiagnosticContextFilter filter = new DiagnosticContextFilter();
    String field = (String) ReflectionTestUtils.getField(DiagnosticContextFilter.class,
        CORRELATION_ID_HEADER_NAME_PARAM_FIELD_NAME);
    assertThat(field).isNotNull();
    MockFilterConfig config = new MockFilterConfig();

    // exercise
    filter.init(config);

    // verify
    String correlationIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter,
        CORRELATION_ID_HEADER_NAME_PARAM);
    assertThat(correlationIdHttpHeaderName).isNotNull()
        .isEqualTo(DiagnosticContextFilter.CORRELATION_ID_HEADER_NAME_DEFAULT);
  }

  @Test
  public void testInitWithNonDefaultParameter() throws Exception {

    // setup
    DiagnosticContextFilter filter = new DiagnosticContextFilter();
    String field = (String) ReflectionTestUtils.getField(DiagnosticContextFilter.class,
        CORRELATION_ID_HEADER_NAME_PARAM_FIELD_NAME);
    assertThat(field).isNotNull();
    String nonDefaultParameter = "test";
    MockFilterConfig config = new MockFilterConfig(nonDefaultParameter);

    // exercise
    filter.init(config);
    // verify
    String correlationIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter,
        CORRELATION_ID_HEADER_NAME_PARAM);
    assertThat(correlationIdHttpHeaderName).isEqualTo(nonDefaultParameter);
  }

  public static class MockFilterConfig implements FilterConfig {

    private final Map<String, String> initParameters;

    /**
     * The constructor.
     */
    public MockFilterConfig() {

      super();
      this.initParameters = new HashMap<>();
    }

    /**
     * The constructor.
     */
    public MockFilterConfig(String value) {

      this();
      this.initParameters.put("correlationIdHeaderName", value);
    }

    /**
     * The constructor.
     */
    public MockFilterConfig(String key, String value) {

      this();
      this.initParameters.put(key, value);
    }

    @Override
    public String getFilterName() {

      return "Mock";
    }

    @Override
    public ServletContext getServletContext() {

      return null;
    }

    @Override
    public String getInitParameter(String name) {

      return this.initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {

      throw new UnsupportedOperationException();
    }

  }
}
