package org.hibernate.search.bugs.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        return "test";
    }
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}