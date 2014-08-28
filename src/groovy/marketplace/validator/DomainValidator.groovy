package marketplace.validator

interface DomainValidator<T> {
    /**
     * Validates that the changes that are about to be made to the existing domain object are
     * valid. This method should not change the object, but should throw exceptions if there are
     * problems with it.
     */
    public void validateChanges(T existing, T dto)

    /**
     * Ensures that all fields on a new domain object have allowed values. This method should
     * not change the object, but should throw exceptions if there are
     * problems with it.
     */
    public void validateNew(T dto)
}
