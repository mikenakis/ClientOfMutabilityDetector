package org.mutabilitydetector.runtime;

import static org.mutabilitydetector.Configurations.OUT_OF_THE_BOX_CONFIGURATION;

import org.mutabilitydetector.AnalysisResult;
import org.mutabilitydetector.AnalysisSession;
import org.mutabilitydetector.IsImmutable;
import org.mutabilitydetector.DefaultCachingAnalysisSession;
import org.mutabilitydetector.locations.Dotted;

import java.util.List;

public final class RuntimeAnalysisExample {
    /*
     * Create an instance of AnalysisSession. This is the object that can tell
     * you if a class is immutable or not.
     *
     * Note there is only currently a thread unsafe implementation, however,
     * it's intended there is no static state. So there should be no problem in
     * creating one per thread. Since analysisSessions cache their result, this
     * will result in a cache miss per thread. This should amortize over time.
     *
     * You may configure the analysis session differently by passing something
     * other than the OUT_OF_THE_BOX_CONFIGURATION. See ConfigurationBuilder for
     * more details.
     */
    private final AnalysisSession analysisSession =
            DefaultCachingAnalysisSession.createWithCurrentClassPath(OUT_OF_THE_BOX_CONFIGURATION);

    private void run(Object requiredToBeImmutable, String message) {

        /*
         * Even though the parameter is defined to be java.lang.Object
         * we are interested in what the actual class is, at runtime.
         */
        Class<?> actualClassOfParameterAtRuntime = requiredToBeImmutable.getClass();

        /*
         * Convert class object into its a representation of its
         * dotted class name.
         */
        Dotted dottedClassName = Dotted.fromClass(actualClassOfParameterAtRuntime);

        /*
         * Request an analysis of the runtime class, to discover if this
         * instance will be immutable or not.
         */
        AnalysisResult result = analysisSession.resultFor(dottedClassName);

        if (result.isImmutable.equals(IsImmutable.IMMUTABLE)) {
            /*
             * rest safe in the knowledge the class is
             * immutable, share across threads with joyful abandon
             */
        } else if (result.isImmutable.equals(IsImmutable.NOT_IMMUTABLE)) {
            /*
             * be careful here: make defensive copies,
             * don't publish the reference,
             * read Java Concurrency In Practice right away!
             */
        }
		System.out.println(message + ": " + result.isImmutable + " (" + result + ")");
    }

    public static void main(String[] args) {
        new RuntimeAnalysisExample().run(new java.util.Date(), "new java.util.Date()");
        new RuntimeAnalysisExample().run(List.of( 1, 2, 3 ), "List.of( 1, 2, 3 )");
        new RuntimeAnalysisExample().run(List.of( new StringBuilder() ), "List.of( new StringBuilder() )");
    }
}
