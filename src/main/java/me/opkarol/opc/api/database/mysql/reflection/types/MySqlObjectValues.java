package me.opkarol.opc.api.database.mysql.reflection.types;

/**
 * OpDatabasePlugin requires IDENTIFICATION_OBJECT, COMPARABLE_OBJECT and UUID_OBJECT to be annotated in values.
 */
public enum MySqlObjectValues {
    /**
     * IDENTIFICATION_OBJECT value type is used to store an object that uniquely identifies the field, this can be only an int,
     * it is the primary object, but also identification object, that means there shouldn't be both this and PRIMARY object in one database.
     */
    IDENTIFICATION_OBJECT,
    /**
     * The PRIMARY value type is used to store the primary value for the field.
     */
    PRIMARY,
    /**
     * Default value that doesn't have special attributes.
     */
    EMPTY,
    /**
     * The UUID_OBJECT value type is used to store a universally unique identifier for the field.
     * Each object UUID should be different and it`s recommended that there are the same length of uuids as uuids` objects
     */
    UUID_OBJECT,
    /**
     * The COMPARABLE_OBJECT value type is used to store an object that can be compared to other objects of the same type.
     * E.g. the generic C object that is provided in the database.
     */
    COMPARABLE_OBJECT
}
