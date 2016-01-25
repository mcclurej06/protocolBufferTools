package org.yogurt.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ReflectiveObjectTest {

    @Test
    public void testSmartGetWhenFieldDoesntExist() throws Exception {
        ReflectiveObject reflectiveObject = new ReflectiveObject(new Foo());

        assertEquals("nonExistantField", reflectiveObject.smartGet("nonExistantField").getObject());
    }

    @Test
    public void testSmartGetWhenGetterDoesntExist() throws Exception {
        ReflectiveObject reflectiveObject = new ReflectiveObject(new Foo());

        assertEquals("fieldWithNoGetter", reflectiveObject.smartGet("fieldWithNoGetter").getObject());
    }

    @Test
    public void testSmartSetWhenFieldDoesntExist() throws Exception {
        Foo foo = new Foo();
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        reflectiveObject.smartSet("nonExistantField", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetWithWhenGetterDoesntExist() throws Exception {
        Foo foo = new Foo();
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        reflectiveObject.smartSet("fieldWithNoGetter", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetWithReflectiveObjectWhenFieldDoesntExist() throws Exception {
        Foo foo = new Foo();
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        reflectiveObject.smartSet("nonExistantField", new ReflectiveObject("foo"));

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testSmartSetReflectiveObjectWhenGetterDoesntExist() throws Exception {
        Foo foo = new Foo();
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        reflectiveObject.smartSet("fieldWithNoGetter", new ReflectiveObject("foo"));

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testGetAnnotatedFieldsReturnsFieldsThatHaveSpecifiedAnnotation() throws Exception {
        ReflectiveObject reflectiveObject = new ReflectiveObject(new Foo());

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
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        reflectiveObject.call("setNonExistantField", "foo");

        assertEquals("foo", foo.fieldWithNoGetter);
    }

    @Test
    public void testGetObjectReturnsObject() throws Exception {
        Foo foo = new Foo();
        ReflectiveObject reflectiveObject = new ReflectiveObject(foo);

        assertSame(foo, reflectiveObject.getObject());
    }

    @Test
    public void testConstructorCanCreateClassWithoutConstructor() throws Exception {
        ReflectiveObject reflectiveObject = new ReflectiveObject(Foo.class);

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
    }
}