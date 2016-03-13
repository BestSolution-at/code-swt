/*******************************************************************************
* Copyright (c) 2016 BestSolution.at and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* 	Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
*******************************************************************************/
package at.bestsolution.code.swt.services.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.eclipse.fx.core.Subscription;
import org.eclipse.fx.core.ThreadSynchronize;
import org.eclipse.swt.widgets.Display;

public class SWTThreadSynchronize implements ThreadSynchronize {
	private final Display display;

	@Inject
	public SWTThreadSynchronize(Display display) {
		this.display = display;
	}

	@Override
	public <V> V syncExec(Callable<V> callable, V defaultValue) {
		if( display.getThread() == Thread.currentThread() ) {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			AtomicReference<V> ref = new AtomicReference<V>();
			display.syncExec( () -> {
				try {
					ref.set(callable.call());
				} catch (Exception e) {
					ref.set(defaultValue);
				}
			} );
			return ref.get();
		}
	}

	@Override
	public void syncExec(Runnable runnable) {
		if( display.getThread() == Thread.currentThread() ) {
			runnable.run();
		} else {
			display.syncExec(runnable);
		}
	}

	@Override
	public <V> Future<V> asyncExec(Callable<V> callable) {
		RunnableFuture<V> task = new FutureTask<V>(callable);
		display.asyncExec(task);
		return task;
	}

	@Override
	public void asyncExec(Runnable runnable) {
		display.asyncExec(runnable);
	}

	@Override
	public Subscription scheduleExecution(long delay, Runnable runnable) {
		display.timerExec((int)delay, runnable);
		return () -> {
			display.timerExec(-1, runnable);
		};
	}

	@Override
	public <T> CompletableFuture<T> scheduleExecution(long delay, Callable<T> runnable) {
		CompletableFuture<T> future = new CompletableFuture<T>();
		display.timerExec((int)delay, () -> {
			try {
				if( ! future.isCancelled() ) {
					future.complete(runnable.call());
				}
			} catch(Exception e) {
				future.completeExceptionally(e);
			}
		});
		return future;
	}

}
