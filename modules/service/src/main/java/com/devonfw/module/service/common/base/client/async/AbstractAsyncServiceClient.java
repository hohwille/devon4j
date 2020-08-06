package com.devonfw.module.service.common.base.client.async;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Abstract base implementation of {@link AsyncServiceClient}.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 */
public abstract class AbstractAsyncServiceClient<S> implements AsyncServiceClient<S> {

  private final S proxy;

  private final ServiceClientStub<S> stub;

  /** The {@link #setErrorHandler(Consumer)} */
  protected Consumer<Throwable> errorHandler;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   */
  public AbstractAsyncServiceClient(S proxy, ServiceClientStub<S> stub) {

    super();
    this.proxy = proxy;
    this.stub = stub;
  }

  @Override
  public S get() {

    return this.proxy;
  }

  @Override
  public Consumer<Throwable> getErrorHandler() {

    return this.errorHandler;
  }

  @Override
  public void setErrorHandler(Consumer<Throwable> errorHandler) {

    this.errorHandler = errorHandler;
  }

  @Override
  public <R> void call(R result, Consumer<R> resultHandler) {

    ServiceClientInvocation<S> invocation = this.stub.getInvocation();
    Objects.requireNonNull(invocation, "invocation");
    ServiceContext<S> context = invocation.getContext();
    Objects.requireNonNull(context, "context");
    Method method = invocation.getMethod();
    Objects.requireNonNull(method, "method");
    Object[] parameters = invocation.getParameters();
    assert (method.getParameterCount() == getLength(parameters));
    doCall(invocation, resultHandler);
  }

  /**
   * @param <R> type of the return/result type.
   * @param invocation the {@link ServiceClientInvocation}.
   * @param resultHandler - see {@link #call(Object, Consumer)}.
   */
  protected abstract <R> void doCall(ServiceClientInvocation<S> invocation, Consumer<R> resultHandler);

  private static int getLength(Object[] parameters) {

    if (parameters == null) {
      return 0;
    }
    return parameters.length;
  }

}
