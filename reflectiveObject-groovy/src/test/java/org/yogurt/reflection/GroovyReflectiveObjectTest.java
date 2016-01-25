package org.yogurt.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class GroovyReflectiveObjectTest {

    @Test
    public void testSmartGetWhenFieldDoesntExist() throws Exception {
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(new Foo());

        assertEquals("nonExistantField", reflectiveObject.smartGet("nonExistantField").getObject());
    }

    @Test
    public void testSmartGetWhenGetterDoesntExist() throws Exception {
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(new Foo());

        assertEquals("fieldWithNoGetter", reflectiveObject.smartGet("fieldWithNoGetter").getObject());
    }

    @Test
    public void testSmartSetWhenFieldDoesntExist() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.smartSet("nonExistantField", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetWithWhenGetterDoesntExist() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.smartSet("fieldWithNoGetter", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetWithReflectiveObjectWhenFieldDoesntExist() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.smartSet("nonExistantField", new GroovyReflectiveObject("foo"));

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetReflectiveObjectWhenGetterDoesntExist() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.smartSet("fieldWithNoGetter", new GroovyReflectiveObject("foo"));

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testGetAnnotatedFieldsReturnsFieldsThatHaveSpecifiedAnnotation() throws Exception {
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(new Foo());

        Set<Field> fields = reflectiveObject.getFieldsAnnotatedWith(Deprecated.class);

        assertTrue(fields.stream().anyMatch(new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return field.getName().equals("deprecatedField");
            }
        }));

        assertTrue(fields.stream().anyMatch(new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return field.getName().equals("anotherDeprecatedField");
            }
        }));
    }

    @Test
    public void testCallCallsMethodWithParameters() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.call("setNonExistantField", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testCallCallsMethodWithMultipleParameters() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        reflectiveObject.call("setDeprecatedFields", "foo", "bar");

        assertEquals("foo", foo.deprecatedField);
        assertEquals("bar", foo.anotherDeprecatedField);
    }

    @Test
    public void testGetObjectReturnsObject() throws Exception {
        Foo foo = new Foo();
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(foo);

        assertSame(foo, reflectiveObject.getObject());
    }

    @Test
    public void testConstructorCanCreateClassWithoutConstructor() throws Exception {
        GroovyReflectiveObject reflectiveObject = new GroovyReflectiveObject(Foo.class);

        assertTrue(reflectiveObject.getObject() instanceof Foo);
    }

    public class Foo {
        private String fieldWithNoGetter = "fieldWithNoGetter";
        @Deprecated
        private String deprecatedField;
        @Deprecated
        private String anotherDeprecatedField;

        public String getNonExistantField() {
            return "nonExistantField";
        }

        public String methodToGetPrivateField() {
            return fieldWithNoGetter;
        }

        public void setNonExistantField(String value) {
            fieldWithNoGetter = value;
        }

        public void setDeprecatedFields(String deprecatedField, String anotherDeprecatedField) {
            this.deprecatedField = deprecatedField;
            this.anotherDeprecatedField = anotherDeprecatedField;
        }
    }
}