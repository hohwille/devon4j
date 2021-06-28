
package com.devonfw.module.service.common.api.client;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * An {@link SyncServiceClient} wraps a {@link #get() service client} allowing to do {@link #call(Object, Consumer)
 * synchronous calls}. Only {@link ServiceClientFactory} is thread-safe but not instances of this interface so please do
 * not share clients between threads. The following code is a simple example how to do an synchronous service
 * invocation:
 *
 * <pre>
 * {@link ServiceClientFactory} factory = getServiceClientFactory();
 * {@link SyncServiceClient}{@literal <MyService>} client = factory.{@link ServiceClientFactory#createSync(Class) createSync}(MyService.class);
 * client.{@link #call(Object, Consumer) call}(client.{@link #get()}.doSomethingOperation(args), r -> {
 *   handleSyncResult(r);
 * });
 * </pre>
 *
 * Please note that the {@link #get() actual service client} is just a stub recording the desired service operation to
 * call synchronously. It will only return a dummy result (typically {@code null} and for primitive types an according
 * default like {@code 0} or {@code false}). This approach allows the {@link #call(Object, Consumer) call} method to be
 * type-safe ensuring that the {@link Consumer} callback fits to the result type of the invoked operation.<br>
 * Therefore ensure that you always call exactly one service operation on the {@link #get() service client} immediately
 * followed by a {@link #call(Object, Consumer) call} method invocation.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2021.08.003
 * @see ServiceClientFactory#createSync(Class)
 */

public interface SyncServiceClient<S> {

  /**
   * @return the actual service client as dynamic proxy of the service interface provided when this
   *         {@link SyncServiceClient} was {@link ServiceClientFactory#createSync(Class) created}. It is just a stub
   *         that will only record the invoked service operation returning a dummy result. In order to actually trigger
   *         the service invocation you need to invoke the {@link #call(Object, Consumer) call} method immediately after
   *         calling the service operation as described in the type comment of this {@link SyncServiceClient}.
   */

  S get();

  /**
   * @return the the {@link Consumer} callback {@link Consumer#accept(Object) accepting} a potential exception that
   *         occurred during sending the request or receiving the response. <b>ATTENTION:</b> The error handler is only
   *         used to report errors for {@link Consumer} usage (via {@link #call(Object, Consumer)} and
   *         {@link #callVoid(Runnable, Consumer)}). When using {@link CompletableFuture} instead, errors will be
   *         reported via the {@link CompletableFuture} itself. Please also note that due to design of
   *         {@link CompletableFuture} the errors (like
   *         {@link net.sf.mmm.util.exception.api.ServiceInvocationFailedException}) will be wrapped in a
   *         {@link java.util.concurrent.ExecutionException}.
   */
  Consumer<Throwable> getErrorHandler();

  /**
   * @param errorHandler the {@link Consumer} callback {@link Consumer#accept(Object) accepting} a potential exception
   *        that occurred during sending the request or receiving the response.
   */
  void setErrorHandler(Consumer<Throwable> errorHandler);
}