package org.mutabilitydetector.issues;

import static org.mutabilitydetector.unittesting.AllowedReason.assumingFieldsNamed;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import java.util.Date;

import org.junit.Test;

public class GitHubIssue_28 {

    public static final class Claim {
        public final double amount;
        private final Date failureDate;

        Claim(double amount, Date failureDate) {
            this.amount = amount;
            this.failureDate = new Date(failureDate.getTime());
        }

        public Date getFailureDate() {
            return new Date(failureDate.getTime());
        }
    }
    
    @Test
    public void isImmutable() {
        assertInstancesOf(Claim.class, 
                          areImmutable(),
                          assumingFieldsNamed("failureDate").areNotModifiedAndDoNotEscape());
    }
}