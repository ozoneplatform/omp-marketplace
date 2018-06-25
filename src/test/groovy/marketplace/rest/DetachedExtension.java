package marketplace.rest;

import org.spockframework.mock.MockUtil;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;
import spock.lang.Specification;

public class DetachedExtension extends AbstractAnnotationDrivenExtension<Detached> {

    private static final MockUtil MOCK_UTIL = new MockUtil();

    @Override
    public void visitFieldAnnotation(Detached annotation, FieldInfo field) {
        field.getParent().addSetupInterceptor(new IMethodInterceptor() {
            @Override
            public void intercept(IMethodInvocation invocation) throws Throwable {
                MOCK_UTIL.attachMock(field.readValue(invocation.getInstance()), (Specification) invocation.getInstance());
                invocation.proceed();
            }
        });
        field.getParent().addCleanupInterceptor(new IMethodInterceptor() {
            @Override
            public void intercept(IMethodInvocation invocation) throws Throwable {
                MOCK_UTIL.detachMock(field.readValue(invocation.getInstance()));
                invocation.proceed();
            }
        });
    }
}
