package test

/**
 * Created by @marcos-carceles on 20/05/15.
 */
class IncautiousPerson {

    String firstName
    String lastName
    String password

    static searchable = {
        only = ['firstName', 'lastName', 'password']
    }
}
